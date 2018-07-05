package com.msc.videoplayer.ui

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.BaseActivity
import kotlinx.android.synthetic.main.activity_picture_detail.*
import com.msc.libcommon.widget.transform.GlideCircleTransform
import com.bumptech.glide.request.RequestOptions
import com.msc.videoplayer.R


@Route(path = ARouterPath.PICTURE_DETAIL_ACT)
class PictureDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarDarkMode(this, ContextCompat.getColor(this, R.color.colorPrimary))
        setContentView(R.layout.activity_picture_detail)
        initView()
    }

    private fun initView() {
        val avatarUrl = intent.extras.getString("avatarUrl")
        val nickname = intent.extras.getString("nickname")
        val description = intent.extras.getString("description")
        val collectionCount = intent.extras.getString("collectionCount")
        val shareCount = intent.extras.getString("shareCount")
        val replyCount = intent.extras.getString("replyCount")
        val pictureUrl = intent.extras.getString("pictureUrl")

        tv_like_num.text = collectionCount
        tv_message_num.text = replyCount
        tv_description.text = description
        tv_author_name.text = nickname

        Glide.with(this).load(avatarUrl).apply(RequestOptions()
                .centerCrop()
                .priority(Priority.HIGH)
                .transform(GlideCircleTransform())).into(iv_author_cover)

        Glide.with(this).load(pictureUrl).apply(RequestOptions()
                .centerInside()
                .priority(Priority.HIGH)).into(photoView)

        iv_action_player_back.setOnClickListener {
            finish()
        }

        photoView.setOnClickListener {
            if (constraintLayout_bottom.visibility== View.VISIBLE) {
                constraintLayout_bottom.visibility= View.GONE
                linearLayout_top.visibility= View.GONE
            } else {
                constraintLayout_bottom.visibility= View.VISIBLE
                linearLayout_top.visibility= View.VISIBLE
            }
        }

    }

}
