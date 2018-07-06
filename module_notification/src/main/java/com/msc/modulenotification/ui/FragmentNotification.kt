package com.msc.modulenotification.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.msc.libcommon.adapter.FragmentAdapter
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.BaseFragment
import com.msc.libcoremodel.datamodel.http.entities.MessagesData
import com.msc.modulenotification.R
import com.msc.modulenotification.viewmodel.NotificationViewModel
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_notification.*

@Route(path = ARouterPath.NOTIFICATION_FGT)
class FragmentNotification : BaseFragment() {

    private lateinit var viewModel: NotificationViewModel
    private val mFragments = java.util.ArrayList<BaseFragment>()
    private var mAdapter: FragmentAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ARouter.getInstance().inject(this@FragmentNotification)
        viewModel = ViewModelProviders.of(this@FragmentNotification).get(NotificationViewModel::class.java)
        subscribeToModel(viewModel)
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        container_pager.offscreenPageLimit = 2
        val fragmentOfficialNotification = ARouter.getInstance()
                .build(ARouterPath.OFFICIAL_NOTIFICATION_FGT).navigation() as BaseFragment
        val fragmentInteractionNotification = ARouter.getInstance()
                .build(ARouterPath.INTERACTION_NOTIFICATION_FGT).navigation() as BaseFragment
        mFragments.add(fragmentOfficialNotification)
        mFragments.add(fragmentInteractionNotification)
        mAdapter = FragmentAdapter(childFragmentManager, mFragments)
        container_pager.adapter = mAdapter

        tabLayout.addTab(tabLayout.newTab())
        tabLayout.addTab(tabLayout.newTab())

        tabLayout.setupWithViewPager(container_pager)

        tabLayout.getTabAt(0)!!.text = "官方"
        tabLayout.getTabAt(1)!!.text = "互动"
    }

    /**
     * 订阅数据变化来刷新UI
     * @param model
     */
    private fun subscribeToModel(model: NotificationViewModel) {
        //观察数据变化来刷新UI
        model.liveObservableData.observe(this, Observer<MessagesData> { messagesData ->
            Logger.d("subscribeToModel onChanged onChanged")
        })
    }

}
