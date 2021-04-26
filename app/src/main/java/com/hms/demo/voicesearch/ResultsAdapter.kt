package com.hms.demo.voicesearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hms.demo.voicesearch.databinding.ResultBinding
import com.huawei.hms.searchkit.bean.WebItem

class ResultsAdapter: RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder>() {
    val items:ArrayList<WebItem> = ArrayList()

    class ResultsViewHolder(private val binding: ResultBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:WebItem){
            binding.item=item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val binding=ResultBinding.inflate(inflater,parent,false)
        return ResultsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}