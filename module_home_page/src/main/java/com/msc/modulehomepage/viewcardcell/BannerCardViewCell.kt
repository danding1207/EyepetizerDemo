package com.msc.modulehomepage.viewcardcell

import android.support.constraint.ConstraintLayout
import com.bumptech.glide.Glide
import com.msc.libcoremodel.datamodel.http.entities.AllRecData
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcommon.util.DensityUtil
import com.msc.modulehomepage.viewcard.BannerCardView

class BannerCardViewCell : CommenDataCell<BannerCardView, AllRecData.ItemListBeanX>() {

    override fun bindView(view: BannerCardView) {
        super.bindView(view)

        Glide.with(view.context!!).load(mData!!.data!!.image).into(view.ivBannerCover!!)

        val resources = view.context.resources
        val dm = resources.displayMetrics
        val width = dm.widthPixels
        val layoutParamsCardView: ConstraintLayout.LayoutParams = view.cardViewCover!!.layoutParams as ConstraintLayout.LayoutParams
        layoutParamsCardView.width = width - DensityUtil.dp2px(view.context, 16f)
        layoutParamsCardView.height = layoutParamsCardView.width * 720 / 1242
        view.cardViewCover!!.layoutParams = layoutParamsCardView

    }

}