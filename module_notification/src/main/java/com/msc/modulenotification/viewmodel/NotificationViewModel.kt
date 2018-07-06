package com.msc.modulenotification.viewmodel

import android.app.Application
import android.arch.core.util.Function
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.text.TextUtils
import com.msc.libcommon.util.Utils
import com.msc.libcoremodel.datamodel.http.entities.AllRecData
import com.msc.libcoremodel.datamodel.http.entities.MessagesData

import com.msc.libcoremodel.datamodel.http.repository.EyepetizerDataRepository
import com.msc.libcoremodel.datamodel.http.utils.NetUtils
import com.orhanobut.logger.Logger

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private var mApplication: Application

    /**
     * LiveData支持了lifecycle生命周期检测
     * @return
     */
    var liveObservableData: MutableLiveData<MessagesData>

    private val mDisposable = CompositeDisposable()

    init {
        ABSENT.value = null
        Logger.d("=======NotificationViewModel--init=========")
        mApplication = application
        //这里的trigger为网络检测，也可以换成缓存数据是否存在检测
        liveObservableData = Transformations
                .switchMap<Boolean, MessagesData>(NetUtils.netConnected(mApplication),
                        Function<Boolean, LiveData<MessagesData>> { isNetConnected ->
                            Logger.d("=======NotificationViewModel--apply=========")
                            if (!isNetConnected) {
                                return@Function ABSENT //网络未连接返回空
                            }
                            val applyData = MutableLiveData<MessagesData>()
//                            initData(applyData)
                            applyData
                        }) as MutableLiveData<MessagesData>
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
    fun initData(liveObservableData: MutableLiveData<MessagesData>) {
        Logger.d("=======NotificationViewModel--initData=========")
    }

    /**
     * 刷新数据
     * @param
     */
    fun getMoreData() {
        getMoreData(liveObservableData)
    }

    private fun getMoreData(liveObservableData: MutableLiveData<MessagesData>) {
        Logger.d("=======NotificationViewModel--initData=========")

    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
        Logger.d("=======NotificationViewModel--onCleared=========")
    }

    companion object {
        private val ABSENT = MutableLiveData<MessagesData>()
    }

}
