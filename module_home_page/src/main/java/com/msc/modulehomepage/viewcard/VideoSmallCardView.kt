package com.msc.modulehomepage.viewcard

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.msc.modulehomepage.R
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle

class VideoSmallCardView : FrameLayout, ITangramViewLifeCycle {

    var ivSmallCover: ImageView? = null
    var cardViewCover: CardView? = null
    var tvTitle: TextView? = null
    var tvTag: TextView? = null
    var ivShare: ImageView? = null
    var tvTime: TextView? = null

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
        View.inflate(context, R.layout.view_small_card, this)
        ivSmallCover = findViewById(R.id.iv_small_cover)
        cardViewCover = findViewById(R.id.cardView_cover)
        tvTitle = findViewById(R.id.tv_title)
        tvTag = findViewById(R.id.tv_tag)
        ivShare = findViewById(R.id.iv_share)
        tvTime = findViewById(R.id.tv_time)
    }

    override fun cellInited(cell: BaseCell<*>) {
        setOnClickListener(cell)
    }

    override fun postBindView(cell: BaseCell<*>) {
    }

    override fun postUnBindView(cell: BaseCell<*>) {
    }
}
