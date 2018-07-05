package com.msc.modulehomepage.viewmodel

import android.app.Application
import android.arch.core.util.Function
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcommon.util.Utils
import com.msc.libcommon.viewcard.VideoSmallCardView
import com.msc.libcoremodel.datamodel.http.entities.AllRecData
import com.msc.libcoremodel.datamodel.http.entities.CommonData

import com.msc.libcoremodel.datamodel.http.repository.EyepetizerDataRepository
import com.msc.libcoremodel.datamodel.http.utils.NetUtils
import com.msc.modulehomepage.BuildConfig
import com.orhanobut.logger.Logger
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.support.SimpleClickSupport
import io.reactivex.Observable

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private var mApplication: Application

    /**
     * LiveData支持了lifecycle生命周期检测
     * @return
     */
    var liveObservableData: MutableLiveData<CommonData>

    var listener: HomeViewModelClickSupport

    private val mDisposable = CompositeDisposable()

    init {
        ABSENT.value = null
        Logger.d("=======HomeViewModel--init=========")
        mApplication = application

        listener = HomeViewModelClickSupport()


        //这里的trigger为网络检测，也可以换成缓存数据是否存在检测
        liveObservableData = Transformations
                .switchMap<Boolean, CommonData>(
                        NetUtils.netConnected(mApplication),
                        Function<Boolean, LiveData<CommonData>> { isNetConnected ->
                            Logger.d("=======HomeViewModel--apply=========")
                            if (!isNetConnected) {
                                return@Function ABSENT //网络未连接返回空
                            }
                            val applyData = MutableLiveData<CommonData>()
                            initData(applyData)
                            applyData
                        }) as MutableLiveData<CommonData>
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
    fun initData(liveObservableData: MutableLiveData<CommonData>) {
        Logger.d("=======HomeViewModel--initData=========")
        EyepetizerDataRepository.getAllRecDataRepository(
                "0", Utils.getUDID(),
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
                        Logger.d("=======HomeViewModel--onNext=========")
                        liveObservableData.value = value
                    }

                    override fun onError(e: Throwable) {
                        liveObservableData.value = ABSENT.value //网络未连接返回空
                        Logger.e("=======HomeViewModel--onError=========")
                        e.printStackTrace()
                    }

                    override fun onComplete() {
                        Logger.d("=======HomeViewModel--onComplete=========")
                    }
                })
    }

    /**
     * 刷新数据
     * @param
     */
    fun getMoreData() {
        getMoreData(liveObservableData)
    }

    private fun getMoreData(liveObservableData: MutableLiveData<CommonData>) {
        Logger.d("=======GirlsViewModel--initData=========")

        if (liveObservableData.value != null && liveObservableData.value!!.nextPageUrl != null) {
            EyepetizerDataRepository.getMoreRecDataRepository(
                    liveObservableData.value!!.nextPageUrl!!
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CommonData> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(value: CommonData) {
                            Logger.d("=======HomeViewModel--onNext=========")
                            val oldValue = liveObservableData.value
                            (oldValue!!.itemList as ArrayList<CommonData.CommonItemList>).addAll(value.itemList!!)
                            oldValue.count += value.count
                            oldValue.total = value.total
                            oldValue.nextPageUrl = value.nextPageUrl
                            oldValue.adExist = value.adExist
                            liveObservableData.value = oldValue
                        }

                        override fun onError(e: Throwable) {
                            Logger.d("=======HomeViewModel--onError=========")
                            e.printStackTrace()
                        }

                        override fun onComplete() {
                            Logger.d("=======HomeViewModel--onComplete=========")
                        }
                    })
        } else {

        }

    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
        Logger.d("=======HomeViewModel--onCleared=========")
    }

    companion object {
        private val ABSENT = MutableLiveData<CommonData>()
    }

    class HomeViewModelClickSupport : SimpleClickSupport() {
        init {
            setOptimizedMode(true)
        }

        override fun defaultClick(targetView: View?, cell: BaseCell<*>?, eventType: Int) {
            super.defaultClick(targetView, cell, eventType)

            val mData: CommonData.CommonItemList? = (cell as CommenDataCell<*, CommonData.CommonItemList>).mData

            when (cell.stringType) {
                "followCard", "autoPlayFollowCard", "pictureFollowCard" -> {
                    when (mData!!.data!!.content!!.type) {
                        "video" -> {
                            ARouter.getInstance()
                                    .build(ARouterPath.VIDEO_PLAYER_ACT)
//                                    .withString("videoUri", mData.data!!.content!!.data!!.playUrl)
//                                    .withString("videoId", mData.data!!.content!!.data!!.id.toString())
                                    .withString("data", cell.extras.toString())
                                    .navigation()
                        }
                        "ugcPicture" -> {
                            ARouter.getInstance()
                                    .build(ARouterPath.PICTURE_DETAIL_ACT)
                                    .withString("data", cell.extras.toString())

                                    .withString("avatarUrl", mData.data!!.content!!.data!!.owner!!.avatar)
                                    .withString("nickname", mData.data!!.content!!.data!!.owner!!.nickname)
                                    .withString("description", mData.data!!.content!!.data!!.description)
                                    .withString("collectionCount", mData.data!!.content!!.data!!.consumption!!.collectionCount.toString())
                                    .withString("shareCount", mData.data!!.content!!.data!!.consumption!!.shareCount.toString())
                                    .withString("replyCount", mData.data!!.content!!.data!!.consumption!!.replyCount.toString())
                                    .withString("pictureUrl", mData.data!!.content!!.data!!.url)
                                    .navigation()
                        }
                    }

                }
                "videoSmallCard" -> {
                    if (mData !== null && mData.data!!.resourceType != null && "video" == mData.data!!.resourceType) {
                        ARouter.getInstance()
                                .build(ARouterPath.VIDEO_PLAYER_ACT)
//                                .withString("videoUri", mData.data!!.playUrl)
//                                .withString("videoId", mData.id.toString())
                                .withString("data", cell.extras.toString())
                                .navigation()
                    }
                }
            }
        }
    }


}
