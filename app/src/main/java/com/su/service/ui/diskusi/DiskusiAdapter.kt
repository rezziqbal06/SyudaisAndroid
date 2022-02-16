package com.su.service.ui.diskusi

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.su.service.R
import com.su.service.model.diskusi.Detail.KomenItem
import com.su.service.utils.DateGenerator

class DiskusiAdapter(private val uid: String,private var messages: MutableList<KomenItem>) :
    RecyclerView.Adapter<DiskusiAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(status: String,komen: String)
    }
    lateinit var listener: OnItemClickListener
    private var index = -1
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    companion object {
        private const val SENT = 0
        private const val RECEIVED = 1
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(komen: KomenItem?) {
            with(itemView) {
                val pengirim = findViewById<TextView>(R.id.tv_pengirim)
                val pesan = findViewById<TextView>(R.id.tv_komen)
                val imgStatus = findViewById<ImageView>(R.id.img_status_komen)
                val waktu = findViewById<TextView>(R.id.tv_waktu)
                val panelKomen = findViewById<LinearLayout>(R.id.panel_komen)

                pengirim?.text = komen?.pengirim
                pesan?.text = komen?.komen

                if(komen?.status == "tidak terkirim"){
                    imgStatus.setImageResource(R.drawable.ic_not_sent)

                }else if(komen?.status == "belum terkirim"){
                    imgStatus.setImageResource(R.drawable.ic_done_black)
                }else{
                    imgStatus.setImageResource(R.drawable.ic_done_all_black)
                }
                val currentDate = DateGenerator.getCurrentDate()
                val textWaktu = if(komen?.cdate == currentDate){
                    DateGenerator.getTanggal("yy-MM-dd HH:mm:ss","hh:mm",komen?.cdate)
                }else{
                    DateGenerator.getTanggal("yy-MM-dd HH:mm:ss","E dd MMMM yyyy", komen?.cdate)
                }
                waktu?.text = textWaktu
                panelKomen?.setOnClickListener {
                    komen?.komen?.let { it1 -> listener.onItemClick(komen.status!!,it1) }
                }
            }

        }
    }

    fun updateMessages(messages: List<KomenItem>) {
        this.messages = messages.toMutableList()
        notifyDataSetChanged()
    }
    fun appendMessage(message: KomenItem) {
        Log.d("DiskusiAdapter", "AppendMessage")
        message.status = "belum terkirim"
        this.messages.add(message)
        Log.d("DiskusiAdapter", "jumlah item: ${this.messages.size}")
        notifyItemInserted(this.messages.size - 1)
    }

    fun setStatusTerkirim(){
        val max = messages.size - 1
        messages[max].status = "terkirim"
        notifyDataSetChanged()
    }

    fun setStatusTidakTerkirim(){
        val max = messages.size - 1
        messages[max].status = "tidak terkirim"
        notifyDataSetChanged()
    }

    fun setStatusBelumTerkirim(){
        val max = messages.size - 1
        messages[max].status = "belum terkirim"
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = when (viewType) {
            SENT -> {
                LayoutInflater.from(parent.context).inflate(R.layout.item_sent, parent, false)
            }
            else -> {
                LayoutInflater.from(parent.context).inflate(R.layout.item_received, parent, false)
            }
        }
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].bUserPengirimId!!.contentEquals(uid)) {
            SENT
        } else {
            RECEIVED
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Komen = messages[position]
        holder.bind(Komen)
    }

    fun appendMessages(komen: ArrayList<KomenItem>) {
        this.messages.addAll(komen)
    }
}