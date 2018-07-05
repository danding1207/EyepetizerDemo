package com.msc.libcommon.viewcard

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.msc.libcommon.R

import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle

class TextCardView : FrameLayout, ITangramViewLifeCycle {

    var constraintLayoutBg: ConstraintLayout? = null
    var tvTextContent: TextView? = null
    var ivArrow: ImageView? = null
    var tvFooterTextContent: TextView? = null
    var ivFooterArrow: ImageView? = null

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
        View.inflate(context, R.layout.view_text_card, this)
        constraintLayoutBg = findViewById(R.id.constraintLayout_bg)
        tvTextContent = findViewById(R.id.tv_text_content)
        ivArrow = findViewById(R.id.iv_arrow)
        tvFooterTextContent = findViewById(R.id.tv_footer_text_content)
        ivFooterArrow = findViewById(R.id.iv_footer_arrow)
    }

    override fun cellInited(cell: BaseCell<*>) {
        setOnClickListener(cell)
    }

    override fun postBindView(cell: BaseCell<*>) {
    }

    override fun postUnBindView(cell: BaseCell<*>) {
    }
}
