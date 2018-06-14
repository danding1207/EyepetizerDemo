package com.msc.libcommon.widget.banner.layoutmanager

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.LayoutManager
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller

/**
 * Class intended to support snapping for a [RecyclerView]
 * which use [BannerLayoutManager] as its [LayoutManager].
 *
 *
 * The implementation will snap the center of the target child view to the center of
 * the attached [RecyclerView].
 */
class CenterSnapHelper : RecyclerView.OnFlingListener() {

    private var mRecyclerView: RecyclerView? = null
    private var mGravityScroller: Scroller? = null

    /**
     * when the dataSet is extremely large
     * [.snapToCenterView]
     * may keep calling itself because the accuracy of float
     */
    private var snapToCenter = false

    // Handles the snap on scroll case.
    private val mScrollListener = object : RecyclerView.OnScrollListener() {

        internal var mScrolled = false

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            val layoutManager = recyclerView!!.layoutManager as BannerLayoutManager
            val onPageChangeListener = layoutManager.onPageChangeListener
            if (onPageChangeListener != null) {
                onPageChangeListener!!.onPageScrollStateChanged(newState)
            }

            if (newState == RecyclerView.SCROLL_STATE_IDLE && mScrolled) {
                mScrolled = false
                if (!snapToCenter) {
                    snapToCenter = true
                    snapToCenterView(layoutManager, onPageChangeListener)
                } else {
                    snapToCenter = false
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            if (dx != 0 || dy != 0) {
                mScrolled = true
            }
        }
    }

    override fun onFling(velocityX: Int, velocityY: Int): Boolean {
        val layoutManager = mRecyclerView!!.layoutManager as BannerLayoutManager
        val adapter = mRecyclerView!!.adapter ?: return false

        if (!layoutManager.infinite && (layoutManager.mOffset == layoutManager.maxOffset || layoutManager.mOffset == layoutManager.minOffset)) {
            return false
        }

        val minFlingVelocity = mRecyclerView!!.minFlingVelocity
        mGravityScroller!!.fling(0, 0, velocityX, velocityY,
                Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE)

        if (layoutManager.mOrientation === BannerLayoutManager.VERTICAL && Math.abs(velocityY) > minFlingVelocity) {
            val currentPosition = layoutManager.currentPosition
            val offsetPosition = (mGravityScroller!!.finalY.toFloat() /
                    layoutManager.mInterval / layoutManager.distanceRatio).toInt()
            mRecyclerView!!.smoothScrollToPosition(if (layoutManager.reverseLayout)
                currentPosition - offsetPosition
            else
                currentPosition + offsetPosition)
            return true
        } else if (layoutManager.mOrientation === BannerLayoutManager.HORIZONTAL && Math.abs(velocityX) > minFlingVelocity) {
            val currentPosition = layoutManager.currentPosition
            val offsetPosition = (mGravityScroller!!.finalX.toFloat() /
                    layoutManager.mInterval / layoutManager.distanceRatio).toInt()
            mRecyclerView!!.smoothScrollToPosition(if (layoutManager.reverseLayout)
                currentPosition - offsetPosition
            else
                currentPosition + offsetPosition)
            return true
        }

        return true
    }

    /**
     * Please attach after {[LayoutManager] is setting}
     * Attaches the [CenterSnapHelper] to the provided RecyclerView, by calling
     * [RecyclerView.setOnFlingListener].
     * You can call this method with `null` to detach it from the current RecyclerView.
     *
     * @param recyclerView The RecyclerView instance to which you want to add this helper or
     * `null` if you want to remove CenterSnapHelper from the current
     * RecyclerView.
     * @throws IllegalArgumentException if there is already a [RecyclerView.OnFlingListener]
     * attached to the provided [RecyclerView].
     */
    @Throws(IllegalStateException::class)
    fun attachToRecyclerView(recyclerView: RecyclerView?) {
        if (mRecyclerView === recyclerView) {
            return  // nothing to do
        }
        if (mRecyclerView != null) {
            destroyCallbacks()
        }
        mRecyclerView = recyclerView
        if (mRecyclerView != null) {
            val layoutManager = mRecyclerView!!.layoutManager as? BannerLayoutManager ?: return

            setupCallbacks()
            mGravityScroller = Scroller(mRecyclerView!!.context,
                    DecelerateInterpolator())

            snapToCenterView(layoutManager,
                    layoutManager.onPageChangeListener)
        }
    }

    internal fun snapToCenterView(layoutManager: BannerLayoutManager,
                                  listener: BannerLayoutManager.OnPageChangeListener?) {
        val delta = layoutManager.offsetToCenter
        if (delta != 0) {
            if (layoutManager.orientation == BannerLayoutManager.VERTICAL)
                mRecyclerView!!.smoothScrollBy(0, delta)
            else
                mRecyclerView!!.smoothScrollBy(delta, 0)
        } else {
            // set it false to make smoothScrollToPosition keep trigger the listener
            snapToCenter = false
        }

        listener?.onPageSelected(layoutManager.currentPosition)
    }

    /**
     * Called when an instance of a [RecyclerView] is attached.
     */
    @Throws(IllegalStateException::class)
    internal fun setupCallbacks() {
        if (mRecyclerView!!.onFlingListener != null) {
            throw IllegalStateException("An instance of OnFlingListener already set.")
        }
        mRecyclerView!!.addOnScrollListener(mScrollListener)
        mRecyclerView!!.onFlingListener = this
    }

    /**
     * Called when the instance of a [RecyclerView] is detached.
     */
    internal fun destroyCallbacks() {
        mRecyclerView!!.removeOnScrollListener(mScrollListener)
        mRecyclerView!!.onFlingListener = null
    }
}