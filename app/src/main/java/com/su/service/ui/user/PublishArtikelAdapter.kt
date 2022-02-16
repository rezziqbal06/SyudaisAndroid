package com.su.service.ui.user

import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.su.service.R
import com.su.service.model.artikel.Artikel
import com.su.service.ui.artikel.detailartikel.DetailArtikelActivity
import kotlinx.android.synthetic.main.item_artikel_publish.view.*

class PublishArtikelAdapter :
    RecyclerView.Adapter<PublishArtikelAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int, id: String)
    }
    private val list: ArrayList<Artikel> = ArrayList()
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var listener: OnItemClickListener
        private var index = -1
        fun setOnItemClickListener(listener: OnItemClickListener) {
            this.listener = listener
        }

        fun bind(artikel: Artikel) {
            with(itemView) {
                val states = arrayOf(
                    intArrayOf(android.R.attr.state_enabled),
                    intArrayOf(-android.R.attr.state_enabled)
                )

                val colors = intArrayOf(
                    R.color.colorPrimaryDark,
                    R.color.colorAccent
                )
                tv_nama_artikel?.text = artikel.title
                tv_kategori_artikel?.text = artikel.kategori
                chip_status_publish?.chipBackgroundColor = ColorStateList(states, colors)
                chip_status_publish?.isEnabled = artikel.status == "publish"
                chip_status_publish?.text = artikel.status
                notif_diskusi?.visibility = if(artikel.is_read == 0){
                    View.VISIBLE
                }else{
                    View.GONE
                }
                setOnClickListener {
                    val intent = Intent(context, DetailArtikelActivity::class.java)
                    intent.putExtra(DetailArtikelActivity.EXTRA_DATA, artikel)
                    context.startActivity(intent)
                }
            }

        }


    }

    fun removeItem(position: Int) {
        this.list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, this.list.size)
    }

    fun updateItem(position: Int, artikel: Artikel) {
        this.list[position] = artikel
        notifyItemChanged(position, artikel)
    }

    fun setList(artikel: List<Artikel>) {
        this.list.clear()
        this.list.addAll(artikel)
    }

    fun addListItem(artikel: List<Artikel>) {
        this.list.addAll(artikel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artikel_publish, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artikel = list.get(position)
        holder.bind(artikel)
    }
}