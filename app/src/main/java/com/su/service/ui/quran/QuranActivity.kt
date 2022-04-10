package com.su.service.ui.quran

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.su.service.R
import com.su.service.model.quran.Surat
import com.su.service.utils.CustomProgressDialog
import com.su.service.utils.SharedPrefManager
import com.su.service.utils.Utils
import kotlinx.android.synthetic.main.activity_quran.*
import kotlinx.android.synthetic.main.content_quran.*
import kotlinx.android.synthetic.main.dialog_popup.view.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class QuranActivity : AppCompatActivity() {
    private var TAG = QuranActivity::class.java.simpleName
    private var listSurat: ArrayList<Surat>? = null
    private var adapter: SuratAdapter? = null
    private lateinit var viewModel: QuranDailyViewModel
    private lateinit var sharedPrefManager: SharedPrefManager
    private var qdArab: String? = null
    private var qdArti: String? = null
    private var qdAyat: String? = null
    private var qdJudul: String? = null
    private var qdNamaSurat: String? = null
    private var isOpen: String? = "0"
    private var lebar = 0;
    private var panjang = 0;
    private lateinit var cps : CustomProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quran)
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        viewModel = ViewModelProvider(this).get(QuranDailyViewModel::class.java)
        cps = CustomProgressDialog()

        getBookmark()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        lebar = displayMetrics.widthPixels
        panjang = displayMetrics.heightPixels
        isOpen = intent.getStringExtra("is_open")

//        if(isOpen == "1"){
//            getDataDailyQuranToShow()
//        }else{
//            getDataDailyQuran()
//        }

        Handler().postDelayed(Runnable {
            val jsonFileString = Utils(this@QuranActivity).getJsonDataFromAsset("list.json")
            val gson = Gson()
            val type = object: TypeToken<List<Surat>>() {}.type
            listSurat = gson.fromJson(jsonFileString, type)

            if(!listSurat.isNullOrEmpty()){
                progress_bar?.visibility = View.GONE
                adapter = SuratAdapter(listSurat)
                rv_list_quran?.layoutManager = LinearLayoutManager(this)
                rv_list_quran?.setHasFixedSize(true)
                adapter?.setOnItemClickListener(object: SuratAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int, surat: String) {
                        cps.show(this@QuranActivity)
                        val intent = Intent(this@QuranActivity, DetailQuranActivity::class.java)
                        intent.putExtra("nomor_surat", surat)
                        startActivity(intent)

                    }

                })
                rv_list_quran?.adapter = adapter
                layout_content?.setBackgroundColor(Color.parseColor("#F0E5CF"))
            }
        }, 100)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.colorQuranPrimary)
        }

        cv_search?.setOnClickListener {
            sv_search_surat?.isIconified = false
        }

        sv_search_surat?.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.filter?.filter(newText)
                return false
            }

        })


        cv_daily_quran?.setOnClickListener {
            val dialog = showQuranDaily()
            dialog.btn_bagikan.setOnClickListener {
                val bitmap = buatGambar(dialog)
                shareBitmap(dialog ,bitmap)
            }
        }
    }

    private fun getBookmark() {
        val bookmark = sharedPrefManager.getBookmark
        panel_bookmark.visibility = if(bookmark?.ayat.isNullOrEmpty()){
            View.GONE
        }else{
            View.VISIBLE
        }

        if(!bookmark?.ayat.isNullOrEmpty()) tv_ayat?.text = "Ayat : "+bookmark?.ayat
        tv_surat?.text = bookmark?.surat

        panel_bookmark?.setOnClickListener {
            Log.d("QuranActivity", "panel clicked")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Buka Ayat Terakhir?")
            builder.setMessage("")

            builder.setPositiveButton("Iya") { dialog, which ->
                cps.show(this@QuranActivity)
                val intent = Intent(this@QuranActivity, DetailQuranActivity::class.java)
                intent.putExtra("ayat_bookmark", bookmark?.ayat)
                startActivity(intent)
            }

            builder.setNegativeButton("Nanti dulu") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        }
    }

    private fun shareBitmap(view: View, bitmap: Bitmap?) {
        val intent = Intent(Intent.ACTION_SEND).setType("image/*")
        val bytes = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bytes)

        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "tempimage", null)
        val uri = Uri.parse(path)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(intent)

        Handler().postDelayed(
            Runnable {
                view.btn_bagikan.visibility = View.VISIBLE
                view.cv_qd.radius = 16F
            }, 300
        )
    }

    private fun buatGambar(view: View): Bitmap? {
        view.btn_bagikan.visibility = View.GONE
        view.cv_qd.radius = 0F
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if(bgDrawable != null){
            bgDrawable.draw(canvas)
        }else{
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }

    private fun showQuranDaily() : View {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_popup, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)

        val dialog = builder.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout((lebar-128), (panjang-256))

        Log.d(TAG, "namaSurat : $qdNamaSurat")
        dialogView.tv_arab?.text = qdArab
        dialogView.tv_arti?.text = qdArti
        dialogView.tv_referensi?.text = "$qdNamaSurat : $qdAyat"
        dialogView.tv_judul?.text = qdJudul

        if(qdArab.isNullOrEmpty() || qdArti.isNullOrEmpty() || qdNamaSurat.isNullOrEmpty() ||qdAyat.isNullOrEmpty() || qdJudul.isNullOrEmpty()){
            dialogView.tv_arab?.text = resources.getString(R.string.default_arab)
            dialogView.tv_arti?.text = resources.getString(R.string.default_arti)
            dialogView.tv_referensi?.text = resources.getString(R.string.default_referensi)
            dialogView.tv_judul?.text = resources.getString(R.string.default_judul)
        }
        return dialogView
    }

    private fun getDataDailyQuran() {
        viewModel.getActive().observe(this, Observer {
            if(it != null){
                if(it.status == 200){
                    val qd = it.result?.qurandaily
                    qdNamaSurat = qd?.namaSurat
                    qdAyat = qd?.noAyat
                    qdJudul = qd?.judul
                    getIsiQuran(qd?.noSurat, qdAyat)
                    tv_judul?.text = qdJudul

                    //masukan ke cache
                    qd?.let { it1 -> sharedPrefManager.setQuranDaily(it1) }

                }else{
                    getFromCache()
                }
            }else{
                getFromCache()
            }
        })
    }

    private fun getDataDailyQuranToShow() {
        viewModel.getActive().observe(this, Observer {
            if(it != null){
                if(it.status == 200){
                    val qd = it.result?.qurandaily
                    qdNamaSurat = qd?.namaSurat
                    qdAyat = qd?.noAyat
                    qdJudul = qd?.judul
                    getIsiQuran(qd?.noSurat, qdAyat)
                    tv_judul?.text = qdJudul

                    //masukan ke cache
                    qd?.let { it1 -> sharedPrefManager.setQuranDaily(it1) }

                    val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_popup, null)
                    val builder = AlertDialog.Builder(this)
                        .setView(dialogView)

                    val dialog = builder.show()
                    dialog.window?.setLayout((lebar-128), (panjang-256))
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


                    dialogView.tv_arab?.text = qdArab
                    dialogView.tv_arti?.text = qdArti
                    dialogView.tv_referensi?.text = "$qdNamaSurat : $qdAyat"
                    dialogView.tv_judul?.text = qdJudul

                }else{
                    getFromCache()
                    val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_popup, null)
                    val builder = AlertDialog.Builder(this)
                        .setView(dialogView)

                    val dialog = builder.show()
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    Log.d(TAG, "namaSurat : $qdNamaSurat")
                    dialogView.tv_arab?.text = qdArab
                    dialogView.tv_arti?.text = qdArti
                    dialogView.tv_referensi?.text = "$qdNamaSurat : $qdAyat"
                    dialogView.tv_judul?.text = qdJudul
                }
            }else{
                getFromCache()
                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_popup, null)
                val builder = AlertDialog.Builder(this)
                    .setView(dialogView)

                val dialog = builder.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val params = dialog.window?.attributes
                params?.y = -100
                dialog.window?.attributes = params

                dialogView.tv_arab?.text = qdArab
                dialogView.tv_arti?.text = qdArti
                dialogView.tv_referensi?.text = "$qdNamaSurat : $qdAyat"
                dialogView.tv_judul?.text = qdJudul
            }
        })

    }

    private fun getFromCache() {
        val qd = sharedPrefManager.quranDaily
        qdNamaSurat = qd?.namaSurat
        qdAyat = qd?.noAyat
        qdJudul = qd?.judul
        getIsiQuran(qd?.noSurat, qdAyat)
        tv_judul?.text = qdJudul
        Log.d(TAG, "namaSurat: $qdNamaSurat")
    }

    private fun getIsiQuran(noSurat: String?, qdAyat: String?) {
        val jsonFileName = "$noSurat.json"
        val jsonFileString = Utils(this).getJsonDataFromAsset(jsonFileName)
        try {
            val obj = JSONObject(jsonFileString)
            val number = obj.getJSONObject(noSurat)
            val translation = number.getJSONObject("translations")
            val  translation_id = translation.getJSONObject("id")

            //ambil arabnya
            val arab = number.getJSONObject("text")
            qdArab = arab.getString(qdAyat)

            //ambil arti
            val artinya = translation_id.getJSONObject("text")
            qdArti = artinya.getString(qdAyat)
        }catch (e: JSONException){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        getBookmark()
        if(cps.isInitialized()){
            if(cps.dialog.isShowing){
                cps.dialog.dismiss()
            }
        }
    }

    override fun onPause() {
        super.onPause()

    }

}
