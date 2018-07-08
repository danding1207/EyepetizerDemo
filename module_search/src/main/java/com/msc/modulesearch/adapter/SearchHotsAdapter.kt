package com.msc.modulesearch.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msc.libcoremodel.datamodel.http.entities.SearchHotsData
import com.msc.modulesearch.R
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter

class SearchHotsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private var dataList: List<SearchHotsData>? = null
    var listener: View.OnClickListener? = null

    fun setDataList(dataList: List<SearchHotsData>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchHotsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_search_hots, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SearchHotsViewHolder).tvName.text = dataList!![position].name
    }

    override fun getHeaderId(position: Int): Long {
        return if (position == 0) {
            -1
        } else {
            getItem(position).type!![0].toLong()
        }
    }

    private fun getItem(position: Int): SearchHotsData {
        return dataList!![position]
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return SearchHotsHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_search_hots_header, parent, false))
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SearchHotsHeaderViewHolder).tvTitle.text = dataList!![position].typeTitle
        if (dataList!![position].isCanDelete)
            holder.tvDelete.visibility = View.VISIBLE
        else
            holder.tvDelete.visibility = View.INVISIBLE

        if (listener != null) holder.tvDelete.setOnClickListener(listener)
    }

    override fun getItemCount(): Int {
        return if (dataList == null) 0 else dataList!!.size
    }

    //必须重写  不然item会错乱
    override fun getItemId(position: Int): Long {
        return getItem(position).name!![0].hashCode().toLong()
    }

    inner class SearchHotsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById<View>(R.id.tv_name) as TextView
    }

    inner class SearchHotsHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
        var tvDelete: TextView = itemView.findViewById<View>(R.id.tv_delete) as TextView
    }

}
