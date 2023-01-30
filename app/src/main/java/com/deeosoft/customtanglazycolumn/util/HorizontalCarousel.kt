package com.deeosoft.customtanglazycolumn.util

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HorizontalCarousel (context: Context): RecyclerView(context) {
    private var sidePadding: Int = 0
    private var recyclerCenter: Int = 0
    private var childCenter: Int = 0
    private var childCenterY: Int = 0

    fun <T : ViewHolder> initialize(newAdapter: Adapter<T>) {
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        newAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                post {
                    recyclerCenter = width / 2
                    childCenter = getChildAt(0).width / 2
                    childCenterY = (getChildAt(0).top + getChildAt(0).bottom) / 2


                    println("Recycler Center is $recyclerCenter")
                    println("Child Center is $childCenter")

                    post {
                        (0 until childCount).forEach { position ->
                            val child = getChildAt(position)
                            val childCenterX = (child.left + child.right) / 2
                            val scaleValue = getGaussianScale(childCenterX, 1f, 1f, 140.toDouble())
                            println("The Scale Factor for position $position is $scaleValue")
                            child.scaleX = scaleValue
                            child.scaleY = scaleValue
                            val params = child.layoutParams as LayoutParams

                            if(childCenterX == recyclerCenter){
                                params.topMargin = child.marginTop
                                child.layoutParams = params
                            }else{
                                params.topMargin = (childCenterY / scaleValue).toInt()
                                child.layoutParams = params
                            }
                        }
                    }

                    sidePadding = recyclerCenter - childCenter
                    setPadding(sidePadding, 0, sidePadding, 0)
                    scrollToPosition(0)
                    addOnScrollListener(object : OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            onScrollChanged()
                        }
                    })
                }
            }
        })
        adapter = newAdapter
    }

    private fun onScrollChanged() {
        post {
            (0 until childCount).forEach { position ->
                val child = getChildAt(position)
                val childCenterX = (child.left + child.right) / 2
                val scaleValue = getGaussianScale(childCenterX, 1f, 1f, 140.toDouble())
                child.scaleX = scaleValue
                child.scaleY = scaleValue
                val params = child.layoutParams as LayoutParams

                if(childCenterX == recyclerCenter){
                    params.topMargin = child.marginTop
                    child.layoutParams = params
                }else{
                    params.topMargin = (childCenterY / scaleValue).toInt()
                    child.layoutParams = params
                }
            }
        }
    }

    private fun getGaussianScale(
        childCenterX: Int,
        minScaleOffset: Float,
        scaleFactor: Float,
        spreadFactor: Double
    ): Float {
        val recyclerCenterX = (left + right) / 2
        return (Math.pow(
            Math.E,
            -Math.pow(childCenterX - recyclerCenterX.toDouble(), 2.toDouble()) / (2 * Math.pow(
                spreadFactor,
                2.toDouble()
            ))
        ) * scaleFactor + minScaleOffset).toFloat()
    }
}