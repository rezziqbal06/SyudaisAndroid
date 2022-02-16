package com.su.service.ui.artikel.buateditartikel

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.su.service.R
import com.su.service.model.artikel.Artikel
import com.su.service.model.artikel.ArtikelLocal
import com.su.service.ui.artikel.KategoriViewModel
import com.su.service.ui.user.UserFragment.Companion.EXTRA_DATA
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.activity_buat_edit_artikel.*
import kotlinx.android.synthetic.main.activity_buat_edit_artikel.rv_kategori
import kotlinx.android.synthetic.main.fragment_artikel.*
import net.dankito.richtexteditor.callback.GetCurrentHtmlCallback
import net.dankito.utils.android.permissions.PermissionsService
import org.jetbrains.annotations.NotNull
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class BuatEditArtikelActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_DATA_PUBLISH = "extra_data_publish"
    }
    private var isEdit = false
    private var isEditPublish = false
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var artikelLocalViewModel: ArtikelLocalViewModel
    private lateinit var kategoriViewModel: KategoriViewModel
    private val kategoriAdapter = KategoriListAdapter()
    private var arrayKategori = arrayListOf<String>()
    private var dataEdit: ArtikelLocal? = null
    private var dataEditPublish: Artikel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buat_edit_artikel)
        artikelLocalViewModel = ViewModelProviders.of(this).get(ArtikelLocalViewModel::class.java)
        kategoriViewModel = ViewModelProviders.of(this).get(KategoriViewModel::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        initKategoriAdapter()
        dataEdit = intent.getParcelableExtra<ArtikelLocal>(EXTRA_DATA)
        dataEditPublish = intent.getParcelableExtra<Artikel>(EXTRA_DATA_PUBLISH)
        if(dataEdit != null){
            setData(dataEdit)
            isEdit = true
        }else if(dataEditPublish != null){
            setDataPublish(dataEditPublish)
            isEditPublish = true
        }
        initRichText()
        btn_save?.setOnClickListener {
            getIsiBeforeSave()
        }
        img_back?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setDataPublish(data: Artikel?) {
        edt_judul.setText(data?.title)
        var kategories = data?.kategori?.split(", ")
        Log.d("BuatArtikelActivity", "kategori $kategories")
        Log.d("BuatArtikelActivity", "kategori size: ${kategories?.size}")
        if(kategories?.size!! > 1){
            kategories.let { kategoriAdapter.setSelectedItem(it as ArrayList<String>) }
        }else{
            kategories = arrayListOf(kategories[0])
            kategories.let { kategoriAdapter.setSelectedItem(it) }
        }
        data?.content?.let { editor.setHtml(it) }
    }

    private fun getIsiBeforeSave() {
        editor.getCurrentHtmlAsync(object : GetCurrentHtmlCallback {
            override fun htmlRetrieved(@NotNull html: String) {
                save(html)
            }
        })
    }

    private fun setData(data: ArtikelLocal?) {
        edt_judul.setText(data?.title)
        var kategories = data?.kategori?.split(", ")
        Log.d("BuatArtikelActivity", "kategori $kategories")
        Log.d("BuatArtikelActivity", "kategori size: ${kategories?.size}")
        if(kategories?.size!! > 1){
            kategories.let { kategoriAdapter.setSelectedItem(it as ArrayList<String>) }
        }else{
            kategories = arrayListOf(kategories[0])
            kategories.let { kategoriAdapter.setSelectedItem(it) }
        }
        data?.content?.let { editor.setHtml(it) }
    }

    private fun initKategoriAdapter() {
        val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL)
        rv_kategori.layoutManager = layoutManager
        rv_kategori.adapter = kategoriAdapter
        val array = ArrayList<String>()
        val kategoriList = sharedPrefManager.listKategori
        if(kategoriList == null){
            getKategori()
        }else{
            for(ktr in kategoriList){
                ktr?.nama?.let { array.add(it) }
            }
            kategoriAdapter.addListItem(array)
        }

    }

    private fun getKategori() {
        kategoriViewModel.getDataKategori("","","","").observe(this, androidx.lifecycle.Observer {
            if(it?.status == 200){
                sharedPrefManager.setKategori(it.result?.kategori)
                val array = ArrayList<String>()
                for(ktr in sharedPrefManager.listKategori!!){
                    ktr?.nama?.let { array.add(it) }
                }
                kategoriAdapter.addListItem(array)

            }else{
                Toast.makeText(this,"Hidupkan koneksi untuk mengambil data kategori ", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun save(isi: String) {

        val judul = edt_judul?.text.toString().trim()
        val kategori = StringBuilder()
        val cdate =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val ldate =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val status = "draft"

        if(judul.length < 5){
            edt_judul?.setError("Judul terlalu pendek")
            edt_judul?.requestFocus()
            return
        }

        arrayKategori = kategoriAdapter.getSelectedItem()
        for(kat in arrayKategori){
            Log.d("BuatEditActivity","kategoriSelected: "+kat.toString())
        }
        if(arrayKategori.isNullOrEmpty()){
            Toast.makeText(this, "Belum memilih kategori", Toast.LENGTH_SHORT).show()
            return
        }else{
            for(ktr in arrayKategori){
                if(ktr == arrayKategori.last()){
                    kategori.append(ktr)
                }else{
                    kategori.append("$ktr, ")
                }
            }
        }

        Log.d("BuatEditActivity",isi)

        if(isi.length < 10){
            Toast.makeText(this,"Artikel masih kosong", Toast.LENGTH_SHORT).show()
            return
        }

        if(TextUtils.isEmpty(judul)){
            edt_judul?.setError("Judul masih kosong")
            return
        }

        //populate data
        val data = ArtikelLocal()
        data.title = judul
        data.kategori = kategori.toString()
        data.content = isi
        data.status = status

        Log.d("IsEdit ",isEdit.toString())
        if(isEdit){
            data.id = dataEdit?.id
            data.cdate = dataEdit?.cdate
            data.ldate = ldate
            val intent = Intent()
            Log.d("BuatEditArtikelActivity","put extra artikel for edit ${data.title}")
            intent.putExtra(EXTRA_DATA, data)
            setResult(Activity.RESULT_OK, intent)
        }else if(isEditPublish){
            dataEditPublish?.title = judul
            dataEditPublish?.kategori = kategori.toString()
            dataEditPublish?.content = isi
            dataEditPublish?.status = status
            dataEditPublish?.ldate = ldate
            val intent = Intent()
            intent.putExtra(EXTRA_DATA_PUBLISH, dataEditPublish)
            setResult(Activity.RESULT_OK, intent)
        }else{
            data.cdate = cdate
            val intent = Intent()
            Log.d("BuatEditArtikelActivity","put extra artikel ${data.title}")
            intent.putExtra(EXTRA_DATA, data)
            setResult(Activity.RESULT_OK, intent)
        }

        val handler = Handler()
        handler.postDelayed(Runnable {
            finish()
        },500)

    }

    private fun initRichText() {
        val permissionsService = PermissionsService(this)
        editor.permissionsService = permissionsService
        editorToolbar.editor = editor
        editorToolbar.background = resources.getDrawable(R.drawable.rich_text_toolbar_bg)
        editor.setEditorFontSize(20)
        editor.setPadding((4 * resources.displayMetrics.density.toInt()))
        editor.setEditorFontFamily("nunito")
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        val dialog = builder.setTitle("Belum menyimpan artikel")
            .setMessage("Yakin ingin keluar tanpa menyimpan?")
            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                val intent = Intent()
                setResult(Activity.RESULT_CANCELED,intent)
                finish()
            })
            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            }).create()
        dialog.show()
        val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positive.setTextColor(resources.getColor(R.color.dark))
        positive.setBackgroundColor(resources.getColor(R.color.white))
        val negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negative.setTextColor(resources.getColor(R.color.dark))
        negative.setBackgroundColor(resources.getColor(R.color.white))
    }
}
