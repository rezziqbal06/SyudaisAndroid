package com.su.service.ui.quran

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.su.service.R
import com.su.service.model.quran.Template
import kotlinx.android.synthetic.main.item_template.view.*

class TemplateAdapter(private val list: ArrayList<Template>) :
    RecyclerView.Adapter<TemplateAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int, template: Template)
    }

    lateinit var listener: OnItemClickListener
    private var index = -1
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(template: Template, position: Int) {
            with(itemView) {
                tv_template?.setTextColor(Color.parseColor(template.color))
                cv_template?.setCardBackgroundColor(Color.parseColor(template.bgColor))
                setOnClickListener {
                    //val intent = Intent(context, ArtikelDetailActivity::class.java)
                    //intent.putExtra(ArtikelDetailActivity.EXTRA_DATA, artikel)
                    //context.startActivity(intent)
                    listener.onItemClick(position, template)
                }
            }

        }
    }

    fun removeItem(position: Int) {
        this.list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, this.list.size)
    }

    fun updateItem(position: Int, template: Template) {
        this.list[position] = template
        notifyItemChanged(position, template)
    }

    fun addListItem(template: List<Template>) {
        this.list.addAll(template)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_template, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val template = list.get(position)
        holder.bind(template, position)
    }
}