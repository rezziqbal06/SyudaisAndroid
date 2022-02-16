package com.su.service.ui.doadzikir

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColor
import androidx.core.view.marginEnd
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.su.service.R
import com.su.service.ui.doa.DoaActivity
import com.su.service.ui.dzikir.DzikirActivity
import com.su.service.utils.SharedPrefManager
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_buat_doa_dzikir.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class BuatDoaDzikirActivity : AppCompatActivity() {
    val TAG = BuatDoaDzikirActivity::class.java.simpleName
    companion object{
        val EXTRA_TAG = "extra_tag"
        val EXTRA_ID= "extra_id"
        val EXTRA_NAMA = "extra_nama"
        val EXTRA_ISI = "extra_isi"
        val EXTRA_DALIL = "extra_dalil"
        val EXTRA_GAMBAR = "extra_gambar"
    }
    private var gambar: MultipartBody.Part? = null
    private var tag: String? = ""
    private lateinit var viewModel: DoaDzikirViewModel
    private lateinit var sharedPrefManager: SharedPrefManager
    private var isEdit = false
    private var id: Int? = null
    val isi = HashMap<String, RequestBody>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buat_doa_dzikir)
        viewModel = ViewModelProviders.of(this).get(DoaDzikirViewModel::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        tag = intent.getStringExtra(EXTRA_TAG)
        img_gambar_placeholder?.visibility = View.VISIBLE
        hideLoader()

        id = intent.getIntExtra(EXTRA_ID,0)
        val nama = intent.getStringExtra(EXTRA_NAMA)
        val isi = intent.getStringExtra(EXTRA_ISI)
        val dalil = intent.getStringExtra(EXTRA_DALIL)
        val gambar = intent.getStringExtra(EXTRA_GAMBAR)

        if(nama != null) isEdit = true

        edt_judul?.setText(nama)
        edt_isi?.setText(isi)
        edt_dalil?.setText(dalil)

        if(gambar != null){
            img_gambar_placeholder?.visibility = View.INVISIBLE
            Glide.with(this)
                .load(gambar)
                .into(img_gambar)
        }

        if(tag == "doa"){
            tv_header?.text = "Buat Doa"
        }else{
            tv_header?.text = "Buat Dzikir"
        }
        btn_save?.setOnClickListener {
            simpan()
        }
        panel_upload_gambar?.setOnClickListener {
            ambilFoto()
        }
        img_back?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun ambilFoto(){
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        getAndCropImage()
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
            .setAspectRatio(1, 1)
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
                    gambar = uploadImage(bitmap)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                    Log.d("error crop ", error.toString())
                }
            }
        } catch (e: Exception) {
            Log.e("error", "Something went wrong" + e.message)
        }
    }

    fun uploadImage(gambarbitmap: Bitmap?): MultipartBody.Part {
        img_gambar_placeholder?.visibility = View.GONE
        img_gambar?.setImageBitmap(gambarbitmap)
        //convert gambar jadi File terlebih dahulu dengan memanggil createTempFile yang di atas tadi.
        val file = gambarbitmap?.let { createTempFile(it) }
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val body =
            MultipartBody.Part.createFormData("gambar", file?.name, reqFile)
        return body
    }

    fun showLoader(){
        progressBar?.visibility = View.VISIBLE
    }

    fun hideLoader(){
        progressBar?.visibility = View.GONE
    }

    private fun simpan() {
        btn_save?.isEnabled = false
        btn_save?.isClickable = false
        val charset = Charsets.UTF_8
        var edtJudul = edt_judul?.text.toString()
        var edtIsi = edt_isi?.text.toString()
        var edtDalil = edt_dalil?.text.toString()
        val byteJudul = edtJudul.toByteArray(charset)

        if(edtJudul.isEmpty()){
            edt_judul?.error = "Nama $tag belum diisi"
            return
        }

        if(edtIsi.isEmpty()){
            edt_isi?.error = "$tag belum diisi"
            return
        }

        if(edtDalil.isEmpty()){
            edt_dalil?.error = "dalil $tag belum diisi"
            return
        }

        if(!isEdit){
            if(gambar == null){
                Snackbar.make(container, "Belum memilih gambar", Snackbar.LENGTH_SHORT).show()
                return
            }
        }

        var judul = RequestBody.create(MediaType.parse("text/plain"),edtJudul)
        var isi = RequestBody.create(MediaType.parse("text/plain"),edtIsi)
        var dalil = RequestBody.create(MediaType.parse("text/plain"),edtDalil)

        showLoader()

        val apisess = sharedPrefManager.user.apiMobileToken
        Log.d(TAG,"isi: $isi")

        if(tag == "doa"){
            this.isi["nama"] = judul
            this.isi["doa"] = isi
            this.isi["dalil"] = dalil
            if(isEdit){
                viewModel.editDoa(id.toString(),apisess,  this.isi, gambar).observe(this, androidx.lifecycle.Observer { data ->
                    hideLoader()
                    btn_save?.isEnabled = true
                    btn_save?.isClickable = true
                    if(data != null){
                        if(data.status == 200){
                            Toast.makeText(this,"Berhasil diedit", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, DoaActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            Snackbar.make(container, data?.message.toString(),Snackbar.LENGTH_SHORT).show()
                        }
                    }else{
                        Snackbar.make(container, "Gagal, coba beberapa saat lagi",Snackbar.LENGTH_SHORT).show()
                    }
                })
            }else{
                apisess?.let { it ->
                    gambar?.let { it1 ->
                        viewModel.uploadDoa(it,  this.isi, it1).observe(this, androidx.lifecycle.Observer { data ->
                            hideLoader()
                            btn_save?.isEnabled = true
                            btn_save?.isClickable = true
                            if(data != null){
                                if(data.status == 200){
                                    Toast.makeText(this,"Berhasil dibuat", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, DoaActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }else{
                                    Snackbar.make(container, data?.message.toString(),Snackbar.LENGTH_SHORT).show()
                                }
                            }else{
                                Snackbar.make(container, "Gagal, coba beberapa saat lagi",Snackbar.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }
        }else{
            this.isi["nama"] = judul
            this.isi["dzikir"] = isi
            this.isi["dalil"] = dalil
            if(isEdit){
                apisess?.let { it ->
                    id?.let { it1 ->
                        gambar?.let { it2 ->
                            viewModel.editDzikir(it1.toString(),it,   this.isi,
                                it2
                            ).observe(this, androidx.lifecycle.Observer { data ->
                                hideLoader()
                                btn_save?.isEnabled = true
                                btn_save?.isClickable = true
                                if(data != null){
                                    if(data.status == 200){
                                        Toast.makeText(this,"Berhasil diedit", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, DzikirActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }else{
                                        Snackbar.make(container, data?.message.toString(),Snackbar.LENGTH_SHORT).show()
                                    }
                                }else{
                                    Snackbar.make(container, "Gagal, coba beberapa saat lagi",Snackbar.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                }
            }else{
                apisess?.let { it ->
                    gambar?.let { it1 ->
                        viewModel.uploadDzikir(it,  this.isi,
                            it1
                        ).observe(this, androidx.lifecycle.Observer { data ->
                            hideLoader()
                            btn_save?.isEnabled = true
                            btn_save?.isClickable = true
                            if(data != null){
                                if(data.status == 200){
                                    Toast.makeText(this,"Berhasil dibuat", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, DzikirActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }else{
                                    Snackbar.make(container, data?.message.toString(),Snackbar.LENGTH_SHORT).show()
                                }
                            }else{
                                Snackbar.make(container, "Gagal, coba beberapa saat lagi",Snackbar.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }
        }
    }

    private fun createRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }

    private fun createPartFromString(isi: String): RequestBody {
        return RequestBody.create(
            MediaType.parse("multipart/form-data"), isi
        )
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

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        val dialog: AlertDialog = builder.setTitle("Belum menyimpan $tag")
            .setMessage("Yakin ingin keluar tanpa menyimpan?")
            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                super.onBackPressed()
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
