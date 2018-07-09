package com.msc.libcommon.viewcardcell

import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.msc.libcommon.util.StringUtils
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcommon.util.DensityUtil
import com.msc.libcommon.viewcard.FollowCardView
import com.msc.libcoremodel.datamodel.http.entities.CommonData

class FollowCardViewCell : CommenDataCell<FollowCardView, CommonData.CommonItemList>() {

    var drawableWidth =0
    var drawableHeight =0

    override fun bindView(view: FollowCardView) {
        super.bindView(view)
        view.tvTitle !!.text = mData!!.data!!.header!!.title
        view.tvDescription !!.text = mData!!.data!!.header!!.description
        view.tvTime !!.text = StringUtils.durationToString(mData!!.data!!.content!!.data!!.duration)

        Glide.with(view.context!!).load(mData!!.data!!.content!!.data!!.author!!.icon).into(view.ivAuthorCover!!)
        Glide.with(view.context!!).load(mData!!.data!!.content!!.data!!.cover!!.feed).into(view.ivFollowCover!!)

        Glide.with(view.context!!)
                .load(mData!!.data!!.content!!.data!!.cover!!.feed)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        drawableWidth = resource.intrinsicWidth
                        drawableHeight = resource.intrinsicHeight
                        view.ivFollowCover!!.setImageDrawable(resource)

                        val resources = view.context.resources
                        val dm = resources.displayMetrics
                        val width = dm.widthPixels
                        val layoutParamsCardView: ConstraintLayout.LayoutParams = view.cardViewCover!!.layoutParams as ConstraintLayout.LayoutParams
                        layoutParamsCardView.width = width - DensityUtil.dp2px(view.context, 16f)
                        layoutParamsCardView.height = layoutParamsCardView.width * drawableHeight / drawableWidth
                        view.cardViewCover!!.layoutParams = layoutParamsCardView
                    }
                })

    }

}