package com.msc.videoplayer.viewmodel

import android.app.Application
import android.arch.core.util.Function
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.design.widget.Snackbar
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.SpeedCalculator
import com.liulishuo.okdownload.core.Util
import com.liulishuo.okdownload.core.breakpoint.BlockInfo
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend
import com.msc.libcommon.R
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcommon.util.FileUtil
import com.msc.libcommon.util.Utils
import com.msc.libcoremodel.datamodel.http.entities.CommonData
import com.msc.libcoremodel.datamodel.http.repository.EyepetizerDataRepository
import com.msc.libcoremodel.datamodel.http.utils.NetUtils
import com.msc.videoplayer.BuildConfig
import com.orhanobut.logger.Logger
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.support.SimpleClickSupport
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class VideoPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private var mApplication: Application

    /**
     * LiveData支持了lifecycle生命周期检测
     * @return
     */
    var liveObservableData: MutableLiveData<CommonData>

    var item: CommonData.CommonItemList? = null

    private val mDisposable = CompositeDisposable()

    var listener: VideoPlayerViewModelClickSupport

    private var task: DownloadTask? = null

    init {
        ABSENT.value = null
        Logger.d("=======VideoPlayerViewModel--init=========")
        mApplication = application
        listener = VideoPlayerViewModelClickSupport()

        //这里的trigger为网络检测，也可以换成缓存数据是否存在检测
        liveObservableData = Transformations
                .switchMap<Boolean, CommonData>(
                        NetUtils.netConnected(mApplication),
                        Function<Boolean, LiveData<CommonData>> { isNetConnected ->
                            Logger.d("=======VideoPlayerViewModel--apply=========")
                            if (!isNetConnected) {
                                return@Function ABSENT //网络未连接返回空
                            }
                            val applyData = MutableLiveData<CommonData>()
//                            initData(applyData)
                            applyData
                        }) as MutableLiveData<CommonData>
    }

    fun initTask() {

        if (item == null) return

        val filename = item!!.data!!.title

        val url = item!!.data!!.playUrl!!

        task = DownloadTask
                .Builder(url, FileUtil.getVideoFile(mApplication))
                .setFilename(filename)
                // the minimal interval millisecond for callback progress
                .setMinIntervalMillisCallbackProcess(16)
                // ignore the same task has already completed in the past.
                .setPassIfAlreadyCompleted(true)
                .build()

        task!!.tag = "mark-task-started"

        task!!.enqueue(object : DownloadListener4WithSpeed() {

            private var totalLength: Long = 0
            private var readableTotalLength: String? = null

            override fun taskStart(@NonNull task: DownloadTask) {

                Logger.d("DownloadTask---->taskStart")

            }

            override fun infoReady(@NonNull task: DownloadTask,
                                   @NonNull info: BreakpointInfo,
                                   fromBreakpoint: Boolean,
                                   @NonNull model: Listener4SpeedAssistExtend.Listener4SpeedModel) {

                totalLength = info.totalLength
                readableTotalLength = Util.humanReadableBytes(info.totalLength, true)
                Logger.d("DownloadTask---->infoReady:totalLength= ${info.totalLength}")
                Logger.d("DownloadTask---->infoReady:readableTotalLength= ${Util.humanReadableBytes(info.totalLength, true)}")
                Logger.d("DownloadTask---->infoReady:Percent= ${info.totalOffset /info.totalLength}")

            }

            override fun connectStart(@NonNull task: DownloadTask, blockIndex: Int,
                                      @NonNull requestHeaders: Map<String, List<String>>) {

                Logger.d("DownloadTask---->connectStart: Connect Start $blockIndex")

            }

            override fun connectEnd(@NonNull task: DownloadTask, blockIndex: Int,
                                    responseCode: Int,
                                    @NonNull responseHeaders: Map<String, List<String>>) {

                Logger.d("DownloadTask---->connectStart: Connect End $blockIndex")

            }

            override fun progressBlock(@NonNull task: DownloadTask, blockIndex: Int,
                                       currentBlockOffset: Long,
                                       @NonNull blockSpeed: SpeedCalculator) {
            }

            override fun progress(@NonNull task: DownloadTask, currentOffset: Long,
                                  @NonNull taskSpeed: SpeedCalculator) {

                Logger.d("DownloadTask---->progress:readableOffset= ${Util.humanReadableBytes(currentOffset, true)}")
                Logger.d("DownloadTask---->progress:progressStatus= ${Util.humanReadableBytes(currentOffset, true)} / $readableTotalLength")
                Logger.d("DownloadTask---->progress:speed=${taskSpeed.speed()}")
                Logger.d("DownloadTask---->progress:Percent= ${currentOffset/(totalLength as Float)}")

            }

            override fun blockEnd(@NonNull task: DownloadTask, blockIndex: Int,
                                  info: BlockInfo,
                                  @NonNull blockSpeed: SpeedCalculator) {
            }

            override fun taskEnd(task: DownloadTask,
                                 cause: EndCause,
                                 realCause: Exception?,
                                 taskSpeed: SpeedCalculator) {

                Logger.d("DownloadTask---->taskEnd:taskSpeed= ${taskSpeed.averageSpeed()}")

                Logger.d("DownloadTask---->taskEnd:taskSpeed= ${taskSpeed.averageSpeed()}")


//                // mark
                task.tag = null

                if (cause == EndCause.COMPLETED) {
                    Logger.d("DownloadTask---->taskEnd:COMPLETED")
                }
            }
        })
    }

    /**
     * 刷新数据
     * @param
     */
    fun initData(vid: String) {
        initData(vid, liveObservableData)
    }

    /**
     * 刷新数据
     * @param
     */
    fun initData(vid: String, liveObservableData: MutableLiveData<CommonData>) {
        Logger.d("=======VideoPlayerViewModel--initData=========")
        EyepetizerDataRepository.getVideoRelatedDataRepository(
                vid, Utils.getUDID(),
                BuildConfig.VERSION_CODE.toString(),
                BuildConfig.VERSION_NAME,
                Utils.getSystemModel(),
                "eyepetizer_xiaomi_market",
                "eyepetizer_xiaomi_market",
                Utils.getSystemVersion()
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonData> {
                    override fun onSubscribe(d: Disposable) {
                        mDisposable.add(d)
                    }

                    override fun onNext(value: CommonData) {
                        Logger.d("=======VideoPlayerViewModel--onNext=========")

                        if (item != null) {
                            item!!.type = "header"
                            (value.itemList as ArrayList<CommonData.CommonItemList>)
                                    .add(0, item!!)
                        }

                        val enddata = CommonData.CommonItemList()
                        enddata.type = "end"
                        (value.itemList as ArrayList<CommonData.CommonItemList>).add(enddata)


                        value.itemList!!.forEach {
                            it.color = "white"
                        }

                        liveObservableData.value = value
                    }

                    override fun onError(e: Throwable) {

                        val value = CommonData()

                        value.itemList = ArrayList()

                        if (item != null) {
                            item!!.type = "header"
                            (value.itemList as ArrayList<CommonData.CommonItemList>)
                                    .add(item!!)
                        }

                        val enddata = CommonData.CommonItemList()
                        enddata.type = "end"
                        (value.itemList as ArrayList<CommonData.CommonItemList>).add(enddata)

                        liveObservableData.value = value //网络未连接返回空

                        Logger.e("=======VideoPlayerViewModel--onError=========")
                        e.printStackTrace()
                    }

                    override fun onComplete() {
                        Logger.d("=======VideoPlayerViewModel--onComplete=========")
                    }
                })
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
        Logger.d("=======VideoPlayerViewModel--onCleared=========")
    }

    companion object {
        private val ABSENT = MutableLiveData<CommonData>()
    }

    inner class VideoPlayerViewModelClickSupport : SimpleClickSupport() {
        init {
            setOptimizedMode(true)
        }

        override fun defaultClick(targetView: View?, cell: BaseCell<*>?, eventType: Int) {
            super.defaultClick(targetView, cell, eventType)
            val mData: CommonData.CommonItemList? = (cell as CommenDataCell<*, CommonData.CommonItemList>).mData
            when (cell.stringType) {
                "videoSmallCard" -> {
                    if (mData !== null && mData.data!!.resourceType != null && "video" == mData.data!!.resourceType) {
                        ARouter.getInstance()
                                .build(ARouterPath.VIDEO_PLAYER_ACT)
                                .withString("data", Gson().toJson(mData))
                                .navigation()
                    }
                }
                "header" -> {
                    when (targetView!!.id) {
                        R.id.iv_download -> {
                            Snackbar.make(targetView, "开始缓存", Snackbar.LENGTH_SHORT).show()
                            initTask()
                        }
                        R.id.iv_like -> {
                            Snackbar.make(targetView, "添加至收藏", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }


}
