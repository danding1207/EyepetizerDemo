package com.msc.libcoremodel.datamodel.http.typeconverters;

import android.arch.persistence.room.TypeConverter;

import com.liulishuo.okdownload.StatusUtil;

public class DownloadStatusConverters {

    @TypeConverter
    public static String stringfromStatus(StatusUtil.Status status) {
        return status.name();
    }

    @TypeConverter
    public static StatusUtil.Status statusToString(String date) {
        return StatusUtil.Status.valueOf(date);
    }

}
