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

class FollowCardView : FrameLayout, ITangramViewLifeCycle {

    var ivFollowCover: ImageView? = null
    var ivAuthorCover: ImageView? = null
    var ivAuthorIcon: ImageView? = null
    var ivActionShare: ImageView? = null
    var tvTitle: TextView? = null
    var tvDescription: TextView? = null
    var tvTime: TextView? = null
    var cardViewCover: CardView? = null


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
        View.inflate(context, R.layout.view_follow_card, this)
        ivFollowCover = findViewById(R.id.iv_follow_cover)
        ivAuthorCover = findViewById(R.id.iv_author_cover)
        ivAuthorIcon = findViewById(R.id.iv_author_icon)
        ivActionShare = findViewById(R.id.iv_action_share)
        tvTitle = findViewById(R.id.tv_title)
        tvDescription = findViewById(R.id.tv_description)
        tvTime = findViewById(R.id.tv_time)
        cardViewCover = findViewById(R.id.cardView_cover)
    }

    override fun cellInited(cell: BaseCell<*>) {
        cardViewCover!!.setOnClickListener(cell)
    }

    override fun postBindView(cell: BaseCell<*>) {
    }

    override fun postUnBindView(cell: BaseCell<*>) {
    }
}