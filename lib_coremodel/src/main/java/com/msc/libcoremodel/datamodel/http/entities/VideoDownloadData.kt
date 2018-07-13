package com.msc.libcoremodel.datamodel.http.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.liulishuo.okdownload.StatusUtil

@Entity(tableName = "VideoDownloadHistory")
class VideoDownloadData {

    @PrimaryKey(autoGenerate = true) // 设置主键
    @ColumnInfo(name = "ID") // 定义对应的数据库的字段名成
    var id: Int = 0

    @ColumnInfo(name = "DOWNLOADSTATUS")
    var downloadStatus: StatusUtil.Status = StatusUtil.Status.UNKNOWN

    @ColumnInfo(name = "PLAYURL")
    var playUrl: String? = null

    @ColumnInfo(name = "COVER")
    var cover: String? = null

    @ColumnInfo(name = "FILEPATH")
    var filePath: String? = null

    @ColumnInfo(name = "FILEID")
    var fileId: Int = -1

    @ColumnInfo(name = "DOWNLOADID")
    var downloadId: Int = -1

    @ColumnInfo(name = "FILENAME")
    var fileName: String? = null

    @ColumnInfo(name = "DURATION")
    var duration: Int = 0

    @ColumnInfo(name = "TOTALOFFSET")
    var totalOffset: Long = 0

    @ColumnInfo(name = "TOTALLENGTH")
    var totalLength: Long = 0

    @ColumnInfo(name = "DATAJSON")
    var dataJson: String? = null

    @Ignore//指示Room需要忽略的字段或方法
    var type: String = "download"

    constructor( )

    constructor(type: String) {
        this.type = type
    }

    constructor(id: Int, downloadStatus: StatusUtil.Status, filePath: String?, fileId: Int, downloadId: Int, fileName: String?, duration: Int, totalOffset: Long, totalLength: Long) {
        this.id = id
        this.downloadStatus = downloadStatus
        this.filePath = filePath
        this.fileId = fileId
        this.downloadId = downloadId
        this.fileName = fileName
        this.duration = duration
        this.totalOffset = totalOffset
        this.totalLength = totalLength
    }

    override fun toString(): String {
        return "VideoDownloadData(id=$id, downloadStatus=$downloadStatus, playUrl=$playUrl, cover=$cover, filePath=$filePath, fileId=$fileId, downloadId=$downloadId, fileName=$fileName, duration=$duration, totalOffset=$totalOffset, totalLength=$totalLength, dataJson=$dataJson, type='$type')"
    }

}
