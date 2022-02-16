package com.su.service.ui.quran

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.su.service.R
import com.su.service.model.qurandaily.QurandailyItem
import com.su.service.utils.Constants
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.activity_setting_quran_daily.*
import kotlinx.android.synthetic.main.activity_setting_quran_daily.progress_bar
import kotlinx.android.synthetic.main.dialog_option.view.*
import kotlinx.android.synthetic.main.dialog_tambah_qd.view.*
import kotlinx.android.synthetic.main.dialog_tambah_qd.view.btn_simpan

class SettingQuranDailyActivity : AppCompatActivity() {
    private lateinit var viewModel: QuranDailyViewModel
    private lateinit var sharedPrefManager: SharedPrefManager
    private var adapter = QuranDailyAdapter()
    private var apisess: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_quran_daily)

        viewModel = ViewModelProvider(this).get(QuranDailyViewModel::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(this)!!

        apisess = sharedPrefManager.user.apiMobileToken
        progress_bar?.visibility = View.VISIBLE

        getData()

        swipeRefresh?.setOnRefreshListener {
            getData()
        }
        img_back?.setOnClickListener {
            onBackPressed()
        }

        //tambah
        img_add?.setOnClickListener {
            Log.d("QuranActivity", "buka dialog")
            val editView = LayoutInflater.from(this@SettingQuranDailyActivity).inflate(R.layout.dialog_tambah_qd,null)
            val formBuilder = AlertDialog.Builder(this@SettingQuranDailyActivity).setView(editView)
            val formDialog = formBuilder.show()
            formDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            editView?.btn_simpan?.text = "Simpan"
            editView?.btn_simpan?.setOnClickListener {
                progress_bar?.visibility = View.VISIBLE

                val judul =   editView?.edt_judul?.text?.toString()
                val namaSurat =   editView?.edt_nama_surat?.text?.toString()
                val noSurat =   editView?.edt_nomor_surat?.text?.toString()
                val ayat =   editView?.edt_ayat?.text?.toString()

                if(judul?.isEmpty()!!){
                    progress_bar?.visibility = View.GONE
                    editView?.edt_judul?.setError("Judul belum diisi")
                    return@setOnClickListener
                }

                if(namaSurat?.isEmpty()!!){
                    progress_bar?.visibility = View.GONE
                    editView?.edt_nama_surat?.setError("Nama Surat belum diisi")
                    return@setOnClickListener
                }
                if(noSurat?.isEmpty()!!){
                    progress_bar?.visibility = View.GONE
                    editView?.edt_nomor_surat?.setError("Nomor Surat belum diisi")
                    return@setOnClickListener
                }

                if(ayat?.isEmpty()!!){
                    progress_bar?.visibility = View.GONE
                    editView?.edt_ayat?.setError("Ayat belum diisi")
                    return@setOnClickListener
                }

                viewModel?.tambah(apisess, judul, namaSurat, noSurat, ayat).observe(this@SettingQuranDailyActivity, Observer {
                    progress_bar?.visibility = View.GONE
                    if(it != null){
                        if(it.status == 200){
                            it.result?.qurandaily?.let { it1 -> adapter.setItems(it1) }
                            rv_qd?.adapter = adapter
                            formDialog?.dismiss()
                            Toast.makeText(this, "Berhasil menambahkan", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@SettingQuranDailyActivity, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this@SettingQuranDailyActivity, "Gagal, coba beberapa saat lagi", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        //hapus atau edit
        adapter?.setOnClickListener(object: QuranDailyAdapter.OnClickListener{
            override fun onItemCLick(position: Int, quranDaily: QurandailyItem) {
                val optionView = LayoutInflater.from(this@SettingQuranDailyActivity).inflate(R.layout.dialog_option,null)
                val builder = AlertDialog.Builder(this@SettingQuranDailyActivity).setView(optionView)
                val dialog = builder.show()
                optionView.btn_hapus?.setOnClickListener {
                    hapus(position, quranDaily.id)
                    dialog.dismiss()
                }
                optionView.btn_edit?.setOnClickListener {
                    val editView = LayoutInflater.from(this@SettingQuranDailyActivity).inflate(R.layout.dialog_tambah_qd,null)
                    val formBuilder = AlertDialog.Builder(this@SettingQuranDailyActivity).setView(editView)
                    val formDialog = formBuilder?.show()
                    editView?.edt_judul?.setText(quranDaily.judul)
                    editView?.edt_nama_surat?.setText(quranDaily.namaSurat)
                    editView?.edt_nomor_surat?.setText(quranDaily.noSurat)
                    editView?.edt_ayat?.setText(quranDaily.noAyat)
                    editView?.btn_simpan?.text = "Edit"
                    editView?.btn_simpan?.setOnClickListener {
                        progress_bar?.visibility = View.VISIBLE

                        val judul =   editView?.edt_judul?.text?.toString()
                        val namaSurat =   editView?.edt_nama_surat?.text?.toString()
                        val noSurat =   editView?.edt_nomor_surat?.text?.toString()
                        val ayat =   editView?.edt_ayat?.text?.toString()

                        if(judul?.isEmpty()!!){
                            progress_bar?.visibility = View.GONE
                            editView?.edt_judul?.setError("Judul belum diisi")
                            return@setOnClickListener
                        }

                        if(namaSurat?.isEmpty()!!){
                            progress_bar?.visibility = View.GONE
                            editView?.edt_nama_surat?.setError("Nama Surat belum diisi")
                            return@setOnClickListener
                        }
                        if(noSurat?.isEmpty()!!){
                            progress_bar?.visibility = View.GONE
                            editView?.edt_nomor_surat?.setError("Nomor Surat belum diisi")
                            return@setOnClickListener
                        }

                        if(ayat?.isEmpty()!!){
                            progress_bar?.visibility = View.GONE
                            editView?.edt_ayat?.setError("Ayat belum diisi")
                            return@setOnClickListener
                        }

                        viewModel?.edit(quranDaily.id,Constants.APIKEY, apisess, judul, namaSurat, noSurat, ayat).observe(this@SettingQuranDailyActivity, Observer {
                            progress_bar?.visibility = View.GONE
                            if(it != null){
                                if(it.status == 200){
                                    it.result?.qurandaily?.let { it1 -> adapter.setItems(it1) }
                                    rv_qd?.adapter = adapter
                                    dialog.dismiss()
                                    formDialog?.dismiss()
                                    Toast.makeText(this@SettingQuranDailyActivity, "Berhasil mengedit", Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(this@SettingQuranDailyActivity, it.message, Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                Toast.makeText(this@SettingQuranDailyActivity, "Gagal, coba beberapa saat lagi", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }

                }
            }


        })

        adapter?.setOnPickListener(object: QuranDailyAdapter.OnPickListener{
            override fun onItemPick(id: Int) {
                progress_bar?.visibility = View.VISIBLE
                viewModel?.pick(id.toString(),Constants.APIKEY, apisess).observe(this@SettingQuranDailyActivity, Observer {
                    progress_bar?.visibility = View.GONE
                    if(it != null){
                        if(it.status == 200){
                            it.result?.qurandaily?.let { it1 -> adapter.setItems(it1) }
                            rv_qd?.adapter = adapter
                            Toast.makeText(this@SettingQuranDailyActivity, "Berhasil memilih", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@SettingQuranDailyActivity, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this@SettingQuranDailyActivity, "Gagal, coba beberapa saat lagi", Toast.LENGTH_SHORT).show()
                    }
                })
            }

        })


    }

    private fun getData() {
        viewModel?.getAll(apisess).observe(this, Observer {
            swipeRefresh?.isRefreshing = false
            progress_bar?.visibility = View.GONE
            if(it != null){
                if(it.status == 200){
                    rv_qd?.layoutManager = LinearLayoutManager(this)
                    rv_qd?.setHasFixedSize(true)
                    it.result?.qurandaily?.let { it1 -> adapter.setItems(it1) }
                    rv_qd?.adapter = adapter
                }else{
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Gagal, coba beberapa saat lagi", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun hapus(position: Int, id: String?) {
        progress_bar?.visibility = View.VISIBLE
        viewModel?.hapus(id, Constants.APIKEY, apisess).observe(this, Observer {
            progress_bar?.visibility = View.GONE
            if(it != null){
                if(it.status == 200){
                    adapter?.removeItem(position)
                    Toast.makeText(this@SettingQuranDailyActivity, "Berhasil menghapus", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@SettingQuranDailyActivity, it.message, Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@SettingQuranDailyActivity, "Gagal, coba beberapa saat lagi", Toast.LENGTH_SHORT).show()
            }
        })
    }


}
