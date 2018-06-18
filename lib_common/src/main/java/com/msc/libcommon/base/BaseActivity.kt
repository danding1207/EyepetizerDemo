package com.msc.libcommon.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View

import com.msc.libcommon.R
import com.msc.libcommon.util.Utils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import android.support.v4.view.ViewCompat
import android.view.Window.ID_ANDROID_CONTENT
import android.view.ViewGroup
import android.view.WindowManager
import android.app.Activity
import android.os.Build
import android.view.Window
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION_CODES.M
import android.os.Build.VERSION
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.KITKAT
import android.os.Build.VERSION_CODES.LOLLIPOP
import java.lang.reflect.Array.setInt
import java.lang.reflect.AccessibleObject.setAccessible






/**
 *
 * Activity基类
 * @name BaseActivity
 */
abstract class BaseActivity : AppCompatActivity() {

    var rootView: ConstraintLayout? = null


    /**
     * 封装的findViewByID方法
     */
    protected fun <T : View> `$`(@IdRes id: Int): T {
        return super.findViewById<View>(id) as T
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewManager.instance.addActivity(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        ViewManager.instance.finishActivity(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    /**
     * Setup the toolbar.
     *
     * @param toolbar   toolbar
     * @param hideTitle 是否隐藏Title
     */
    protected fun setupToolBar(toolbar: Toolbar, hideTitle: Boolean) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            if (hideTitle) {
                //隐藏Title
                actionBar.setDisplayShowTitleEnabled(false)
            }
        }
    }


    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    fun addFragment(fragment: BaseFragment, @IdRes frameId: Int) {
        Utils.checkNotNull(fragment)
        supportFragmentManager.beginTransaction()
                .add(frameId, fragment, fragment.javaClass.simpleName)
                .addToBackStack(fragment.javaClass.simpleName)
                .commitAllowingStateLoss()

    }


    /**
     * 替换fragment
     * @param fragment
     * @param frameId
     */
    fun replaceFragment(fragment: BaseFragment, @IdRes frameId: Int) {
        Utils.checkNotNull(fragment)
        supportFragmentManager.beginTransaction()
                .replace(frameId, fragment, fragment.javaClass.simpleName)
                .addToBackStack(fragment.javaClass.simpleName)
                .commitAllowingStateLoss()

    }


    /**
     * 隐藏fragment
     * @param fragment
     */
    fun hideFragment(fragment: BaseFragment) {
        Utils.checkNotNull(fragment)
        supportFragmentManager.beginTransaction()
                .hide(fragment)
                .commitAllowingStateLoss()

    }


    /**
     * 显示fragment
     * @param fragment
     */
    fun showFragment(fragment: BaseFragment) {
        Utils.checkNotNull(fragment)
        supportFragmentManager.beginTransaction()
                .show(fragment)
                .commitAllowingStateLoss()

    }


    /**
     * 移除fragment
     * @param fragment
     */
    fun removeFragment(fragment: BaseFragment) {
        Utils.checkNotNull(fragment)
        supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss()

    }


    /**
     * 弹出栈顶部的Fragment
     */
    fun popFragment() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun finish() {
        super.finish()
        this.overridePendingTransition(R.anim.activity_down_in, R.anim.activity_down_out)
    }


    private var firstPressedTime: Long = 0

    override fun onBackPressed() {
        if (this.localClassName == "MainActivity") {
            if (System.currentTimeMillis() - firstPressedTime < 2000) {
                super.onBackPressed()
            } else {
                if (rootView != null)
                    Snackbar.make(rootView!!, "再次退出", Snackbar.LENGTH_LONG).show()
                firstPressedTime = System.currentTimeMillis()
            }
        } else if (this.localClassName == "WelcomeActivity" || this.localClassName == "LaunchActivity") {

        } else {
            super.onBackPressed()
        }
    }

    fun setStatusBarColor(activity: Activity, statusColor: Int) {
        val window = activity.window
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //设置状态栏颜色
        window.statusBarColor = statusColor
        //设置系统状态栏处于可见状态
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        //让view不根据系统窗口来调整自己的布局
        val mContentView = window.findViewById(Window.ID_ANDROID_CONTENT) as ViewGroup
        val mChildView = mContentView.getChildAt(0)
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false)
            ViewCompat.requestApplyInsets(mChildView)
        }
    }

    fun setStatusBarDarkMode(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //判断是否为小米或魅族手机，如果是则将状态栏文字改为黑色
            if (MIUISetStatusBarLightMode(activity, false) || FlymeSetStatusBarLightMode(activity, false)) {
                //设置状态栏为指定颜色
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
                    activity.window.statusBarColor = color
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
                    //调用修改状态栏颜色的方法
                    setStatusBarColor(activity, color)
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
                activity.window.setBackgroundDrawableResource(android.R.color.transparent)
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                activity.window.statusBarColor = color
                //fitsSystemWindow 为 false, 不预留系统栏位置.
                val mContentView = activity.window.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup
                val mChildView = mContentView.getChildAt(0)
                if (mChildView != null) {
                    ViewCompat.setFitsSystemWindows(mChildView, true)
                    ViewCompat.requestApplyInsets(mChildView)
                }
            }
        }
    }

    fun setStatusBarLightMode(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //判断是否为小米或魅族手机，如果是则将状态栏文字改为黑色
            if (MIUISetStatusBarLightMode(activity, true) || FlymeSetStatusBarLightMode(activity, true)) {
                //设置状态栏为指定颜色
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
                    activity.window.statusBarColor = color
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
                    //调用修改状态栏颜色的方法
                    setStatusBarColor(activity, color)
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
                activity.window.setBackgroundDrawableResource(android.R.color.transparent)
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                activity.window.statusBarColor = color
                //fitsSystemWindow 为 false, 不预留系统栏位置.
                val mContentView = activity.window.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup
                val mChildView = mContentView.getChildAt(0)
                if (mChildView != null) {
                    ViewCompat.setFitsSystemWindows(mChildView, true)
                    ViewCompat.requestApplyInsets(mChildView)
                }
            }
        }
    }

    fun MIUISetStatusBarLightMode(activity: Activity, darkmode: Boolean): Boolean {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        var result = false
        val clazz = activity.window.javaClass
        try {
            var darkModeFlag = 0
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            extraFlagField.invoke(activity.window, if (darkmode) darkModeFlag else 0, darkModeFlag)
            result = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun FlymeSetStatusBarLightMode(activity: Activity, darkmode: Boolean): Boolean {
        var result = false
        try {
            val lp = activity.window.attributes
            val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            if (darkmode) {
                value = value or bit
            } else {
                value = value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            activity.window.attributes = lp
            result = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

}
