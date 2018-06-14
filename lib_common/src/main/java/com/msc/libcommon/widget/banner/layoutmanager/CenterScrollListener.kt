package com.msc.libcommon.widget.banner.layoutmanager

import android.support.v7.widget.RecyclerView


/**
 * A [android.support.v7.widget.RecyclerView.OnScrollListener] which helps [OverFlyingLayoutManager]
 * to center the current position
 */
class CenterScrollListener : RecyclerView.OnScrollListener() {
    private var mAutoSet = false

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val layoutManager = recyclerView!!.layoutManager

        val onPageChangeListener = (layoutManager as OverFlyingLayoutManager).onPageChangeListener
        onPageChangeListener?.onPageScrollStateChanged(newState)

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (mAutoSet) {
                onPageChangeListener?.onPageSelected(layoutManager.currentPosition)
                mAutoSet = false
            } else {
                val delta: Int
                delta = layoutManager.offsetToCenter
                if (delta != 0) {
                    if (layoutManager.orientation == OverFlyingLayoutManager.VERTICAL)
                        recyclerView.smoothScrollBy(0, delta)
                    else
                        recyclerView.smoothScrollBy(delta, 0)
                    mAutoSet = true
                } else {
                    onPageChangeListener?.onPageSelected(layoutManager.currentPosition)
                    mAutoSet = false
                }
            }
        } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
            mAutoSet = false
        }
    }
}