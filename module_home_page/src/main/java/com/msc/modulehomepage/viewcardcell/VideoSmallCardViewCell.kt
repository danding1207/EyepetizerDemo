package com.msc.modulehomepage.viewcardcell

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.msc.libcommon.util.StringUtils
import com.msc.libcoremodel.datamodel.http.entities.CommenDataCell
import com.msc.mmdemo.Utils.DensityUtil
import com.msc.modulehomepage.viewcard.TextCardView
import com.msc.modulehomepage.viewcard.VideoSmallCardView

class VideoSmallCardViewCell : CommenDataCell<VideoSmallCardView>() {

    override fun bindView(view: VideoSmallCardView) {
        super.bindView(view)
        view.tvTitle !!.text = mData!!.title
        view.tvTag !!.text = "#${mData!!.category}"

        Glide.with(view.context!!).load(mData!!.cover!!.feed).into(view.ivSmallCover!!)

        val resources = view.context.resources
        val dm = resources.displayMetrics
        val width = dm.widthPixels
        val layoutParamsCardView: ConstraintLayout.LayoutParams = view.cardViewCover!!.layoutParams as ConstraintLayout.LayoutParams
        layoutParamsCardView.width = (width - DensityUtil.dp2px(view.context, 24f))/2
        layoutParamsCardView.height = layoutParamsCardView.width * 777 / 1242
        view.cardViewCover!!.layoutParams = layoutParamsCardView

        view.tvTime!!.text =  StringUtils.durationToString(mData!!.duration)

    }

}