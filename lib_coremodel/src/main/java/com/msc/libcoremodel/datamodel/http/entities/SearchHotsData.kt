package com.msc.libcoremodel.datamodel.http.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "SearchHotsHistory")
class SearchHotsData {

    @PrimaryKey(autoGenerate = true) // 设置主键
    @ColumnInfo(name = "ID") // 定义对应的数据库的字段名成
    var id: Int = 0

    @ColumnInfo(name = "TYPE")
    var type: String? = null

    @ColumnInfo(name = "TYPETITLE")
    var typeTitle: String? = null

    @ColumnInfo(name = "NAME")
    var name: String? = null

    @ColumnInfo(name = "ISCANDELETE")
    var isCanDelete: Boolean = false

    @Ignore//指示Room需要忽略的字段或方法
    var isHeader: Boolean = false

    constructor()

    constructor(type: String?, typeTitle: String?, name: String?, isCanDelete: Boolean) {
        this.type = type
        this.typeTitle = typeTitle
        this.name = name
        this.isCanDelete = isCanDelete
    }

    fun clone(oldData:SearchHotsData) :SearchHotsData {
        return SearchHotsData(oldData.type,
                oldData.typeTitle,
                oldData.name,
                oldData.isCanDelete)
    }

}
