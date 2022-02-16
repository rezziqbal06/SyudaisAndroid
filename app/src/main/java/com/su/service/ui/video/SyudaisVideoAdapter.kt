package com.su.service.ui.video

import android.app.Activity
import android.content.Intent
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.su.service.R
import com.su.service.model.youtube.YoutubeItem
import com.su.service.ui.video.DetailVideoActivity
import com.su.service.utils.DateGenerator
import kotlinx.android.synthetic.main.item_video.view.*


class SyudaisVideoAdapter(private val activity: Activity) :
    RecyclerView.Adapter<SyudaisVideoAdapter.ViewHolder>() {
    private lateinit var listener: OnItemClickListener
    private val list: ArrayList<YoutubeItem> = ArrayList<YoutubeItem>()
    private val RECOVERY_DIALOG_REQUEST = 1
    val VIEW_TYPE_NORMAL = 1
    var displayMetrics = DisplayMetrics()


    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    interface OnItemClickListener{
        fun onItemClick(youtube: YoutubeItem, itemView: View)
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_NORMAL
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(youtube: YoutubeItem) {
            with(itemView) {
                tv_video_judul?.text = youtube.snippet?.title
                val tgl = DateGenerator.getTanggal("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "E, dd MMM yyyy", youtube.snippet?.publishedAt)
                tv_video_tanggal?.text = tgl
                Glide.with(context)
                    .load(youtube.snippet?.thumbnails?.medium?.url)
                    .into(img_video)
                setOnClickListener {
                    val intent = Intent(context, DetailVideoActivity::class.java)
                    intent.putExtra(DetailVideoActivity.EXTRA_DATA, youtube)
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

    fun updateItem(position: Int, youtube: YoutubeItem) {
        this.list[position] = youtube
        notifyItemChanged(position, youtube)
    }

    fun addListItem(youtube: ArrayList<YoutubeItem>) {
        this.list.addAll(youtube)
        notifyDataSetChanged()
    }

    fun setItem(youtube: ArrayList<YoutubeItem>) {
        this.list.clear()
        this.list.addAll(youtube)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val youtube = list.get(position)
        holder.bind(youtube)
    }
}