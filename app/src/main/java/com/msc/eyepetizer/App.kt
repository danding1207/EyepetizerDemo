package com.msc.eyepetizer

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.exoplayer2.offline.DownloadAction
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
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.api.RefreshHeader
import java.io.File


class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        if (Utils.isAppDebug()) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)
    }

    private val DOWNLOAD_ACTION_FILE = "actions"
    private val DOWNLOAD_TRACKER_ACTION_FILE = "tracked_actions"
    private val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
    private val MAX_SIMULTANEOUS_DOWNLOADS = 2
    private val DOWNLOAD_DESERIALIZERS = arrayOf(DashDownloadAction.DESERIALIZER, HlsDownloadAction.DESERIALIZER, SsDownloadAction.DESERIALIZER, ProgressiveDownloadAction.DESERIALIZER)
    protected var userAgent: String? = null

    private var downloadDirectory: File? = null
    private var downloadCache: Cache? = null
    private var downloadManager: DownloadManager? = null
    private var downloadTracker: DownloadTracker? = null


    /** Returns a [DataSource.Factory].  */
    fun buildDataSourceFactory(listener: TransferListener<in DataSource>?): DataSource.Factory {
        val upstreamFactory = DefaultDataSourceFactory(this, listener, buildHttpDataSourceFactory(listener))
        return buildReadOnlyCacheDataSource(upstreamFactory, getDownloadCache())
    }

    /** Returns a [HttpDataSource.Factory].  */
    fun buildHttpDataSourceFactory(
            listener: TransferListener<in DataSource>?): HttpDataSource.Factory {
        return DefaultHttpDataSourceFactory(userAgent, listener)
    }

    /** Returns whether extension renderers should be used.  */
    fun useExtensionRenderers(): Boolean {
        return "withExtensions" == BuildConfig.FLAVOR
    }

    fun getDownloadManager(): DownloadManager {
        initDownloadManager()
        return downloadManager!!
    }

    fun getDownloadTracker(): DownloadTracker {
        initDownloadManager()
        return downloadTracker!!
    }

    @Synchronized
    private fun initDownloadManager() {
        if (downloadManager == /* listener= */ null) {
            val downloaderConstructorHelper = DownloaderConstructorHelper(
                    getDownloadCache(), buildHttpDataSourceFactory(/* listener= */null))
            downloadManager = DownloadManager(
                    downloaderConstructorHelper,
                    MAX_SIMULTANEOUS_DOWNLOADS,
                    DownloadManager.DEFAULT_MIN_RETRY_COUNT,
                    File(getDownloadDirectory(), DOWNLOAD_ACTION_FILE),
                    DashDownloadAction.DESERIALIZER, HlsDownloadAction.DESERIALIZER, SsDownloadAction.DESERIALIZER, ProgressiveDownloadAction.DESERIALIZER)
            downloadTracker = DownloadTracker(
                    /* context= */ this,
                    buildDataSourceFactory(null),
                    File(getDownloadDirectory(), DOWNLOAD_TRACKER_ACTION_FILE),
                    DOWNLOAD_DESERIALIZERS)
            downloadManager!!.addListener(downloadTracker)
        }
    }

    @Synchronized
    private fun getDownloadCache(): Cache {
        if (downloadCache == null) {
            val downloadContentDirectory = File(getDownloadDirectory(), DOWNLOAD_CONTENT_DIRECTORY)
            downloadCache = SimpleCache(downloadContentDirectory, NoOpCacheEvictor())
        }
        return downloadCache!!
    }

    private fun getDownloadDirectory(): File {
        if (downloadDirectory == null) {
            downloadDirectory = getExternalFilesDir(null)
            if (downloadDirectory == null) {
                downloadDirectory = filesDir
            }
        }
        return downloadDirectory!!
    }

    private fun buildReadOnlyCacheDataSource(
            upstreamFactory: DefaultDataSourceFactory, cache: Cache): CacheDataSourceFactory {
        return CacheDataSourceFactory(
                cache,
                upstreamFactory,
                FileDataSourceFactory(),
                /* eventListener= */ null,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null)/* cacheWriteDataSinkFactory= */
    }

}