package com.msc.libcommon.viewcard

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.msc.libcommon.R
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle

class VideoHeaderCardView : FrameLayout, ITangramViewLifeCycle {


    var tvName: TextView? = null
    var tvCategory: TextView? = null
    var tvDescription: TextView? = null
    var tvLikeNum: TextView? = null
    var tvMessageNum: TextView? = null
    var tvShareNum: TextView? = null
    var ivOwnerCover: ImageView? = null
    var tvOwnerName: TextView? = null
    var tvOwnerDesc: TextView? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.view_video_header_card, this)

        tvName = findViewById(R.id.tv_name)
        tvCategory = findViewById(R.id.tv_category)
        tvDescription = findViewById(R.id.tv_description)
        tvLikeNum = findViewById(R.id.tv_like_num)
        tvMessageNum = findViewById(R.id.tv_message_num)
        tvShareNum = findViewById(R.id.tv_share_num)
        ivOwnerCover = findViewById(R.id.iv_owner_cover)
        tvOwnerName = findViewById(R.id.tv_owner_name)
        tvOwnerDesc = findViewById(R.id.tv_owner_desc)

    }

    override fun cellInited(cell: BaseCell<*>) {
//        setOnClickListener(cell)
    }

    override fun postBindView(cell: BaseCell<*>) {
    }

    override fun postUnBindView(cell: BaseCell<*>) {
    }
}
