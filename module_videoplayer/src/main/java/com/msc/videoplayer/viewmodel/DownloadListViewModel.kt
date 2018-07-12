package com.msc.videoplayer.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.support.design.widget.Snackbar
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.StatusUtil
import com.msc.libcommon.R
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcommon.util.FileUtil
import com.msc.libcoremodel.datamodel.http.database.VideoDownloadDatabase
import com.msc.libcoremodel.datamodel.http.entities.CommonData
import com.msc.libcoremodel.datamodel.http.entities.VideoDownloadData
import com.orhanobut.logger.Logger
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.support.SimpleClickSupport
import io.reactivex.disposables.CompositeDisposable

class DownloadListViewModel(application: Application) : AndroidViewModel(application) {

    private var mApplication: Application

    /**
     * LiveData支持了lifecycle生命周期检测
     * @return
     */
    var liveObservableData: MutableLiveData<List<VideoDownloadData>>

    private val mDisposable = CompositeDisposable()

    var listener: DownloadListViewModelClickSupport

    init {
        ABSENT.value = null
        Logger.d("=======DownloadListViewModel--init=========")
        mApplication = application
        listener = DownloadListViewModelClickSupport()
        liveObservableData = MutableLiveData()
    }

    /**
     * 刷新数据
     * @param
     */
    fun initData() {
        initData(liveObservableData)
    }

    /**
     * 刷新数据
     * @param
     */
    private fun initData(liveObservableData: MutableLiveData<List<VideoDownloadData>>) {
        Logger.d("=======DownloadListViewModel--initData=========")

        var list = VideoDownloadDatabase.getDefault(mApplication)
                .videoDownloadDao.videoDownloadHistoryAll

        if (list == null) {
            list = ArrayList<VideoDownloadData>()
        }

        list.forEach {
            val filename = "${it.fileId}.mp4"
            val url = it.playUrl!!
            val task = DownloadTask
                    .Builder(url, FileUtil.getVideoFile(mApplication))
                    .setFilename(filename)
                    // the minimal interval millisecond for callback progress
                    .setMinIntervalMillisCallbackProcess(200)
                    // ignore the same task has already completed in the past.
                    .setPassIfAlreadyCompleted(true)
                    .build()

            val status = StatusUtil.getStatus(task!!)
            val info = StatusUtil.getCurrentInfo(task)

            it.downloadStatus = status
            it.totalOffset = info?.totalOffset ?: 0
            it.totalLength = info?.totalLength ?: 0

            VideoDownloadDatabase.getDefault(mApplication)
                    .videoDownloadDao.update(it)
        }

        list.add(VideoDownloadData("end"))
        liveObservableData.value = list

    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
        Logger.d("=======DownloadListViewModel--onCleared=========")
    }

    companion object {
        private val ABSENT = MutableLiveData<List<VideoDownloadData>>()
    }

    inner class DownloadListViewModelClickSupport : SimpleClickSupport() {
        init {
            setOptimizedMode(true)
        }

        override fun defaultClick(targetView: View?, cell: BaseCell<*>?, eventType: Int) {
            super.defaultClick(targetView, cell, eventType)
            val mData: VideoDownloadData? = (cell as CommenDataCell<*, VideoDownloadData>).mData
            when (cell.stringType) {
                "download" -> {
//                        ARouter.getInstance()
//                                .build(ARouterPath.VIDEO_PLAYER_ACT)
//                                .withString("data", Gson().toJson(mData))
//                                .navigation()
                }
            }
        }
    }


}
