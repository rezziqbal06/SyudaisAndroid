package com.su.service.ui.quran

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.su.service.R
import com.su.service.model.quran.Ayat
import kotlinx.android.synthetic.main.item_ayat.view.*

class AyatAdapter(private val list: ArrayList<Ayat>) :
    RecyclerView.Adapter<AyatAdapter.ViewHolder>() {
    lateinit var listener: OnItemSearchClickListener
    interface OnItemSearchClickListener {
        fun onSearchClick(index: Int)
    }
    fun setOnItemSearchListener(listener: OnItemSearchClickListener) {
        this.listener = listener
    }

    lateinit var clickListener: OnItemClickListener
    interface OnItemClickListener {
        fun onClickListener(ayat: Ayat)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var index = -1

        fun bind(ayat: Ayat, position: Int) {
            with(itemView) {
                tv_nomor_ayat?.text = ayat.nomor
                tv_arab_ayat?.text = ayat.arab
                tv_arti_ayat?.text = ayat.artinya
                val text = "--- Syudais: AlQuran ---\n\n"+ayat.arab + "\n\n" + ayat.artinya + "\n\n" +
                        "Download Aplikasinya di Google Play Store: \n\nhttps://play.google.com/store/apps/details?id=com.su.service&hl=en_US"
                img_cari?.setOnClickListener {
                    listener?.onSearchClick(position)
                }
                img_share?.setOnClickListener {
                    val share = Intent.createChooser(Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, text )
                        putExtra(Intent.EXTRA_TITLE,"Syudais App: AlQuran Digital")
                        type = "text/plain"
                    }, null)
                    context.startActivity(share)
                }
                img_generate_image?.setOnClickListener {
                    clickListener?.onClickListener(ayat)
                }
            }

        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ayat, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ayat = list.get(position)
        holder.bind(ayat, position)
    }
}