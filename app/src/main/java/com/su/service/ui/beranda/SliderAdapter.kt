package com.su.service.ui.beranda

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.su.service.R
import com.su.service.model.beranda.SliderItem
import kotlinx.android.synthetic.main.item_slider.view.*

class SliderAdapter(context: Context, list: List<SliderItem?>?) : PagerAdapter(){
    val context = context
    val list = list
    private lateinit var listener: OnItemClickListener
    fun setOnItemClickListener(mListener: OnItemClickListener) {
        listener = mListener
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int, url: String)
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageLayout = LayoutInflater.from(context).inflate(R.layout.item_slider, container, false)
        Glide.with(context)
            .load(list?.get(position)?.image)
            .placeholder(R.drawable.placeholder_slider)
            .error(R.drawable.placeholder_slider)
            .into(imageLayout.img_slider)
        imageLayout.setOnClickListener {
            list?.get(position)?.id?.let { it1 -> listener.onItemClick(position, it1) }
        }
        container.addView(imageLayout)
        return imageLayout
    }
    override fun getCount(): Int {
        return list?.size!!
    }

}