package com.su.service.ui.user

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.su.service.R
import com.su.service.model.artikel.ArtikelLocal
import com.su.service.ui.artikel.detailartikel.DetailArtikelActivity
import com.su.service.ui.artikel.uploadartikel.UploadArtikelActivity
import com.su.service.utils.HtmlToStringGenerator
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.item_artikel_draft.view.*

class ArtikelLocalAdapter :
    RecyclerView.Adapter<ArtikelLocalAdapter.ViewHolder>() {
    private val list: ArrayList<ArtikelLocal> = ArrayList<ArtikelLocal>()
    interface OnItemClickListener {
        fun onItemClick(data: ArtikelLocal, tag: String)
    }

    lateinit var listener: OnItemClickListener
    private var index = -1
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(artikel: ArtikelLocal) {
            with(itemView) {
                tv_nama_artikel_draft?.text = artikel.title
                tv_kategori_artikel_draft?.text = artikel.kategori
                val isi = artikel.content?.let { HtmlToStringGenerator.generate(it) }
                tv_isi_artikel_draft?.text = isi

                setOnClickListener {
                    val intent = Intent(context, DetailArtikelActivity::class.java)
                    intent.putExtra(DetailArtikelActivity.EXTRA_DATA_LOCAL, artikel)
                    context?.startActivity(intent)
                }
                img_upload?.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    val dialog = builder.setTitle("Upload Artikel")
                        .setMessage("Apakah antum ingin upload artikel agar artikel antum dapat dibaca semua orang?")
                        .setNegativeButton("Tidak"
                    ) { dInterface, p1 -> dInterface.dismiss() }
                        .setPositiveButton("Ya"){ dialogInterface: DialogInterface, i: Int ->
                        val sharedPrefManager = SharedPrefManager.getInstance(context)
                        if(sharedPrefManager?.isLoggedIn!!){
                            val intent = Intent(context, UploadArtikelActivity::class.java)
                            intent.putExtra(UploadArtikelActivity.EXTRA_DATA,artikel)
                            context?.startActivity(intent)
                            dialogInterface.dismiss()
                        }else{
                            Toast.makeText(context,"Login terlebih dahulu",Toast.LENGTH_SHORT).show()
                        }

                    }.create()
                    dialog.show()
                    val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    positive.setTextColor(resources.getColor(R.color.dark))
                    positive.setBackgroundColor(resources.getColor(R.color.white))
                    val negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    negative.setTextColor(resources.getColor(R.color.dark))
                    negative.setBackgroundColor(resources.getColor(R.color.white))

                }
                img_edit?.setOnClickListener {
                    listener.onItemClick(artikel,"edit")
                }
                img_delete?.setOnClickListener {
                    listener.onItemClick(artikel,"hapus")
                }
            }

        }
    }

    fun removeItem(position: Int) {
        this.list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, this.list.size)
    }

    fun updateItem(position: Int, ArtikelLocal: ArtikelLocal) {
        this.list[position] = ArtikelLocal
        notifyItemChanged(position, ArtikelLocal)
    }

    fun setListItem(ArtikelLocal: List<ArtikelLocal>) {
        this.list.clear()
        this.list.addAll(ArtikelLocal)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artikel_draft, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ArtikelLocal = list.get(position)
        holder.bind(ArtikelLocal)
    }
}