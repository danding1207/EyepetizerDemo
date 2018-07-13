package com.msc.libcommon.viewcard

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.msc.libcommon.R
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle

class EndCardView : FrameLayout, ITangramViewLifeCycle {

    var constraintLayoutBg: ConstraintLayout? = null
    var tvEnd: TextView? = null

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
        View.inflate(context, R.layout.view_end_card, this)
        constraintLayoutBg = findViewById(R.id.constraintLayout_bg)
        tvEnd = findViewById(R.id.tv_end)
    }

    override fun cellInited(cell: BaseCell<*>) {
//        setOnClickListener(cell)
    }

    override fun postBindView(cell: BaseCell<*>) {
    }

    override fun postUnBindView(cell: BaseCell<*>) {

    }
}
