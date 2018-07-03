package com.msc.modulehomepage.viewmodel

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
import com.msc.modulehomepage.BuildConfig
import com.orhanobut.logger.Logger

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
    var liveObservableData: MutableLiveData<AllRecData>

    private val mDisposable = CompositeDisposable()

    init {
        ABSENT.value = null
        Logger.d("=======HomeViewModel--init=========")
        mApplication = application
        //这里的trigger为网络检测，也可以换成缓存数据是否存在检测
        liveObservableData = Transformations
                .switchMap<Boolean, AllRecData>(
                        NetUtils.netConnected(mApplication),
                        Function<Boolean, LiveData<AllRecData>> {
                            isNetConnected ->
                            Logger.d("=======HomeViewModel--apply=========")
                            if (!isNetConnected) {
                                return@Function ABSENT //网络未连接返回空
                            }
                            val applyData = MutableLiveData<AllRecData>()
                            initData(applyData)
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
                .subscribe(object : Observer<AllRecData> {
                    override fun onSubscribe(d: Disposable) {
                        mDisposable.add(d)
                    }
                    override fun onNext(value: AllRecData) {
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

    private fun getMoreData(liveObservableData: MutableLiveData<AllRecData>) {
        Logger.d("=======GirlsViewModel--initData=========")

        if(liveObservableData.value!=null && liveObservableData.value!!.nextPageUrl !=null) {
            EyepetizerDataRepository.getMoreRecDataRepository(
                    liveObservableData.value!!.nextPageUrl!!
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<AllRecData> {
                        override fun onSubscribe(d: Disposable) {
                        }
                        override fun onNext(value: AllRecData) {
                            Logger.d("=======HomeViewModel--onNext=========")
                            val oldValue = liveObservableData.value
                            (oldValue!!.itemList as MutableList<AllRecData.ItemListBeanX>).addAll(value.itemList!!)
                            oldValue.count +=value.count
                            oldValue.total =value.total
                            oldValue.nextPageUrl =value.nextPageUrl
                            oldValue.adExist =value.adExist
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
        private val ABSENT = MutableLiveData<AllRecData>()
    }

}
