package com.msc.libcommon.base

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View

import com.msc.libcommon.R
import com.msc.libcommon.util.Utils


/**
 *
 * Activity基类
 * @name BaseActivity
 */
abstract class BaseActivity : AppCompatActivity() {

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
}
