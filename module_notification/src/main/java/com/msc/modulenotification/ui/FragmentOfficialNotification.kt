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
import com.msc.libcommon.viewcard.EndCardView
import com.msc.libcommon.viewcardcell.EndCardViewCell
import com.msc.libcoremodel.datamodel.http.entities.MessagesData
import com.msc.modulenotification.R
import com.msc.modulenotification.viewcard.MessageCardView
import com.msc.modulenotification.viewcardcell.MessageCardViewCell
import com.msc.modulenotification.viewmodel.NotificationViewModel
import com.msc.modulenotification.viewmodel.OfficialNotificationViewModel
import com.orhanobut.logger.Logger
import com.tmall.wireless.tangram.TangramBuilder
import com.tmall.wireless.tangram.TangramEngine
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.support.SimpleClickSupport
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_official_notification.*
import org.json.JSONArray

@Route(path = ARouterPath.OFFICIAL_NOTIFICATION_FGT)
class FragmentOfficialNotification : BaseFragment() {

    private lateinit var viewModel: OfficialNotificationViewModel
    private lateinit var engine: TangramEngine
    private lateinit var builder: TangramBuilder.InnerBuilder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ARouter.getInstance().inject(this@FragmentOfficialNotification)
        viewModel = ViewModelProviders.of(this@FragmentOfficialNotification)
                .get(OfficialNotificationViewModel::class.java)
        subscribeToModel(viewModel)
        return inflater.inflate(R.layout.fragment_official_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshHeader.setColorSchemeResources(R.color.colorPrimaryDark)
        refreshHeader.setShowBezierWave(true)

        builder = TangramBuilder.newInnerBuilder(this@FragmentOfficialNotification.context!!)

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
    private fun subscribeToModel(model: OfficialNotificationViewModel) {
        //观察数据变化来刷新UI
        model.liveObservableData.observe(this, Observer<MessagesData> { messagesData ->
            Logger.d("subscribeToModel onChanged onChanged")

            refreshLayout.finishRefresh(true)//传入false表示刷新失败
            refreshLayout.finishLoadMore()

            if (messagesData!=null) {
                if(messagesData.messageList!![messagesData.messageList!!.size-1].type == "end") {
                    refreshLayout.isEnableLoadMore = false
                }
                val data = Gson().toJson(messagesData.messageList)
                Logger.d(data)
                val s = JSONArray(data)
                engine.setData(s)
            }

        })
    }

    class SampleClickSupport : SimpleClickSupport() {

        init {
            setOptimizedMode(true)
        }

        override fun defaultClick(targetView: View?, cell: BaseCell<*>?, eventType: Int) {
            super.defaultClick(targetView, cell, eventType)

        }
    }

}
