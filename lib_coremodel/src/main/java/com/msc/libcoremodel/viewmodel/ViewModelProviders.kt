package com.msc.libcoremodel.viewmodel


import android.app.Activity
import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelStores
import android.support.annotation.MainThread
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * Created by danxx on 2018/3/31.
 * Global ViewModel Provider
 * ViewModel的创建不可直接new，需要使用这个[ViewModelProviders]才能与Activity或者
 * Fragment的生命周期关联起来！
 */
object ViewModelProviders {

    /**
     * 通过Activity获取可用的Application
     * 或者检测Activity是否可用
     * @param activity
     * @return
     */
    private fun checkApplication(activity: Activity): Application {
        val application = activity.application
                ?: throw IllegalStateException("Your activity/fragment is not yet attached to " + "Application. You can't request ViewModel before onCreate call.")
        return activity.application
                ?: throw IllegalStateException("Your activity/fragment is not yet attached to " + "Application. You can't request ViewModel before onCreate call.")
    }

    /**
     * 通过Fragment获取Activity
     * 或者检测Fragment是否可用
     * @param fragment
     * @return
     */
    private fun checkActivity(fragment: Fragment): Activity {
        val activity = fragment.activity
                ?: throw IllegalStateException("Can't create ViewModelProvider for detached fragment")
        return fragment.activity
                ?: throw IllegalStateException("Can't create ViewModelProvider for detached fragment")
    }

    /**
     * 通过Fragment获得ViewModelProvider
     * @param fragment
     * @return
     */
    @MainThread
    fun of(fragment: Fragment): ViewModelProvider {

        /**获取默认的单例AndroidViewModelFactory，它内部是通过反射来创建具体的ViewModel */
        val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
                checkApplication(checkActivity(fragment)))
        /***
         * 利用HolderFragment来关联生命周期并使用HolderFragment中的ViewModelStore的HashMap存储ViewModel
         * AndroidViewModelFactory创建ViewModel
         */
        return ViewModelProvider(ViewModelStores.of(fragment), factory)
    }

    /**
     * 通过FragmentActivity获得ViewModelProvider
     * @param activity
     * @return
     */
    @MainThread
    fun of(activity: FragmentActivity): ViewModelProvider {
        /**获取默认的单例AndroidViewModelFactory，它内部是通过反射来创建具体的ViewModel */
        val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
                checkApplication(activity))
        /***
         * 利用HolderFragment来关联生命周期并使用HolderFragment中的ViewModelStore的HashMap存储ViewModel
         * AndroidViewModelFactory创建ViewModel
         */
        return ViewModelProvider(ViewModelStores.of(activity), factory)
    }

    /**
     *
     * @param fragment
     * @param factory 提供了自定义创建ViewModel的方法
     * @return
     */
    @MainThread
    fun of(fragment: Fragment, factory: ViewModelProvider.Factory): ViewModelProvider {
        //检测Fragment
        checkApplication(checkActivity(fragment))
        return ViewModelProvider(ViewModelStores.of(fragment), factory)
    }

    /**
     *
     * @param activity
     * @param factory 提供了自定义创建ViewModel的方法
     * @return
     */
    @MainThread
    fun of(activity: FragmentActivity,
           factory: ViewModelProvider.Factory): ViewModelProvider {
        //检测activity
        checkApplication(activity)
        return ViewModelProvider(ViewModelStores.of(activity), factory)
    }
}
