package com.msc.modulelaunch.viewmodel

import android.app.Application
import android.arch.core.util.Function
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.msc.libcommon.util.Utils
import com.msc.libcoremodel.datamodel.http.entities.ConfigsData

import com.msc.libcoremodel.datamodel.http.repository.EyepetizerDataRepository
import com.msc.libcoremodel.datamodel.http.utils.NetUtils
import com.msc.modulelaunch.BuildConfig
import com.orhanobut.logger.Logger

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class WelcomeViewModel(application: Application) : AndroidViewModel(application) {

    private var mApplication: Application

    /**
     * LiveData支持了lifecycle生命周期检测
     * @return
     */
    var liveObservableData: MutableLiveData<ConfigsData>

    private val mDisposable = CompositeDisposable()

    init {
        ABSENT.value = null
        Logger.d("=======WelcomeViewModel--init=========")
        mApplication = application
        //这里的trigger为网络检测，也可以换成缓存数据是否存在检测
        liveObservableData = Transformations
                .switchMap<Boolean, ConfigsData>(
                        NetUtils.netConnected(mApplication),
                        Function<Boolean, LiveData<ConfigsData>> {
                            isNetConnected ->
                            Logger.d("=======WelcomeViewModel--apply=========")
                            if (!isNetConnected) {
                                return@Function ABSENT //网络未连接返回空
                            }
                            val applyData = MutableLiveData<ConfigsData>()
                            initData(applyData)
                            applyData
                        }) as MutableLiveData<ConfigsData>
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
    fun initData(liveObservableData: MutableLiveData<ConfigsData>) {
        Logger.d("=======WelcomeViewModel--initData=========")
        EyepetizerDataRepository.getConfigsDataRepository(
                "Android", Utils.getUDID(),
                BuildConfig.VERSION_CODE.toString(),
                BuildConfig.VERSION_NAME,
                Utils.getSystemModel(),
                "eyepetizer_xiaomi_market",
                "eyepetizer_xiaomi_market",
                Utils.getSystemVersion()
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ConfigsData> {
                    override fun onSubscribe(d: Disposable) {
                        mDisposable.add(d)
                    }
                    override fun onNext(value: ConfigsData) {
                        Logger.d("=======WelcomeViewModel--onNext=========")
                        Logger.d("=======ConfigsData=========:"+value.toString())
                        liveObservableData.value = value
                    }
                    override fun onError(e: Throwable) {
                        liveObservableData.value = ABSENT.value //网络未连接返回空
                        Logger.e("=======WelcomeViewModel--onError=========")
                        e.printStackTrace()
                    }
                    override fun onComplete() {
                        Logger.d("=======WelcomeViewModel--onComplete=========")
                    }
                })
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
        Logger.d("=======WelcomeViewModel--onCleared=========")
    }

    companion object {
        private val ABSENT = MutableLiveData<ConfigsData>()
    }

}
