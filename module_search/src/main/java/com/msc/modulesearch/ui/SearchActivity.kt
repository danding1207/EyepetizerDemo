package com.msc.modulesearch.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.gson.Gson
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.BaseActivity
import com.msc.libcommon.util.KeyboardUtils
import com.msc.libcoremodel.datamodel.http.entities.CommonData
import com.msc.libcoremodel.datamodel.http.entities.SearchHotsData
import com.msc.modulesearch.R
import com.msc.modulesearch.adapter.SearchHotsAdapter
import com.msc.modulesearch.viewmodel.SearchViewModel
import com.orhanobut.logger.Logger
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import com.tmall.wireless.tangram.TangramBuilder
import com.tmall.wireless.tangram.TangramEngine
import kotlinx.android.synthetic.main.activity_search.*
import org.json.JSONArray
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.msc.libcommon.viewcard.*
import com.msc.libcommon.viewcardcell.*


@Route(path = ARouterPath.SEARCH_ACT)
class SearchActivity : BaseActivity() {

    private val adapter = SearchHotsAdapter()
    private var headersDecor: StickyRecyclerHeadersDecoration = StickyRecyclerHeadersDecoration(adapter)

    private lateinit var viewModel: SearchViewModel
    private lateinit var engine: TangramEngine
    private lateinit var builder: TangramBuilder.InnerBuilder

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

        refreshLayout.isEnableRefresh = false
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            viewModel.getMoreSearchResultData()
        }

        builder = TangramBuilder.newInnerBuilder(this@SearchActivity)

        //Step 3: register business cells and cards
        builder.registerCell("textCard", TextCardViewCell::class.java, TextCardView::class.java)
        builder.registerCell("briefCard", BriefCardViewCell::class.java, BriefCardView::class.java)
        builder.registerCell("followCard", FollowCardViewCell::class.java, FollowCardView::class.java)
        builder.registerCell("empty", EmptyCardViewCell::class.java, EmptyCardView::class.java)
        builder.registerCell("networkError", NetworkErrorCardViewCell::class.java, NetworkErrorCardView::class.java)

        engine = builder.build()
        engine.bindView(recyclerView)
        //Step 6: enable auto load more if your page's data is lazy loaded
        engine.enableAutoLoadMore(true)
        engine.addSimpleClickSupport(viewModel.listener)

        tv_cancle.setOnClickListener(viewModel)
        iv_action_clear_dark.setOnClickListener(viewModel)
        adapter.listener = viewModel
//        textInputLayout.editText!!.setOnKeyListener(viewModel)

        textInputLayout.editText!!.setOnEditorActionListener(viewModel)
        textInputLayout.editText!!.addTextChangedListener(viewModel)
    }

    /**
     * 订阅数据变化来刷新UI
     * @param model
     */
    private fun subscribeToModel(model: SearchViewModel) {
        //观察数据变化来刷新UI
        model.liveObservableSearchHotsData.observe(this, Observer<List<SearchHotsData>> { list ->
            Logger.d("subscribeToModel onChanged onChanged")
            if (list != null) {
                adapter.setDataList(list)
            }
        })
        //观察数据变化来刷新UI
        model.liveObservableSearchData.observe(this, Observer<String> { content ->
            Logger.d("subscribeToModel onChanged onChanged")
            if (content == null) {
                iv_action_clear_dark.visibility = View.INVISIBLE
            } else {
                iv_action_clear_dark.visibility = View.VISIBLE
            }
            textInputLayout.editText!!.removeTextChangedListener(viewModel)

            textInputLayout.editText!!.setText(content)
            //将光标移至文字末尾
            textInputLayout.editText!!.setSelection(if (content==null) 0 else content!!.length)

            textInputLayout.editText!!.addTextChangedListener(viewModel)
        })
        //观察数据变化来刷新UI
        model.liveObservableSearchResultData.observe(this, Observer<CommonData> { commonData ->
            Logger.d("subscribeToModel onChanged onChanged")
            refreshLayout.finishLoadMore()
            if (commonData != null) {
                if(TextUtils.isEmpty(commonData.nextPageUrl)) {
                    refreshLayout.isEnableLoadMore = false
                }
                refreshLayout.visibility = View.VISIBLE
                KeyboardUtils.hideSoftInput(this)
                val data = Gson().toJson(commonData.itemList)
                val s = JSONArray(data)
                engine.setData(s)

            } else {
                refreshLayout.visibility = View.GONE

            }
        })
    }


}
