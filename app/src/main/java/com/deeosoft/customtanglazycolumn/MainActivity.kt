package com.deeosoft.customtanglazycolumn

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deeosoft.customtanglazycolumn.ui.theme.CustomTangLazyColumnTheme
import com.deeosoft.customtanglazycolumn.util.HorizontalCarousel
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomTangLazyColumnTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ContentScreen()

                    val recyclerView = HorizontalCarousel(LocalContext.current)

//                    AndroidView(factory = )
                }
            }
        }
    }
}

/*fun determineMagnifyingBasedOnIndex(totalVisibleItem: Int, itemStartOffset: Int, screenCenterInPixel: Float): Float{
    if(screenCenterInPixel)
}*/

@OptIn(ExperimentalPagerApi::class)
@Composable
fun <R, T>CustomHorizontalGaussianPager(modifier: Modifier = Modifier,
                                        list: List<T>,
                                        contentPaddingStart: Int,
                                        contentPaddingEnd: Int,
                                        painter: Painter,
                                        itemComposable: (Int, Int, T) -> R){
    val pagerState = rememberPagerState()

    HorizontalPager(state = pagerState, count = list.size) {
        itemComposable(this.currentPage, this.currentPage, list[this.currentPage])
    }
}





@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewLazyGaussianRow(items: List<Painter>){
    val lazyRowState = rememberLazyListState()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenCenter = screenWidth/2
    val context = LocalContext.current

    val midIndex by remember(lazyRowState.firstVisibleItemIndex) {
        derivedStateOf {

            lazyRowState.layoutInfo.visibleItemsInfo.run {
                /*if(this.isEmpty()) -1
                else {
                    this.takeWhile{

                        println("screen center $screenCenter")
                        println("offset and size is ${it.offset.toDP(context)} and ${it.size.toDP(context)}")
                        (screenCenter.toFloat() in it.offset.toDP(context)..(it.size.toDP(context) + 80))
                    }.forEach {
                        println("here")
                        it.index
                    }
                }*/
                val firstVisibleIndex = lazyRowState.firstVisibleItemIndex
                if (isEmpty()) -1 else firstVisibleIndex + (last().index - firstVisibleIndex) / 2
            }
        }
    }

    LazyRow(
        modifier = Modifier.fillMaxSize(),
        state = lazyRowState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyRowState),
        content = {
            item {
                Spacer(modifier = Modifier
                    .background(Color.White)
                    .width(((screenWidth / 2) - 30).dp))
            }
            items(items.size){
                val itemSize = if(it == midIndex) 80F else 65F
                val itemPadding = if(it == midIndex) 0F else 20F
                NewGaussianLazyRowItem(itemSize, items[it], itemPadding)
            }
            item {
                Spacer(modifier = Modifier
                    .background(Color.White)
                    .width(((screenWidth / 2) - 30).dp))
            }
        }
    )
}


@Composable
fun NewGaussianLazyRowItem(size: Float, painter: Painter, padding: Float){

    Box(modifier = Modifier
        .height((painter.intrinsicSize.height * 0.4).dp)
        .padding(top = padding.dp)){
        Box(modifier = Modifier
            .size(width = size.dp, height = size.dp)
            .padding(start = 10.dp, top = 10.dp)
            .background(Color.Blue, CircleShape)
        ){
            Box(modifier = Modifier
                .size(
                    width = (size * 0.7).dp,
                    height = (size * 0.7).dp
                )
                .background(Color.White, CircleShape)
                .aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height)
                .align(Alignment.Center)){
                Image(modifier = Modifier
                    .align(Alignment.Center)
                    .size(
                        width = (size * 0.4).dp,
                        height = (size * 0.4).dp
                    ),
                    painter = painter,
                    contentScale = ContentScale.Fit,
                    contentDescription = "Item")
            }
        }
    }

}










@SuppressLint("UnrememberedMutableState")
@Composable
fun <R, T> LazyGaussianRow(modifier: Modifier = Modifier,
                           contentPaddingStart: Int,
                           contentPaddingEnd: Int,
                           scaleFactor: MutableState<Float>,
                           items: List<T>,
                           itemComposable: @Composable (Int, Int, LazyListState, T) -> R){
    val lazyRowState = rememberLazyListState()

    //Use screen width to determine scale factor...
    val screenWidth = LocalConfiguration.current.screenWidthDp
    println("screenWidth $screenWidth")
    val recyclerWidth = screenWidth - (contentPaddingStart + contentPaddingEnd)
    val recyclerCenterX = screenWidth / 2
    LazyRow(modifier = modifier
        .padding(contentPaddingStart.dp, 10.dp, contentPaddingEnd.dp, 0.dp)
        .fillMaxWidth(),
        state = lazyRowState,
        content = {
            item {
                Spacer(modifier = Modifier
                    .background(Color.White)
                    .width(((screenWidth / 2) - 30).dp))
            }
            items(items.size){
                itemComposable(it, recyclerCenterX, lazyRowState, items[it])
            }
            item {
                Spacer(modifier = Modifier
                    .background(Color.White)
                    .width(((screenWidth / 2) - 30).dp))
            }
    })
}

@Composable
fun ContentScreen(){
    val scaleFactor = remember {mutableStateOf(80F)}
    NewLazyGaussianRow(items = CircularLazyColumnImages())
    /*LazyGaussianRow(
        contentPaddingStart = 10,
        contentPaddingEnd = 10,
        scaleFactor = scaleFactor,
        items = CircularLazyColumnImages()) { pos, recyclerCenterX, lazyListState, obj ->
        val listScaleValues = remember {
            mutableStateListOf<Float>()
        }
        for(i in CircularLazyColumnImages().indices){
            if(i == 0)
                listScaleValues.add(80F)
            else
                listScaleValues.add(80F * 0.8497F)
        }
        NewLazyRowItem(pos, recyclerCenterX.toDouble(), listScaleValues, lazyListState, scaleValue = scaleFactor, painter = obj)
//        CircularLazyRowItem(pos, painter = obj, recyclerCenterX)
    }*/
}

private fun Number.toDP(context: Context): Float{
    val density = context.resources.displayMetrics.density
    return this.toFloat().div(density)
}

var itemComposableCounter = 0
var itemOffsetX = 0f
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun NewLazyRowItem(position: Int, recyclerCenterX: Double, listScaleValues: MutableList<Float>, lazyRowState: LazyListState, scaleValue: MutableState<Float>, painter: Painter){
    val context = LocalContext.current
    val density = LocalDensity.current
    var itemCenterX = 0
    val coroutineScope = rememberCoroutineScope()
    var itemSize = 80


    val localScaleValue = remember{mutableStateOf(80F)}

    // array of state values for each item in the row and update the state values based on the position of the item and then

    /*coroutineScope.launch {
        if(lazyRowState.isScrollInProgress) {
            println("scrolling ...")
            val itemInfo =
                lazyRowState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == position }
            if (itemInfo != null) {
                val recyclerCenter = lazyRowState.layoutInfo.viewportEndOffset / 2
                val itemCenter = itemInfo.offset + itemInfo.size / 2
                var scaleFactor = 1f
                if (itemCenter !in itemCenterX - 10..itemCenterX + 10) {
                    scaleFactor =
                        determineGaussianScale(itemCenter.toFloat(), recyclerCenter.toDouble())
//                    scaleValue.value *= scaleFactor
                    localScaleValue.value *= scaleFactor
                    listScaleValues[position] = listScaleValues[position] * scaleFactor
                    itemCenterX = itemCenter

                    println(
                        "recyclerCenterX is $recyclerCenterX " +
                                "position is $position " +
                                "itemCenter is $itemCenter " +
                                "and item size is ${itemInfo.size.toDP(context) / 2} " +
                                "scaleFactor is $scaleFactor"
                    )
                }
            }
        }
//        lazyRowState.animateScrollItemToCenter(position, this)
    }*/

    /*if(lazyRowState.isScrollInProgress){
        runBlocking {
            val itemInfo = lazyRowState.layoutInfo.visibleItemsInfo.firstOrNull{it.index == position}
            if(itemInfo != null){
                val recyclerCenter = lazyRowState.layoutInfo.viewportEndOffset / 2
                val itemCenter = itemInfo.offset + itemInfo.size / 2
                var scaleFactor = 1f
                if(itemCenter !in itemCenterX - 10..itemCenterX + 10){
                    scaleFactor = determineGaussianScale(itemCenter.toFloat(), recyclerCenter.toDouble())
                    scaleValue.value *= scaleFactor
                    itemCenterX = itemCenter

                    println("recyclerCenterX is $recyclerCenterX " +
                            "position is $position " +
                            "itemCenter is $itemCenter " +
                            "and item size is ${itemInfo.size.toDP(context) / 2} " +
                            "scaleFactor is $scaleFactor")
                }
            }
        }
    }*/

    Box(modifier = Modifier
        .size(width = listScaleValues[position].dp, height = listScaleValues[position].dp)
        .padding(start = 10.dp, top = 10.dp)
        .background(Color.Blue, CircleShape)
        .onGloballyPositioned {

            val childWidthInPixelWithoutPadding =
                it.positionInRoot().x
            println("position is $position x offset is ${it.positionInRoot().x} childWidthInPixelWithoutPadding is $childWidthInPixelWithoutPadding")
            val childWidthInDP = density.run { childWidthInPixelWithoutPadding.toDp() }
            val childCenterX = childWidthInDP / 2


//            localScaleValue.value *= scaleFactor
            if (itemOffsetX !in it.positionInRoot().x - 20..it.positionInRoot().x + 20) {

                val scaleFactor = determineGaussianScale(
                    childCenterX.value,
                    recyclerCenterX
                )
                listScaleValues[position] *= scaleFactor
                println("called how many times ${itemComposableCounter++}")
                /*println(
                    "recyclerCenterX is $recyclerCenterX " +
                            "position is $position " +
                            "itemOffsetX is $itemOffsetX " +
                            "item position is ${it.positionInRoot().x} " +
                            "item position is ${it.positionInRoot().x} " +
                            "scaleFactor is $scaleFactor"
                )*/
                itemOffsetX = it.positionInRoot().x
            }


        }
    ){
        Box(modifier = Modifier
            .size(
                width = (listScaleValues[position] * 0.7).dp,
                height = (listScaleValues[position] * 0.7).dp
            )
            .background(Color.White, CircleShape)
            .aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height)
            .align(Alignment.Center)){
            Image(modifier = Modifier
                .align(Alignment.Center)
                .size(
                    width = (listScaleValues[position] * 0.4).dp,
                    height = (listScaleValues[position] * 0.4).dp
                ),
                painter = painter,
                contentScale = ContentScale.Fit,
                contentDescription = "Item")
        }
    }
}

@Composable
private fun CircularLazyRowItem(position: Int, painter: Painter, recyclerCenterInPixels: Int){
    val scaleValue = remember{mutableStateOf(80F)}
    val spaceWidth = remember { mutableStateOf(1) }
    val density = LocalDensity.current
    Column(
        modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
            val childWidth = layoutCoordinates.size.width
            val childWidthInPixel = layoutCoordinates.positionInRoot().x + childWidth
            val childWidthInDP = density.run { childWidthInPixel.toDp() }
            val childCenterX = childWidthInDP / 2
            val scaleFactor = determineGaussianScale(
                childCenterX.value,
                recyclerCenterInPixels.toDouble()
            )
            scaleValue.value = scaleFactor
            spaceWidth.value = 0
            println("position $position, recyclerCenterInPixels $recyclerCenterInPixels, childWidth ${childWidthInDP.value}, childCenterX $childCenterX, scaleFactor $scaleFactor")
        }
    ){
        Spacer(modifier = Modifier
            .background(Color.White)
            .width(spaceWidth.value.dp))
        Box(modifier = Modifier
            .size(width = (scaleValue.value * 60).dp, height = (scaleValue.value * 60).dp)
            .padding(start = 10.dp, top = (scaleValue.value * 10).dp)
            .background(Color.Blue, CircleShape)
        ){
            val width = scaleValue.value * 40 * 0.4
            val height = scaleValue.value * 40 * 0.4
            println("boxWidth $width, boxHeight $height")
            Box(modifier = Modifier
                .size(
                    width = (scaleValue.value * 40 * 0.4).dp,
                    height = (scaleValue.value * 40 * 0.4).dp
                )
                .background(Color.White, CircleShape)
                .aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height)
                .align(Alignment.Center)){
                Image(modifier = Modifier
                    .align(Alignment.Center)
                    .size(
                        width = (scaleValue.value * 40 * 0.2).dp,
                        height = (scaleValue.value * 40 * 0.2).dp
                    ),
                    painter = painter,
                    contentScale = ContentScale.Fit,
                    contentDescription = "Item")
            }
        }
    }
}

@Composable
private fun CircularLazyColumnImages(): List<Painter>{
    val list = mutableListOf<Painter>()
    list.add(painterResource(id = R.drawable.user))
    list.add(painterResource(id = R.drawable.book))
    list.add(painterResource(id = R.drawable.telephone))
    list.add(painterResource(id = R.drawable.user))
    list.add(painterResource(id = R.drawable.book))
    list.add(painterResource(id = R.drawable.telephone))
    list.add(painterResource(id = R.drawable.user))
    list.add(painterResource(id = R.drawable.book))
    list.add(painterResource(id = R.drawable.telephone))
    list.add(painterResource(id = R.drawable.user))
    list.add(painterResource(id = R.drawable.book))
    list.add(painterResource(id = R.drawable.telephone))
    return list
}


private fun determineGaussianScale(childCenterX: Float,
                                   recyclerCenterX: Double,
                                   scaleFactor: Float = 1f,
                                   spreadFactor: Double = 140.toDouble(),
                                   minScaleOffset: Float = 1f): Float{
    return (Math.E.pow(
        -(childCenterX - recyclerCenterX).pow(2.toDouble()) / (2 * spreadFactor.pow(2.toDouble()))
    ) * scaleFactor + minScaleOffset).toFloat()
}

private fun LazyListState.animateScrollItemToCenter(position: Int, scope: CoroutineScope){
    val itemInfo = this.layoutInfo.visibleItemsInfo.firstOrNull{it.index == position}

    scope.launch {
        if(itemInfo != null){
            val recyclerCenter = this@animateScrollItemToCenter.layoutInfo.viewportEndOffset / 2
            val itemCenter = itemInfo.offset + itemInfo.size / 2
            this@animateScrollItemToCenter.animateScrollBy((itemCenter - recyclerCenter).toFloat())
        }else{
            this@animateScrollItemToCenter.animateScrollToItem(position)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CustomTangLazyColumnTheme {
        ContentScreen()
    }
}