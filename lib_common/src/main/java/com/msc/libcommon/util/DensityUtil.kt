package com.msc.libcommon.util

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Display
import android.view.WindowManager


/**
 * Created by danding1207 on 2017/12/9.
 */

object DensityUtil {


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }


    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return width of the screen.
     */
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return heiht of the screen.
     */
    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun getRealHeight(context: Context): Int {
        val wm: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = wm.defaultDisplay
        var screenHeight = 0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val dm = DisplayMetrics()
            display.getRealMetrics(dm)
            screenHeight = dm.heightPixels

            //或者也可以使用getRealSize方法
//            Point size = new Point();
//            display.getRealSize(size);
//            screenHeight = size.y;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            screenHeight = try {
                Display::javaClass.invoke(display).getMethod("getRawHeight") as Int
            } catch (e: Exception) {
                val dm = DisplayMetrics()
                display.getMetrics(dm)
                dm.heightPixels
            }
        }
        return screenHeight
    }


    /**
     * dp转px
     *
     * @param context
     * @param dpVal   dp value
     * @return px value
     */
    fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                context.resources.displayMetrics).toInt()
    }

}
