/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.su.service.ui.artikel.viewholder

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.su.service.R
import com.su.service.model.artikel.Artikel
import com.su.service.ui.artikel.detailartikel.DetailArtikelActivity
import com.su.service.ui.kegiatan.DetailKegiatanActivity
import com.su.service.utils.HtmlToStringGenerator

class ArtikelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.tv_nama_artikel)
    private val description: TextView = view.findViewById(R.id.tv_deskripsi_artikel)
    private val kategori: TextView = view.findViewById(R.id.tv_kategori_artikel)
    private val gambar: ImageView = view.findViewById(R.id.img_artikel)

    private var artikel: Artikel? = null
    private var context: Context? = null
    private var activity: Activity? = null

    init {
        view.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                val intent = Intent(context, DetailArtikelActivity::class.java)
                intent.putExtra(DetailKegiatanActivity.EXTRA_DATA, artikel)
                val options = ActivityOptions.makeSceneTransitionAnimation(activity, gambar, "gambar")
                activity?.startActivity(intent, options.toBundle())
            }else{
                val intent = Intent(context, DetailArtikelActivity::class.java)
                intent.putExtra(DetailKegiatanActivity.EXTRA_DATA, artikel)
                context?.startActivity(intent)
            }

        }
    }

    fun setContext(activity: Activity){
        this.context = activity
        this.activity = activity
    }

    fun bind(artikel: Artikel?) {
        showartikelData(artikel)
        Log.d("ArtikelViewHolder ", "bind data artikel")
    }

    private fun showartikelData(artikel: Artikel?) {
        this.artikel = artikel
        name.text = artikel?.title

        val deskripsi = artikel?.content?.let { HtmlToStringGenerator.generate(it) }
        description.text = deskripsi
        // if the description is missing, hide the TextView
        kategori.text = artikel?.kategori
        context?.let {
            Glide.with(it)
                .load(artikel?.gambar)
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                .into(gambar)
        }
    }

    companion object {
        fun create(parent: ViewGroup): ArtikelViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_artikel, parent, false)
            return ArtikelViewHolder(view)
        }
    }
}
