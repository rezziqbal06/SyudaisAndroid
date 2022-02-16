package com.su.service.ui.artikel


import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.su.service.model.artikel.Artikel
import com.su.service.ui.artikel.viewholder.ArtikelPopularViewHolder

class ArtikelPopularAdapter: PagedListAdapter<Artikel, androidx.recyclerview.widget.RecyclerView.ViewHolder>(REPO_COMPARATOR){
    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return ArtikelPopularViewHolder.create(parent)
    }

    fun setContext(context: Context){
        this.context = context
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        context?.let { (holder as ArtikelPopularViewHolder).setContext(it) }
        if (repoItem != null) {
            (holder as ArtikelPopularViewHolder).bind(repoItem)
        }
    }
    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Artikel>() {
            override fun areItemsTheSame(oldItem: Artikel, newItem: Artikel): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: Artikel, newItem: Artikel): Boolean =
                oldItem == newItem
        }
    }
}