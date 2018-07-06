package com.msc.libcommon.viewcardcell

import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import com.bumptech.glide.Glide
import com.msc.libcommon.R
import com.msc.libcommon.util.StringUtils
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcommon.util.DensityUtil
import com.msc.libcommon.viewcard.VideoHeaderCardView
import com.msc.libcommon.viewcard.VideoSmallCardView
import com.msc.libcoremodel.datamodel.http.entities.CommonData

class VideoHeaderCardViewCell : CommenDataCell<VideoHeaderCardView, CommonData.CommonItemList>() {

    override fun bindView(view: VideoHeaderCardView) {
        super.bindView(view)
        view.tvName !!.text = mData!!.data!!.title
        view.tvCategory !!.text = "#${mData!!.data!!.category}"
        view.tvDescription !!.text = mData!!.data!!.description


//        when (mData!!.color) {
//            "black" -> {
//                view.tvTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.colorPrimary))
//                view.tvTag!!.setTextColor(ContextCompat.getColor(view.context, R.color.colorPrimary))
//            }
//            "white" -> {
//                view.tvTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.white))
//                view.tvTag!!.setTextColor(ContextCompat.getColor(view.context, R.color.white))
//            }
//        }

        view.tvLikeNum !!.text = mData!!.data!!.consumption!!.collectionCount.toString()
        view.tvMessageNum !!.text = mData!!.data!!.consumption!!.replyCount.toString()
        view.tvShareNum !!.text = mData!!.data!!.consumption!!.shareCount.toString()

        view.tvOwnerName !!.text = mData!!.data!!.author!!.name
        view.tvOwnerDesc !!.text = mData!!.data!!.author!!.description

        Glide.with(view.context!!)
                .load(mData!!.data!!.author!!.icon)
                .into(view.ivOwnerCover!!)

    }

}