package com.su.service.ui.quran

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.ArrayMap
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.su.service.R
import com.su.service.model.quran.Ayat
import com.su.service.model.quran.Bookmark
import com.su.service.utils.SharedPrefManager
import com.su.service.utils.Utils
import kotlinx.android.synthetic.main.activity_detail_quran.*
import kotlinx.android.synthetic.main.dialog_cari_ayat.view.*
import kotlinx.android.synthetic.main.dialog_edit_qd.view.*
import kotlinx.android.synthetic.main.dialog_edit_qd.view.btn_simpan
import kotlinx.android.synthetic.main.dialog_edit_qd.view.edt_judul
import kotlinx.android.synthetic.main.dialog_tambah_qd.view.*
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailQuranActivity : AppCompatActivity() {
    private lateinit var nama: String
    private lateinit var arti: String
    private lateinit var prefManager: SharedPrefManager
    private var ayatList = ArrayList<Ayat>()
    private var arabList = ArrayList<String>()
    private var artiList = ArrayList<String>()
    private var jumlahSelected = 0
    private var ayatSelectedList = ArrayMap<Int, Ayat>()
    private var ayatSelected = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_quran)
        prefManager = SharedPrefManager.getInstance(this)!!

        nama = ""
        arti = ""

        var nomor = intent.getStringExtra("nomor_surat")
//        val is_open_bookmark = intent.getBooleanExtra("is_open_bookmark", false)
        var ayatBookmark = intent.getStringExtra("ayat_bookmark")
//        Log.d("is_open_bookmark", is_open_bookmark.toString())
        if(ayatBookmark != null){
            nomor = prefManager.getBookmark?.no_surat
        }
        if(nomor.equals("1") || nomor.equals("9")){
            bismillah.visibility = View.GONE
        }else{
            bismillah.visibility = View.VISIBLE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.colorAyat)
        }
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
                                    val y = rv_ayat?.getChildAt(numAyat.toInt().minus(1))?.y
                                    nest_scroll_view?.post {
                                        nest_scroll_view.fling(0)
                                        y?.toInt()?.let { it1 -> nest_scroll_view.smoothScrollTo(0, it1) }
                                    }
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
                            val y = rv_ayat?.getChildAt(numAyat.toInt().minus(1))?.y
                            nest_scroll_view?.post {
                                nest_scroll_view.fling(0)
                                y?.toInt()?.let { it1 -> nest_scroll_view.smoothScrollTo(0, it1) }
                            }
                            alertDialog.dismiss()
                        }
                    }
                }



            })

            adapter.setOnItemClickListener(object : AyatAdapter.OnItemClickListener{
                override fun onClickListener(ayat: Ayat) {
                    makeTemplate("single", ayat.arab, ayat.artinya, ayat.nomor)
                }

            })

            adapter.setOnBookListener(object: AyatAdapter.OnItemBookListener{
                override fun onBookClick(position:Int, ayat: Ayat) {
                    val bookmark = Bookmark()
                    bookmark.no_surat = nomor
                    bookmark.surat = nama
                    bookmark.ayat = ayat.nomor
                    bookmark.position = position
                    var positionBefore: Int? = null
                    if(prefManager.getBookmark?.no_surat == nomor){
                        positionBefore = prefManager.getBookmark?.position
                    }
                    adapter.updateBookmark(positionBefore, position)
                    prefManager.setBookmark(bookmark)
                    Toast.makeText(this@DetailQuranActivity, "Ayat berhasil disimpan", Toast.LENGTH_SHORT).show()
                }

            })

            adapter?.setOnSelectListener(object: AyatAdapter.OnItemSelectListener{
                override fun onSelectListener(position: Int, ayat: Ayat) {
                    if(prefManager.isLoggedIn){
                        if(prefManager.user.utype == "admin" || prefManager.user.utype == "tim" ){
                            val isSelected = adapter.setSelected(position)
                            if(isSelected){
                                jumlahSelected++
                                ayatSelectedList[position] = ayat
                                ayatSelected.add(position)
                            }else{
                                jumlahSelected--
                                ayatSelectedList.remove(position)
                                ayatSelected.remove(position)
                            }
                            if(jumlahSelected > 0){
                                panel_back?.visibility = View.GONE
                                panel_select?.visibility = View.VISIBLE
                                tv_selected_ayat.text = "Selected: ${jumlahSelected} ayat"
                            }else{
                                panel_back?.visibility = View.VISIBLE
                                panel_select?.visibility = View.GONE
                            }
                        }
                    }
                }

            })

            img_clear?.setOnClickListener {
                jumlahSelected = 0;
                ayatSelectedList.clear()
                ayatSelected.clear()
                adapter.clearSelected()
                if(jumlahSelected > 0){
                    panel_back?.visibility = View.GONE
                    panel_select?.visibility = View.VISIBLE
                    tv_selected_ayat.text = "Selected: ${jumlahSelected} ayat"
                }else{
                    panel_back?.visibility = View.VISIBLE
                    panel_select?.visibility = View.GONE
                }
            }

            img_share_image?.setOnClickListener {
                var arab = ""
                var artinya = ""
                var sumber = ""
                if(jumlahSelected > 0){
                    ayatSelected.sort()
                    for(position in ayatSelected){
                        val ayat = ayatSelectedList[position]
                        var nomorArab = ayat?.nomor;
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            val nf = NumberFormat.getInstance(Locale.forLanguageTag("AR"))
//                            nomorArab = nf.format(ayat?.nomor?.toInt())
//                        }

                        if(ayatSelected.size > 1){
                            arab += " " + ayat?.arab + " \ufd3f" + nomorArab + "\ufd3e"
                            artinya += " " + ayat?.artinya + " (${ayat?.nomor}) "

                        }else{
                            arab += " " + ayat?.arab
                            artinya += " " + ayat?.artinya

                        }

                    }

                    if(ayatSelected.size > 1){
                        sumber = "$nama : ${ayatSelectedList[ayatSelected[0]]?.nomor} - ${ayatSelectedList[ayatSelected[ayatSelected.lastIndex]]?.nomor}"
                    }else{
                        sumber = "$nama: ${ayatSelectedList[ayatSelected[0]]?.nomor}"
                    }
                    makeTemplate("multi", arab, artinya, sumber)

                }else{
                    Toast.makeText(this, "Pilih ayat terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }
            rv_ayat?.adapter = adapter
            val positionAyat = prefManager.getBookmark?.position
            val nomorSurat = prefManager.getBookmark?.no_surat

            if(ayatBookmark != null){
                Handler().postDelayed({
                    val y = rv_ayat?.getChildAt(ayatBookmark.toInt().minus(1))?.y
                    Log.d("Y axis", y.toString())
                    nest_scroll_view?.post {
                        Log.d("scrolling", y.toString())
                        nest_scroll_view.fling(0)
                        y?.toInt()?.let { it1 -> nest_scroll_view.smoothScrollTo(0, it1) }
                    }
                },500)
            }

            if(nomorSurat == nomor){
                positionAyat?.let { adapter.updateBookmark(null, it) }
            }

        }

        img_back?.setOnClickListener {
            onBackPressed()
        }



    }

    private fun makeTemplate(type: String?, arab: String?, artinya: String?, nomor: String?) {
        val editView = LayoutInflater.from(this@DetailQuranActivity).inflate(R.layout.dialog_edit_qd,null)
        val formBuilder = androidx.appcompat.app.AlertDialog.Builder(this@DetailQuranActivity).setView(editView)
        val formDialog = formBuilder.show()
        formDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var nomorArab = "";
        if(type == "single"){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val nf = NumberFormat.getInstance(Locale.forLanguageTag("AR"))
                nomorArab = nf.format(nomor?.toInt())
            }
            editView?.edt_arab?.setText(arab + " " + nomorArab)
            editView?.edt_artinya?.setText(artinya)
            editView?.edt_surat?.setText("$nama: ${nomor}")
        }else{
            editView?.edt_arab?.setText(arab)
            editView?.edt_artinya?.setText(artinya)
            editView?.edt_surat?.setText(nomor)
        }

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
}
