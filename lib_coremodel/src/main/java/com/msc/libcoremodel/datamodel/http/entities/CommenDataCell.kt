package com.msc.libcoremodel.datamodel.http.entities

import android.view.View
import android.widget.FrameLayout
import com.google.gson.Gson
import com.tmall.wireless.tangram.MVHelper
import com.tmall.wireless.tangram.structure.BaseCell

import org.json.JSONObject

open class CommenDataCell<V : View> : BaseCell<V>() {

    var mData: AllRecData.ItemListBeanX.DataBeanXX? = null

    override fun parseWith(data: JSONObject, resolver: MVHelper) {
        super.parseWith(data, resolver)
        data.optString("data")
        mData   = Gson().fromJson<AllRecData.ItemListBeanX.DataBeanXX>(data.optString("data"), AllRecData.ItemListBeanX.DataBeanXX::class.java)
    }

}
