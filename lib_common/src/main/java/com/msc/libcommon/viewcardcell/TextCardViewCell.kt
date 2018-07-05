package com.msc.libcommon.viewcardcell

import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import com.msc.libcommon.R
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcommon.viewcard.TextCardView
import com.msc.libcoremodel.datamodel.http.entities.CommonData

class TextCardViewCell : CommenDataCell<TextCardView, CommonData.CommonItemList>() {

    override fun bindView(view: TextCardView) {
        super.bindView(view)

        when (mData!!.data!!.type) {
            "header4" -> {

                view.constraintLayoutBg!!.setBackgroundResource(R.color.transparent)
                view.tvTextContent!!.setTextColor(ContextCompat.getColor(view.tvTextContent!!.context, R.color.white))

                view.tvTextContent!!.textSize = 12f


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
            "header5" -> {
                view.constraintLayoutBg!!.setBackgroundResource(R.color.white)
                view.tvTextContent!!.setTextColor(ContextCompat.getColor(view.tvTextContent!!.context, R.color.colorPrimary))
                view.tvTextContent!!.textSize = 24f


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
