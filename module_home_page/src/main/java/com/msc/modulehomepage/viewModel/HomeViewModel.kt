package com.msc.modulehomepage.viewModel

import android.app.Application
import android.arch.core.util.Function
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.support.design.widget.Snackbar
import android.util.Log
import com.msc.libcoremodel.datamodel.http.entities.AllRecData

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

    private var mApplication: Application

    private var page = 0

    /**
     * LiveData支持了lifecycle生命周期检测
     * @return
     */
    var liveObservableData: MutableLiveData<AllRecData>

    //UI使用可观察的数据 ObservableField是一个包装类
    //    public ObservableField<TabsSelectedData> uiObservableData = new ObservableField<>();

    private val mDisposable = CompositeDisposable()

    init {
        ABSENT.value = null
        Logger.d("=======GirlsViewModel--init=========")
        mApplication = application
        //这里的trigger为网络检测，也可以换成缓存数据是否存在检测
        liveObservableData = Transformations
                .switchMap<Boolean, AllRecData>(NetUtils.netConnected(mApplication),
                        Function<Boolean, LiveData<AllRecData>> { isNetConnected ->
                            Logger.d("=======GirlsViewModel--apply=========")
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
        Logger.d("=======GirlsViewModel--initData=========")
        EyepetizerDataRepository.getAllRecDataRepository(
                page.toString(),"5ab5bd3e87e04215bf7820e58576aa192784ca51","341","3.19",
                "MI%203W","eyepetizer_xiaomi_market","eyepetizer_xiaomi_market",
                "23"
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<AllRecData> {
                    override fun onSubscribe(d: Disposable) {
                        mDisposable.add(d)
                    }
                    override fun onNext(value: AllRecData) {
                        Logger.d("=======GirlsViewModel--onNext=========")
                        liveObservableData.value = value
                    }
                    override fun onError(e: Throwable) {
                        Logger.d("=======GirlsViewModel--onError=========")
                        e.printStackTrace()
                    }
                    override fun onComplete() {
                        Logger.d("=======GirlsViewModel--onComplete=========")
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
                            mDisposable.add(d)
                        }
                        override fun onNext(value: AllRecData) {
                            Logger.d("=======GirlsViewModel--onNext=========")
                            val oldValue = liveObservableData.value
                            (oldValue!!.itemList as MutableList<AllRecData.ItemListBeanX>).addAll(value.itemList!!)
                            oldValue.count +=value.count
                            oldValue.total =value.total
                            oldValue.nextPageUrl =value.nextPageUrl
                            oldValue.adExist =value.adExist
                            liveObservableData.value = oldValue
                        }
                        override fun onError(e: Throwable) {
                            Logger.d("=======GirlsViewModel--onError=========")
                            e.printStackTrace()
                        }
                        override fun onComplete() {
                            Logger.d("=======GirlsViewModel--onComplete=========")
                        }
                    })
        } else {

        }

    }

    /**
     * 设置
     * @param product
     */
    fun setUiObservableData(product: AllRecData?) {
//        this.uiObservableData.set(product)
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
        Logger.d("=======GirlsViewModel--onCleared=========")
    }

    companion object {
        private val ABSENT = MutableLiveData<AllRecData>()
    }

}
