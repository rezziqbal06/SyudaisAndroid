package com.su.service.ui.quran

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.su.service.R
import com.su.service.model.quran.Ayat
import com.su.service.utils.Utils
import kotlinx.android.synthetic.main.activity_detail_quran.*
import kotlinx.android.synthetic.main.dialog_cari_ayat.view.*
import kotlinx.android.synthetic.main.dialog_edit_qd.view.*
import kotlinx.android.synthetic.main.dialog_edit_qd.view.btn_simpan
import kotlinx.android.synthetic.main.dialog_edit_qd.view.edt_judul
import kotlinx.android.synthetic.main.dialog_tambah_qd.view.*
import org.json.JSONException
import org.json.JSONObject

class DetailQuranActivity : AppCompatActivity() {
    private lateinit var nama: String
    private lateinit var arti: String
    private var ayatList = ArrayList<Ayat>()
    private var arabList = ArrayList<String>()
    private var artiList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_quran)

        nama = ""
        arti = ""

        val nomor = intent.getStringExtra("nomor_surat")
        val jsonFileName = "$nomor.json"
        val jsonFileString = Utils(this).getJsonDataFromAsset(jsonFileName)
        try {
            val obj = JSONObject(jsonFileString)
            val number = obj.getJSONObject(nomor)
            nama = number.getString("name_latin")
            val translation = number.getJSONObject("translations")
            val  translation_id = translation.getJSONObject("id")
            arti = translation_id.getString("name")

            //ambil arabnya
            val arab = number.getJSONObject("text")
            arab.keys().forEach {
                arabList.add(arab.getString(it))
            }

            //ambil arti
            val artinya = translation_id.getJSONObject("text")
            artinya.keys().forEach {
                artiList.add(artinya.getString(it))
            }
        }catch (e: JSONException){
            e.printStackTrace()
        }

        tv_judul_surat?.text = "$nama ($arti)"
        for((i,v) in arabList.withIndex()){
            ayatList.add(Ayat((i+1).toString(),v, artiList[i]))
        }

        if (!ayatList.isNullOrEmpty()){
            rv_ayat?.layoutManager = LinearLayoutManager(this)
            rv_ayat?.setHasFixedSize(true)
            val adapter = AyatAdapter(ayatList)
            adapter.setOnItemSearchListener(object: AyatAdapter.OnItemSearchClickListener{
                override fun onSearchClick(position: Int) {
                    val dialogView = LayoutInflater.from(this@DetailQuranActivity).inflate(R.layout.dialog_cari_ayat,null)
                    val builder = AlertDialog.Builder(this@DetailQuranActivity).setView(dialogView)
                    val alertDialog = builder.show()
                    dialogView.edt_cari_ayat.setOnEditorActionListener(object: TextView.OnEditorActionListener{
                        override fun onEditorAction(
                            p0: TextView?,
                            p1: Int,
                            p2: KeyEvent?
                        ): Boolean {
                            if(p1 == EditorInfo.IME_ACTION_SEARCH){
                                val edtNomor = dialogView.edt_cari_ayat
                                val numAyat = edtNomor.text.toString()
                                if(numAyat.isEmpty()){
                                    alertDialog.dismiss()
                                }else{
                                    rv_ayat?.layoutManager?.scrollToPosition(numAyat.toInt()-1)
                                    alertDialog.dismiss()
                                }
                            }
                            return false
                        }



                    })
                    dialogView.btn_cari?.setOnClickListener {
                        val edtNomor = dialogView.edt_cari_ayat
                        val numAyat = edtNomor.text.toString()
                        if(numAyat.isEmpty()){
                            alertDialog.dismiss()
                        }else{
                            rv_ayat?.layoutManager?.scrollToPosition(numAyat.toInt()-1)
                            alertDialog.dismiss()
                        }
                    }
                }



            })

            adapter.setOnItemClickListener(object : AyatAdapter.OnItemClickListener{
                override fun onClickListener(ayat: Ayat) {
                    val editView = LayoutInflater.from(this@DetailQuranActivity).inflate(R.layout.dialog_edit_qd,null)
                    val formBuilder = androidx.appcompat.app.AlertDialog.Builder(this@DetailQuranActivity).setView(editView)
                    val formDialog = formBuilder.show()
                    formDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    editView?.edt_arab?.setText(ayat.arab)
                    editView?.edt_artinya?.setText(ayat.artinya)
                    editView?.edt_surat?.setText("$nama: ${ayat.nomor}")
                    editView?.btn_simpan?.text = "Go To Template"
                    editView?.btn_simpan?.setOnClickListener {

                        val judul = editView?.edt_judul?.text?.toString()
                        val arab = editView?.edt_arab?.text?.toString()
                        val artinya = editView?.edt_artinya?.text?.toString()
                        val surat = editView?.edt_surat?.text?.toString()

                        if(judul?.isEmpty()!!){
                            editView?.edt_judul?.setError("Judul belum diisi")
                            return@setOnClickListener
                        }

                        if(arab?.isEmpty()!!){
                            editView?.edt_arab?.setError("Ayat arabnya belum diisi")
                            return@setOnClickListener
                        }

                        if(artinya?.isEmpty()!!){
                            editView?.edt_artinya?.setError("Ayat artinya belum diisi")
                            return@setOnClickListener
                        }

                        if(surat?.isEmpty()!!){
                            editView?.edt_surat?.setError("Nama surat belum diisi")
                            return@setOnClickListener
                        }

                        val intent = Intent(this@DetailQuranActivity, TemplateActivity::class.java)
                        intent.putExtra("judul", judul)
                        intent.putExtra("artinya", artinya)
                        intent.putExtra("surat", surat)
                        intent.putExtra("arab", arab)
                        startActivity(intent)

                    }
                }

            })
            rv_ayat?.adapter = adapter
        }

        img_back?.setOnClickListener {
            onBackPressed()
        }



    }
}
