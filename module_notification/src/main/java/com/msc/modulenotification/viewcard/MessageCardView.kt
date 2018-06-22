package com.msc.modulenotification.viewcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.msc.modulenotification.R
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle

class MessageCardView : FrameLayout, ITangramViewLifeCycle {

    var tvName: TextView? = null
    var tvDescription: TextView? = null
    var tvMessageTime: TextView? = null
    var ivAuthorCover: ImageView? = null

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
        View.inflate(context, R.layout.view_message_card, this)
        ivAuthorCover = findViewById(R.id.iv_author_cover)
        tvName = findViewById(R.id.tv_name)
        tvDescription = findViewById(R.id.tv_description)
        tvMessageTime = findViewById(R.id.tv_message_time)

    }

    override fun cellInited(cell: BaseCell<*>) {
        setOnClickListener(cell)
    }

    override fun postBindView(cell: BaseCell<*>) {
    }

    override fun postUnBindView(cell: BaseCell<*>) {

    }
}
