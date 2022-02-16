package com.su.service.ui.doa


import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.su.service.model.doa.Doa

class DoaAdapter: PagedListAdapter<Doa, RecyclerView.ViewHolder>(REPO_COMPARATOR){
    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DoaViewHolder.create(parent)
    }

    fun setContext(context: Context){
        this.context = context
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val artikel = getItem(position)
        context?.let { (holder as DoaViewHolder).setContext(it) }
        if (artikel != null) {
            Log.d("DoalAdapter ", "bind data")
            (holder as DoaViewHolder).bind(artikel, position)
        }
    }
    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Doa>() {
            override fun areItemsTheSame(oldItem: Doa, newItem: Doa): Boolean =
                oldItem.nama == newItem.nama

            override fun areContentsTheSame(oldItem: Doa, newItem: Doa): Boolean =
                oldItem == newItem
        }
    }
}