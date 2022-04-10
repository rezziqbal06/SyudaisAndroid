package com.su.service.ui.quran

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.su.service.R
import com.su.service.model.quran.Template
import com.su.service.ui.quran.template.T1Fragment
import com.su.service.ui.quran.template.T2Fragment
import com.su.service.ui.quran.template.T3Fragment
import com.su.service.ui.quran.template.T4Fragment
import kotlinx.android.synthetic.main.activity_template.*
import kotlinx.android.synthetic.main.dialog_popup.view.*
import java.io.ByteArrayOutputStream

class TemplateActivity : AppCompatActivity() {
    private lateinit var surat: String
    private lateinit var arab: String
    private lateinit var artinya: String
    private lateinit var judul: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template)

        surat = intent.getStringExtra("surat").toString()
        arab = intent.getStringExtra("arab").toString()
        artinya = intent.getStringExtra("artinya").toString()
        judul = intent.getStringExtra("judul").toString()

        val templates = ArrayList<Template>()
        templates.add(Template(T1Fragment(), "#fffbdb","#686234"))
        templates.add(Template(T2Fragment(), "#4B6587","#FFFFFF"))
        templates.add(Template(T3Fragment(), "#FFFFFF","#bebebe"))
        templates.add(Template(T4Fragment(), "#131313","#e0a825"))

        val adapter = TemplateAdapter(templates)
        rv_template?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_template?.setHasFixedSize(true)
        rv_template?.adapter = adapter

        adapter.setOnItemClickListener(object: TemplateAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, template: Template) {
                if(savedInstanceState == null) { // initial transaction should be wrapped like this
                    val bundle = Bundle()
                    bundle.putString("surat", surat)
                    bundle.putString("judul", judul)
                    bundle.putString("artinya", artinya)
                    bundle.putString("arab", arab)

                    template.fragment?.arguments = bundle
                    template.fragment?.let {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fr_template, it)
                            .commitAllowingStateLoss()
                    }
                }
            }
        })

    }

}