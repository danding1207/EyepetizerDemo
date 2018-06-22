package com.msc.moduleme.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route

import com.alibaba.android.arouter.launcher.ARouter
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.BaseFragment
import com.msc.moduleme.R
import com.msc.moduleme.viewmodel.MeViewModel
import com.tmall.wireless.tangram.TangramBuilder
import com.tmall.wireless.tangram.TangramEngine

@Route(path = ARouterPath.ME_FGT)
class FragmentMe : BaseFragment() {

    private lateinit var viewModel: MeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ARouter.getInstance().inject(this@FragmentMe)
        viewModel = ViewModelProviders.of(this@FragmentMe).get(MeViewModel::class.java!!)
//        viewModel.initData()
//        subscribeToModel(viewModel)
        return inflater.inflate(R.layout.fragment_me, container, false)
    }






}
