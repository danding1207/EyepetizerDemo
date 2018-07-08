package com.msc.modulesearch.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.BaseActivity
import com.msc.libcoremodel.datamodel.http.entities.SearchHotsData
import com.msc.modulesearch.R
import com.msc.modulesearch.adapter.SearchHotsAdapter
import com.msc.modulesearch.viewmodel.SearchViewModel
import com.orhanobut.logger.Logger
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import kotlinx.android.synthetic.main.activity_search.*

@Route(path = ARouterPath.SEARCH_ACT)
class SearchActivity : BaseActivity() {

    private val adapter = SearchHotsAdapter()
    private var headersDecor: StickyRecyclerHeadersDecoration = StickyRecyclerHeadersDecoration(adapter)

    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarLightMode(this, ContextCompat.getColor(this, R.color.white))
        setContentView(R.layout.activity_search)
        setFitsSystemWindows(this, true)

        viewModel = ViewModelProviders.of(this@SearchActivity)
                .get(SearchViewModel::class.java)
        subscribeToModel(viewModel)

        recyclerView_hots.layoutManager = LinearLayoutManager(this)
        recyclerView_hots.adapter = adapter
        recyclerView_hots.addItemDecoration(headersDecor)

        recyclerView_hots.addOnItemTouchListener(viewModel.getStickyRecyclerHeadersTouchListener(recyclerView_hots, headersDecor))
        recyclerView_hots.addOnItemTouchListener(viewModel.itemsListener)

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                headersDecor.invalidateHeaders()
            }
        })

        tv_cancle.setOnClickListener(viewModel)
        adapter.listener = viewModel
    }

    /**
     * 订阅数据变化来刷新UI
     * @param model
     */
    private fun subscribeToModel(model: SearchViewModel) {
        //观察数据变化来刷新UI
        model.liveObservableData.observe(this, Observer<List<SearchHotsData>> { list ->
            Logger.d("subscribeToModel onChanged onChanged")
            if (list != null) {
                adapter.setDataList(list)
            }
        })
    }


}
