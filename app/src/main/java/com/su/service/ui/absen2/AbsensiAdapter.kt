package com.su.service.ui.absen2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.su.service.R
import com.su.service.model.santri.SantriItem
import com.su.service.utils.DateGenerator
import kotlinx.android.synthetic.main.item_absen.view.*

class AbsensiAdapter(private val list: List<SantriItem>) :
    RecyclerView.Adapter<AbsensiAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(
            position: Int,
            email: String?,
            view: View,
            utype: String
        )
    }

    lateinit var listener: OnItemClickListener
    private var index = -1
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun updateItem(position: Int, status: String?, jam: String?){
        this.list[position].keterangan = status
        this.list[position].jam = jam
        notifyItemChanged(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(santri: SantriItem, position: Int) {
            with(itemView) {
                tv_status?.text = santri.keterangan
                tv_nama?.text= santri.nama
                tv_jam?.text = santri.jam
                when(santri.keterangan?.toLowerCase()){
                    "sakit" -> led_status?.setBackgroundColor(resources.getColor(R.color.colorSakit))
                    "alpa" -> led_status?.setBackgroundColor(resources.getColor(R.color.colorAlpa))
                    "izin" -> led_status?.setBackgroundColor(resources.getColor(R.color.colorIzin))
                    "hadir" -> led_status?.setBackgroundColor(resources.getColor(R.color.colorHadir))
                    else -> led_status?.setBackgroundColor(resources.getColor(R.color.colorDefault))
                }
                img_action?.setOnClickListener {
                    listener?.onItemClick(position,santri.email,itemView, "other")
                }
                setOnLongClickListener {
                    listener?.onItemClick(position,santri.email,itemView, "other")
                    true
                }
                setOnClickListener { listener?.onItemClick(position,santri.email,itemView, "hadir") }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_absen, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Santri = list.get(position)
        holder.bind(Santri, position)
    }
}