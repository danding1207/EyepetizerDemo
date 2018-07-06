package com.msc.modulehomepage.viewcardcell

import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.widget.banner.BannerLayout
import com.msc.libcoremodel.datamodel.http.entities.AllRecData
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcoremodel.datamodel.http.entities.CommonData
import com.msc.modulehomepage.viewcard.SquareCardCollectionView
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

internal class SquareCardCollectionViewCell : CommenDataCell<SquareCardCollectionView, CommonData.CommonItemList>() {

    override fun bindView(view: SquareCardCollectionView) {
        super.bindView(view)
        view.webBannerAdapter!!.setOnBannerItemClickListener(
                BannerLayout.OnBannerItemClickListener { position ->

                    if (mData!!.data!!.itemList!![position].data!!.content!!.type == "video"){
//                        Observable.fromIterable(mData!!.data!!.itemList!![position].data!!.content!!.data!!.playInfo)
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

                        val objectStr = Gson().toJson(mData!!.data!!.itemList!![position].data!!.content)//把对象转为JSON格式的字符串

                        Logger.e("JSONDATA--->$objectStr")

                        ARouter.getInstance()
                                .build(ARouterPath.VIDEO_PLAYER_ACT)
                                .withString("data", objectStr)
                                .navigation()
                    }
                }
        )
        view.tvDate!!.text = mData!!.data!!.header!!.subTitle
        view.tvTitle!!.text = mData!!.data!!.header!!.title
        view.webBannerAdapter!!.refreshData(mData!!.data!!)
    }

}