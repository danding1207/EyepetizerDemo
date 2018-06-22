package com.msc.modulehomepage.viewcardcell

import android.text.TextUtils
import android.view.View
import com.msc.libcoremodel.datamodel.http.entities.AllRecData
import com.msc.libcommon.base.CommenDataCell
import com.msc.modulehomepage.viewcard.TextCardView

class TextCardViewCell : CommenDataCell<TextCardView, AllRecData.ItemListBeanX>() {

    override fun bindView(view: TextCardView) {
        super.bindView(view)

        when (mData!!.type) {
            "header5" -> {
                view.tvFooterTextContent!!.visibility = View.INVISIBLE
                view.ivFooterArrow!!.visibility = View.INVISIBLE
                view.tvTextContent!!.visibility = View.VISIBLE

                view.tvTextContent!!.text = mData!!.data!!.text

                if (TextUtils.isEmpty(mData!!.data!!.actionUrl)) {
                    view.ivArrow!!.visibility = View.INVISIBLE
                } else {
                    view.ivArrow!!.visibility = View.VISIBLE
                }
            }
            "footer2" -> {
                view.tvFooterTextContent!!.visibility = View.VISIBLE
                view.tvTextContent!!.visibility = View.INVISIBLE
                view.ivArrow!!.visibility = View.INVISIBLE

                view.tvFooterTextContent!!.text = mData!!.data!!.text

                if (TextUtils.isEmpty(mData!!.data!!.actionUrl)) {
                    view.ivFooterArrow!!.visibility = View.INVISIBLE
                } else {
                    view.ivFooterArrow!!.visibility = View.VISIBLE
                }
            }
        }

    }

}
