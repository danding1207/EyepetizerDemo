package com.msc.libcommon.viewcardcell

import com.msc.libcommon.base.CommenDataCell
import com.msc.libcommon.util.DensityUtil
import com.msc.libcommon.viewcard.EmptyCardView
import com.msc.libcoremodel.datamodel.http.entities.CommonData

class EmptyCardViewCell : CommenDataCell<EmptyCardView, CommonData.CommonItemList>() {

    override fun bindView(view: EmptyCardView) {
        super.bindView(view)

        view.tvTipTitle!!.text = mData!!.emptyTitle
        view.tvTipDescription!!.text = mData!!.emptyDescription

        val layoutParams = view.layoutParams
        layoutParams.height = DensityUtil.getScreenHeight(view.context)-400
        view.layoutParams = layoutParams
    }

}
