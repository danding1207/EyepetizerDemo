package com.msc.videoplayer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.msc.libcoremodel.datamodel.http.entities.AllRecData
import com.msc.videoplayer.R

class VideoAdapter(private val context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val HEADERTYPE = 1001
    val FOOTERTYPE = 1002
    val NORMALTYPE = 1003

    var mData : AllRecData.DataBeanXX? = null

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> HEADERTYPE
            (itemCount - 1) -> FOOTERTYPE
            else -> NORMALTYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADERTYPE -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_video_header_item, parent, false))
            FOOTERTYPE -> FooterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_video_footer_item, parent, false))
            NORMALTYPE -> NormalViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_video_normal_item, parent, false))
            else -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_video_header_item, parent, false))
        }
    }

    fun setData(data : AllRecData.DataBeanXX?) {
        mData = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return  if (mData==null) 2 else mData!!.content!!.data!!.tags!!.size+2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is HeaderViewHolder -> {

            }
            is FooterViewHolder -> {

            }
            is NormalViewHolder -> {

            }
        }

    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var ivCover: ImageView = itemView.findViewById<View>(R.id.iv_cover) as ImageView
//        var ivFlag: ImageView = itemView.findViewById<View>(R.id.iv_flag) as ImageView
//        var ivAuthorCover: ImageView = itemView.findViewById<View>(R.id.iv_author_cover) as ImageView
//        var ivAuthorIcon: ImageView = itemView.findViewById<View>(R.id.iv_author_icon) as ImageView
//        var ivActionShare: ImageView = itemView.findViewById<View>(R.id.iv_action_share) as ImageView
//        var tvTitle: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
//        var tvDescription: TextView = itemView.findViewById<View>(R.id.tv_description) as TextView
//        var tvTime: TextView = itemView.findViewById<View>(R.id.tv_time) as TextView
//        var cardView: CardView = itemView.findViewById<View>(R.id.cardView) as CardView
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var ivCover: ImageView = itemView.findViewById<View>(R.id.iv_cover) as ImageView
//        var ivFlag: ImageView = itemView.findViewById<View>(R.id.iv_flag) as ImageView
//        var ivAuthorCover: ImageView = itemView.findViewById<View>(R.id.iv_author_cover) as ImageView
//        var ivAuthorIcon: ImageView = itemView.findViewById<View>(R.id.iv_author_icon) as ImageView
//        var ivActionShare: ImageView = itemView.findViewById<View>(R.id.iv_action_share) as ImageView
//        var tvTitle: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
//        var tvDescription: TextView = itemView.findViewById<View>(R.id.tv_description) as TextView
//        var tvTime: TextView = itemView.findViewById<View>(R.id.tv_time) as TextView
//        var cardView: CardView = itemView.findViewById<View>(R.id.cardView) as CardView
    }

    inner class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var ivCover: ImageView = itemView.findViewById<View>(R.id.iv_cover) as ImageView
//        var ivFlag: ImageView = itemView.findViewById<View>(R.id.iv_flag) as ImageView
//        var ivAuthorCover: ImageView = itemView.findViewById<View>(R.id.iv_author_cover) as ImageView
//        var ivAuthorIcon: ImageView = itemView.findViewById<View>(R.id.iv_author_icon) as ImageView
//        var ivActionShare: ImageView = itemView.findViewById<View>(R.id.iv_action_share) as ImageView
//        var tvTitle: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
//        var tvDescription: TextView = itemView.findViewById<View>(R.id.tv_description) as TextView
//        var tvTime: TextView = itemView.findViewById<View>(R.id.tv_time) as TextView
//        var cardView: CardView = itemView.findViewById<View>(R.id.cardView) as CardView
    }

}
