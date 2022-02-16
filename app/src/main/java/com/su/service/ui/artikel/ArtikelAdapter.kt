package com.su.service.ui.artikel


import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.su.service.model.artikel.Artikel
import com.su.service.ui.artikel.viewholder.ArtikelViewHolder

class ArtikelAdapter: PagedListAdapter<Artikel, RecyclerView.ViewHolder>(REPO_COMPARATOR){
    private var context: Context? = null
    private var activity: Activity? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ArtikelViewHolder.create(parent)
    }

    fun setContext(activity: Activity){
        this.activity = activity
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val artikel = getItem(position)
        activity?.let { (holder as ArtikelViewHolder).
        setContext(it) }
        if (artikel != null) {
            Log.d("ArtikelAdapter ", "bind data")
            (holder as ArtikelViewHolder).bind(artikel)
        }
        holder.itemView.setOnLongClickListener {

            return@setOnLongClickListener true
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