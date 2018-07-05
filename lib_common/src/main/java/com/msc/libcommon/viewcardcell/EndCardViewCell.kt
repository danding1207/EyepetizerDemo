package com.msc.libcommon.viewcardcell

import android.support.v4.content.ContextCompat
import com.msc.libcommon.R
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcommon.viewcard.EndCardView
import com.msc.libcoremodel.datamodel.http.entities.MessagesData

class EndCardViewCell : CommenDataCell<EndCardView, MessagesData.MessageListBean>() {

    override fun bindView(view: EndCardView) {
        super.bindView(view)

        when (mData!!.color) {
            "black" -> view.tvEnd!!.setTextColor(ContextCompat.getColor(view.context, R.color.colorPrimary))
            "white" -> view.tvEnd!!.setTextColor(ContextCompat.getColor(view.context, R.color.white))
        }

    }

}
