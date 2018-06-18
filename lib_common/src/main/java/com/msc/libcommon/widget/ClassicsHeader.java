package com.msc.libcommon.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.header.internal.MaterialProgressDrawable;
import com.scwang.smartrefresh.header.material.CircleImageView;
import com.scwang.smartrefresh.layout.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.internal.ArrowDrawable;
import com.scwang.smartrefresh.layout.internal.InternalClassics;
import com.scwang.smartrefresh.layout.internal.ProgressDrawable;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.view.View.MeasureSpec.getSize;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 经典下拉头部
 * Created by SCWANG on 2017/5/28.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ClassicsHeader extends InternalClassics<ClassicsHeader> implements RefreshHeader {

    public static final byte ID_TEXT_UPDATE = 4;

    protected SharedPreferences mShared;

    protected ImageView mCircleView;
    protected MaterialProgressDrawable mProgress;
    @VisibleForTesting
    protected static final int CIRCLE_DIAMETER = 40;
    protected static final int CIRCLE_BG_LIGHT = 0xFFFAFAFA;
    protected static final float MAX_PROGRESS_ANGLE = .8f;

    protected int mCircleDiameter;
    protected int mHeadHeight;
    protected boolean mFinished;
    protected RefreshState mState;

    public ClassicsHeader(Context context) {
        this(context, null);
    }

    public ClassicsHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicsHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final View thisView = this;
        final ViewGroup thisGroup = this;
        final ViewGroup centerLayout = mCenterLayout;
        final DensityUtil density = new DensityUtil();

        mTitleText.setVisibility(View.GONE);
        mArrowView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.GONE);
        mCenterLayout.setVisibility(View.GONE);

        mProgress = new MaterialProgressDrawable(this);
        mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);
        mProgress.setAlpha(255);
        mProgress.setColorSchemeColors(0xff0099cc,0xffff4444,0xff669900,0xffaa66cc,0xffff8800);
        mCircleView = new CircleImageView(context,CIRCLE_BG_LIGHT);
        mCircleView.setImageDrawable(mProgress);
        centerLayout.addView(mCircleView);

        final DisplayMetrics metrics = thisView.getResources().getDisplayMetrics();
        mCircleDiameter = (int) (CIRCLE_DIAMETER * metrics.density);

    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        final View circleView = mCircleView;
        mProgress.stop();
        circleView.animate().scaleX(0).scaleY(0);
        mFinished = true;
        return 0;
    }

    @Override
    public void onReleased(@NonNull RefreshLayout layout, int height, int maxDragHeight) {
        mProgress.start();
        final View circleView = mCircleView;
        if ((int) circleView.getTranslationY() != height / 2 + mCircleDiameter / 2) {
            circleView.animate().translationY(height / 2 + mCircleDiameter / 2);
        }
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        final View circleView = mCircleView;
        mState = newState;
        switch (newState) {
            case None:
                break;
            case PullDownToRefresh:
                mFinished = false;
                circleView.setVisibility(VISIBLE);
                circleView.setScaleX(1);
                circleView.setScaleY(1);
                break;
            case ReleaseToRefresh:
                break;
            case Refreshing:
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final ViewGroup thisGroup = this;
        if (thisGroup.getChildCount() == 0) {
            return;
        }
        final View thisView = this;
        final View circleView = mCircleView;
        final int width = thisView.getMeasuredWidth();
        int circleWidth = circleView.getMeasuredWidth();
        int circleHeight = circleView.getMeasuredHeight();

        if (thisView.isInEditMode() && mHeadHeight > 0) {
            int circleTop = mHeadHeight - circleHeight / 2;
            circleView.layout((width / 2 - circleWidth / 2), circleTop,
                    (width / 2 + circleWidth / 2), circleTop + circleHeight);

            mProgress.showArrow(true);
            mProgress.setStartEndTrim(0f, MAX_PROGRESS_ANGLE);
            mProgress.setArrowScale(1);
            circleView.setAlpha(1f);
            circleView.setVisibility(VISIBLE);
        } else {
            circleView.layout((width / 2 - circleWidth / 2), -mCircleDiameter,
                    (width / 2 + circleWidth / 2), circleHeight - mCircleDiameter);
        }
    }

    @Override
    public ClassicsHeader setAccentColor(@ColorInt int accentColor) {
        return super.setAccentColor(accentColor);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.setMeasuredDimension(getSize(widthMeasureSpec), getSize(heightMeasureSpec));
        final View circleView = mCircleView;
        circleView.measure(MeasureSpec.makeMeasureSpec(mCircleDiameter, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mCircleDiameter, MeasureSpec.EXACTLY));
//        setMeasuredDimension(resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec),
//                resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        final View thisView = this;
        if (thisView.isInEditMode()) {
            mHeadHeight = height / 2;
        }
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

        if (isDragging || (!mProgress.isRunning() && !mFinished)) {

            final View circleView = mCircleView;

            if (mState != RefreshState.Refreshing) {
                float originalDragPercent = 1f * offset / height;

                float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
                float adjustedPercent = (float) Math.max(dragPercent - .4, 0) * 5 / 3;
                float extraOS = Math.abs(offset) - height;
                float tensionSlingshotPercent = Math.max(0, Math.min(extraOS, (float) height * 2)
                        / (float) height);
                float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
                        (tensionSlingshotPercent / 4), 2)) * 2f;
                float strokeStart = adjustedPercent * .8f;
                mProgress.showArrow(true);
                mProgress.setStartEndTrim(0f, Math.min(MAX_PROGRESS_ANGLE, strokeStart));
                mProgress.setArrowScale(Math.min(1f, adjustedPercent));

                float rotation = (-0.25f + .4f * adjustedPercent + tensionPercent * 2) * .5f;
                mProgress.setProgressRotation(rotation);
                circleView.setAlpha(Math.min(1f, originalDragPercent * 2));
            }

            float targetY = offset / 2 + mCircleDiameter / 2;
            circleView.setTranslationY(Math.min(offset, targetY));//setTargetOffsetTopAndBottom(targetY - mCurrentTargetOffsetTop, true /* requires update */);
        }
    }

}
