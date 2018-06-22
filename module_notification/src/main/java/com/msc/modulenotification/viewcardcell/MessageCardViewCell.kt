package com.msc.modulenotification.viewcardcell

import com.bumptech.glide.Glide
import com.msc.libcommon.util.StringUtils
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcoremodel.datamodel.http.entities.MessagesData
import com.msc.modulenotification.viewcard.MessageCardView

class MessageCardViewCell : CommenDataCell<MessageCardView, MessagesData.MessageListBean>() {

    override fun bindView(view: MessageCardView) {
        super.bindView(view)
        view.tvName!!.text = mData!!.title
        view.tvDescription!!.text = mData!!.content
        view.tvMessageTime!!.text = StringUtils.getStringDate(mData!!.date)

        Glide.with(view.context!!).load(mData!!.icon).into(view.ivAuthorCover!!)

    }

}
