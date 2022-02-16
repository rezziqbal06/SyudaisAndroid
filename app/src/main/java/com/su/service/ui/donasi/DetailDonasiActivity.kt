package com.su.service.ui.donasi

import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.su.service.R
import com.su.service.model.detaildonasi.DonasiItem
import com.su.service.utils.Constants
import com.su.service.utils.RpGenerator
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.activity_detail_donasi.*

class DetailDonasiActivity : AppCompatActivity() {
    private var donasi: DonasiItem? = null
    private lateinit var donasiViewModel: DonasiViewModel
    private lateinit var sharedPrefManager: SharedPrefManager
    private var donasiId = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_donasi)
        showLoading()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        donasiViewModel = ViewModelProvider(this).get(DonasiViewModel::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        donasiId = intent.getIntExtra("extra_id",1)
        initData()
        container.setOnRefreshListener {
            initData()
            container.isRefreshing = false
        }
    }

    private fun initData() {
        donasiViewModel.detailDonasi(donasiId.toString()).observe(this, Observer {
            hideLoading()
            if(it != null){
                if(it.status == 200){
                    updateUi(it.result?.donasi)
                }else{
                    Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }else{
                Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUi(donasi: DonasiItem?) {
        this@DetailDonasiActivity.donasi = donasi
        tv_nama?.text = donasi?.nama
        tv_deskripsi?.text = donasi?.deskripsi
        tv_dana?.text = "Rp. "+donasi?.dana?.let { RpGenerator().generate(it) }
        tv_dana_title?.text = "Dana yang dibutuhkan"
        tv_an?.text = "a.n. ${donasi?.atas_nama}"
        tv_rekening?.text = "${donasi?.bank} - ${donasi?.rekening}"
        tv_wa?.text = donasi?.wa
        tv_nomor?.text = donasi?.nomor
        Glide.with(this)
            .load(donasi?.thumb)
            .placeholder(R.drawable.default_picture)
            .into(img_donasi)

        /*rv_galeri_donasi?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)
        val adapter = donasi?.galeri?.let { DonasiGambarAdapter(it) }
        rv_galeri_donasi?.adapter = adapter*/

        tv_salin_an?.setOnClickListener {
            copyText("atas nama",tv_an?.text)
        }

        tv_salin_rekening?.setOnClickListener {
            copyText("rekening",tv_rekening?.text)
        }

        tv_wa?.setOnClickListener {
            var wa = donasi?.wa
            if(wa?.startsWith("0")!!){
                wa.drop(0)
                wa = "+62$wa"
            }
            val waIntent = Intent(Intent.ACTION_VIEW)
            waIntent.data = Uri.parse("https://api.whatsapp.com/send?phone=$wa")
            startActivity(waIntent)
        }

        tv_nomor?.setOnClickListener {
            val uri = "tel:" + donasi?.nomor?.trim()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(uri)
            startActivity(intent)
        }
    }

    private fun copyText(label: String, text: CharSequence?) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label,text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Copied",Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        progress_bar?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progress_bar?.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(sharedPrefManager.isLoggedIn){
            if(sharedPrefManager.user.utype == "admin"){
                menuInflater.inflate(R.menu.menu_edit_donasi, menu)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_edit){
            val intent = Intent(this, BuatEditDonasiActivity::class.java)
            intent.putExtra("extra_donasi", donasi)
            startActivityForResult(intent,103)
        }else if(item.itemId == R.id.menu_hapus){
            val builder = AlertDialog.Builder(this)
            val dialog: AlertDialog = builder.setTitle("Menghapus data")
                .setMessage("Yakin ingin menghapus?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    hapusDonasi()
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
        return super.onOptionsItemSelected(item)
    }

    private fun hapusDonasi() {
        donasiViewModel.hapusDonasi(donasiId.toString(), Constants.APIKEY, sharedPrefManager.user.apiMobileToken).observe(this, Observer {
            if(it != null){
                if(it.status == 200){
                    Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                    Handler().postDelayed(Runnable { finish() },1500)
                }else{
                    Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }else{
                Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 103 && resultCode == Activity.RESULT_OK){
            initData()
        }
    }
}
