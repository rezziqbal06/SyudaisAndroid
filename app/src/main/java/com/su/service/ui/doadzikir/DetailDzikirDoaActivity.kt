package com.su.service.ui.doadzikir

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.su.service.R
import com.su.service.ui.detailgambar.ImageFullScreenActivity
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.activity_detail_dzikir_doa.*
import kotlinx.android.synthetic.main.content_dzikir_doa.*

class DetailDzikirDoaActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_TAG = "extra_tag"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_DOADZIKIR = "extra_doadzikir"
        const val EXTRA_DALIL = "extra_dalil"
        const val EXTRA_GAMBAR = "extra_gambar"
    }
    private lateinit var doaDzikirViewModel: DoaDzikirViewModel
    private lateinit var sharedPrefManager: SharedPrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_dzikir_doa)
        setSupportActionBar(toolbar)
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        doaDzikirViewModel = ViewModelProvider(this).get(DoaDzikirViewModel::class.java)

        if(sharedPrefManager.isLoggedIn){
            if(sharedPrefManager.user.utype == "admin"){
                fab_dd_edit?.visibility = View.VISIBLE
                fab_dd_hapus?.visibility = View.VISIBLE
            }else{
                fab_dd_edit?.visibility = View.GONE
                fab_dd_hapus?.visibility = View.GONE
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = " "
        val tag = intent.getStringExtra(EXTRA_TAG)
        val id = intent.getIntExtra(EXTRA_ID,0)
        Log.d("DetailDoaDzikirActivity", "id: $id")
        val nama = intent.getStringExtra(EXTRA_NAMA)
        val doadzikir = intent.getStringExtra(EXTRA_DOADZIKIR)
        val dalil = intent.getStringExtra(EXTRA_DALIL)
        val gambar = intent.getStringExtra(EXTRA_GAMBAR)
        tv_judul_dzikirdoa?.text = nama
        tv_dzikirdoa?.text = doadzikir
        tv_dalil?.text = dalil
        Glide.with(this)
            .load(gambar)
            .placeholder(R.drawable.default_picture)
            .error(R.drawable.default_picture)
            .into(img_doadzikir)
        fab_dd_edit?.setOnClickListener {
            val intent = Intent(this, BuatDoaDzikirActivity::class.java)
            intent.putExtra(BuatDoaDzikirActivity.EXTRA_TAG,tag)
            intent.putExtra(BuatDoaDzikirActivity.EXTRA_ID,id)
            intent.putExtra(BuatDoaDzikirActivity.EXTRA_NAMA,nama)
            intent.putExtra(BuatDoaDzikirActivity.EXTRA_ISI,doadzikir)
            intent.putExtra(BuatDoaDzikirActivity.EXTRA_DALIL,dalil)
            intent.putExtra(BuatDoaDzikirActivity.EXTRA_GAMBAR,gambar)
            startActivity(intent)
        }

        fab_dd_hapus?.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialog = builder.setIcon(R.drawable.logo_syudais_merah)
                .setIcon(R.drawable.logo_syudais_merah)
                .setMessage("Apakah yakin doa ingin dihapus?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener{ dialogInterface: DialogInterface, i: Int ->
                if(tag == "doa"){
                    sharedPrefManager.user.apiMobileToken?.let {
                        doaDzikirViewModel.hapusDoa(id.toString(),
                            it
                        ).observe(this, Observer { data ->
                            if(data != null){
                                if(data.status == 200){
                                    Snackbar.make(container, "Doa Berhasil Dihapus, Silahkan Refresh", Snackbar.LENGTH_SHORT).show()
                                    Handler().postDelayed(Runnable { finish() }, 1000)
                                }else{
                                    Snackbar.make(container, data.message.toString(), Snackbar.LENGTH_SHORT).show()
                                }
                            }else{
                                Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
                            }

                        })
                    }
                }else{
                    sharedPrefManager.user.apiMobileToken?.let {
                        doaDzikirViewModel.hapusDzikir(id.toString(),
                            it
                        ).observe(this, Observer { data ->
                            if(data != null){
                                if(data.status == 200){
                                    Snackbar.make(container, "Dzikir Berhasil Dihapus, Silahkan Refresh", Snackbar.LENGTH_SHORT).show()
                                    Handler().postDelayed(Runnable { finish() }, 1000)
                                }else{
                                    Snackbar.make(container, data.message.toString(), Snackbar.LENGTH_SHORT).show()
                                }
                            }else{
                                Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
                            }
                        })
                    }
                }

            })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener{ dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }).create()
            dialog.show()
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.colorPrimaryDark))
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.colorPrimaryDark))

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
