package com.msc.libcommon.viewcardcell

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.msc.libcommon.R
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcommon.viewcard.VideoHeaderCardView
import com.msc.libcoremodel.datamodel.http.entities.CommonData

class VideoHeaderCardViewCell : CommenDataCell<VideoHeaderCardView, CommonData.CommonItemList>() {


    private var adapter: VideoTagsAdapter? = null

    override fun bindView(view: VideoHeaderCardView) {
        super.bindView(view)
        view.tvName!!.text = mData!!.data!!.title
        view.tvCategory!!.text = "#${mData!!.data!!.category}"
        view.tvDescription!!.text = mData!!.data!!.description

        view.tvLikeNum!!.text = mData!!.data!!.consumption!!.collectionCount.toString()
        view.tvMessageNum!!.text = mData!!.data!!.consumption!!.replyCount.toString()
        view.tvShareNum!!.text = mData!!.data!!.consumption!!.shareCount.toString()

        view.tvOwnerName!!.text = mData!!.data!!.author!!.name
        view.tvOwnerDesc!!.text = mData!!.data!!.author!!.description

        Glide.with(view.context!!)
                .load(mData!!.data!!.author!!.icon)
                .into(view.ivOwnerCover!!)

        if (mData!!.data!!.tags != null && !mData!!.data!!.tags!!.isEmpty()) {
            view.recyclerViewTags!!.visibility = View.VISIBLE
            val layoutManager = LinearLayoutManager(view.context!!)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            view.recyclerViewTags!!.layoutManager = layoutManager
            adapter = VideoTagsAdapter()
            view.recyclerViewTags!!.adapter = adapter
            adapter!!.setData(mData!!.data!!.tags!!)
        } else {
            view.recyclerViewTags!!.visibility = View.INVISIBLE
        }

    }

    class VideoTagsAdapter : RecyclerView.Adapter<VideoTagsAdapter.VideoTagsViewHolder>() {

        var mData: List<CommonData.CommonItemList.CommonItemListData.CommonItemListDataTags>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoTagsAdapter.VideoTagsViewHolder {
            return VideoTagsViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_video_tags_item, parent, false))
        }

        fun setData(data: List<CommonData.CommonItemList.CommonItemListData.CommonItemListDataTags>) {
            mData = data
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return if (mData == null) 0 else mData!!.size
        }

        override fun onBindViewHolder(holder: VideoTagsAdapter.VideoTagsViewHolder, position: Int) {
            holder.tvName.text = "#${mData!![position].name}#"
            Glide.with(holder.ivBgPicture.context!!)
                    .load(mData!![position].bgPicture!!)
                    .into(holder.ivBgPicture)
        }

        inner class VideoTagsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var ivBgPicture: ImageView = itemView.findViewById<View>(R.id.iv_bg_picture) as ImageView
            var tvName: TextView = itemView.findViewById<View>(R.id.tv_name) as TextView
        }

    }

}