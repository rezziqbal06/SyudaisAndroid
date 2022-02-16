package com.su.service.ui.artikel.buateditartikel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.su.service.R
import com.su.service.model.kategori.KategoriItem
import kotlinx.android.synthetic.main.item_artikel.view.*
import kotlinx.android.synthetic.main.item_kategori_buat.view.*

class KategoriListAdapter :
    RecyclerView.Adapter<KategoriListAdapter.ViewHolder>() {
    private val list: ArrayList<String> = ArrayList<String>()
    private var listSelected: ArrayList<String> = ArrayList<String>()
    interface OnItemClickListener{
        fun onItemClick(position: Int, kategori: String)
    }
    lateinit var listener: OnItemClickListener
    private var index = -1
    private var isEnable = false
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int,string: String) {
            with(itemView) {
                tv_kategori?.text = string
                tv_kategori?.isEnabled = false
                for (kat in listSelected){
                    if(string == kat){
                        tv_kategori.isEnabled = true
                    }
                }
                if(tv_kategori.isEnabled){
                    panel_kategori?.setBackgroundResource(R.drawable.enable_kategori)
                    tv_kategori?.setTextColor(resources.getColor(R.color.darker_gray))
                }else{
                    panel_kategori?.setBackgroundResource(R.drawable.disable_kategori)
                    tv_kategori?.setTextColor(resources.getColor(R.color.dark))
                }
                setOnClickListener {
                    if(tv_kategori.isEnabled){
                        panel_kategori?.setBackgroundResource(R.drawable.disable_kategori)
                        tv_kategori?.setTextColor(resources.getColor(R.color.dark))
                        removeSelectedItem(string)
                        tv_kategori?.isEnabled = false
                    }else{
                        panel_kategori?.setBackgroundResource(R.drawable.enable_kategori)
                        tv_kategori?.setTextColor(resources.getColor(R.color.darker_gray))
                        addSelectedItem(string)
                        tv_kategori?.isEnabled = true
                    }
                }
            }
        }
    }

    fun getSelectedItem(): ArrayList<String>{
        return this.listSelected
    }

    fun setSelectedItem(list: ArrayList<String>){
        this.listSelected = list
    }

    fun removeSelectedItem(string: String) {
        this.listSelected.remove(string)
    }

    fun updateItem(position: Int, String: String) {
        this.list[position] = String
        notifyItemChanged(position, String)
    }

    fun addSelectedItem(string: String){
        var isAdding = true
        if(listSelected.size > 0){
            for(stringKategori in listSelected){
                if(stringKategori == string) isAdding = false
            }
            if(isAdding) this.listSelected.add(string)
        }else{
            this.listSelected.add(string)
        }
    }

    fun addListItem(listString: List<String>) {
        this.list.addAll(listString)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_kategori_buat, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val String = list.get(position)
        holder.bind(position, String)
    }

    fun getList(): ArrayList<String> {
        return this.list
    }
}