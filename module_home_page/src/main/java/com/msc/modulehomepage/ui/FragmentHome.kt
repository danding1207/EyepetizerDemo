package com.msc.modulehomepage.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route

import com.alibaba.android.arouter.launcher.ARouter
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.BaseFragment
import com.msc.libcoremodel.datamodel.http.entities.TabsSelectedData
import com.msc.modulehomepage.R
import com.msc.modulehomepage.viewModel.HomeViewModel
import com.orhanobut.logger.Logger
import android.widget.Toast
import com.msc.libcommon.widget.banner.BannerLayout
import com.msc.modulehomepage.adapter.WebBannerAdapter
import kotlinx.android.synthetic.main.fragment_home.*


@Route(path = ARouterPath.HOME_PAGE_FGT)
class FragmentHome : BaseFragment() {

    private lateinit var homeViewModel:HomeViewModel
    private lateinit var webBannerAdapter:WebBannerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ARouter.getInstance().inject(this@FragmentHome)
        homeViewModel = ViewModelProviders.of(this@FragmentHome).get(HomeViewModel::class.java!!)
        homeViewModel.initData()
        subscribeToModel(homeViewModel)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout.selectTab(1)
        webBannerAdapter = WebBannerAdapter(this.context)
        webBannerAdapter.setOnBannerItemClickListener(object : BannerLayout.OnBannerItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(this@FragmentHome.context, "点击了第  $position  项", Toast.LENGTH_SHORT).show()
            }
        })
        bannerLayout.setAdapter(webBannerAdapter)
    }

    /**
     * 订阅数据变化来刷新UI
     * @param model
     */
    private fun subscribeToModel(model: HomeViewModel) {
        //观察数据变化来刷新UI
        model.liveObservableData!!.observe(this, Observer<TabsSelectedData> { tabsSelectedData ->
            Logger.d("subscribeToModel onChanged onChanged")
            model.setUiObservableData(tabsSelectedData)
            webBannerAdapter.refreshData(tabsSelectedData)
        })
    }

}
