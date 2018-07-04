package com.msc.libcommon.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

import com.msc.libcommon.R
import com.msc.libcommon.widget.banner.BannerLayout
import com.msc.libcommon.widget.banner.layoutmanager.BannerLayoutManager
import com.msc.libcommon.widget.banner.layoutmanager.CenterSnapHelper
import com.orhanobut.logger.Logger

import java.util.Calendar
import java.util.concurrent.TimeUnit

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SkipView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var ivExtra: ImageView
    private lateinit var tvSkipTip: TextView
    private var timeBeforeSkip: Int = 0
    private var mDisposable: Disposable? = null

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null)
            : this(context, attrs, 0)

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {

        //        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerLayout);
        //        showIndicator = a.getBoolean(R.styleable.BannerLayout_showIndicator, true);
        //        autoPlayDuration = a.getInt(R.styleable.BannerLayout_interval, 4000);
        //        isAutoPlaying = a.getBoolean(R.styleable.BannerLayout_autoPlaying, true);
        //        itemSpace = a.getInt(R.styleable.BannerLayout_itemSpace, 20);
        //        centerScale = a.getFloat(R.styleable.BannerLayout_centerScale, 1.2f);
        //        moveSpeed = a.getFloat(R.styleable.BannerLayout_moveSpeed, 1.0f);
        //        if (mSelectedDrawable == null) {
        //            //绘制默认选中状态图形
        //            GradientDrawable selectedGradientDrawable = new GradientDrawable();
        //            selectedGradientDrawable.setShape(GradientDrawable.OVAL);
        //            selectedGradientDrawable.setColor(Color.RED);
        //            selectedGradientDrawable.setSize(dp2px(5), dp2px(5));
        //            selectedGradientDrawable.setCornerRadius(dp2px(5) / 2);
        //            mSelectedDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});
        //        }
        //        if (mUnselectedDrawable == null) {
        //            //绘制默认未选中状态图形
        //            GradientDrawable unSelectedGradientDrawable = new GradientDrawable();
        //            unSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
        //            unSelectedGradientDrawable.setColor(Color.GRAY);
        //            unSelectedGradientDrawable.setSize(dp2px(5), dp2px(5));
        //            unSelectedGradientDrawable.setCornerRadius(dp2px(5) / 2);
        //            mUnselectedDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});
        //        }
        //
        //        indicatorMargin = dp2px(4);
        //        int marginLeft = dp2px(16);
        //        int marginRight = dp2px(0);
        //        int marginBottom = dp2px(11);
        //        int gravity = GravityCompat.START;
        //        int o = a.getInt(R.styleable.BannerLayout_orientation, 0);
        //        int orientation = 0;
        //        if (o == 0) {
        //            orientation = OrientationHelper.HORIZONTAL;
        //        } else if (o == 1) {
        //            orientation = OrientationHelper.VERTICAL;
        //        }
        //        a.recycle();

        //轮播图部分


    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        LayoutInflater.from(context).inflate(R.layout.layout_skip_view, this)
        ivExtra = findViewById(R.id.iv_extra)
        tvSkipTip = findViewById(R.id.tv_skip_tip)
    }

    fun startSkipCountDown(timeBeforeSkip: Int) {

        this.timeBeforeSkip = if (timeBeforeSkip > 0) timeBeforeSkip else 0

        if (mDisposable != null) {
            mDisposable!!.dispose()
            mDisposable = null
        }


        mDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { updateSkipTip() }


        tvSkipTip.isEnabled = false
    }

    private fun updateSkipTip() {

        if(timeBeforeSkip>0) {
            tvSkipTip.text = "${timeBeforeSkip}秒后可以跳过"
            timeBeforeSkip--
        } else {
            tvSkipTip.text = "跳过"
            tvSkipTip.isEnabled = true

            if (mDisposable != null) {
                mDisposable!!.dispose()
                mDisposable = null
            }
        }

    }

    fun setSkipClickListener(listener: OnClickListener) {
        tvSkipTip.setOnClickListener(listener)
    }

}
