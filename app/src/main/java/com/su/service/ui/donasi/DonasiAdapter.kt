package com.su.service.ui.donasi

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.su.service.R
import com.su.service.model.donasi.DonasiItem
import com.su.service.utils.RpGenerator
import kotlinx.android.synthetic.main.item_donasi.view.*

class DonasiAdapter(private val list: ArrayList<DonasiItem>) :
    RecyclerView.Adapter<DonasiAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int, kategori: String)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var listener: OnItemClickListener
        private var index = -1
        fun setOnItemClickListener(listener: OnItemClickListener) {
            this.listener = listener
        }

        fun bind(donasi: DonasiItem) {
            with(itemView) {
                Glide.with(context)
                    .load(donasi?.thumb)
                    .placeholder(R.drawable.default_picture)
                    .error(R.drawable.default_picture)
                    .into(img_donasi)
                tv_nama?.text = donasi?.nama
                setOnClickListener {
                    val intent = Intent(context, DetailDonasiActivity::class.java)
                    intent.putExtra("extra_id",donasi.id)
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

    fun updateItem(position: Int, Donasi: DonasiItem) {
        this.list[position] = Donasi
        notifyItemChanged(position, Donasi)
    }

    fun addListItem(Donasi: List<DonasiItem>) {
        this.list.addAll(Donasi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_donasi, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Donasi = list.get(position)
        holder.bind(Donasi)
    }
}