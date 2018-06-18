package com.msc.libcommon.base

import android.app.Application
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.msc.libcommon.R

import com.msc.libcommon.util.ClassUtils
import com.msc.libcommon.util.Utils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import com.tmall.wireless.tangram.util.IInnerImageSetter
import com.tmall.wireless.tangram.TangramBuilder
import io.reactivex.annotations.NonNull
import io.reactivex.annotations.Nullable


/**
 * 要想使用BaseApplication，必须在组件中实现自己的Application，并且继承BaseApplication；
 * 组件中实现的Application必须在debug包中的AndroidManifest.xml中注册，否则无法使用；
 * 组件的Application需置于java/debug文件夹中，不得放于主代码；
 * 组件中获取Context的方法必须为:Utils.getContext()，不允许其他写法；
 * @name BaseApplication
 */
open class BaseApplication : Application() {

    private var mAppDelegateList: List<ApplicationDelegate>? = null

    override fun onCreate() {
        super.onCreate()
        ins = this
        Logger.addLogAdapter(AndroidLogAdapter())
        Utils.init(this)
        mAppDelegateList = ClassUtils
                .getObjectsWithInterface(this,
                        ApplicationDelegate::class.java,
                        ROOT_PACKAGE)
        for (delegate in mAppDelegateList!!) {
            delegate.onCreate()
        }

        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/fzltl.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build())

        TangramBuilder.init(this, MIInnerImageSetter(), ImageView::class.java)
    }

    class MIInnerImageSetter : IInnerImageSetter {
        override fun <IMAGE : ImageView> doLoadImageUrl(view: IMAGE, url: String?) {
            Glide.with(ins!!.baseContext).load(url).into(view)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        for (delegate in mAppDelegateList!!) {
            delegate.onTerminate()
        }
    }


    override fun onLowMemory() {
        super.onLowMemory()
        for (delegate in mAppDelegateList!!) {
            delegate.onLowMemory()
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        for (delegate in mAppDelegateList!!) {
            delegate.onTrimMemory(level)
        }
    }

    companion object {
        val ROOT_PACKAGE = "com.msc.eyepetizer"
        var ins: BaseApplication? = null
            private set
    }
}