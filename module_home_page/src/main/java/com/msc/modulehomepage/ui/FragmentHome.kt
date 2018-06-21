package com.msc.modulehomepage.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.BaseFragment
import com.msc.libcommon.widget.banner.BannerLayout
import com.msc.libcoremodel.datamodel.http.entities.AllRecData
import com.msc.libcoremodel.datamodel.http.entities.CommenDataCell
import com.msc.modulehomepage.R
import com.msc.modulehomepage.adapter.WebBannerAdapter
import com.msc.modulehomepage.viewModel.HomeViewModel
import com.msc.modulehomepage.viewcard.*
import com.msc.modulehomepage.viewcardcell.*
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_home.*
import com.tmall.wireless.tangram.TangramBuilder
import com.tmall.wireless.tangram.TangramEngine
import com.tmall.wireless.tangram.support.SimpleClickSupport
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.tmall.wireless.tangram.structure.BaseCell
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


@Route(path = ARouterPath.HOME_PAGE_FGT)
class FragmentHome : BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var engine: TangramEngine
    private lateinit var builder: TangramBuilder.InnerBuilder

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

        refreshHeader.setColorSchemeResources(R.color.colorPrimaryDark)
        refreshHeader.setShowBezierWave(true)

        builder = TangramBuilder.newInnerBuilder(this@FragmentHome.context!!)

        //Step 3: register business cells and cards
        builder.registerCell("squareCardCollection", SquareCardCollectionViewCell::class.java,  SquareCardCollectionView::class.java)
        builder.registerCell("textCard", TextCardViewCell::class.java, TextCardView::class.java)
        builder.registerCell("pictureFollowCard",  PictureFollowCardViewCell::class.java, PictureFollowCardView::class.java)
        builder.registerCell("banner",  BannerCardViewCell::class.java, BannerCardView::class.java)
        builder.registerCell("videoSmallCard",  VideoSmallCardViewCell::class.java, VideoSmallCardView::class.java)
        builder.registerCell("followCard",  FollowCardViewCell::class.java, FollowCardView::class.java)
        builder.registerCell("autoPlayFollowCard",  AutoPlayFollowCardViewCell::class.java, AutoPlayFollowCardView::class.java)

        engine = builder.build()
        engine.bindView(recyclerView)
        //Step 6: enable auto load more if your page's data is lazy loaded
        engine.enableAutoLoadMore(true)
        engine.addSimpleClickSupport(SampleClickSupport())

        //Step 9: set an offset to fix card

        refreshLayout.setOnRefreshListener { refreshlayout ->
            homeViewModel.initData()
        }
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            homeViewModel.getMoreData()
        }

    }

    /**
     * 订阅数据变化来刷新UI
     * @param model
     */
    private fun subscribeToModel(model: HomeViewModel) {
        //观察数据变化来刷新UI
        model.liveObservableData!!.observe(this, Observer<AllRecData> { allRecData ->
            Logger.d("subscribeToModel onChanged onChanged")
            model.setUiObservableData(allRecData)
            refreshLayout.finishRefresh(true)//传入false表示刷新失败
            refreshLayout.finishLoadMore()
            val data = Gson().toJson(allRecData!!.itemList)
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
            Toast.makeText(targetView!!.context, " 您点击了组件  type:" + cell!!.stringType, Toast.LENGTH_SHORT).show()

            val mData: AllRecData.ItemListBeanX.DataBeanXX?  = Gson().fromJson<AllRecData.ItemListBeanX.DataBeanXX>(cell.optStringParam("data"), AllRecData.ItemListBeanX.DataBeanXX::class.java)

            when(cell.stringType){
                "followCard", "pictureFollowCard"->{

                    when(mData!!.content!!.type) {
                        "video"->{
                            Observable.fromIterable(mData.content!!.data!!.playInfo)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .filter {
                                        return@filter "high" == it.type && it.url != null
                                    }
                                    .subscribe { it ->
                                        ARouter.getInstance()
                                                .build(ARouterPath.VIDEO_PLAYER_ACT)
                                                .withString("videoUri", it.url)
                                                .navigation()
                                    }
                        }
                        "ugcPicture"->{
                            ARouter.getInstance()
                                    .build(ARouterPath.PICTURE_DETAIL_ACT)
                                    .withString("avatarUrl", mData.content!!.data!!.owner!!.avatar)
                                    .withString("nickname", mData.content!!.data!!.owner!!.nickname)
                                    .withString("description", mData.content!!.data!!.description)
                                    .withString("collectionCount", mData.content!!.data!!.consumption!!.collectionCount.toString())
                                    .withString("shareCount", mData.content!!.data!!.consumption!!.shareCount.toString())
                                    .withString("replyCount", mData.content!!.data!!.consumption!!.replyCount.toString())
                                    .withString("pictureUrl", mData.content!!.data!!.url)
                                    .navigation()
                        }
                    }

                }
                "videoSmallCard"->{
                    if(mData!==null && mData.resourceType!=null && "video" == mData.resourceType) {
                        Observable.fromIterable(mData.playInfo)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .filter {
                                    return@filter "high" == it.type && it.url != null
                                }
                                .subscribe { it ->
                                    ARouter.getInstance()
                                            .build(ARouterPath.VIDEO_PLAYER_ACT)
                                            .withString("videoUri", it.url)
                                            .navigation()
                                }

                    }

                }
            }

        }
    }

}
