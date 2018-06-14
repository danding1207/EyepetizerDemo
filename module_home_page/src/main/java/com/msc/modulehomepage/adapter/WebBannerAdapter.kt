package com.msc.modulehomepage.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.msc.libcommon.widget.banner.BannerLayout
import com.msc.libcoremodel.datamodel.http.entities.TabsSelectedData
import com.msc.modulehomepage.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
//import kotlinx.android.synthetic.layout_home_banner_item.view.*
import android.util.DisplayMetrics



/**
 * Created by test on 2017/11/22.
 */
class WebBannerAdapter(private val context: Context?, tabsSelectedData: TabsSelectedData?) : RecyclerView.Adapter<WebBannerAdapter.MzViewHolder>() {

    private var urlList: List<TabsSelectedData.ItemListBean>? = null
    private var onBannerItemClickListener: BannerLayout.OnBannerItemClickListener? = null

    init {
        Observable.fromIterable(tabsSelectedData!!.itemList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter {
                    return@filter "video" == it.type && it.data != null && "VideoBeanForClient" == it.data!!.dataType
                }
                .toList()
                .subscribe { it -> urlList = it
                }
    }

    fun setOnBannerItemClickListener(onBannerItemClickListener: BannerLayout.OnBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebBannerAdapter.MzViewHolder {
        return MzViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_home_banner_item, parent, false))
    }

    override fun onBindViewHolder(holder: WebBannerAdapter.MzViewHolder, position: Int) {
        if (urlList == null || urlList!!.isEmpty())
            return
        val item:TabsSelectedData.ItemListBean = urlList!![position]

        Glide.with(context!!).load(item.data!!.cover!!.feed).into(holder.iv_cover)
        Glide.with(context!!).load(item.data!!.author!!.icon).into(holder.iv_author_cover)

        holder.itemView.setOnClickListener {
            if (onBannerItemClickListener != null) {
                onBannerItemClickListener!!.onItemClick(position)
            }
        }


//        val layoutParams:ConstraintLayout.LayoutParams = holder.cardView.layoutParams as ConstraintLayout.LayoutParams
//
//        val resources = context.resources
//        val dm = resources.displayMetrics
//        val width = dm.widthPixels
//        val height = dm.heightPixels
//
//
//        layoutParams.width = width-80
//        layoutParams.height = layoutParams.width*720/1242
//
//        holder.cardView.layoutParams = layoutParams

    }

    override fun getItemCount(): Int {
        return urlList?.size ?: 0
    }

    inner class MzViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_cover: ImageView = itemView.findViewById<View>(R.id.iv_cover) as ImageView
        var iv_flag: ImageView = itemView.findViewById<View>(R.id.iv_flag) as ImageView
        var iv_author_cover: ImageView = itemView.findViewById<View>(R.id.iv_author_cover) as ImageView
        var iv_author_icon: ImageView = itemView.findViewById<View>(R.id.iv_author_icon) as ImageView
        var iv_action_share: ImageView = itemView.findViewById<View>(R.id.iv_action_share) as ImageView
        var tv_title: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
        var tv_description: TextView = itemView.findViewById<View>(R.id.tv_description) as TextView
        var cardView: CardView = itemView.findViewById<View>(R.id.cardView) as CardView


    }

}