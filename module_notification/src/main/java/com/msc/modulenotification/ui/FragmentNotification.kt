package com.msc.modulenotification.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route

import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.BaseFragment
import com.msc.libcoremodel.datamodel.http.entities.AllRecData
import com.msc.libcoremodel.datamodel.http.entities.MessagesData
import com.msc.modulenotification.R
import com.msc.modulenotification.viewcard.EndCardView
import com.msc.modulenotification.viewcard.MessageCardView
import com.msc.modulenotification.viewcardcell.EndCardViewCell
import com.msc.modulenotification.viewcardcell.MessageCardViewCell
import com.msc.modulenotification.viewmodel.NotificationViewModel
import com.orhanobut.logger.Logger
import com.tmall.wireless.tangram.TangramBuilder
import com.tmall.wireless.tangram.TangramEngine
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.support.SimpleClickSupport
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_notification.*
import org.json.JSONArray

@Route(path = ARouterPath.NOTIFICATION_FGT)
class FragmentNotification : BaseFragment() {

    private lateinit var viewModel: NotificationViewModel
    private lateinit var engine: TangramEngine
    private lateinit var builder: TangramBuilder.InnerBuilder


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ARouter.getInstance().inject(this@FragmentNotification)
        viewModel = ViewModelProviders.of(this@FragmentNotification).get(NotificationViewModel::class.java)
        viewModel.initData()
        subscribeToModel(viewModel)
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout.selectTab(0)

        refreshHeader.setColorSchemeResources(R.color.colorPrimaryDark)
        refreshHeader.setShowBezierWave(true)

        builder = TangramBuilder.newInnerBuilder(this@FragmentNotification.context!!)

        //Step 3: register business cells and cards
        builder.registerCell("normal", MessageCardViewCell::class.java, MessageCardView::class.java)
        builder.registerCell("end", EndCardViewCell::class.java, EndCardView::class.java)


        engine = builder.build()
        engine.bindView(recyclerView)
        //Step 6: enable auto load more if your page's data is lazy loaded
        engine.enableAutoLoadMore(true)
        engine.addSimpleClickSupport(SampleClickSupport())

        //Step 9: set an offset to fix card

        refreshLayout.setOnRefreshListener { refreshlayout ->
            viewModel.initData()
        }
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            viewModel.getMoreData()
        }

    }



    /**
     * 订阅数据变化来刷新UI
     * @param model
     */
    private fun subscribeToModel(model: NotificationViewModel) {
        //观察数据变化来刷新UI
        model.liveObservableData.observe(this, Observer<MessagesData> { messagesData ->
            Logger.d("subscribeToModel onChanged onChanged")
            model.setUiObservableData(messagesData)
            if(messagesData!!.messageList!![messagesData.messageList!!.size-1].type == "end") {
                refreshLayout.isEnableLoadMore = false
            }
            refreshLayout.finishRefresh(true)//传入false表示刷新失败
            refreshLayout.finishLoadMore()
            val data = Gson().toJson(messagesData.messageList)
            Logger.d(data)
            val s = JSONArray(data)
            engine.setData(s)
        })
    }

    class SampleClickSupport : SimpleClickSupport() {

        init {
            setOptimizedMode(true)
        }

        override fun defaultClick(targetView: View?, cell: BaseCell<*>?, eventType: Int) {
            super.defaultClick(targetView, cell, eventType)
//            val mData: AllRecData.ItemListBeanX.DataBeanXX? = Gson().fromJson<AllRecData.ItemListBeanX.DataBeanXX>(cell!!.optStringParam("data"), AllRecData.ItemListBeanX.DataBeanXX::class.java)
//            when (cell.stringType) {
//                "followCard", "autoPlayFollowCard", "pictureFollowCard" -> {
//                    when (mData!!.content!!.type) {
//                        "video" -> {
//                            Observable.fromIterable(mData.content!!.data!!.playInfo)
//                                    .subscribeOn(Schedulers.io())
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .filter {
//                                        return@filter "high" == it.type && it.url != null
//                                    }
//                                    .subscribe { it ->
//                                        ARouter.getInstance()
//                                                .build(ARouterPath.VIDEO_PLAYER_ACT)
//                                                .withString("videoUri", it.url)
//                                                .navigation()
//                                    }
//                        }
//                        "ugcPicture" -> {
//                            ARouter.getInstance()
//                                    .build(ARouterPath.PICTURE_DETAIL_ACT)
//                                    .withString("avatarUrl", mData.content!!.data!!.owner!!.avatar)
//                                    .withString("nickname", mData.content!!.data!!.owner!!.nickname)
//                                    .withString("description", mData.content!!.data!!.description)
//                                    .withString("collectionCount", mData.content!!.data!!.consumption!!.collectionCount.toString())
//                                    .withString("shareCount", mData.content!!.data!!.consumption!!.shareCount.toString())
//                                    .withString("replyCount", mData.content!!.data!!.consumption!!.replyCount.toString())
//                                    .withString("pictureUrl", mData.content!!.data!!.url)
//                                    .navigation()
//                        }
//                    }
//
//                }
//                "videoSmallCard" -> {
//                    if (mData !== null && mData.resourceType != null && "video" == mData.resourceType) {
//                        Observable.fromIterable(mData.playInfo)
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .filter {
//                                    return@filter "high" == it.type && it.url != null
//                                }
//                                .subscribe { it ->
//                                    ARouter.getInstance()
//                                            .build(ARouterPath.VIDEO_PLAYER_ACT)
//                                            .withString("videoUri", it.url)
//                                            .navigation()
//                                }
//                    }
//                }
//            }
        }
    }

}
