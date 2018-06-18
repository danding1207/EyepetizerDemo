package com.msc.modulehomepage.viewcard

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.msc.mmdemo.Utils.DensityUtil

import com.msc.modulehomepage.R
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle

class PictureFollowCardView : FrameLayout, ITangramViewLifeCycle {

    var tvName: TextView? = null
    var tvDescription: TextView? = null
    var tvDate: TextView? = null
    var tvLikeNum: TextView? = null
    var tvMessageNum: TextView? = null
    var ivAuthorCover: ImageView? = null
    var ivCover: ImageView? = null
    var ivLike: ImageView? = null
    var ivMessage: ImageView? = null
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
        View.inflate(context, R.layout.view_picture_follow_card, this)
        ivAuthorCover = findViewById(R.id.iv_author_cover)
        tvName = findViewById(R.id.tv_name)
        tvDescription = findViewById(R.id.tv_description)
        ivCover = findViewById(R.id.iv_cover)
        ivLike = findViewById(R.id.iv_like)
        ivMessage = findViewById(R.id.iv_message)
        tvDate = findViewById(R.id.tv_date)
        tvLikeNum = findViewById(R.id.tv_like_num)
        tvMessageNum = findViewById(R.id.tv_message_num)
        cardViewCover = findViewById(R.id.cardView_cover)

    }

    override fun cellInited(cell: BaseCell<*>) {
        setOnClickListener(cell)
    }

    override fun postBindView(cell: BaseCell<*>) {
    }

    override fun postUnBindView(cell: BaseCell<*>) {

    }
}
