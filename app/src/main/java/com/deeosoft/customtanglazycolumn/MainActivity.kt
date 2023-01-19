package com.deeosoft.customtanglazycolumn

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.animateScrollBy
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import com.deeosoft.customtanglazycolumn.ui.theme.CustomTangLazyColumnTheme
import com.google.accompanist.pager.*
import dev.chrisbanes.snapper.ExperimentalSnapperApi
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
//                    CircularLazyColumn(CircularLazyColumnImages())
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPagerApi::class)
@Composable
private fun CircularLazyColumn(painters: List<Painter>){
    val firstPairIs = Pair(80, 80)
    val secondPairIs = Pair(60, 60)
    val thirdPairIs = Pair(40, 40)
    val fourthPairIs = Pair(40, 40)
    val fifthPairIs = Pair(40, 40)
    val sixthPairIs = Pair(40, 40)
    val listOfPairAre = mutableListOf<Pair<Int, Int>>()
    listOfPairAre.add(firstPairIs)
    listOfPairAre.add(secondPairIs)
    listOfPairAre.add(thirdPairIs)
    listOfPairAre.add(fourthPairIs)
    listOfPairAre.add(fifthPairIs)
    listOfPairAre.add(sixthPairIs)
    var listOfState = remember{listOfPairAre}
    val lazyRowState = rememberLazyListState(5)
    var listOfFullyVisibleItemInfoSize = 5
    var recyclerCenterInPixels = LocalConfiguration.current.screenWidthDp
    var childCenterX = ((recyclerCenterInPixels / listOfFullyVisibleItemInfoSize).minus(10)).div(2)
    val listOfSize: MutableList<Pair<Int, Int>> by remember {
        derivedStateOf {
            val layoutInfo = lazyRowState.layoutInfo
            val listOfVisibleItemInfo = layoutInfo.visibleItemsInfo
            if(listOfVisibleItemInfo.isEmpty()){
                emptyList<Pair<Int, Int>>()
            }else{
                val listOfFullyVisibleItemInfo = layoutInfo.visibleItemsInfo.toMutableList()
                println("lazy row state ${listOfVisibleItemInfo.size}")
                val lastItem = listOfVisibleItemInfo.last()
                recyclerCenterInPixels = lazyRowState.layoutInfo.viewportEndOffset / 2
                val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset

                if (lastItem.offset + lastItem.size > viewportHeight) {
                    listOfFullyVisibleItemInfo.removeLast()
                }

                val firstItemIfLeft = listOfFullyVisibleItemInfo.firstOrNull()
                if (firstItemIfLeft != null && firstItemIfLeft.offset < layoutInfo.viewportStartOffset) {
                    listOfFullyVisibleItemInfo.removeFirst()
                }
                listOfFullyVisibleItemInfoSize = listOfFullyVisibleItemInfo.map { index -> index }.size
                childCenterX = ((recyclerCenterInPixels / listOfFullyVisibleItemInfo.size) - 10) / 2
                println("get list of visible item info ${listOfFullyVisibleItemInfo.size}")
            }
//            if(listOfVisibleItemInfo.isEmpty()){
//                emptyList()
//            }else{





                determineItemSizeBasedScaleFactor(
                    listOfFullyVisibleItemInfoSize,
                    childCenterX = childCenterX,
                    recyclerCenterInPixels = recyclerCenterInPixels.toDouble()
                )
//            }

        }
    }

    LazyRow(modifier = Modifier
        .fillMaxWidth(),
        state = lazyRowState,
        contentPadding = PaddingValues(9.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp, alignment = Alignment.CenterHorizontally),
        content = {
        items(painters.size){
            /*val firstPairIs = Pair(40, 40)
            val secondPairIs = Pair(60, 60)
            val thirdPairIs = Pair(80, 80)
            val fourthPairIs = Pair(60, 60)
            val fifthPairIs = Pair(40, 40)
            val sixthPairIs = Pair(30, 30)
            val listOfPairAre = mutableListOf<Pair<Int, Int>>()
            listOfPairAre.add(firstPairIs)
            listOfPairAre.add(secondPairIs)
            listOfPairAre.add(thirdPairIs)
            listOfPairAre.add(fourthPairIs)
            listOfPairAre.add(fifthPairIs)
            listOfPairAre.add(sixthPairIs)*/
            CircularLazyRowItem(painters[it], listOfSize[it].first, listOfSize[it].second)
        }
    })
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun <R, T> LazyGuassianRow(modifier: Modifier = Modifier,
                           contentPaddingStart: Int,
                           contentPaddingEnd: Int,
                           items: List<T>,
                           itemComposable: @Composable (Int, Int, T) -> R){
    val lazyRowState = rememberLazyListState()
    //Use screen width to determine scale factor...

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val recyclerWidth = screenWidth - (contentPaddingStart + contentPaddingEnd)
    val recyclerCenterX = recyclerWidth / 2
    /*if(lazyRowState.isScrollInProgress){
        println("Enter here ...")
        derivedStateOf {
            minNoOfCardsOnTheScreen = lazyRowState.layoutInfo.visibleItemsInfo.toMutableList().size
            childCenterX = (recyclerWidth / minNoOfCardsOnTheScreen) / 2
            scaleFactor = determineItemSizeBasedScaleFactor(
                noOfCards = minNoOfCardsOnTheScreen,
                childCenterX = childCenterX,
                recyclerCenterInPixels = recyclerCenterX.toDouble()
            )
        }
    }*/

    LazyRow(modifier = modifier
        .padding(contentPaddingStart.dp, 10.dp, contentPaddingEnd.dp, 0.dp)
        .fillMaxWidth(),
        state = lazyRowState,
        content = {
            item {
                Spacer(modifier = Modifier
                    .background(Color.White)
                    .width(((screenWidth / 2) - 40).dp))
            }
            items(items.size){
                itemComposable(it, recyclerCenterX, items[it])
            }
            item {
                Spacer(modifier = Modifier
                    .background(Color.White)
                    .width(((screenWidth / 2) - 100).dp))
            }
    })
}

@Composable
fun ContentScreen(){
    LazyGuassianRow(contentPaddingStart = 10, contentPaddingEnd = 10, items = CircularLazyColumnImages()) { pos, recyclerCenterX, obj ->
        CircularLazyRowItem(pos, painter = obj, recyclerCenterX)
    }
}

private fun determineItemSizeBasedScaleFactor(noOfCards: Int, childCenterX: Int, recyclerCenterInPixels: Double): MutableList<Pair<Int, Int>>{
    val result = mutableListOf<Pair<Int, Int>>()
    (0 until noOfCards).forEach { _ ->
        val scaleValue =
            determineGuassianScale(childCenterX, recyclerCenterInPixels.toDouble())
        result.add(Pair((scaleValue * 80).toInt(), (scaleValue * 80).toInt()))
    }
    return result
}

@Composable
private fun CircularLazyRowItem(painter: Painter, itemWidth: Int, itemHeight: Int){
    Box(modifier = Modifier
        .size(width = itemWidth.dp, height = itemHeight.dp)
        .background(Color.Blue, CircleShape)){
        Box(modifier = Modifier
            .size(width = (itemWidth * 0.4).toInt().dp, height = (itemHeight * 0.4).toInt().dp)
            .background(Color.White, CircleShape)
            .aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height)
            .align(Alignment.Center)){
            Image(modifier = Modifier
                .align(Alignment.Center)
                .size(width = (itemWidth * 0.2).toInt().dp, height = (itemHeight * 0.2).toInt().dp),
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
    Column{
        Spacer(modifier = Modifier
            .background(Color.White)
            .width(spaceWidth.value.dp))
        Box(modifier = Modifier
            .size(width = (scaleValue.value * 60).dp, height = (scaleValue.value * 60).dp)
            .padding(start = 10.dp, top = (scaleValue.value * 10).dp)
            .background(Color.Blue, CircleShape)
            .onGloballyPositioned { layoutCoordinates ->
                val childWidth = layoutCoordinates.size.width
                val childWidthInPixel = layoutCoordinates.positionInRoot().x + childWidth
                val childWidthInDP = density.run { childWidthInPixel.toDp() }
                val childCenterX = childWidthInDP / 2
                val scaleFactor = determineGuassianScale(
                    childCenterX.value.toInt(),
                    recyclerCenterInPixels.toDouble()
                )
                scaleValue.value = scaleFactor
                spaceWidth.value = 0
                println("position $position, recyclerCenterInPixels $recyclerCenterInPixels, childWidth ${childWidthInDP.value}, childCenterX $childCenterX, scaleFactor $scaleFactor")
            }){
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
    return list
}


private fun determineGuassianScale(childCenterX: Int,
                                 recyclerCenterX: Double,
                                 scaleFactor: Float = 1f,
                                 spreadFactor: Double = 140.toDouble(),
                                 minScaleOffset: Float = 1f): Float{
    return (Math.E.pow(
        -(childCenterX - recyclerCenterX).pow(2.toDouble()) / (2 * spreadFactor.pow(2.toDouble()))
    ) * scaleFactor + minScaleOffset).toFloat()
}

private fun LazyListState.animateScrollAndCentralizeItem(index: Int, scope: CoroutineScope){
    val itemInfo = this.layoutInfo.visibleItemsInfo.firstOrNull{it.index == index}
    val viewPortCenter = this.layoutInfo.viewportSize.center

    scope.launch {
        if(itemInfo != null){
            val viewPortEndOffsetCenter = this@animateScrollAndCentralizeItem.layoutInfo.viewportEndOffset / 2
            val itemCenter = itemInfo.offset + itemInfo.size / 2
            println("View port EndOffset center $viewPortEndOffsetCenter")
            this@animateScrollAndCentralizeItem.animateScrollBy((itemCenter - viewPortEndOffsetCenter).toFloat())
        }else{
            this@animateScrollAndCentralizeItem.animateScrollToItem(index)
        }
    }
//    println("View port center $viewPortCenter")


    println()
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CustomTangLazyColumnTheme {
//        CircularLazyColumn(CircularLazyColumnImages())
        ContentScreen()
    }
}