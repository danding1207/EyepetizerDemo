package com.msc.eyepetizer

import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper
import com.google.android.exoplayer2.offline.ProgressiveDownloadAction
import com.google.android.exoplayer2.source.dash.offline.DashDownloadAction
import com.google.android.exoplayer2.source.hls.offline.HlsDownloadAction
import com.google.android.exoplayer2.source.smoothstreaming.offline.SsDownloadAction
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.*
import com.msc.libcommon.base.BaseApplication
import com.msc.libcommon.util.Utils
import com.msc.videoplayer.BuildConfig
import com.msc.videoplayer.DownloadTracker
import java.io.File

class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        if (Utils.isAppDebug()) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)
    }

}