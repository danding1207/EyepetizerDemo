package com.msc.modulelaunch.ui

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.view.View
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.BaseActivity
import com.msc.libcoremodel.datamodel.http.entities.ConfigsData
import com.msc.modulelaunch.R
import com.msc.modulelaunch.viewmodel.WelcomeViewModel
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_welcome.*
import java.util.*

@Route(path = ARouterPath.LAUNCH_ACT)
class WelcomeActivity : BaseActivity() {

    private lateinit var viewModel: WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        rootView = constraintLayout
        viewModel = ViewModelProviders.of(this@WelcomeActivity).get(WelcomeViewModel::class.java!!)
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

            if (configsData == null) {

                textureView.visibility = View.GONE
                skipView.visibility = View.GONE

                Glide.with(this)
                        .load(R.drawable.launch_bg)
                        .into(iv_launch_bg)

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
                        Logger.d("MAIN_ACT    1")
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

                val calendar = Calendar.getInstance()

                if (configsData.startPageVideo != null
                        && configsData.startPageVideo!!.url != null
                        && calendar.time.time < configsData.startPageVideo!!.endTime
                        && calendar.time.time > configsData.startPageVideo!!.startTime
                ) {

                    textureView.visibility = View.VISIBLE
                    skipView.visibility = View.VISIBLE

                    // 1. Create a default TrackSelector
                    val bandwidthMeter = DefaultBandwidthMeter()

                    val trackSelector: TrackSelector =
                            DefaultTrackSelector(bandwidthMeter)

                    // 2. Create a default LoadControl
                    val loadControl = DefaultLoadControl()

                    // 3. Create the player
                    val player =
                            ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl)

                    // Produces DataSource instances through which media data is loaded.
                    val dataSourceFactory = DefaultDataSourceFactory(this,
                            Util.getUserAgent(this, "com.msc.eyepetizer"),
                            bandwidthMeter)
                    // Produces Extractor instances for parsing the media data.
                    val extractorsFactory = DefaultExtractorsFactory()
                    // This is the MediaSource representing the media to be played.
                    val mediaSource = ExtractorMediaSource(Uri.parse(configsData.startPageVideo!!.url),
                            dataSourceFactory, extractorsFactory, null, null)

                    player!!.playWhenReady = true
                    player.setVideoTextureView(textureView)
                    player.prepare(mediaSource, false, false)

                    skipView.startSkipCountDown(configsData.startPageVideo!!.timeBeforeSkip)

                    player.addListener(object : Player.DefaultEventListener() {
                        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                            if (playbackState == Player.STATE_ENDED) {
                                Logger.d("MAIN_ACT    2")
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
                        }
                    })

                } else if (configsData.startPage != null && configsData.startPage!!.imageUrl != null) {

                    textureView.visibility = View.GONE
                    skipView.visibility = View.GONE

                    Glide.with(this)
                            .load(configsData.startPage!!.imageUrl)
                            .into(iv_launch_bg)

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
                            Logger.d("MAIN_ACT    3")
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

                }
            }
        })
    }

}
