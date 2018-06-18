package com.msc.modulehomepage.viewcardcell

import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.msc.libcommon.util.StringUtils
import com.msc.libcoremodel.datamodel.http.entities.CommenDataCell
import com.msc.mmdemo.Utils.DensityUtil
import com.msc.modulehomepage.viewcard.FollowCardView

class FollowCardViewCell : CommenDataCell<FollowCardView>() {

    var drawableWidth =0
    var drawableHeight =0

    override fun bindView(view: FollowCardView) {
        super.bindView(view)
        view.tvTitle !!.text = mData!!.header!!.title
        view.tvDescription !!.text = mData!!.header!!.description
        view.tvTime !!.text = StringUtils.durationToString(mData!!.content!!.data!!.duration)

        Glide.with(view.context!!).load(mData!!.content!!.data!!.author!!.icon).into(view.ivAuthorCover!!)
        Glide.with(view.context!!).load(mData!!.content!!.data!!.cover!!.feed).into(view.ivFollowCover!!)

        Glide.with(view.context!!)
                .load(mData!!.content!!.data!!.cover!!.feed)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        drawableWidth = resource.intrinsicWidth
                        drawableHeight = resource.intrinsicHeight
                        view.ivFollowCover!!.setImageDrawable(resource)

                        val resources = view.context.resources
                        val dm = resources.displayMetrics
                        val width = dm.widthPixels
                        val layoutParamsCardView: ConstraintLayout.LayoutParams = view.cardViewCover!!.layoutParams as ConstraintLayout.LayoutParams
                        layoutParamsCardView.width = width - DensityUtil.dip2px(view.context, 16f)
                        layoutParamsCardView.height = layoutParamsCardView.width * drawableHeight / drawableWidth
                        view.cardViewCover!!.layoutParams = layoutParamsCardView
                    }
                })

    }

}