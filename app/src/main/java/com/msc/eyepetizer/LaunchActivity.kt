package com.msc.eyepetizer

import android.os.Bundle

import com.alibaba.android.arouter.launcher.ARouter
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.BaseActivity
import android.content.Intent

class LaunchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        //跳转到 WelcomeActivity
        ARouter.getInstance()
                .build(ARouterPath.LAUNCH_ACT)
                .navigation(this@LaunchActivity)
        finish()
    }
}
