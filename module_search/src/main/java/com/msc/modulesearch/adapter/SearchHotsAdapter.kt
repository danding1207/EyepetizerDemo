package com.msc.modulesearch.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msc.libcoremodel.datamodel.http.entities.SearchHotsData
import com.msc.modulesearch.R
import com.orhanobut.logger.Logger

class SearchHotsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataList: ArrayList<SearchHotsData>? = null
    private var headerDataList: ArrayList<String> = ArrayList()
    private var headerPositionArray: IntArray? = null

    var deleteListener: View.OnClickListener? = null

    val HEADERTYPE = 1001
    val NORMALTYPE = 1002

    fun setDataList(dataList: ArrayList<SearchHotsData>) {

        headerDataList.clear()

        val typeGroup = dataList.groupBy { it.type }
        headerPositionArray = IntArray(typeGroup.size)

        dataList.forEachIndexed { index, searchHotsData ->
            if (!headerDataList.contains(searchHotsData.type)) {
                val i = headerDataList.size
                headerDataList.add(searchHotsData.type!!)
                headerPositionArray!![i] = i + index
            }
        }
        headerPositionArray!!.forEach {
            dataList.add(it, dataList[it].clone(dataList[it]))
            dataList[it].isHeader = true
        }
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (dataList == null)
            return NORMALTYPE
        return when (position) {
            in headerPositionArray!! -> HEADERTYPE
            else -> NORMALTYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADERTYPE -> SearchHotsHeaderViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_search_hots_header, parent, false))
            else -> SearchHotsViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_search_hots, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SearchHotsViewHolder -> holder.tvName.text = dataList!![position].name
            is SearchHotsHeaderViewHolder -> {
                holder.tvTitle.text = dataList!![position].typeTitle
                if (dataList!![position].isCanDelete)
                    holder.tvDelete.visibility = View.VISIBLE
                else
                    holder.tvDelete.visibility = View.INVISIBLE

                if (deleteListener != null) holder.tvDelete.setOnClickListener(deleteListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (dataList == null) 0 else dataList!!.size
    }

    inner class SearchHotsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById<View>(R.id.tv_name) as TextView
    }

    inner class SearchHotsHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
        var tvDelete: TextView = itemView.findViewById<View>(R.id.tv_delete) as TextView
    }

}
