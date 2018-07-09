package com.msc.libcommon.viewcardcell

import com.msc.libcommon.base.CommenDataCell
import com.msc.libcommon.viewcard.NetworkErrorCardView
import com.msc.libcoremodel.datamodel.http.entities.CommonData

class NetworkErrorCardViewCell : CommenDataCell<NetworkErrorCardView, CommonData.CommonItemList>() {

    override fun bindView(view: NetworkErrorCardView) {
        super.bindView(view)

        view.tvTipTitle!!.text = mData!!.emptyTitle
        view.tvTipDescription!!.text = mData!!.emptyDescription

    }

}
