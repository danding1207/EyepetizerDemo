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
import com.msc.libcommon.util.StringUtils
import com.orhanobut.logger.Logger
import java.util.*


/**
 * Created by test on 2017/11/22.
 */
class WebBannerAdapter(private val context: Context?) : RecyclerView.Adapter<WebBannerAdapter.MzViewHolder>() {

    private var urlList: List<TabsSelectedData.ItemListBean> =  ArrayList()
    private var onBannerItemClickListener: BannerLayout.OnBannerItemClickListener? = null

    init {

    }

    fun refreshData(tabsSelectedData: TabsSelectedData?){

        Logger.d("refreshData")
        Logger.d("listsize--->"+tabsSelectedData!!.itemList!!.size)

        val date = Date()

        Observable.fromIterable(tabsSelectedData!!.itemList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter {
                    return@filter "video" == it.type && it.data != null && "VideoBeanForClient" == it.data!!.dataType && it!!.data!!.library!=null && "DAILY" == it.data!!.library && (date.time-it!!.data!!.date)<86400000
                }
                .toList()
                .subscribe { it ->
                    urlList = it
                    Logger.d("listsize--->"+urlList!!.size)
                    notifyDataSetChanged()
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
        Logger.d("coverurl--->"+item.data!!.cover!!.feed)
        Logger.d("authorurl--->"+item.data!!.author!!.icon)

        Glide.with(context!!).load(item.data!!.cover!!.feed).into(holder.iv_cover)
        Glide.with(context!!).load(item.data!!.author!!.icon).into(holder.iv_author_cover)

        holder.itemView.setOnClickListener {
            if (onBannerItemClickListener != null) {
                onBannerItemClickListener!!.onItemClick(position)
            }
        }

        val layoutParams:RecyclerView.LayoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
//
        val resources = context.resources
        val dm = resources.displayMetrics
        val width = dm.widthPixels

        layoutParams.width = width-60
        holder.itemView.layoutParams = layoutParams

        val layoutParamsCardView:ConstraintLayout.LayoutParams = holder.cardView.layoutParams as ConstraintLayout.LayoutParams
        layoutParamsCardView.width = width-76
        layoutParamsCardView.height = layoutParams.width*720/1242
        holder.cardView.layoutParams = layoutParamsCardView

        holder.tv_description.text = item!!.data!!.slogan
        holder.tv_title.text =  item!!.data!!.title
        holder.tv_time.text =  StringUtils.durationToString(item!!.data!!.duration)




    }

    override fun getItemCount(): Int {
        return urlList.size
    }

    inner class MzViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_cover: ImageView = itemView.findViewById<View>(R.id.iv_cover) as ImageView
        var iv_flag: ImageView = itemView.findViewById<View>(R.id.iv_flag) as ImageView
        var iv_author_cover: ImageView = itemView.findViewById<View>(R.id.iv_author_cover) as ImageView
        var iv_author_icon: ImageView = itemView.findViewById<View>(R.id.iv_author_icon) as ImageView
        var iv_action_share: ImageView = itemView.findViewById<View>(R.id.iv_action_share) as ImageView
        var tv_title: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
        var tv_description: TextView = itemView.findViewById<View>(R.id.tv_description) as TextView
        var tv_time: TextView = itemView.findViewById<View>(R.id.tv_time) as TextView


        var cardView: CardView = itemView.findViewById<View>(R.id.cardView) as CardView
    }



}