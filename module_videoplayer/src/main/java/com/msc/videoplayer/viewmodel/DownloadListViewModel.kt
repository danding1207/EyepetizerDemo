package com.msc.videoplayer.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.StatusUtil
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.util.FileUtil
import com.msc.libcommon.viewcardcell.DownloadCardViewCell
import com.msc.libcoremodel.datamodel.http.database.VideoDownloadDatabase
import com.msc.libcoremodel.datamodel.http.entities.VideoDownloadData
import com.msc.videoplayer.R
import com.orhanobut.logger.Logger
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.support.SimpleClickSupport
import io.reactivex.disposables.CompositeDisposable
import java.io.File

class DownloadListViewModel(application: Application) : AndroidViewModel(application), View.OnClickListener {

    private var mApplication: Application

    /**
     * LiveData支持了lifecycle生命周期检测
     * @return
     */
    var liveObservableData: MutableLiveData<List<VideoDownloadData>>
    var liveObservableEditModeData: MutableLiveData<Boolean>

    private val mDisposable = CompositeDisposable()

    var listener: DownloadListViewModelClickSupport

    init {
        ABSENT.value = null
        Logger.d("=======DownloadListViewModel--init=========")
        mApplication = application
        listener = DownloadListViewModelClickSupport()
        liveObservableEditModeData = MutableLiveData()
        liveObservableData = MutableLiveData()
        liveObservableEditModeData.value = false
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

            Logger.d("DownloadCardView--->list.forEach:$it")

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

            if(it.downloadStatus!=StatusUtil.Status.COMPLETED){
                it.downloadStatus = status
                it.totalOffset = info?.totalOffset ?: 0
                it.totalLength = info?.totalLength ?: 0

                VideoDownloadDatabase.getDefault(mApplication)
                        .videoDownloadDao.update(it)
            }

            Logger.d("DownloadCardView--->list.forEach-afterFresh:$it")

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



    override fun onClick(view: View) {
        when(view.id) {
            R.id.tv_edit->{
                liveObservableEditModeData.value = !(liveObservableEditModeData.value!!)
            }
            R.id.tv_delete_all->{
                VideoDownloadDatabase.getDefault(mApplication)
                        .videoDownloadDao.videoDownloadHistoryAll?.forEach {
                    val file = File(it.filePath)
                    if (file.exists()){
                        file.delete()
                    }
                    VideoDownloadDatabase.getDefault(mApplication)
                            .videoDownloadDao.delete(it)
                }

                initData(liveObservableData)
            }
            R.id.tv_delete->{



            }
        }
    }

    inner class DownloadListViewModelClickSupport : SimpleClickSupport() {
        init {
            setOptimizedMode(true)
        }

        override fun defaultClick(targetView: View?, cell: BaseCell<*>?, eventType: Int) {
            super.defaultClick(targetView, cell, eventType)
            val mData: VideoDownloadData? = (cell as DownloadCardViewCell).mData
            val task: DownloadTask? = cell.task

            when (cell.stringType) {
                "download" -> {
                    when (targetView!!.id) {
                        R.id.iv_small_cover ->
                            when (mData!!.downloadStatus) {
                                StatusUtil.Status.COMPLETED -> {
                                    if (mData.dataJson != null) {
                                        ARouter.getInstance()
                                                .build(ARouterPath.VIDEO_PLAYER_ACT)
                                                .withString("data", mData.dataJson)
                                                .navigation()
                                    }
                                }
                                else -> {
                                }
                            }

                        R.id.iv_action ->

                            when (StatusUtil.getStatus(task!!)) {
                                StatusUtil.Status.COMPLETED -> {
                                }
                                StatusUtil.Status.RUNNING, StatusUtil.Status.PENDING -> cell.taskCancel()
                                StatusUtil.Status.IDLE -> cell.taskStart()
                                else -> cell.taskStart()
                            }

                    }

                }
            }
        }
    }


}
