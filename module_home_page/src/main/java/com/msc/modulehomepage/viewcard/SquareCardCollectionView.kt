package com.msc.modulehomepage.viewcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson

import com.msc.libcommon.widget.banner.BannerLayout
import com.msc.libcoremodel.datamodel.http.entities.AllRecData
import com.msc.modulehomepage.R
import com.msc.modulehomepage.adapter.WebBannerAdapter
import com.orhanobut.logger.Logger
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle

class SquareCardCollectionView : FrameLayout, ITangramViewLifeCycle {

    var webBannerAdapter: WebBannerAdapter? = null
    var bannerLayout: BannerLayout? = null
    var tvDate: TextView? = null
    var tvTitle: TextView? = null

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
        View.inflate(context, R.layout.view_square_card_collection, this)
        bannerLayout = findViewById(R.id.bannerLayout)
        tvDate = findViewById(R.id.tv_date)
        tvTitle = findViewById(R.id.tv_title)
        webBannerAdapter = WebBannerAdapter(this.context)
        webBannerAdapter!!.setOnBannerItemClickListener(
                BannerLayout.OnBannerItemClickListener { position ->
                    Toast.makeText(this.context, "点击了第  $position  项", Toast.LENGTH_SHORT).show()
                }
        )
        bannerLayout!!.setAdapter(webBannerAdapter)
    }

    override fun cellInited(cell: BaseCell<*>) {
        setOnClickListener(cell)
    }

    override fun postBindView(cell: BaseCell<*>) {
    }

    override fun postUnBindView(cell: BaseCell<*>) {
    }
}
