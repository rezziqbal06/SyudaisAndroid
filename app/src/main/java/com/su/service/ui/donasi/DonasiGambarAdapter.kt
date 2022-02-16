package com.su.service.ui.donasi

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.su.service.R
import com.su.service.model.detaildonasi.GaleriDonasi
import com.su.service.ui.detailgambar.ImageFullScreenActivity
import kotlinx.android.synthetic.main.item_donasi.view.*
import kotlinx.android.synthetic.main.item_donasi_gambar.view.*

class DonasiGambarAdapter(private val list: ArrayList<GaleriDonasi>) :
    RecyclerView.Adapter<DonasiGambarAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int, kategori: String)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var listener: OnItemClickListener
        private var index = -1
        fun setOnItemClickListener(listener: OnItemClickListener) {
            this.listener = listener
        }

        fun bind(gambar: GaleriDonasi) {
            with(itemView) {
                Glide.with(context)
                    .load(gambar.gambar)
                    .into(img_galeri)
                setOnClickListener {
                    val intent = Intent(context, ImageFullScreenActivity::class.java)
                    intent.putExtra(ImageFullScreenActivity.EXTRA_IMAGE, gambar.gambar)
                    intent.putExtra(ImageFullScreenActivity.EXTRA_TEXT, "Gambar donasi")
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

    fun updateItem(position: Int, gambar: GaleriDonasi) {
        this.list[position] = gambar
        notifyItemChanged(position, gambar)
    }

    fun addListItem(gambar: List<GaleriDonasi>) {
        this.list.addAll(gambar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_donasi_gambar, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gambar = list.get(position)
        holder.bind(gambar)
    }
}