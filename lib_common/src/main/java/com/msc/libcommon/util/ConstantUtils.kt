package com.msc.libcommon.util

import com.msc.libcoremodel.datamodel.http.entities.CommonData

object ConstantUtils {

    /**
     *
     */
    fun getEmptyCommonItemList(): CommonData.CommonItemList {
        val empty = CommonData.CommonItemList()
        empty.type = "empty"
        empty.emptyTitle = "很抱歉没有找到相匹配的内容"
        empty.emptyDescription = "可以通过所属分类，\n以及标题或者描述中的关键字来查找"
        return empty
    }

    /**
     *
     */
    fun getNetworkErrorCommonItemList(): CommonData.CommonItemList {
        val empty = CommonData.CommonItemList()
        empty.type = "networkError"
        empty.emptyTitle = "很抱歉网络连接出现问题"
        empty.emptyDescription = "可以再次尝试查找，\n或者检查网络设置后再试"
        return empty
    }

}
