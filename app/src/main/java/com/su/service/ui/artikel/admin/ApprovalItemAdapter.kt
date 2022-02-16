package com.su.service.ui.artikel.admin

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.su.service.R
import com.su.service.model.artikel.Artikel
import com.su.service.ui.artikel.detailartikel.DetailArtikelActivity
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.item_approval.view.*

class ApprovalItemAdapter(val sharedPrefManager: SharedPrefManager) :
    RecyclerView.Adapter<ApprovalItemAdapter.ViewHolder>() {
    private val list: ArrayList<Artikel> = ArrayList()
    interface OnItemClickListener {
        fun onItemClick(position: Int, kategori: String)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var listener: OnItemClickListener
        private var index = -1
        fun setOnItemClickListener(listener: OnItemClickListener) {
            this.listener = listener
        }

        fun bind(artikel: Artikel?) {
            with(itemView) {
                tv_nama_artikel?.text = artikel?.title
                tv_kategori_artikel?.text = artikel?.kategori
                Glide.with(context)
                    .load(artikel?.thumb)
                    .placeholder(R.drawable.default_picture)
                    .error(R.drawable.default_picture)
                    .into(img_artikel)
                notif_follow_up?.visibility = if(artikel?.is_follow_up == 1) View.VISIBLE else View.GONE
                chip_status_publish?.text = artikel?.status
                chip_status_publish?.chipBackgroundColor = if(artikel?.status == "revisi"){
                    ColorStateList.valueOf(ContextCompat.getColor(context,R.color.colorPrimaryDark))
                }else if(artikel?.status == "reject"){
                    ColorStateList.valueOf(ContextCompat.getColor(context,R.color.colorAccent))
                }else{
                    ColorStateList.valueOf(ContextCompat.getColor(context,R.color.disabled_color))
                }
                tv_author?.text = artikel?.author
                setOnClickListener {
                    if(artikel?.b_user_admin_id != null){
                        chip_is_handle?.visibility = View.VISIBLE
                        if(artikel.b_user_admin_id == sharedPrefManager.user.id){
                            chip_is_handle?.text = "Diatasi oleh antum"
                            val intent = Intent(context, DetailArtikelActivity::class.java)
                            intent.putExtra(DetailArtikelActivity.EXTRA_DATA, artikel)
                            context.startActivity(intent)
                        }else{
                            chip_is_handle?.text = "Diatasi oleh admin lain"
                            Toast.makeText(context, "Artikel sudah diatasi oleh admin lain", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        chip_is_handle?.visibility = View.GONE
                        val intent = Intent(context, DetailArtikelActivity::class.java)
                        intent.putExtra(DetailArtikelActivity.EXTRA_DATA, artikel)
                        context.startActivity(intent)
                    }

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

    fun addListItem(artikel: List<Artikel>) {
        this.list.addAll(artikel)
    }
    fun setList(artikel: List<Artikel>) {
        this.list.clear()
        this.list.addAll(artikel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_approval, parent, false)
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