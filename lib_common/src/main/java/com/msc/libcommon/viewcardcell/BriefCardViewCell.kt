package com.msc.libcommon.viewcardcell

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcommon.viewcard.BriefCardView
import com.msc.libcommon.widget.transform.GlideCircleTransform
import com.msc.libcommon.widget.transform.GlideSquareTransform
import com.msc.libcoremodel.datamodel.http.entities.CommonData

class BriefCardViewCell : CommenDataCell<BriefCardView, CommonData.CommonItemList>() {

    override fun bindView(view: BriefCardView) {
        super.bindView(view)
        view.tvTitle!!.text = mData!!.data!!.title
        view.tvDescription!!.text = mData!!.data!!.description

        if (mData!!.data!!.follow!!.followed) {
            view.tvFollow!!.visibility = View.INVISIBLE
        } else {
            view.tvFollow!!.visibility = View.VISIBLE
        }

        when (mData!!.data!!.iconType) {
            "square" -> Glide.with(view.context!!)
                    .load(mData!!.data!!.icon)
                    .apply(RequestOptions()
                            .centerCrop()
                            .priority(Priority.HIGH)
                            .transform(GlideSquareTransform())).into(view.ivAuthorCover!!)
            "round" -> Glide.with(view.context!!)
                    .load(mData!!.data!!.icon)
                    .apply(RequestOptions()
                            .centerCrop()
                            .priority(Priority.HIGH)
                            .transform(GlideCircleTransform())).into(view.ivAuthorCover!!)

        }

    }

}