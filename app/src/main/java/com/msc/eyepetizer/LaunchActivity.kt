package com.msc.eyepetizer

import android.os.Bundle

import com.alibaba.android.arouter.launcher.ARouter
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.BaseActivity

class LaunchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //跳转到 WelcomeActivity
        ARouter.getInstance()
                .build(ARouterPath.LAUNCH_ACT)
//                /**可以针对性跳转跳转动画 */
//                .withTransition(R.anim.activity_up_in, R.anim.activity_up_out)
                .navigation(this@LaunchActivity)
        finish()
    }
}
