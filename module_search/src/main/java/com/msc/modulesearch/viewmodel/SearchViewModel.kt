package com.msc.modulesearch.viewmodel

import android.app.Application
import android.arch.core.util.Function
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.support.v7.widget.RecyclerView
import android.view.View
import com.msc.libcommon.base.ViewManager
import com.msc.libcommon.listener.RecyclerItemClickListener
import com.msc.libcommon.util.Utils
import com.msc.libcoremodel.datamodel.http.database.SearchHotsDatabase
import com.msc.libcoremodel.datamodel.http.entities.SearchHotsData
import com.msc.libcoremodel.datamodel.http.repository.EyepetizerDataRepository
import com.msc.libcoremodel.datamodel.http.utils.NetUtils
import com.msc.modulesearch.BuildConfig
import com.msc.modulesearch.R
import com.msc.modulesearch.ui.SearchActivity
import com.orhanobut.logger.Logger
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel(application: Application) : AndroidViewModel(application), View.OnClickListener {


    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_cancle ->
                ViewManager.instance.finishAfterTransitionActivity(SearchActivity::class.java)
            R.id.tv_delete ->{
                Logger.i("=======TouchListener=========   delete")

                SearchHotsDatabase.getDefault(mApplication).searchHotsDao.delete(SearchHotsDatabase.getDefault(mApplication).searchHotsDao.searchHotsHistoryAll)
            }

        }
    }

    private var mApplication: Application

    private var headersListener: StickyRecyclerHeadersTouchListener? = null
    var itemsListener: RecyclerItemClickListener

    fun getStickyRecyclerHeadersTouchListener(recyclerView: RecyclerView,
                                              decor: StickyRecyclerHeadersDecoration): StickyRecyclerHeadersTouchListener {
        if (headersListener == null) {
            headersListener = StickyRecyclerHeadersTouchListener(recyclerView, decor)
            headersListener!!.setOnHeaderClickListener { header, position, headerId ->
                Logger.i("=======TouchListener=========   Header position: $position, id: $headerId")

                    SearchHotsDatabase.getDefault(mApplication).searchHotsDao.delete(SearchHotsDatabase.getDefault(mApplication).searchHotsDao.searchHotsHistoryAll)

            }
        }
        return headersListener!!
    }

    /**
     * LiveData支持了lifecycle生命周期检测
     * @return
     */
    var liveObservableData: MutableLiveData<List<SearchHotsData>>

    private val mDisposable = CompositeDisposable()

    init {
        ABSENT.value = null
        Logger.d("=======SearchViewModel--init=========")
        mApplication = application

        //这里的trigger为网络检测，也可以换成缓存数据是否存在检测
        liveObservableData = Transformations
                .switchMap<Boolean, List<SearchHotsData>>(NetUtils.netConnected(mApplication),
                        Function<Boolean, LiveData<List<SearchHotsData>>> { isNetConnected ->
                            Logger.d("=======SearchViewModel--apply=========")
                            if (!isNetConnected) {
                                return@Function ABSENT //网络未连接返回空
                            }
                            val applyData = MutableLiveData<List<SearchHotsData>>()
                            initData(applyData)
                            applyData
                        }) as MutableLiveData<List<SearchHotsData>>

        itemsListener = RecyclerItemClickListener(mApplication.applicationContext, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Logger.i("=======TouchListener=========   position: $position")

                //TODO 搜索
                //加入搜索历史记录
                val data = liveObservableData.value!![position]
                if (data.type == "nethots") {
                    data.isCanDelete = true
                    data.type = "cacheList"
                    data.typeTitle = "搜索历史"
                    SearchHotsDatabase.getDefault(mApplication).searchHotsDao.insert(data)
                }
            }
        })
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
    private fun initData(liveObservableData: MutableLiveData<List<SearchHotsData>>) {
        Logger.d("=======SearchViewModel--initData=========")

        val historylist = SearchHotsDatabase.getDefault(mApplication).searchHotsDao.searchHotsHistoryAll

        EyepetizerDataRepository.getSearchHotsDataRepository(
                Utils.getUDID(),
                BuildConfig.VERSION_CODE.toString(),
                BuildConfig.VERSION_NAME,
                Utils.getSystemModel(),
                "eyepetizer_xiaomi_market",
                "eyepetizer_xiaomi_market",
                Utils.getSystemVersion()
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<SearchHotsData>> {
                    override fun onSubscribe(d: Disposable) {
                        mDisposable.add(d)
                    }

                    override fun onNext(value: List<SearchHotsData>) {
                        Logger.d("=======SearchViewModel--onNext=========")
                        if (historylist != null)
                            (value as ArrayList).addAll(0, historylist)


                        (value as ArrayList).add(0, SearchHotsData("", "", "", false))

                        value.forEach {
                            Logger.d("=======historylist=========    ${it.id }    ${it.type }    ${it.typeTitle }    ${it.name }    ${it.isCanDelete }")
                        }



                        liveObservableData.value = value
                    }

                    override fun onError(e: Throwable) {
                        Logger.d("=======SearchViewModel--onError=========")
                        if (historylist != null) {
                            (historylist as ArrayList).add(0, SearchHotsData("", "", "", false))

                            liveObservableData.value = historylist
                        } else
                            liveObservableData.value = ABSENT.value
                        e.printStackTrace()
                    }

                    override fun onComplete() {
                        Logger.d("=======SearchViewModel--onComplete=========")
                    }
                })

    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
        Logger.d("=======SearchViewModel--onCleared=========")
    }

    companion object {
        private val ABSENT = MutableLiveData<List<SearchHotsData>>()
    }

}
