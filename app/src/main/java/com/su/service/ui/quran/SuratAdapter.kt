package com.su.service.ui.quran

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.su.service.R
import com.su.service.model.quran.Surat
import kotlinx.android.synthetic.main.item_surat.view.*
import java.util.*
import kotlin.collections.ArrayList

class SuratAdapter(private val list: ArrayList<Surat>?) :
    RecyclerView.Adapter<SuratAdapter.ViewHolder>(), Filterable {

    var filterList = ArrayList<Surat>()
    init {
        if (list != null) {
            filterList = list
        }
    }

    lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int, surat: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var index = -1


        fun bind(surat: Surat) {
            with(itemView) {
                tv_arab?.text = surat.name
                tv_jumlah_ayat?.text = surat.numberOfAyah + " ayat"
                tv_nomor_surat?.text = surat.number
                tv_nama_surat?.text = surat.nameLatin
                setOnClickListener {
                    surat?.number?.let { it1 -> listener.onItemClick(position, it1) }

                }
            }

        }
    }

    fun removeItem(position: Int) {
        this.list?.removeAt(position)
        notifyItemRemoved(position)
        this.list?.size?.let { notifyItemRangeRemoved(position, it) }
    }

    fun updateItem(position: Int, Surat: Surat) {
        this.list?.set(position, Surat)
        notifyItemChanged(position, Surat)
    }

    fun addListItem(Surat: List<Surat>) {
        this.list?.addAll(Surat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_surat, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filterList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Surat = filterList[position]
        Surat.let { holder.bind(it) }
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if(charSearch.isEmpty()){
                    if (list != null) {
                        filterList = list
                    }
                }else{
                    val resultList = ArrayList<Surat>()
                    if (list != null) {
                        for(row in list){
                            if(row.name?.contains(charSearch)!! || row.nameLatin?.toLowerCase(Locale.ROOT)?.contains(charSearch.toLowerCase(Locale.ROOT))!!){
                                resultList.add(row)
                            }
                        }
                    }
                    filterList = resultList
                }
                val filterResult = FilterResults()
                filterResult.values = filterList
                return filterResult

            }

            override fun publishResults(constraint: CharSequence?, result: FilterResults?) {
                filterList = result?.values as ArrayList<Surat>
                notifyDataSetChanged()
            }

        }
    }
}