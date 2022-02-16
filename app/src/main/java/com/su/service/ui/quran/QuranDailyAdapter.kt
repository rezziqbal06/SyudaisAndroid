package com.su.service.ui.quran

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.su.service.R
import com.su.service.model.qurandaily.QDResponse
import com.su.service.model.qurandaily.QurandailyItem
import kotlinx.android.synthetic.main.item_qurandaily.view.*

class QuranDailyAdapter() :
    RecyclerView.Adapter<QuranDailyAdapter.ViewHolder>() {
    private var list: ArrayList<QurandailyItem> = ArrayList<QurandailyItem>()

    var listener: OnPickListener? = null
    var clickListener: OnClickListener? = null
    private var index = -1
    fun setOnPickListener(listener: OnPickListener) {
        this.listener = listener
    }

    fun setOnClickListener(clickListener: OnClickListener) {
        this.clickListener = clickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int, quranDaily: QurandailyItem) {
            with(itemView) {
                tv_judul?.text = quranDaily.judul
                tv_dalil?.text = "${quranDaily.namaSurat} : ${quranDaily.noAyat}"
                if(quranDaily?.isPick == "1"){
                    cv_qd?.setBackgroundColor(resources.getColor(R.color.colorQuranPrimary))
                }else{
                    cv_qd?.setBackgroundColor(resources.getColor(R.color.white))
                }

                setOnLongClickListener(object: View.OnLongClickListener{
                    override fun onLongClick(v: View?): Boolean {
                        quranDaily.id?.toInt()?.let { listener?.onItemPick(it) }
                        return true
                    }

                })

                setOnClickListener {
                    clickListener?.onItemCLick(position, quranDaily)
                }
            }

        }
    }

    fun removeItem(position: Int) {
        this.list?.removeAt(position)
        notifyItemRemoved(position)
        this.list?.size?.let { notifyItemRangeRemoved(position, it) }
    }

    fun updateItem(position: Int, quranDaily: QurandailyItem) {
        this.list?.set(position, quranDaily)
        notifyItemChanged(position, quranDaily)
    }

    fun setItems(quranDaily: ArrayList<QurandailyItem>){
        this.list = quranDaily
    }

    fun addListItem(quranDaily: List<QurandailyItem>) {
        this.list?.addAll(quranDaily)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_qurandaily, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quranDaily = list?.get(position)
        quranDaily?.let { holder.bind(position, it) }
    }

    interface OnPickListener {
        fun onItemPick(id: Int)
    }

    interface OnClickListener {
        fun onItemCLick(position: Int, quranDaily: QurandailyItem)
    }
}