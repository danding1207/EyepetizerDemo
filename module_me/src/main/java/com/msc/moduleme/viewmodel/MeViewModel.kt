package com.msc.moduleme.viewmodel

import android.app.Application
import android.arch.core.util.Function
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.msc.libcommon.util.Utils
import com.msc.libcoremodel.datamodel.http.entities.AllRecData

import com.msc.libcoremodel.datamodel.http.repository.EyepetizerDataRepository
import com.msc.libcoremodel.datamodel.http.utils.NetUtils
import com.orhanobut.logger.Logger

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MeViewModel(application: Application) : AndroidViewModel(application) {

    private var mApplication: Application

    /**
     * LiveData支持了lifecycle生命周期检测
     * @return
     */
    var liveObservableData: MutableLiveData<AllRecData>

    private val mDisposable = CompositeDisposable()

    init {
        ABSENT.value = null
        Logger.d("=======MeViewModel--init=========")
        mApplication = application
        //这里的trigger为网络检测，也可以换成缓存数据是否存在检测
        liveObservableData = Transformations
                .switchMap<Boolean, AllRecData>(NetUtils.netConnected(mApplication),
                        Function<Boolean, LiveData<AllRecData>> { isNetConnected ->
                            Logger.d("=======MeViewModel--apply=========")
                            if (!isNetConnected) {
                                return@Function ABSENT //网络未连接返回空
                            }
                            val applyData = MutableLiveData<AllRecData>()
//                            initData(applyData)
                            applyData
                        }) as MutableLiveData<AllRecData>
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
    fun initData(liveObservableData: MutableLiveData<AllRecData>) {
        Logger.d("=======MeViewModel--initData=========")
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
        Logger.d("=======MeViewModel--onCleared=========")
    }

    companion object {
        private val ABSENT = MutableLiveData<AllRecData>()
    }

}
