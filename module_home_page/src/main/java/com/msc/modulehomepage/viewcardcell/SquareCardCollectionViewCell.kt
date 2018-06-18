package com.msc.modulehomepage.viewcardcell

import com.msc.libcoremodel.datamodel.http.entities.CommenDataCell
import com.msc.modulehomepage.viewcard.SquareCardCollectionView

internal class SquareCardCollectionViewCell : CommenDataCell<SquareCardCollectionView>() {

    override fun bindView(view: SquareCardCollectionView) {
        super.bindView(view)
        view.tvDate!!.text = mData!!.header!!.subTitle
        view.tvTitle!!.text = mData!!.header!!.title
        view.webBannerAdapter!!.refreshData(mData)
    }

}