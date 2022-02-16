package com.su.service.ui.dzikir


import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.su.service.model.dzikir.Dzikir

class DzikirAdapter: PagedListAdapter<Dzikir, RecyclerView.ViewHolder>(REPO_COMPARATOR){
    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DzikirViewHolder.create(parent)
    }

    fun setContext(context: Context){
        this.context = context
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dzikir = getItem(position)
        context?.let { (holder as DzikirViewHolder).setContext(it) }
        if (dzikir != null) {
            Log.d("DzikirAdapter ", "bind data")
            (holder as DzikirViewHolder).bind(dzikir)
        }
    }
    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Dzikir>() {
            override fun areItemsTheSame(oldItem: Dzikir, newItem: Dzikir): Boolean =
                oldItem.nama == newItem.nama

            override fun areContentsTheSame(oldItem: Dzikir, newItem: Dzikir): Boolean =
                oldItem == newItem
        }
    }
}