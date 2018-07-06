package com.msc.libcommon.viewcardcell

import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import com.bumptech.glide.Glide
import com.msc.libcommon.R
import com.msc.libcommon.util.StringUtils
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcommon.util.DensityUtil
import com.msc.libcommon.viewcard.VideoSmallCardView
import com.msc.libcoremodel.datamodel.http.entities.CommonData

class VideoSmallCardViewCell : CommenDataCell<VideoSmallCardView, CommonData.CommonItemList>() {

    override fun bindView(view: VideoSmallCardView) {
        super.bindView(view)
        view.tvTitle !!.text = mData!!.data!!.title
        view.tvTag !!.text = "#${mData!!.data!!.category}"

        when (mData!!.color) {
            "black" -> {
                view.ivShare!!.setImageResource(R.drawable.ic_action_share_dark)
                view.tvTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.colorPrimary))
                view.tvTag!!.setTextColor(ContextCompat.getColor(view.context, R.color.colorPrimary))
            }
            "white" -> {
                view.ivShare!!.setImageResource(R.drawable.ic_action_share_light)
                view.tvTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.white))
                view.tvTag!!.setTextColor(ContextCompat.getColor(view.context, R.color.white))
            }
        }

        Glide.with(view.context!!).load(mData!!.data!!.cover!!.feed).into(view.ivSmallCover!!)

        val resources = view.context.resources
        val dm = resources.displayMetrics
        val width = dm.widthPixels
        val layoutParamsCardView: ConstraintLayout.LayoutParams = view.cardViewCover!!.layoutParams as ConstraintLayout.LayoutParams
        layoutParamsCardView.width = (width - DensityUtil.dp2px(view.context, 24f))/2
        layoutParamsCardView.height = layoutParamsCardView.width * 777 / 1242
        view.cardViewCover!!.layoutParams = layoutParamsCardView

        view.tvTime!!.text =  StringUtils.durationToString(mData!!.data!!.duration)

    }

}