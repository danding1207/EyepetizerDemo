package com.msc.libcommon.widget.banner

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Handler
import android.os.Message
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView

import com.msc.libcommon.R

import java.util.ArrayList

abstract class RecyclerViewBannerBase<L : RecyclerView.LayoutManager, A : RecyclerView.Adapter<*>> @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var autoPlayDuration = 4000//刷新间隔时间

    private var showIndicator: Boolean = false//是否显示指示器
    private var indicatorContainer: RecyclerView? = null
    private var mSelectedDrawable: Drawable? = null
    private var mUnselectedDrawable: Drawable? = null
    private var indicatorAdapter: IndicatorAdapter? = null
    private var indicatorMargin: Int = 0//指示器间距

    private var mRecyclerView: RecyclerView? = null
    private var adapter: A? = null
    private var mLayoutManager: L? = null


    private var WHAT_AUTO_PLAY = 1000

    private var hasInit: Boolean = false
    private var bannerSize = 1
    private var currentIndex: Int = 0
    private var isPlaying: Boolean = false

    private var isAutoPlaying: Boolean = false
    private var tempUrlList: List<String> = ArrayList()


    private var mHandler:Handler? = null

    init {
        initView(context, attrs)
    }

    protected fun initView(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewBannerBase)
        showIndicator = a.getBoolean(R.styleable.RecyclerViewBannerBase_showIndicator, true)
        autoPlayDuration = a.getInt(R.styleable.RecyclerViewBannerBase_interval, 4000)
        isAutoPlaying = a.getBoolean(R.styleable.RecyclerViewBannerBase_autoPlaying, true)
        mSelectedDrawable = a.getDrawable(R.styleable.RecyclerViewBannerBase_indicatorSelectedSrc)
        mUnselectedDrawable = a.getDrawable(R.styleable.RecyclerViewBannerBase_indicatorUnselectedSrc)


        mHandler = Handler(Handler.Callback { msg ->
            if (msg.what == WHAT_AUTO_PLAY) {
                mRecyclerView!!.smoothScrollToPosition(++currentIndex)
                refreshIndicator()
                mHandler!!.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration.toLong())
            }
            false
        })

        if (mSelectedDrawable == null) {
            //绘制默认选中状态图形
            val selectedGradientDrawable = GradientDrawable()
            selectedGradientDrawable.shape = GradientDrawable.OVAL
            selectedGradientDrawable.setColor(Color.RED)
            selectedGradientDrawable.setSize(dp2px(5), dp2px(5))
            selectedGradientDrawable.cornerRadius = (dp2px(5) / 2).toFloat()
            mSelectedDrawable = LayerDrawable(arrayOf<Drawable>(selectedGradientDrawable))
        }
        if (mUnselectedDrawable == null) {
            //绘制默认未选中状态图形
            val unSelectedGradientDrawable = GradientDrawable()
            unSelectedGradientDrawable.shape = GradientDrawable.OVAL
            unSelectedGradientDrawable.setColor(Color.GRAY)
            unSelectedGradientDrawable.setSize(dp2px(5), dp2px(5))
            unSelectedGradientDrawable.cornerRadius = (dp2px(5) / 2).toFloat()
            mUnselectedDrawable = LayerDrawable(arrayOf<Drawable>(unSelectedGradientDrawable))
        }

        indicatorMargin = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorSpace, dp2px(4))
        val marginLeft = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorMarginLeft, dp2px(16))
        val marginRight = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorMarginRight, dp2px(0))
        val marginBottom = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorMarginBottom, dp2px(11))
        val g = a.getInt(R.styleable.RecyclerViewBannerBase_indicatorGravity, 0)
        val gravity: Int
        if (g == 0) {
            gravity = GravityCompat.START
        } else if (g == 2) {
            gravity = GravityCompat.END
        } else {
            gravity = Gravity.CENTER
        }
        val o = a.getInt(R.styleable.RecyclerViewBannerBase_orientation, 0)
        var orientation = 0
        if (o == 0) {
            orientation = LinearLayoutManager.HORIZONTAL
        } else if (o == 1) {
            orientation = LinearLayoutManager.VERTICAL
        }
        a.recycle()
        //recyclerView部分
        mRecyclerView = RecyclerView(context)
        PagerSnapHelper().attachToRecyclerView(mRecyclerView)
        mLayoutManager = getLayoutManager(context, orientation)
        mRecyclerView!!.layoutManager = mLayoutManager
        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                onBannerScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                onBannerScrollStateChanged(recyclerView, newState)

            }
        })
        val vpLayoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        addView(mRecyclerView, vpLayoutParams)
        //指示器部分
        indicatorContainer = RecyclerView(context)

        val indicatorLayoutManager = LinearLayoutManager(context, orientation, false)
        indicatorContainer!!.layoutManager = indicatorLayoutManager
        indicatorAdapter = IndicatorAdapter()
        indicatorContainer!!.adapter = indicatorAdapter
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.BOTTOM or gravity
        params.setMargins(marginLeft, 0, marginRight, marginBottom)
        addView(indicatorContainer, params)
        if (!showIndicator) {
            indicatorContainer!!.visibility = View.GONE
        }
    }

    protected fun onBannerScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {

    }

    protected fun onBannerScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {

    }

    protected abstract fun getLayoutManager(context: Context, orientation: Int): L

    protected abstract fun getAdapter(context: Context, list: List<String>, onBannerItemClickListener: OnBannerItemClickListener?): A

    /**
     * 设置轮播间隔时间
     *
     * @param millisecond 时间毫秒
     */
    fun setIndicatorInterval(millisecond: Int) {
        this.autoPlayDuration = millisecond
    }

    /**
     * 设置是否自动播放（上锁）
     *
     * @param playing 开始播放
     */
    @Synchronized
    protected fun setPlaying(playing: Boolean) {
        if (isAutoPlaying && hasInit) {
            if (!isPlaying && playing) {
                mHandler!!.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration.toLong())
                isPlaying = true
            } else if (isPlaying && !playing) {
                mHandler!!.removeMessages(WHAT_AUTO_PLAY)
                isPlaying = false
            }
        }
    }

    /**
     * 设置是否禁止滚动播放
     */
    fun setAutoPlaying(isAutoPlaying: Boolean) {
        this.isAutoPlaying = isAutoPlaying
        setPlaying(this.isAutoPlaying)
    }

    fun setShowIndicator(showIndicator: Boolean) {
        this.showIndicator = showIndicator
        indicatorContainer!!.visibility = if (showIndicator) View.VISIBLE else View.GONE
    }

    /**
     * 设置轮播数据集
     */
    @JvmOverloads
    fun initBannerImageView(newList: List<String>, onBannerItemClickListener: OnBannerItemClickListener? = null) {
        //解决recyclerView嵌套问题
        if (compareListDifferent(newList, tempUrlList)) {
            hasInit = false
            visibility = View.VISIBLE
            setPlaying(false)
            adapter = getAdapter(context, newList, onBannerItemClickListener)
            mRecyclerView!!.adapter = adapter
            tempUrlList = newList
            bannerSize = tempUrlList.size
            if (bannerSize > 1) {
                indicatorContainer!!.visibility = View.VISIBLE
                currentIndex = bannerSize * 10000
                mRecyclerView!!.scrollToPosition(currentIndex)
                indicatorAdapter!!.notifyDataSetChanged()
                setPlaying(true)
            } else {
                indicatorContainer!!.visibility = View.GONE
                currentIndex = 0
            }
            hasInit = true
        }
        if (!showIndicator) {
            indicatorContainer!!.visibility = View.GONE
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> setPlaying(false)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> setPlaying(true)
        }
        //解决recyclerView嵌套问题
        try {
            return super.dispatchTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }

        return false
    }

    //解决recyclerView嵌套问题
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        try {
            return super.onTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }

        return false
    }

    //解决recyclerView嵌套问题
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }

        return false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setPlaying(true)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setPlaying(false)
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == View.VISIBLE) {
            setPlaying(true)
        } else {
            setPlaying(false)
        }
    }

    /**
     * 标示点适配器
     */
    protected inner class IndicatorAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        internal var currentPosition = 0

        fun setPosition(currentPosition: Int) {
            this.currentPosition = currentPosition
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            val bannerPoint = ImageView(context)
            val lp = RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.setMargins(indicatorMargin, indicatorMargin, indicatorMargin, indicatorMargin)
            bannerPoint.layoutParams = lp
            return object : RecyclerView.ViewHolder(bannerPoint) {

            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val bannerPoint = holder.itemView as ImageView
            bannerPoint.setImageDrawable(if (currentPosition == position) mSelectedDrawable else mUnselectedDrawable)

        }

        override fun getItemCount(): Int {
            return bannerSize
        }
    }


    /**
     * 改变导航的指示点
     */
    @Synchronized
    protected fun refreshIndicator() {
        if (showIndicator && bannerSize > 1) {
            indicatorAdapter!!.setPosition(currentIndex % bannerSize)
            indicatorAdapter!!.notifyDataSetChanged()
        }
    }

    interface OnBannerItemClickListener {
        fun onItemClick(position: Int)
    }

    protected fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
                Resources.getSystem().displayMetrics).toInt()
    }

    /**
     * 获取颜色
     */
    protected fun getColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(context, color)
    }

    protected fun compareListDifferent(newTabList: List<String>, oldTabList: List<String>?): Boolean {
        if (oldTabList == null || oldTabList.isEmpty())
            return true
        if (newTabList.size != oldTabList.size)
            return true
        for (i in newTabList.indices) {
            if (TextUtils.isEmpty(newTabList[i]))
                return true
            if (newTabList[i] != oldTabList[i]) {
                return true
            }
        }
        return false
    }

}