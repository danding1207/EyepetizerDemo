package com.msc.modulehomepage.viewcardcell

import android.support.constraint.ConstraintLayout
import com.bumptech.glide.Glide
import com.msc.libcommon.util.StringUtils
import com.msc.libcoremodel.datamodel.http.entities.AllRecData
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcommon.util.DensityUtil
import com.msc.modulehomepage.viewcard.VideoSmallCardView

class VideoSmallCardViewCell : CommenDataCell<VideoSmallCardView, AllRecData.ItemListBeanX>() {

    override fun bindView(view: VideoSmallCardView) {
        super.bindView(view)
        view.tvTitle !!.text = mData!!.data!!.title
        view.tvTag !!.text = "#${mData!!.data!!.category}"

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