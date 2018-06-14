package com.msc.libcommon.widget.banner.layoutmanager

import android.content.Context
import android.graphics.PointF
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

import android.support.v7.widget.RecyclerView.NO_POSITION

/**
 * An implementation of [RecyclerView.LayoutManager] which behaves like view pager.
 * Please make sure your child view have the same size.
 */

class OverFlyingLayoutManager
/**
 * @param orientation   Layout orientation. Should be [.HORIZONTAL] or [.VERTICAL]
 * @param reverseLayout When set to true, layouts from end to start
 */
(orientation: Int, reverseLayout: Boolean) : RecyclerView.LayoutManager(), RecyclerView.SmoothScroller.ScrollVectorProvider {
    var minScale = 0.75f//两侧图片缩放比
    var angle = 8f//翻转角度
    var itemSpace = 385
    var infinite = true
        set(enable) {
            assertNotInLayoutOrScroll(null)
            if (enable == infinite) {
                return
            }
            field = enable
            requestLayout()
        }

    protected var mDecoratedMeasurement: Int = 0

    protected var mDecoratedMeasurementInOther: Int = 0

    /**
     * Current orientation. Either [.HORIZONTAL] or [.VERTICAL]
     */
    internal var mOrientation: Int = 0

    protected var mSpaceMain: Int = 0

    protected var mSpaceInOther: Int = 0

    /**
     * The offset of property which will change while scrolling
     */
    protected var mOffset: Float = 0.toFloat()

    /**
     * Many calculations are made depending on orientation. To keep it clean, this interface
     * helps [LinearLayoutManager] make those decisions.
     * Based on [.mOrientation], an implementation is lazily created in
     * [.ensureLayoutState] method.
     */
    protected var mOrientationHelper: OrientationHelper? = null

    /**
     * Defines if layout should be calculated from end to start.
     */
    private var mReverseLayout = false

    /**
     * Works the same way as [android.widget.AbsListView.setSmoothScrollbarEnabled].
     * see [android.widget.AbsListView.setSmoothScrollbarEnabled]
     */
    /**
     * Returns the current state of the smooth scrollbar feature. It is enabled by default.
     *
     * @return True if smooth scrollbar is enabled, false otherwise.
     * @see .setSmoothScrollbarEnabled
     */
    /**
     * When smooth scrollbar is enabled, the position and size of the scrollbar thumb is computed
     * based on the number of visible pixels in the visible items. This however assumes that all
     * list items have similar or equal widths or heights (depending on list orientation).
     * If you use a list in which items have different dimensions, the scrollbar will change
     * appearance as the user scrolls through the list. To avoid this issue,  you need to disable
     * this property.
     *
     *
     * When smooth scrollbar is disabled, the position and size of the scrollbar thumb is based
     * solely on the number of items in the adapter and the position of the visible items inside
     * the adapter. This provides a stable scrollbar as the user navigates through a list of items
     * with varying widths / heights.
     *
     * @param enabled Whether or not to enable smooth scrollbar.
     * @see .setSmoothScrollbarEnabled
     */
    var smoothScrollbarEnabled = true

    /**
     * When LayoutManager needs to scroll to a position, it sets this variable and requests a
     * layout which will check this variable and re-layout accordingly.
     */
    private var mPendingScrollPosition = RecyclerView.NO_POSITION

    private var mPendingSavedState: SavedState? = null

    protected var mInterval: Float = 0.toFloat() //the mInterval of each item's mOffset

    /* package */ internal var onPageChangeListener: OnPageChangeListener? = null

    /**
     * Returns whether LayoutManager will recycle its children when it is detached from
     * RecyclerView.
     *
     * @return true if LayoutManager will recycle its children when it is detached from
     * RecyclerView.
     */
    /**
     * Set whether LayoutManager will recycle its children when it is detached from
     * RecyclerView.
     *
     *
     * If you are using a [RecyclerView.RecycledViewPool], it might be a good idea to set
     * this flag to `true` so that views will be available to other RecyclerViews
     * immediately.
     *
     *
     * Note that, setting this flag will result in a performance drop if RecyclerView
     * is restored.
     *
     * @param recycleChildrenOnDetach Whether children should be recycled in detach or not.
     */
    var recycleChildrenOnDetach: Boolean = false


    var enableBringCenterToFront: Boolean = false
        set(bringCenterToTop) {
            assertNotInLayoutOrScroll(null)
            if (enableBringCenterToFront == bringCenterToTop) {
                return
            }
            field = bringCenterToTop
            requestLayout()
        }

    /**
     * ugly code for fix bug caused by float
     */
    /**
     * see [.mIntegerDy]
     */
    /**
     * see [.mIntegerDy]
     */
    var isIntegerDy = false

    private var mLeftItems: Int = 0

    private var mRightItems: Int = 0

    /**
     * max visible item count
     */
    /**
     * Returns the max visible item count, [.DETERMINE_BY_MAX_AND_MIN] means it haven't been set now
     * And it will use [.maxRemoveOffset] and [.minRemoveOffset] to handle the range
     *
     * @return Max visible item count
     */
    /**
     * Set the max visible item count, [.DETERMINE_BY_MAX_AND_MIN] means it haven't been set now
     * And it will use [.maxRemoveOffset] and [.minRemoveOffset] to handle the range
     *
     * @param mMaxVisibleItemCount Max visible item count
     */
    var maxVisibleItemCount = DETERMINE_BY_MAX_AND_MIN
        set(mMaxVisibleItemCount) {
            assertNotInLayoutOrScroll(null)
            if (this.maxVisibleItemCount == mMaxVisibleItemCount) return
            field = mMaxVisibleItemCount
            removeAllViews()
        }

    /**
     * Returns the current orientation of the layout.
     *
     * @return Current orientation,  either [.HORIZONTAL] or [.VERTICAL]
     * @see .setOrientation
     */
    /**
     * will do its best to keep scroll position.
     *
     * @param orientation [.HORIZONTAL] or [.VERTICAL]
     */
    var orientation: Int
        get() = mOrientation
        set(orientation) {
            if (orientation != HORIZONTAL && orientation != VERTICAL) {
                throw IllegalArgumentException("invalid orientation:$orientation")
            }
            assertNotInLayoutOrScroll(null)
            if (orientation == mOrientation) {
                return
            }
            mOrientation = orientation
            mOrientationHelper = null
            removeAllViews()
        }

    /**
     * Returns if views are laid out from the opposite direction of the layout.
     *
     * @return If layout is reversed or not.
     * @see .setReverseLayout
     */
    /**
     * Used to reverse item traversal and layout order.
     * This behaves similar to the layout change for RTL views. When set to true, first item is
     * laid out at the end of the UI, second item is laid out before it etc.
     *
     *
     * For horizontal layouts, it depends on the layout direction.
     * When set to true, If [android.support.v7.widget.RecyclerView] is LTR, than it will
     * layout from RTL, if [android.support.v7.widget.RecyclerView]} is RTL, it will layout
     * from LTR.
     */
    var reverseLayout: Boolean
        get() = mReverseLayout
        set(reverseLayout) {
            assertNotInLayoutOrScroll(null)
            if (reverseLayout == mReverseLayout) {
                return
            }
            mReverseLayout = reverseLayout
            removeAllViews()
        }

    val totalSpaceInOther: Int
        get() = if (mOrientation == HORIZONTAL) {
            (height - paddingTop
                    - paddingBottom)
        } else {
            (width - paddingLeft
                    - paddingRight)
        }

    private val maxOffset: Float
        get() = if (!mReverseLayout) (itemCount - 1) * mInterval else 0f

    private val minOffset: Float
        get() = if (!mReverseLayout) 0f else -(itemCount - 1) * mInterval

    protected val distanceRatio: Float
        get() = 1f

    //take care of position = getItemCount()
    val currentPosition: Int
        get() {
            var position = currentPositionOffset
            if (!infinite) return Math.abs(position)
            position = if (!mReverseLayout)
                if (position >= 0)
                    position % itemCount
                else
                    itemCount + position % itemCount
            else
                if (position > 0)
                    itemCount - position % itemCount
                else
                    -position % itemCount
            return position
        }

    private val currentPositionOffset: Int
        get() = Math.round(mOffset / mInterval)

    /**
     * Sometimes we need to get the right offset of matching adapter position
     * cause when [.mInfinite] is set true, there will be no limitation of [.mOffset]
     */
    private val offsetOfRightAdapterPosition: Float
        get() = if (mReverseLayout)
            if (infinite)
                if (mOffset <= 0)
                    mOffset % (mInterval * itemCount)
                else
                    itemCount * -mInterval + mOffset % (mInterval * itemCount)
            else
                mOffset
        else
            if (infinite)
                if (mOffset >= 0)
                    mOffset % (mInterval * itemCount)
                else
                    itemCount * mInterval + mOffset % (mInterval * itemCount)
            else
                mOffset

    /**
     * @return the dy between center and current position
     */
    val offsetToCenter: Int
        get() = if (infinite) ((currentPositionOffset * mInterval - mOffset) * distanceRatio).toInt() else ((currentPosition * if (!mReverseLayout) mInterval else -mInterval - mOffset) * distanceRatio).toInt()

    /**
     * @return the mInterval of each item's mOffset
     */
    protected fun setInterval(): Float {
        return (mDecoratedMeasurement - itemSpace).toFloat()
    }

    protected fun setItemViewProperty(itemView: View, targetOffset: Float) {
        val scale = calculateScale(targetOffset + mSpaceMain)
        itemView.scaleX = scale
        itemView.scaleY = scale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemView.elevation = 0f
        }
        val rotation = calRotation(targetOffset)
        if (orientation == HORIZONTAL) {
            itemView.rotationY = rotation
        } else {
            itemView.rotationX = -rotation
        }
    }

    private fun calRotation(targetOffset: Float): Float {
        return -angle / mInterval * targetOffset
    }

    private fun calculateScale(x: Float): Float {
        val deltaX = Math.abs(x - (mOrientationHelper!!.totalSpace - mDecoratedMeasurement) / 2f)
        return (minScale - 1) * deltaX / (mOrientationHelper!!.totalSpace / 2f) + 1f
    }

    /**
     * cause elevation is not support below api 21,
     * so you can set your elevation here for supporting it below api 21
     * or you can just setElevation in [.setItemViewProperty]
     */
    protected fun setViewElevation(itemView: View, targetOffset: Float): Float {
        return itemView.scaleX * 5
    }

    /**
     * Creates a horizontal ViewPagerLayoutManager
     */
    constructor(context: Context) : this(HORIZONTAL, false) {}

    init {
        mOrientation = orientation
        mReverseLayout = reverseLayout
        isAutoMeasureEnabled = true
        enableBringCenterToFront = true
        isIntegerDy = true
    }

    constructor(minScale: Float, itemSpace: Int, orientation: Int) : this(orientation, false) {
        this.minScale = minScale
        this.itemSpace = itemSpace
        mOrientation = orientation
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDetachedFromWindow(view: RecyclerView, recycler: RecyclerView.Recycler?) {
        super.onDetachedFromWindow(view, recycler)
        if (recycleChildrenOnDetach) {
            removeAndRecycleAllViews(recycler)
            recycler!!.clear()
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        if (mPendingSavedState != null) {
            return SavedState(mPendingSavedState!!)
        }
        val savedState = SavedState()
        savedState.position = mPendingScrollPosition
        savedState.offset = mOffset
        savedState.isReverseLayout = mReverseLayout
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            mPendingSavedState = SavedState((state as SavedState?)!!)
            requestLayout()
        }
    }

    /**
     * @return true if [.getOrientation] is [.HORIZONTAL]
     */
    override fun canScrollHorizontally(): Boolean {
        return mOrientation == HORIZONTAL
    }

    /**
     * @return true if [.getOrientation] is [.VERTICAL]
     */
    override fun canScrollVertically(): Boolean {
        return mOrientation == VERTICAL
    }

    /**
     * Calculates the view layout order. (e.g. from end to start or start to end)
     * RTL layout support is applied automatically. So if layout is RTL and
     * [.getReverseLayout] is `true`, elements will be laid out starting from left.
     */
    private fun resolveShouldLayoutReverse() {
        if (mOrientation == HORIZONTAL && layoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL) {
            mReverseLayout = !mReverseLayout
        }
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        val linearSmoothScroller = LinearSmoothScroller(recyclerView!!.context)
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        if (childCount == 0) {
            return null
        }
        val firstChildPos = getPosition(getChildAt(0))
        val direction = if (targetPosition < firstChildPos == !mReverseLayout)
            -1 / distanceRatio
        else
            1 / distanceRatio
        return if (mOrientation == HORIZONTAL) {
            PointF(direction, 0f)
        } else {
            PointF(0f, direction)
        }
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if (state!!.itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            mOffset = 0f
            return
        }

        ensureLayoutState()
        resolveShouldLayoutReverse()

        //make sure properties are correct while measure more than once
        val scrap = recycler!!.getViewForPosition(0)
        measureChildWithMargins(scrap, 0, 0)
        mDecoratedMeasurement = mOrientationHelper!!.getDecoratedMeasurement(scrap)
        mDecoratedMeasurementInOther = mOrientationHelper!!.getDecoratedMeasurementInOther(scrap)
        mSpaceMain = (mOrientationHelper!!.totalSpace - mDecoratedMeasurement) / 2
        mSpaceInOther = (totalSpaceInOther - mDecoratedMeasurementInOther) / 2
        mInterval = setInterval()
        setUp()
        mLeftItems = Math.abs(minRemoveOffset() / mInterval).toInt() + 1
        mRightItems = Math.abs(maxRemoveOffset() / mInterval).toInt() + 1

        if (mPendingSavedState != null) {
            mReverseLayout = mPendingSavedState!!.isReverseLayout
            mPendingScrollPosition = mPendingSavedState!!.position
            mOffset = mPendingSavedState!!.offset
        }

        if (mPendingScrollPosition != RecyclerView.NO_POSITION) {
            mOffset = if (mReverseLayout)
                mPendingScrollPosition * -mInterval
            else
                mPendingScrollPosition * mInterval
        }

        detachAndScrapAttachedViews(recycler)
        layoutItems(recycler)
    }

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        mPendingSavedState = null
        mPendingScrollPosition = RecyclerView.NO_POSITION
    }

    internal fun ensureLayoutState() {
        if (mOrientationHelper == null) {
            mOrientationHelper = OrientationHelper.createOrientationHelper(this, mOrientation)
        }
    }

    /**
     * You can set up your own properties here or change the exist properties like mSpaceMain and mSpaceInOther
     */
    protected fun setUp() {

    }

    private fun getProperty(position: Int): Float {
        return if (mReverseLayout) position * -mInterval else position * mInterval
    }

    override fun onAdapterChanged(oldAdapter: RecyclerView.Adapter<*>?, newAdapter: RecyclerView.Adapter<*>?) {
        removeAllViews()
        mOffset = 0f
    }

    override fun scrollToPosition(position: Int) {
        mPendingScrollPosition = position
        mOffset = if (mReverseLayout) position * -mInterval else position * mInterval
        requestLayout()
    }

    override fun computeHorizontalScrollOffset(state: RecyclerView.State?): Int {
        return computeScrollOffset()
    }

    override fun computeVerticalScrollOffset(state: RecyclerView.State?): Int {
        return computeScrollOffset()
    }

    override fun computeHorizontalScrollExtent(state: RecyclerView.State?): Int {
        return computeScrollExtent()
    }

    override fun computeVerticalScrollExtent(state: RecyclerView.State?): Int {
        return computeScrollExtent()
    }

    override fun computeHorizontalScrollRange(state: RecyclerView.State?): Int {
        return computeScrollRange()
    }

    override fun computeVerticalScrollRange(state: RecyclerView.State?): Int {
        return computeScrollRange()
    }

    private fun computeScrollOffset(): Int {
        if (childCount == 0) {
            return 0
        }

        if (!smoothScrollbarEnabled) {
            return if (!mReverseLayout)
                currentPosition
            else
                itemCount - currentPosition - 1
        }

        val realOffset = offsetOfRightAdapterPosition
        return if (!mReverseLayout) realOffset.toInt() else ((itemCount - 1) * mInterval + realOffset).toInt()
    }

    private fun computeScrollExtent(): Int {
        if (childCount == 0) {
            return 0
        }

        return if (!smoothScrollbarEnabled) {
            1
        } else mInterval.toInt()

    }

    private fun computeScrollRange(): Int {
        if (childCount == 0) {
            return 0
        }

        return if (!smoothScrollbarEnabled) {
            itemCount
        } else (itemCount * mInterval).toInt()

    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        return if (mOrientation == VERTICAL) {
            0
        } else scrollBy(dx, recycler, state)
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        return if (mOrientation == HORIZONTAL) {
            0
        } else scrollBy(dy, recycler, state)
    }

    private fun scrollBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        if (childCount == 0 || dy == 0) {
            return 0
        }
        ensureLayoutState()
        var willScroll = dy

        var realDx = dy / distanceRatio
        if (Math.abs(realDx) < 0.00000001f) {
            return 0
        }
        val targetOffset = mOffset + realDx

        //handle the boundary
        if (!infinite && targetOffset < minOffset) {
            willScroll -= ((targetOffset - minOffset) * distanceRatio).toInt()
        } else if (!infinite && targetOffset > maxOffset) {
            willScroll = ((maxOffset - mOffset) * distanceRatio).toInt()
        }

        if (isIntegerDy) {
            realDx = (willScroll / distanceRatio).toInt().toFloat()
        } else {
            realDx = willScroll / distanceRatio
        }

        mOffset += realDx

        // we re-layout all current views in the right place
        for (i in 0 until childCount) {
            val scrap = getChildAt(i)
            val delta = propertyChangeWhenScroll(scrap) - realDx
            layoutScrap(scrap, delta)
        }

        //handle recycle
        layoutItems(recycler)

        return willScroll
    }

    private fun layoutItems(recycler: RecyclerView.Recycler?) {
        detachAndScrapAttachedViews(recycler)

        // make sure that current position start from 0 to 1
        val currentPos = if (mReverseLayout)
            -currentPositionOffset
        else
            currentPositionOffset
        var start = currentPos - mLeftItems
        var end = currentPos + mRightItems

        // handle max visible count
        if (useMaxVisibleCount()) {
            val isEven = maxVisibleItemCount % 2 == 0
            if (isEven) {
                val offset = maxVisibleItemCount / 2
                start = currentPos - offset + 1
                end = currentPos + offset + 1
            } else {
                val offset = (maxVisibleItemCount - 1) / 2
                start = currentPos - offset
                end = currentPos + offset + 1
            }
        }

        val itemCount = itemCount
        if (!infinite) {
            if (start < 0) {
                start = 0
                if (useMaxVisibleCount()) end = maxVisibleItemCount
            }
            if (end > itemCount) end = itemCount
        }

        var lastOrderWeight:Float = java.lang.Float.MIN_VALUE
        for (i in start until end) {
            if (useMaxVisibleCount() || !removeCondition(getProperty(i) - mOffset)) {
                // start and end base on current position,
                // so we need to calculate the adapter position
                var adapterPosition = i
                if (i >= itemCount) {
                    adapterPosition %= itemCount
                } else if (i < 0) {
                    var delta = -adapterPosition % itemCount
                    if (delta == 0) delta = itemCount
                    adapterPosition = itemCount - delta
                }
                val scrap = recycler!!.getViewForPosition(adapterPosition)
                measureChildWithMargins(scrap, 0, 0)
                resetViewProperty(scrap)
                // we need i to calculate the real offset of current view
                val targetOffset = getProperty(i) - mOffset
                layoutScrap(scrap, targetOffset)
                val orderWeight:Float = if (enableBringCenterToFront)
                    setViewElevation(scrap, targetOffset)
                else
                    adapterPosition.toFloat()
                if (orderWeight > lastOrderWeight) {
                    addView(scrap)
                } else {
                    addView(scrap, 0)
                }
                lastOrderWeight = orderWeight
            }
        }
    }

    private fun useMaxVisibleCount(): Boolean {
        return maxVisibleItemCount != DETERMINE_BY_MAX_AND_MIN
    }

    private fun removeCondition(targetOffset: Float): Boolean {
        return targetOffset > maxRemoveOffset() || targetOffset < minRemoveOffset()
    }

    private fun resetViewProperty(v: View) {
        v.rotation = 0f
        v.rotationY = 0f
        v.rotationX = 0f
        v.scaleX = 1f
        v.scaleY = 1f
        v.alpha = 1f
    }

    private fun layoutScrap(scrap: View, targetOffset: Float) {
        val left = calItemLeft(scrap, targetOffset)
        val top = calItemTop(scrap, targetOffset)
        if (mOrientation == VERTICAL) {
            layoutDecorated(scrap, mSpaceInOther + left, mSpaceMain + top,
                    mSpaceInOther + left + mDecoratedMeasurementInOther, mSpaceMain + top + mDecoratedMeasurement)
        } else {
            layoutDecorated(scrap, mSpaceMain + left, mSpaceInOther + top,
                    mSpaceMain + left + mDecoratedMeasurement, mSpaceInOther + top + mDecoratedMeasurementInOther)
        }
        setItemViewProperty(scrap, targetOffset)
    }

    protected fun calItemLeft(itemView: View, targetOffset: Float): Int {
        return if (mOrientation == VERTICAL) 0 else targetOffset.toInt()
    }

    protected fun calItemTop(itemView: View, targetOffset: Float): Int {
        return if (mOrientation == VERTICAL) targetOffset.toInt() else 0
    }

    /**
     * when the target offset reach this,
     * the view will be removed and recycled in [.layoutItems]
     */
    protected fun maxRemoveOffset(): Float {
        return (mOrientationHelper!!.totalSpace - mSpaceMain).toFloat()
    }

    /**
     * when the target offset reach this,
     * the view will be removed and recycled in [.layoutItems]
     */
    protected fun minRemoveOffset(): Float {
        return (-mDecoratedMeasurement - mOrientationHelper!!.startAfterPadding - mSpaceMain).toFloat()
    }

    protected fun propertyChangeWhenScroll(itemView: View): Float {
        return if (mOrientation == VERTICAL) (itemView.top - mSpaceMain).toFloat() else (itemView.left - mSpaceMain).toFloat()
    }

    fun setOnPageChangeListener(onPageChangeListener: OnPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener
    }

    private class SavedState : Parcelable {
        internal var position: Int = 0
        internal var offset: Float = 0.toFloat()
        internal var isReverseLayout: Boolean = false

        internal constructor() {

        }

        internal constructor(`in`: Parcel) {
            position = `in`.readInt()
            offset = `in`.readFloat()
            isReverseLayout = `in`.readInt() == 1
        }

        constructor(other: SavedState) {
            position = other.position
            offset = other.offset
            isReverseLayout = other.isReverseLayout
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(position)
            dest.writeFloat(offset)
            dest.writeInt(if (isReverseLayout) 1 else 0)
        }

        companion object {

            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    interface OnPageChangeListener {
        fun onPageSelected(position: Int)

        fun onPageScrollStateChanged(state: Int)
    }

    companion object {
        val DETERMINE_BY_MAX_AND_MIN = -1

        val HORIZONTAL = OrientationHelper.HORIZONTAL

        val VERTICAL = OrientationHelper.VERTICAL
    }
}