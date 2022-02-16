package com.su.service.ui.artikel.uploadartikel

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.su.service.R
import com.su.service.model.artikel.ArtikelLocal
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.activity_upload_artikel.*
import okhttp3.MultipartBody

class UploadArtikelActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_DATA = "extra_data_artikel"
    }
    private lateinit var viewModel: UploadArtikelViewModel
    private lateinit var sharedPrefManager: SharedPrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_artikel)
        viewModel = ViewModelProvider(this).get(UploadArtikelViewModel::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        val data = intent.getParcelableExtra<ArtikelLocal>(EXTRA_DATA)
        showLoading()
        tv_judul_artikel?.text = "judul: "+data.title
        uploadArtikel(data)
    }

    private fun uploadArtikel(data: ArtikelLocal?) {
        val slug = data?.title?.replace(" ","-")
        data?.title?.let {
            data.content?.let { it1 ->
                data.kategori?.let { it2 ->
                    slug?.let { it3 ->
                        viewModel.uploadArtikel(sharedPrefManager.user.apiMobileToken.toString(),
                            it, it1, it2, it3, data.content!!
                        ).observe(this, Observer {
                            hideLoading()
                            if(it != null){
                                if(it.status == 200){
                                    hideLoading()
                                    Snackbar.make(container,"Berhasil, tunggu review dari kami",Snackbar.LENGTH_SHORT).show()
                                    Handler().postDelayed(Runnable {
                                        setResult(Activity.RESULT_OK)
                                        finish() },1350)
                                }else{
                                    Snackbar.make(container,it.message.toString(),Snackbar.LENGTH_SHORT).show()
                                    if(it.status == 104){
                                        Handler().postDelayed(Runnable { finish() },1350)
                                    }
                                }
                            }else{
                                Snackbar.make(container,resources.getString(R.string.failed),Snackbar.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }
        }
    }

    private fun showLoading() {
        progress_bar?.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        progress_bar?.visibility = View.GONE
    }
}
