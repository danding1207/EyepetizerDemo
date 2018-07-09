package com.msc.modulehomepage.ui

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
import com.msc.libcommon.viewcard.FollowCardView
import com.msc.libcommon.viewcard.TextCardView
import com.msc.libcommon.viewcard.VideoSmallCardView
import com.msc.libcommon.viewcardcell.FollowCardViewCell
import com.msc.libcommon.viewcardcell.TextCardViewCell
import com.msc.libcommon.viewcardcell.VideoSmallCardViewCell
import com.msc.libcoremodel.datamodel.http.entities.CommonData
import com.msc.modulehomepage.R
import com.msc.modulehomepage.viewcard.AutoPlayFollowCardView
import com.msc.modulehomepage.viewcard.BannerCardView
import com.msc.modulehomepage.viewcard.PictureFollowCardView
import com.msc.modulehomepage.viewcard.SquareCardCollectionView
import com.msc.modulehomepage.viewcardcell.AutoPlayFollowCardViewCell
import com.msc.modulehomepage.viewcardcell.BannerCardViewCell
import com.msc.modulehomepage.viewcardcell.PictureFollowCardViewCell
import com.msc.modulehomepage.viewcardcell.SquareCardCollectionViewCell
import com.msc.modulehomepage.viewmodel.HomeViewModel
import com.orhanobut.logger.Logger
import com.tmall.wireless.tangram.TangramBuilder
import com.tmall.wireless.tangram.TangramEngine
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray

@Route(path = ARouterPath.HOME_PAGE_FGT)
class FragmentHome : BaseFragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var engine: TangramEngine
    private lateinit var builder: TangramBuilder.InnerBuilder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ARouter.getInstance().inject(this@FragmentHome)
        viewModel = ViewModelProviders.of(this@FragmentHome).get(HomeViewModel::class.java!!)
//        viewModel.initData()
        subscribeToModel(viewModel)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout.selectTab(1)

        refreshHeader.setColorSchemeResources(R.color.colorPrimaryDark)
        refreshHeader.setShowBezierWave(true)

        builder = TangramBuilder.newInnerBuilder(this@FragmentHome.context!!)

        //Step 3: register business cells and cards
        builder.registerCell("squareCardCollection", SquareCardCollectionViewCell::class.java, SquareCardCollectionView::class.java)
        builder.registerCell("textCard", TextCardViewCell::class.java, TextCardView::class.java)
        builder.registerCell("pictureFollowCard", PictureFollowCardViewCell::class.java, PictureFollowCardView::class.java)
        builder.registerCell("banner", BannerCardViewCell::class.java, BannerCardView::class.java)
        builder.registerCell("videoSmallCard", VideoSmallCardViewCell::class.java, VideoSmallCardView::class.java)
        builder.registerCell("followCard", FollowCardViewCell::class.java, FollowCardView::class.java)
        builder.registerCell("autoPlayFollowCard", AutoPlayFollowCardViewCell::class.java, AutoPlayFollowCardView::class.java)

        engine = builder.build()
        engine.bindView(recyclerView)
        //Step 6: enable auto load more if your page's data is lazy loaded
        engine.enableAutoLoadMore(true)
        engine.addSimpleClickSupport(viewModel.listener)

        //Step 9: set an offset to fix card

        refreshLayout.setOnRefreshListener { refreshlayout ->
            viewModel.initData()
        }
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            viewModel.getMoreData()
        }

        iv_action_search.setOnClickListener(viewModel)
    }

    /**
     * 订阅数据变化来刷新UI
     * @param model
     */
    private fun subscribeToModel(model: HomeViewModel) {
        //观察数据变化来刷新UI
        model.liveObservableData.observe(this, Observer<CommonData> { commonData ->
            Logger.d("subscribeToModel onChanged onChanged")
            refreshLayout.finishRefresh(true)//传入false表示刷新失败
            refreshLayout.finishLoadMore()
            if(commonData!=null) {
                val data = Gson().toJson(commonData.itemList)
                val s = JSONArray(data)
                engine.setData(s)
            }
        })
    }

}
