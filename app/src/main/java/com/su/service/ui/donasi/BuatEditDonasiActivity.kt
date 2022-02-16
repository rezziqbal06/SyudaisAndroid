package com.su.service.ui.donasi

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.su.service.R
import com.su.service.model.detaildonasi.DonasiItem
import com.su.service.utils.Constants
import com.su.service.utils.SharedPrefManager
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_buat_doa_dzikir.*
import kotlinx.android.synthetic.main.activity_buat_edit_donasi.*
import kotlinx.android.synthetic.main.activity_buat_edit_donasi.container
import kotlinx.android.synthetic.main.activity_buat_edit_donasi.img_gambar
import kotlinx.android.synthetic.main.activity_buat_edit_donasi.img_gambar_placeholder
import kotlinx.android.synthetic.main.activity_buat_edit_donasi.progress_bar
import kotlinx.android.synthetic.main.activity_buat_edit_donasi.toolbar
import kotlinx.android.synthetic.main.activity_donasi.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.HashMap

class BuatEditDonasiActivity : AppCompatActivity() {
    private var gambar1Id: String? = null
    private val TAG = BuatEditDonasiActivity::class.java.simpleName
    private lateinit var viewModel: DonasiViewModel
    private lateinit var sharedPrefManager: SharedPrefManager
    private var isEdit = false
    private var data: DonasiItem? = null
    private var gambar1 : MultipartBody.Part? = null
    private var gambar2 : MultipartBody.Part? = null
    private var gambar3 : MultipartBody.Part? = null
    private var choosedPanelImage = 0
    val isi = HashMap<String, RequestBody>()
    private var donasiId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buat_edit_donasi)
        viewModel = ViewModelProvider(this).get(DonasiViewModel::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        data = intent?.getParcelableExtra<DonasiItem>("extra_donasi")

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Donasi"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pug2?.visibility = View.GONE
        pug3?.visibility = View.GONE

        hideLoading()
        if(data != null){
            isEdit = true
            updateUi()
        }

        btn_simpan?.setOnClickListener {
            showLoading()
            simpan()
        }

        pug1?.setOnClickListener {
            ambilFoto(0)
        }

        pug2.setOnClickListener {
            ambilFoto(1)
        }

        pug3?.setOnClickListener {
            ambilFoto(2)
        }
    }
    private fun showLoading(){
        btn_simpan?.isEnabled = false
        progress_bar?.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        btn_simpan?.isEnabled = true
        progress_bar?.visibility = View.GONE
    }

    private fun simpan() {
        val nama = edt_nama?.text?.toString()
        val no = edt_no?.text?.toString()
        val rekening = edt_rekening?.text?.toString()
        val wa = edt_wa?.text?.toString()
        val deskripsi = edt_deskripsi?.text?.toString()
        val an = edt_an?.text?.toString()
        val bank = edt_bank?.text?.toString()
        val dana = edt_dana?.text?.toString()

        if(nama?.isEmpty()!!){
            hideLoading()
            edt_nama?.setError("Belum diisi")
            edt_nama?.requestFocus()
            return
        }

        if(no?.isEmpty()!!){
            hideLoading()
            edt_no?.setError("Belum diisi")
            edt_no?.requestFocus()
            return
        }

        if(rekening?.isEmpty()!!){
            hideLoading()
            edt_rekening?.setError("Belum diisi")
            edt_rekening?.requestFocus()
            return
        }

        if(wa?.isEmpty()!!){
            hideLoading()
            edt_wa?.setError("Belum diisi")
            edt_wa?.requestFocus()
            return
        }

        if(deskripsi?.isEmpty()!!){
            hideLoading()
            edt_deskripsi?.setError("Belum diisi")
            edt_deskripsi?.requestFocus()
            return
        }

        if(an?.isEmpty()!!){
            hideLoading()
            edt_an?.setError("Belum diisi")
            edt_an?.requestFocus()
            return
        }

        if(bank?.isEmpty()!!){
            hideLoading()
            edt_bank?.setError("Belum diisi")
            edt_bank?.requestFocus()
            return
        }

        if(!isEdit){
            if(gambar1 == null){
                hideLoading()
                Snackbar.make(container,"Harus mengupload 3 gambar", Snackbar.LENGTH_SHORT).show()
                return
            }
        }

        this.isi?.set("nama", generateRequestBody(nama))
        this.isi?.set("nomor", generateRequestBody(no))
        this.isi?.set("rekening", generateRequestBody(rekening))
        this.isi?.set("wa", generateRequestBody(wa))
        this.isi?.set("deskripsi", generateRequestBody(deskripsi))
        this.isi?.set("atas_nama", generateRequestBody(an))
        this.isi?.set("bank", generateRequestBody(bank))
        dana?.let { generateRequestBody(it) }?.let { this.isi?.set("dana", it) }

        if(isEdit){
            viewModel?.editDonasi(donasiId.toString(), Constants.APIKEY,
                sharedPrefManager.user.apiMobileToken, isi
            ).observe(this, Observer {
                hideLoading()
                if(it != null){
                    if(it.status == 200){
                        Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                        Handler().postDelayed(Runnable {
                            setResult(Activity.RESULT_OK)
                            finish() },1500)
                    }else{
                        Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                    }
                }else{
                    Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
                }
            })
        }else{
            Log.d(TAG, "uploading donasi")
            viewModel?.postDonasi(Constants.APIKEY,
                sharedPrefManager.user.apiMobileToken, gambar1, gambar2, gambar3,
                isi
            ).observe(this, Observer {
                hideLoading()
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

    }

    private fun generateRequestBody(s: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"),s)
    }

    private fun updateUi() {
        donasiId = data?.id!!

        edt_nama?.setText(data?.nama)
        edt_no?.setText(data?.nomor)
        edt_rekening?.setText(data?.rekening)
        edt_wa?.setText(data?.wa)
        edt_deskripsi?.setText(data?.deskripsi)
        edt_an?.setText(data?.atas_nama)
        edt_dana?.setText(data?.dana)
        edt_bank?.setText(data?.bank)

        if(data?.galeri?.size == 1){
            img_gambar_placeholder?.visibility = View.INVISIBLE
            Glide.with(this).load(data?.galeri?.get(0)?.thumb).into(img_gambar)
            this.gambar1Id = data?.galeri?.get(0)?.id
        }else if(data?.galeri?.size == 2){
            img_gambar_placeholder?.visibility = View.INVISIBLE
            Glide.with(this).load(data?.galeri?.get(0)?.thumb).into(img_gambar)
            this.gambar1Id = data?.galeri?.get(0)?.id

            img_gambar_placeholder2?.visibility = View.INVISIBLE
            Glide.with(this).load(data?.galeri?.get(1)?.thumb).into(img_gambar2)
        }else if(data?.galeri?.size == 3){
            img_gambar_placeholder?.visibility = View.INVISIBLE
            Glide.with(this).load(data?.galeri?.get(0)?.thumb).into(img_gambar)
            this.gambar1Id = data?.galeri?.get(0)?.id

            img_gambar_placeholder2?.visibility = View.INVISIBLE
            Glide.with(this).load(data?.galeri?.get(1)?.thumb).into(img_gambar2)

            img_gambar_placeholder3?.visibility = View.INVISIBLE
            Glide.with(this).load(data?.galeri?.get(2)?.thumb).into(img_gambar3)
        }
    }

    private fun ambilFoto(choosedPanel: Int){
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        getAndCropImage()
                        choosedPanelImage = choosedPanel
                    } else {
                        // TODO - handle permission denied case
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun getAndCropImage() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setOutputCompressQuality(20)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                Log.d("APP_DEBUG", result.toString())
                if (resultCode == Activity.RESULT_OK) {
                    val resultUri = result.uri
                    Log.d("APP_DEBUG", resultUri.toString())
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(contentResolver, resultUri)
                    updateUiGambar(bitmap)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                    Log.d("error crop ", error.toString())
                }
            }
        } catch (e: Exception) {
            Log.e("error", "Something went wrong" + e.message)
        }
    }

    private fun updateUiGambar(bitmap: Bitmap?) {
        when(choosedPanelImage){
            0 -> {
                img_gambar_placeholder?.visibility = View.GONE
                img_gambar?.setImageBitmap(bitmap)
                gambar1 = uploadImage(bitmap,"1")
                if(isEdit){
                    updateGambarKeServer()
                }
            }
            1 -> {
                img_gambar_placeholder2?.visibility = View.GONE
                img_gambar2?.setImageBitmap(bitmap)
                gambar2 = uploadImage(bitmap,"2")
            }
            2 -> {
                img_gambar_placeholder3?.visibility = View.GONE
                img_gambar3?.setImageBitmap(bitmap)
                gambar3 = uploadImage(bitmap,"3")
            }
        }

    }

    private fun updateGambarKeServer() {
        viewModel.updateGambar(donasiId.toString(), gambar1Id, Constants.APIKEY, sharedPrefManager.user.apiMobileToken,gambar1 ).observe(this, Observer {
            if(it != null){
                if(it.status == 200){
                    Log.d(TAG, "status: ${it.status}")
                }else{
                    Log.d(TAG, "status: ${it.status}")

                }
            }else{
                Log.d(TAG, "status: failed")
            }
        })
    }

    fun uploadImage(gambarbitmap: Bitmap?, ke: String): MultipartBody.Part {

        //convert gambar jadi File terlebih dahulu dengan memanggil createTempFile yang di atas tadi.
        val file = gambarbitmap?.let { createTempFile(it) }
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val body =
            MultipartBody.Part.createFormData("gambar$ke", file?.name, reqFile)
        return body
    }

    private fun createTempFile(bitmap: Bitmap): File? {
        val file = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            , System.currentTimeMillis().toString() + "_image.jpg"
        )
        val bos = ByteArrayOutputStream()
        Log.d("image size ", bitmap.byteCount.toString())
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapdata = bos.toByteArray()
        //write the bytes in file
        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
