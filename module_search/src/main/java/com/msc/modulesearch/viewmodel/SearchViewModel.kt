package com.msc.modulesearch.viewmodel

import android.app.Application
import android.arch.core.util.Function
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcommon.base.ViewManager
import com.msc.libcommon.listener.RecyclerItemClickListener
import com.msc.libcommon.util.ConstantUtils
import com.msc.libcommon.util.KeyboardUtils
import com.msc.libcommon.util.Utils
import com.msc.libcoremodel.datamodel.http.database.SearchHotsDatabase
import com.msc.libcoremodel.datamodel.http.entities.CommonData
import com.msc.libcoremodel.datamodel.http.entities.MessagesData
import com.msc.libcoremodel.datamodel.http.entities.SearchHotsData
import com.msc.libcoremodel.datamodel.http.repository.EyepetizerDataRepository
import com.msc.libcoremodel.datamodel.http.utils.NetUtils
import com.msc.modulesearch.BuildConfig
import com.msc.modulesearch.R
import com.msc.modulesearch.ui.SearchActivity
import com.orhanobut.logger.Logger
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.support.SimpleClickSupport
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel(application: Application) : AndroidViewModel(application),
        View.OnClickListener, TextView.OnEditorActionListener, TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        if (s!!.isEmpty()) {
            liveObservableSearchData.value = null
            liveObservableSearchResultData.value = ABSSENT.value //网络未连接返回空
        } else {
            liveObservableSearchData.value = s.toString()
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        //按下了右下角的搜索,扫描枪测试actionId为0
        //输入法显示的时候，actionId=3，就是右下角的按钮的id
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == 0) {
            KeyboardUtils.hideSoftInput(v)
            liveObservableSearchData.value = v!!.text.trim().toString()
            initSearchResultDataData(v.text.trim().toString())

            val list = SearchHotsDatabase.getDefault(mApplication).searchHotsDao
                    .loadSearchHotsDataByName(v.text.trim().toString())
            if (list.isEmpty()) {
                val data = SearchHotsData()
                //加入搜索历史记录
                data.isCanDelete = true
                data.type = "cacheList"
                data.typeTitle = "搜索历史"
                data.name = v.text.trim().toString()
                SearchHotsDatabase.getDefault(mApplication).searchHotsDao
                        .insert(data)
                initSearchHotsData(true)
            }

            return true
        }
        return false
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_action_clear_dark -> {
                liveObservableSearchData.value = null
                liveObservableSearchResultData.value = ABSSENT.value //网络未连接返回空
            }
            R.id.tv_cancle ->
                ViewManager.instance.finishAfterTransitionActivity(SearchActivity::class.java)
            R.id.tv_delete -> {
                Logger.i("=======TouchListener=========   delete")

                SearchHotsDatabase.getDefault(mApplication).searchHotsDao.delete(SearchHotsDatabase.getDefault(mApplication).searchHotsDao.searchHotsHistoryAll)
                initSearchHotsData(true)
            }

        }
    }

    private var mApplication: Application

    var itemsListener: RecyclerItemClickListener

    /**
     * LiveData支持了lifecycle生命周期检测
     * @return
     */
    var liveObservableSearchHotsData: MutableLiveData<List<SearchHotsData>>
    var liveObservableSearchData: MutableLiveData<String>
    var liveObservableSearchResultData: MutableLiveData<CommonData>

    private val mDisposable = CompositeDisposable()

    var listener: SearchViewModelClickSupport

    init {
        ABSENT.value = null
        ABSSENT.value = null

        Logger.d("=======SearchViewModel--init=========")
        mApplication = application

        listener = SearchViewModelClickSupport()

        //这里的trigger为网络检测，也可以换成缓存数据是否存在检测
        liveObservableSearchHotsData = Transformations
                .switchMap<Boolean, List<SearchHotsData>>(NetUtils.netConnected(mApplication),
                        Function<Boolean, LiveData<List<SearchHotsData>>> { isNetConnected ->
                            Logger.d("=======SearchViewModel--apply=========")
                            if (!isNetConnected) {
                                return@Function ABSENT //网络未连接返回空
                            }
                            val applyData = MutableLiveData<List<SearchHotsData>>()
                            initSearchHotsData(false, applyData)
                            applyData
                        }) as MutableLiveData<List<SearchHotsData>>

        liveObservableSearchResultData = Transformations
                .switchMap<Boolean, CommonData>(NetUtils.netConnected(mApplication),
                        Function<Boolean, LiveData<CommonData>> { isNetConnected ->
                            Logger.d("=======SearchViewModel--apply=========")
                            if (!isNetConnected) {
                                return@Function ABSSENT //网络未连接返回空
                            }
                            val applyData = MutableLiveData<CommonData>()
                            applyData
                        }) as MutableLiveData<CommonData>


        liveObservableSearchData = MutableLiveData()


        itemsListener = RecyclerItemClickListener(mApplication.applicationContext, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Logger.i("=======TouchListener=========   position: $position")
                val data = liveObservableSearchHotsData.value!![position]

                if (data.isHeader) return

                liveObservableSearchData.value = data.name

                initSearchResultDataData(data.name!!)

                val list = SearchHotsDatabase.getDefault(mApplication).searchHotsDao.loadSearchHotsDataByName(data.name!!)
                //加入搜索历史记录
                if (list.isEmpty() && data.type == "nethots") {
                    data.isCanDelete = true
                    data.type = "cacheList"
                    data.typeTitle = "搜索历史"
                    SearchHotsDatabase.getDefault(mApplication).searchHotsDao.insert(data)
                    initSearchHotsData(true)
                }
            }
        })
    }

    /**
     * 刷新数据
     * @param
     */
    fun initSearchHotsData(onlyRoom: Boolean) {
        initSearchHotsData(onlyRoom, liveObservableSearchHotsData)
    }

    /**
     * 刷新数据
     * @param
     */
    private fun initSearchHotsData(onlyRoom: Boolean, liveObservableData: MutableLiveData<List<SearchHotsData>>) {
        Logger.d("=======SearchViewModel--initData=========")

        val historylist = SearchHotsDatabase.getDefault(mApplication).searchHotsDao.searchHotsHistoryAll

        if (onlyRoom) {

            var notHeader: List<SearchHotsData>? = null

            if (liveObservableData.value != null) {
                notHeader = liveObservableData.value!!.filter {
                    it.type == "nethots" && it.isHeader==false
                }
            }

            if (historylist != null) {
                if (notHeader != null)
                    historylist.addAll(notHeader)
                liveObservableData.value = historylist
            } else
                liveObservableData.value = notHeader
            return
        }

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
                        liveObservableData.value = value
                    }

                    override fun onError(e: Throwable) {
                        Logger.d("=======SearchViewModel--onError=========")
                        if (historylist != null) {
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

    /**
     * 刷新数据
     * @param
     */
    fun initSearchResultDataData(query: String) {
        initSearchResultDataData(query, liveObservableSearchResultData)
    }

    /**
     * 刷新数据
     * @param
     */
    private fun initSearchResultDataData(query: String, liveObservableData: MutableLiveData<CommonData>) {
        Logger.d("=======SearchViewModel--initData=========")
        EyepetizerDataRepository.getSearchDataRepository(
                query, Utils.getUDID(),
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
                        Logger.d("=======SearchViewModel--onNext=========")
                        if (value.count == 0) {
                            val emptyTip = ConstantUtils.getEmptyCommonItemList()
                            (value.itemList
                                    as MutableList<CommonData.CommonItemList>)
                                    .add(emptyTip)
                        }
                        liveObservableData.value = value
                    }

                    override fun onError(e: Throwable) {

                        val value = CommonData()

                        value.itemList = ArrayList()

                        val emptyTip = ConstantUtils.getNetworkErrorCommonItemList()
                        (value.itemList
                                as MutableList<CommonData.CommonItemList>)
                                .add(emptyTip)

                        liveObservableData.value = value //网络未连接返回空
                        Logger.e("=======SearchViewModel--onError=========")
                        e.printStackTrace()
                    }

                    override fun onComplete() {
                        Logger.d("=======SearchViewModel--onComplete=========")
                    }
                })
    }

    /**
     * 刷新数据
     * @param
     */
    fun getMoreSearchResultData() {
        getMoreSearchResultData(liveObservableSearchResultData)
    }

    private fun getMoreSearchResultData(liveObservableData: MutableLiveData<CommonData>) {
        Logger.d("=======SearchViewModel--initData=========")

        if (liveObservableData.value != null && liveObservableData.value!!.nextPageUrl != null) {
            EyepetizerDataRepository.getMoreSearchDataRepository(
                    liveObservableData.value!!.nextPageUrl!!
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CommonData> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(value: CommonData) {
                            Logger.d("=======SearchViewModel--onNext=========")
                            val oldValue = liveObservableData.value
                            (oldValue!!.itemList as ArrayList<CommonData.CommonItemList>).addAll(value.itemList!!)
                            oldValue.count += value.count
                            oldValue.total = value.total
                            oldValue.nextPageUrl = value.nextPageUrl
                            oldValue.adExist = value.adExist

                            if (TextUtils.isEmpty(value.nextPageUrl)) {
                                val endmessage = CommonData.CommonItemList()
                                endmessage.type = "end"
                                (value.itemList
                                        as MutableList<CommonData.CommonItemList>)
                                        .add(endmessage)
                            }
                            liveObservableData.value = oldValue
                        }

                        override fun onError(e: Throwable) {
                            Logger.d("=======SearchViewModel--onError=========")
                            e.printStackTrace()
                        }

                        override fun onComplete() {
                            Logger.d("=======SearchViewModel--onComplete=========")
                        }
                    })
        } else {

        }

    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
        Logger.d("=======SearchViewModel--onCleared=========")
    }

    companion object {
        private val ABSENT = MutableLiveData<List<SearchHotsData>>()
        private val ABSSENT = MutableLiveData<CommonData>()
    }

    class SearchViewModelClickSupport : SimpleClickSupport() {
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
                                    .withString("data", Gson().toJson(mData.data!!.content!!))
                                    .navigation()
                        }
                        "ugcPicture" -> {
                            ARouter.getInstance()
                                    .build(ARouterPath.PICTURE_DETAIL_ACT)
                                    .withString("data", Gson().toJson(mData.data!!.content!!))
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
                                .withString("data", Gson().toJson(mData))
                                .navigation()
                    }
                }
            }
        }
    }

}
