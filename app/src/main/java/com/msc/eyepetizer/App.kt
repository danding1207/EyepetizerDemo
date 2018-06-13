package com.msc.eyepetizer

import com.alibaba.android.arouter.launcher.ARouter
import com.msc.libcommon.base.BaseApplication
import com.msc.libcommon.util.Utils

class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        if (Utils.isAppDebug()) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)
    }
}