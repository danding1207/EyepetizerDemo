package com.msc.libcommon.base

import android.view.View
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import com.tmall.wireless.tangram.MVHelper
import com.tmall.wireless.tangram.structure.BaseCell
import org.json.JSONObject
import java.lang.reflect.ParameterizedType

open class CommenDataCell<V : View, T : Any> : BaseCell<V>() {

    var mData: T? = null

    override fun parseWith(data: JSONObject, resolver: MVHelper) {
        super.parseWith(data, resolver)

        Logger.i(data.toString())

        val rawType = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<T>

        mData   = Gson().fromJson<T>(data.toString(), rawType)
    }

}
