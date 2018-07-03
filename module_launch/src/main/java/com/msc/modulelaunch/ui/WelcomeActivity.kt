package com.msc.modulelaunch.ui

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.view.View
import com.alibaba.android.arouter.facade.Postcard

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.BaseActivity
import com.msc.libcoremodel.datamodel.http.entities.AllRecData
import com.msc.libcoremodel.datamodel.http.entities.ConfigsData
import com.msc.modulelaunch.R
import com.msc.modulelaunch.viewmodel.WelcomeViewModel
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_welcome.*
import org.json.JSONArray

@Route(path = ARouterPath.LAUNCH_ACT)
class WelcomeActivity : BaseActivity() {

    private lateinit var viewModel: WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        rootView = constraintLayout

        viewModel = ViewModelProviders.of(this@WelcomeActivity).get(WelcomeViewModel::class.java!!)
        viewModel.initData()
        subscribeToModel(viewModel)

    }

    /**
     * 订阅数据变化来刷新UI
     * @param model
     */
    private fun subscribeToModel(model: WelcomeViewModel) {
        //观察数据变化来刷新UI
        model.liveObservableData.observe(this, Observer<ConfigsData> { configsData ->
            Logger.d("subscribeToModel onChanged onChanged")

            if(configsData==null) {
                val animatorSet = AnimatorSet()

                val animator1 = ObjectAnimator.ofFloat(iv_launch_bg, View.SCALE_X, 1.0f, 1.1f)
                val animator2 = ObjectAnimator.ofFloat(iv_launch_bg, View.SCALE_Y, 1.0f, 1.1f)

                animator2.interpolator = LinearOutSlowInInterpolator()
                animator1.interpolator = LinearOutSlowInInterpolator()
                animator2.duration = 3000
                animator1.duration = 3000


                animatorSet.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {
                    }
                    override fun onAnimationEnd(animator: Animator) {
                        //跳转到 MainActivity
                        ARouter.getInstance()
                                .build(ARouterPath.MAIN_ACT)
                                /**可以针对性跳转跳转动画  */
                                .withTransition(R.anim.activity_up_in, R.anim.activity_up_out)
                                .navigation(this@WelcomeActivity, object : NavCallback() {
                                    override fun onArrival(postcard: Postcard?) {
                                        finish()
                                    }
                                })
                    }
                    override fun onAnimationCancel(animator: Animator) {}
                    override fun onAnimationRepeat(animator: Animator) {}
                })
                animatorSet.play(animator1).with(animator2)
                animatorSet.startDelay = 500
                animatorSet.start()
            } else {

            }



        })
    }



}
