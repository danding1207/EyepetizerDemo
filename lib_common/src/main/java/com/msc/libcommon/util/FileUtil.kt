package com.msc.libcommon.util

import android.content.Context

import java.io.File

object FileUtil {

    fun getVideoFile(context: Context): File {
        val externalSaveDir = context.getExternalFilesDir(null)
        val file = File(externalSaveDir ?: context.externalCacheDir, "video")
        if (!file.exists()) file.mkdir()
        return file
    }

}
