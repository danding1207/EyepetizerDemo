package com.msc.libcoremodel.datamodel.http.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

/**
 * Created by dxx on 2017/11/20.
 */

object JsonUtil {

    private var gson: Gson? = null

    init {
        if (gson == null) {
            gson = Gson()
        }
    }

    fun <T> Str2JsonBean(json: String, clazz: Class<T>): T? {
        var bean: T? = null
        if (null != gson) {
            try {
                bean = gson!!.fromJson(json, clazz)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            }

        }
        return bean
    }


    fun JsonBean2Str(`object`: Any): String? {
        var str: String? = null
        if (null != gson) {
            try {
                str = gson!!.toJson(`object`)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return str
    }

    fun JsonList2Str(list: List<*>): String? {
        var str: String? = null
        if (null != gson && list.size > 0) {
            str = "["
            for (i in list.indices) {
                if (i != list.size - 1) {
                    str += gson!!.toJson(list[i]) + ","
                } else if (i == list.size - 1) {
                    str += gson!!.toJson(list[i]) + "]"
                }
            }
        }
        return str
    }
}