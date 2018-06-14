package com.msc.modulehomepage.viewModel

import android.app.Application
import android.arch.core.util.Function
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.util.Log

import com.msc.libcoremodel.datamodel.http.entities.TabsSelectedData
import com.msc.libcoremodel.datamodel.http.repository.EyepetizerDataRepository
import com.msc.libcoremodel.datamodel.http.utils.NetUtils
import com.orhanobut.logger.Logger

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var mApplication: Application

    //生命周期观察的数据
    /**
     * LiveData支持了lifecycle生命周期检测
     * @return
     */
    var liveObservableData: LiveData<TabsSelectedData>? = null
    //UI使用可观察的数据 ObservableField是一个包装类
    //    public ObservableField<TabsSelectedData> uiObservableData = new ObservableField<>();

    private val mDisposable = CompositeDisposable()

    init {
        ABSENT.value = null
        Logger.d("=======GirlsViewModel--init=========")
        mApplication = application
    }

    /**
     * 设置
     * @param
     */
    fun initData() {
        //这里的trigger为网络检测，也可以换成缓存数据是否存在检测
        liveObservableData = Transformations
                .switchMap<Boolean, TabsSelectedData>(NetUtils.netConnected(mApplication),
                        Function<Boolean, LiveData<TabsSelectedData>> { isNetConnected ->
                            Logger.d("=======GirlsViewModel--apply=========")

                            if (!isNetConnected) {
                                return@Function ABSENT //网络未连接返回空
                            }
                            val applyData = MutableLiveData<TabsSelectedData>()

                            EyepetizerDataRepository.tabsSelectedDataRepository
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(object : Observer<TabsSelectedData> {
                                        override fun onSubscribe(d: Disposable) {
                                            mDisposable.add(d)
                                        }
                                        override fun onNext(value: TabsSelectedData) {
                                            Logger.d("=======GirlsViewModel--onNext=========")
                                            applyData.value = value
                                        }
                                        override fun onError(e: Throwable) {
                                            Logger.d("=======GirlsViewModel--onError=========")
                                            e.printStackTrace()
                                        }
                                        override fun onComplete() {
                                            Logger.d("=======GirlsViewModel--onComplete=========")
                                        }
                                    })
                            applyData
                        })    }

    /**
     * 设置
     * @param product
     */
    fun setUiObservableData(product: TabsSelectedData?) {
//        this.uiObservableData.set(product)
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
        Logger.d("=======GirlsViewModel--onCleared=========")
    }

    companion object {

        private val ABSENT = MutableLiveData<TabsSelectedData>()
    }

}
