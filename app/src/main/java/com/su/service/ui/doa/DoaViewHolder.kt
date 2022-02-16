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

package com.su.service.ui.doa

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.su.service.R
import com.su.service.model.doa.Doa
import com.su.service.ui.doadzikir.DetailDzikirDoaActivity
import com.su.service.ui.kegiatan.DetailKegiatanActivity
import com.su.service.utils.HtmlToStringGenerator

class DoaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val nama = view.findViewById<TextView>(R.id.tv_nama_doadzikir)
    private val gambar = view.findViewById<ImageView>(R.id.img_doadzikir)
    private var doa: Doa? = null
    private var context: Context? = null

    init {
        view.setOnClickListener {
            val intent = Intent(context, DetailDzikirDoaActivity::class.java)
            intent.putExtra(DetailDzikirDoaActivity.EXTRA_TAG, "doa")
            intent.putExtra(DetailDzikirDoaActivity.EXTRA_ID, doa?.id)
            intent.putExtra(DetailDzikirDoaActivity.EXTRA_GAMBAR, doa?.gambar)
            intent.putExtra(DetailDzikirDoaActivity.EXTRA_NAMA, doa?.nama)
            intent.putExtra(DetailDzikirDoaActivity.EXTRA_DOADZIKIR, doa?.doa)
            intent.putExtra(DetailDzikirDoaActivity.EXTRA_DALIL, doa?.dalil)
            context?.startActivity(intent)
        }
    }

    fun setContext(context: Context){
        this.context = context
    }

    fun bind(doa: Doa?, position: Int) {
        showdoaData(doa, position)
        Log.d("DoaViewHolder ", "bind data doa")
    }

    private fun showdoaData(doa: Doa?, position: Int) {
        this.doa = doa
        nama?.text = doa?.nama

        context?.let {
            Glide.with(it)
                .load(doa?.gambar)
                .placeholder(R.drawable.default_picture_2)
                .error(R.drawable.default_picture_2)
                .into(gambar)
        }

    }

    companion object {
        fun create(parent: ViewGroup): DoaViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_doadzikir, parent, false)
            return DoaViewHolder(view)
        }
    }
}
