package com.msc.libcommon.base

import android.content.Context
import android.support.annotation.IdRes
import android.support.v4.app.Fragment

import com.msc.libcommon.util.Utils

abstract class BaseFragment : Fragment() {

    /**
     * 获取宿主Activity
     *
     * @return BaseActivity
     */
    private var holdingActivity: BaseActivity? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.holdingActivity = context as BaseActivity?
    }


    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    protected fun addFragment(fragment: BaseFragment, @IdRes frameId: Int) {
        Utils.checkNotNull(fragment)
        holdingActivity!!.addFragment(fragment, frameId)

    }


    /**
     * 替换fragment
     *
     * @param fragment
     * @param frameId
     */
    protected fun replaceFragment(fragment: BaseFragment, @IdRes frameId: Int) {
        Utils.checkNotNull(fragment)
        holdingActivity!!.replaceFragment(fragment, frameId)
    }


    /**
     * 隐藏fragment
     *
     * @param fragment
     */
    protected fun hideFragment(fragment: BaseFragment) {
        Utils.checkNotNull(fragment)
        holdingActivity!!.hideFragment(fragment)
    }


    /**
     * 显示fragment
     *
     * @param fragment
     */
    protected fun showFragment(fragment: BaseFragment) {
        Utils.checkNotNull(fragment)
        holdingActivity!!.showFragment(fragment)
    }


    /**
     * 移除Fragment
     *
     * @param fragment
     */
    protected fun removeFragment(fragment: BaseFragment) {
        Utils.checkNotNull(fragment)
        holdingActivity!!.removeFragment(fragment)

    }


    /**
     * 弹出栈顶部的Fragment
     */
    protected fun popFragment() {
        holdingActivity!!.popFragment()
    }

}