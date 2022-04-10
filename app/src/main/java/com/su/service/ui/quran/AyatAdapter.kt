package com.su.service.ui.quran

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.su.service.R
import com.su.service.model.quran.Ayat
import kotlinx.android.synthetic.main.item_ayat.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AyatAdapter(private val list: ArrayList<Ayat>) :
    RecyclerView.Adapter<AyatAdapter.ViewHolder>() {
    lateinit var listener: OnItemSearchClickListener
    lateinit var bookmarkListener: OnItemBookListener
    lateinit var selectListener: OnItemSelectListener

    interface OnItemSearchClickListener {
        fun onSearchClick(index: Int)
    }

    interface OnItemSelectListener {
        fun onSelectListener(position: Int, ayat: Ayat)
    }

    interface OnItemBookListener {
        fun onBookClick(position: Int, ayat: Ayat)
    }

    fun setOnItemSearchListener(listener: OnItemSearchClickListener) {
        this.listener = listener
    }

    fun setOnBookListener(listener: OnItemBookListener) {
        this.bookmarkListener = listener
    }

    fun setOnSelectListener(listener: OnItemSelectListener) {
        this.selectListener = listener
    }

    lateinit var clickListener: OnItemClickListener
    interface OnItemClickListener {
        fun onClickListener(ayat: Ayat)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }

    fun updateBookmark(positionBefore: Int?, position: Int){
        if(positionBefore != null){
            this.list[positionBefore].isBookmarked = false
            notifyItemChanged(positionBefore)
        }
        this.list[position].isBookmarked = true
        notifyItemChanged(position)
    }

    fun setSelected(position: Int): Boolean{
        this.list[position].isSelected = !this.list[position].isSelected
        notifyItemChanged(position)
        return this.list[position].isSelected
    }

    fun clearSelected(){
        this.list.forEach {
            it.isSelected = false
        }
        notifyDataSetChanged()
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var index = -1
        fun bind(ayat: Ayat, position: Int) {
            with(itemView) {
                tv_nomor_ayat?.text = ayat.nomor
                tv_arti_ayat?.text = ayat.artinya
                var nomorArab = ayat.nomor;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    val nf = NumberFormat.getNumberInstance(Locale.forLanguageTag("AR"))
//                    nomorArab = nf.format(ayat.nomor?.toInt())
//                    nomorArab = "\ufd3f" + nf.format(ayat.nomor?.toInt()) + "\ufd3e"
//                }
                tv_arab_ayat?.text = ayat.arab
                if(ayat.isBookmarked == true){
                    img_bookmark?.setImageResource(R.drawable.ic_baseline_bookmark_24)
                }else{
                    img_bookmark?.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
                }

                panel_ayat?.setBackgroundColor(if (ayat.isSelected){
                    resources.getColor(R.color.color_selected)
                }else{
                    Color.WHITE
                })

//                val nomorArab = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    String.valueOf(nf.format(285))+"\u06DD"
//                } else {
//                    ""
//                }

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
                img_bookmark?.setOnClickListener {
                    bookmarkListener?.onBookClick(position, ayat)
                }

                panel_ayat?.setOnLongClickListener {
                    selectListener?.onSelectListener(position, ayat)
                    true
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