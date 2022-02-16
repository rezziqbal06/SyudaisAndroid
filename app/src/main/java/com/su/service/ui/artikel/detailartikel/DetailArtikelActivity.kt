package com.su.service.ui.artikel.detailartikel

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.SpannableString
import android.text.Spanned
import android.text.style.QuoteSpan
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.su.service.R
import com.su.service.model.artikel.Artikel
import com.su.service.model.artikel.ArtikelLocal
import com.su.service.ui.artikel.buateditartikel.BuatEditArtikelActivity
import com.su.service.ui.artikel.buateditartikel.BuatEditArtikelActivity.Companion.EXTRA_DATA_PUBLISH
import com.su.service.ui.detailgambar.ImageFullScreenActivity
import com.su.service.ui.diskusi.DiskusiActivity
import com.su.service.ui.diskusi.DiskusiViewModel
import com.su.service.ui.user.UserViewModel
import com.su.service.utils.DateGenerator
import com.su.service.utils.HtmlToStringGenerator
import com.su.service.utils.SharedPrefManager
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_detail_artikel.*
import kotlinx.android.synthetic.main.content_detail_artikel.*
import net.dankito.utils.android.extensions.getDimension
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.sufficientlysecure.htmltextview.HtmlFormatter
import org.sufficientlysecure.htmltextview.HtmlFormatterBuilder
import org.sufficientlysecure.htmltextview.HtmlResImageGetter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DetailArtikelActivity : AppCompatActivity(), View.OnClickListener {
    val TAG = DetailArtikelActivity::class.java.simpleName
    companion object{
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_DATA_LOCAL = "extra_data_local"
        const val REQUEST_EDIT = 103
    }
    private var artikelId: Int? = null
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var userViewModel: UserViewModel
    private lateinit var diskusiViewModel: DiskusiViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_artikel)
        setSupportActionBar(toolbar)
        hideLoader()
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        diskusiViewModel = ViewModelProvider(this).get(DiskusiViewModel::class.java)
        val data = intent.getParcelableExtra<Artikel>(EXTRA_DATA)
        val dataLocal = intent.getParcelableExtra<ArtikelLocal>(EXTRA_DATA_LOCAL)
        supportActionBar?.title = " "
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(dataLocal != null){
            updateUiLocal(dataLocal)
        }else if(data != null){
            getRead(data.id)
            updateUi(data)
        }
        tv_detail_artikel_isi?.textSize = resources.getDimension(R.dimen.font_small)
        btn_1x.setTextColor(resources.getColor(R.color.colorAccent))

        btn_1x.setOnClickListener(this)
        btn_2x.setOnClickListener(this)
        btn_3x.setOnClickListener(this)
    }

    private fun getRead(id: Int?) {
        userViewModel.bacaArtikel(id, sharedPrefManager.user.apiMobileToken).observe(this, Observer {
            if(it != null){
                Log.d(TAG, "status baca: ${it.status}")
            }else{
                Log.d(TAG, "status baca: gagal")
            }
        })
    }

    private fun updateUiLocal(data: ArtikelLocal?) {
        fab_edit?.visibility = View.GONE
        fab_notif?.visibility = View.GONE
        notif_read?.visibility = View.GONE
        tv_detail_artikel_judul?.text = data?.title
        tv_detail_artikel_kategori?.text = data?.kategori
        if(data?.ldate != null){
            val tgl = DateGenerator.getTanggal("yyyy-MM-dd","EEEE, dd MMMM yyyy", data?.ldate)
            tv_detail_artikel_tanggal?.text = tgl
        }
        val isi = data?.content?.let { HtmlToStringGenerator.generate(it) }
        val htmlBuilder = HtmlFormatterBuilder()
        val htmlResImageGetter = tv_detail_artikel_isi?.context?.let { HtmlResImageGetter(it) }
        htmlBuilder.html = data?.content
        htmlBuilder.setImageGetter(htmlResImageGetter)
        val isiSpanned = HtmlFormatter.formatHtml(htmlBuilder)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tv_detail_artikel_isi?.justificationMode
        }
        tv_detail_artikel_isi?.text = isiSpanned
        if(data?.gambar != null){
            if(data?.gambar?.length!! < 30){
                img_detail_artikel?.setImageResource(R.drawable.default_picture)
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    img_detail_artikel?.transitionName = "gambar"
                    Glide.with(this)
                        .load(data?.gambar)
                        .placeholder(R.drawable.default_picture)
                        .error(R.drawable.default_picture)
                        .into(img_detail_artikel)
                }else{
                    Glide.with(this)
                        .load(data?.gambar)
                        .placeholder(R.drawable.default_picture)
                        .error(R.drawable.default_picture)
                        .into(img_detail_artikel)
                }
            }
        }

        img_detail_artikel?.setOnClickListener {
            val intent = Intent(this, ImageFullScreenActivity::class.java)
            intent.putExtra(ImageFullScreenActivity.EXTRA_IMAGE, data?.gambar)
            intent.putExtra(ImageFullScreenActivity.EXTRA_TEXT, data?.title)
            startActivity(intent)
        }
    }

    private fun updateUi(data: Artikel?) {
        artikelId = data?.id
        tv_detail_artikel_judul?.text = data?.title
        tv_detail_artikel_kategori?.text = data?.kategori
        if(data?.ldate != null){
            val tgl = DateGenerator.getTanggal("yyyy-MM-dd","EEEE, dd MMMM yyyy", data?.ldate)
            tv_detail_artikel_tanggal?.text = tgl
        }
        notif_read?.visibility = View.GONE
        val isi = data?.content?.let { HtmlToStringGenerator.generate(it) }
        val htmlBuilder = HtmlFormatterBuilder()
        val htmlResImageGetter = tv_detail_artikel_isi?.context?.let { HtmlResImageGetter(it) }
        htmlBuilder.html = data?.content
        htmlBuilder.setImageGetter(htmlResImageGetter)
        val isiSpanned = HtmlFormatter.formatHtml(htmlBuilder)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tv_detail_artikel_isi?.justificationMode
        }
        tv_detail_artikel_isi?.text = isiSpanned
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            img_detail_artikel?.transitionName = "gambar"
            Glide.with(this)
                .load(data?.gambar)
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                .into(img_detail_artikel)
        }else{
            Glide.with(this)
                .load(data?.gambar)
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                .into(img_detail_artikel)
        }
        img_detail_artikel?.setOnClickListener {
            val intent = Intent(this, ImageFullScreenActivity::class.java)
            intent.putExtra(ImageFullScreenActivity.EXTRA_IMAGE, data?.gambar)
            intent.putExtra(ImageFullScreenActivity.EXTRA_TEXT, data?.title)
            startActivity(intent)
        }
        Log.d(TAG, "user_id: ${data?.b_user_id}, pref_user_id: ${sharedPrefManager.user.id}")
        fab_edit?.setImageResource(R.drawable.ic_edit)
        if(data?.b_user_id == sharedPrefManager.user.id){
            data?.let { getDataDiskusi(it) }
            fab_edit?.visibility = View.VISIBLE
            fab_edit?.setImageResource(R.drawable.ic_edit)
            fab_edit?.setOnClickListener {
                editArtikel(data)
            }
        }else{
            fab_edit?.visibility = View.GONE
            fab_notif?.visibility = View.GONE
            notif_read?.visibility = View.GONE
            if(sharedPrefManager.user.utype == "admin"){
                fab_notif?.visibility = View.VISIBLE
                notif_read?.visibility = if(data?.is_read == 0) View.GONE else View.VISIBLE
                fab_edit?.visibility = View.VISIBLE
                fab_edit?.setImageResource(R.drawable.ic_option)
                fab_edit?.setOnClickListener {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Pilihan")
                    val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
                    adapter.addAll("Publish","Revisi","Tolak","Edit","Update Gambar")
                    builder.setAdapter(adapter, DialogInterface.OnClickListener { dialogInterface, i ->
                        when(i){
                            0 -> {
                                data?.id?.let { it1 -> setStatus(it1, "publish") }
                                dialogInterface.dismiss()
                            }
                            1 -> {
                                data?.id?.let { it1 -> setStatus(it1, "revisi") }
                                dialogInterface.dismiss()
                            }
                            2 -> {
                                data?.id?.let { it1 -> setStatus(it1, "reject") }
                                dialogInterface.dismiss()
                            }
                            3 -> {
                                editArtikel(data)
                                dialogInterface.dismiss()
                            }
                            4 -> {
                                updateGambar(data)
                                dialogInterface.dismiss()
                            }
                        }
                    }).show()
                }
            }
        }

        fab_notif?.setOnClickListener {
            val intent = Intent(this@DetailArtikelActivity, DiskusiActivity::class.java)
            intent.putExtra("extra_id",data?.id.toString())
            startActivity(intent)
        }

    }

    private fun updateGambar(data: Artikel?) {
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
            .setOutputCompressQuality(20)
            .start(this)
    }

    private fun getDataDiskusi(data: Artikel) {
        notif_read?.visibility = View.GONE
        fab_notif?.visibility = View.GONE
        if(sharedPrefManager.isLoggedIn){
            diskusiViewModel.getDetailDiskusi(data?.id.toString(), sharedPrefManager.user.apiMobileToken!!,"","","").observe(this, Observer {
                if(it != null){
                    if(it.status == 200){
                        Log.d(TAG,"status diskusi ${it.status}")
                        notif_read?.visibility = if(data?.is_read == 1) View.GONE else View.VISIBLE
                        fab_notif?.visibility = View.VISIBLE
                    }else{
                        notif_read?.visibility = View.GONE
                        fab_notif?.visibility = View.GONE
                    }
                }else{
                    notif_read?.visibility = View.GONE
                    fab_notif?.visibility = View.GONE
                }
            })
        }
    }

    private fun setStatus(id: Int,status: String) {
        showLoader()
        userViewModel.setStatus(id,sharedPrefManager.user.apiMobileToken,status).observe(this, Observer {
            hideLoader()
            if(it != null){
                if(it.status == 200){
                    Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }else{
                    Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }else{
                Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
            }
        })

    }

    private fun editArtikel(data: Artikel?) {
        if(data?.status == "publish"){
            val builder = AlertDialog.Builder(this)
            val dialog: AlertDialog = builder.setTitle("Yakin akan diubah?")
                .setMessage("Artikel sudah publish, jika artikel diubah dan disimpan, maka akan menjadi draft dan akan direview kembali")
                .setPositiveButton("Ubah", DialogInterface.OnClickListener { dialogInterface, i ->
                    goToUpdateData(data)
                })
                .setNegativeButton("Batal", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                }).create()
            dialog.show()
            val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positive.setTextColor(resources.getColor(R.color.dark))
            positive.setBackgroundColor(resources.getColor(R.color.white))
            val negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negative.setTextColor(resources.getColor(R.color.dark))
            negative.setBackgroundColor(resources.getColor(R.color.white))
        }else{
            goToUpdateData(data)
        }
    }

    private fun goToUpdateData(data: Artikel?) {
        val intent = Intent(this@DetailArtikelActivity, BuatEditArtikelActivity::class.java)
        intent.putExtra(EXTRA_DATA_PUBLISH,data)
        startActivityForResult(intent, REQUEST_EDIT)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_1x -> {
                tv_detail_artikel_isi?.textSize = resources.getDimension(R.dimen.font_small)
                btn_1x.setTextColor(resources.getColor(R.color.colorAccent))
                btn_2x.setTextColor(resources.getColor(R.color.white))
                btn_3x.setTextColor(resources.getColor(R.color.white))
            }
            R.id.btn_2x -> {
                tv_detail_artikel_isi?.textSize = resources.getDimension(R.dimen.font_medium)
                btn_1x.setTextColor(resources.getColor(R.color.white))
                btn_2x.setTextColor(resources.getColor(R.color.colorAccent))
                btn_3x.setTextColor(resources.getColor(R.color.white))
            }
            R.id.btn_3x -> {
                tv_detail_artikel_isi?.textSize = resources.getDimension(R.dimen.font_large)
                btn_1x.setTextColor(resources.getColor(R.color.white))
                btn_2x.setTextColor(resources.getColor(R.color.white))
                btn_3x.setTextColor(resources.getColor(R.color.colorAccent))
            }
        }
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        }else{
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_EDIT && resultCode == Activity.RESULT_OK){
            updateDataOnServer(data)
        }
        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                Log.d("APP_DEBUG", result.toString())
                if (resultCode == Activity.RESULT_OK) {
                    val resultUri = result.uri
                    Log.d("APP_DEBUG", resultUri.toString())
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(contentResolver, resultUri)
                    showLoader()
                    uploadImage(generateGambar(bitmap), bitmap)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                    Log.d("error crop ", error.toString())
                }
            }
        } catch (e: Exception) {
            Log.e("error", "Something went wrong" + e.message)
        }
    }

    private fun uploadImage(gambar: MultipartBody.Part?, bitmap: Bitmap?) {
        userViewModel.updateGambarArtikel(artikelId, sharedPrefManager.user.apiMobileToken,gambar).observe(this, Observer {
            hideLoader()
            if(it != null){
                if(it.status == 200){
                    img_detail_artikel.setImageBitmap(bitmap)
                }else{
                    Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }else{
                Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
            }
        })
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

    private fun generateGambar(bitmap: Bitmap?): MultipartBody.Part {
        val file = bitmap?.let { createTempFile(it) }
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val body =
            MultipartBody.Part.createFormData("gambar", file?.name, reqFile)
        return body
    }

    private fun updateDataOnServer(data: Intent?) {
        showLoader()
        val artikel = data?.getParcelableExtra<Artikel>(EXTRA_DATA_PUBLISH)
        val slug = artikel?.title?.replace("","-")
        artikel?.title?.let {
            artikel.content?.let { it1 ->
                artikel.kategori?.let { it2 ->
                    userViewModel.editArtikel(artikel.id.toString(), sharedPrefManager.user.apiMobileToken!!,
                        it, it1,
                        it2,slug!!,artikel.content!!).observe(this, Observer {
                        if(it != null){
                            if(it.status == 200){
                                Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                                hideLoader()
                                updateUi(artikel)
                            }else{
                                Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                            }
                        }else{
                            Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
        updateUi(artikel)
    }

    private fun hideLoader() {
        progress_bar?.visibility = View.GONE
    }
    private fun showLoader() {
        progress_bar?.visibility = View.VISIBLE
    }

}
