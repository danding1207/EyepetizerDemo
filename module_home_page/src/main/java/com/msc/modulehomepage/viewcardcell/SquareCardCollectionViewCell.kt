package com.msc.modulehomepage.viewcardcell

import com.alibaba.android.arouter.launcher.ARouter
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.widget.banner.BannerLayout
import com.msc.libcoremodel.datamodel.http.entities.AllRecData
import com.msc.libcommon.base.CommenDataCell
import com.msc.modulehomepage.viewcard.SquareCardCollectionView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

internal class SquareCardCollectionViewCell : CommenDataCell<SquareCardCollectionView, AllRecData.ItemListBeanX>() {

    override fun bindView(view: SquareCardCollectionView) {
        super.bindView(view)
        view.webBannerAdapter!!.setOnBannerItemClickListener(
                BannerLayout.OnBannerItemClickListener { position ->

                    mData!!.data!!.itemList!![position].data!!.content!!.type

                    if (mData!!.data!!.itemList!![position].data!!.content!!.type == "video"){
                        Observable.fromIterable(mData!!.data!!.itemList!![position].data!!.content!!.data!!.playInfo)
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
        )
        view.tvDate!!.text = mData!!.data!!.header!!.subTitle
        view.tvTitle!!.text = mData!!.data!!.header!!.title
        view.webBannerAdapter!!.refreshData(mData!!.data!!)
    }

}