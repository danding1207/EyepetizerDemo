package com.msc.libcommon.base

import android.os.Bundle
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter

class SchemeFilterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().build(intent.data).navigation(this, object :NavCallback() {
            override fun onArrival(postcard: Postcard?) {
                finish()
            }
        })
    }

}
