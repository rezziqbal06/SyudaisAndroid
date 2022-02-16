package com.su.service.ui.kegiatan

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.su.service.R
import com.su.service.model.beranda.KajianItem
import com.su.service.model.beranda.KegiatanItem
import com.su.service.ui.detailgambar.ImageFullScreenActivity
import com.su.service.utils.DateGenerator
import com.su.service.utils.HtmlToStringGenerator
import kotlinx.android.synthetic.main.activity_detail_kegiatan.*
import kotlinx.android.synthetic.main.content_detail_kegiatan.*

class DetailKegiatanActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object{
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_TAG = "extra_tag"
    }

    private var latitude: Double? = null
    private var longitude: Double? = null
    private var namaTempat: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kegiatan)
        setSupportActionBar(toolbar)
        /*val map = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?*/
        fab.setOnClickListener { view ->
            Dexter.withActivity(this).withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (!report.areAllPermissionsGranted()) {
                        Snackbar.make(
                                view,
                                "Permission Denied",
                                Snackbar.LENGTH_SHORT
                            )
                            .setDuration(800)
                            .show()
                    } else {
                        getCurrentLocation(view)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                }
            }).check()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

        val tag = intent.getStringExtra(EXTRA_TAG)
        if(tag.equals("kajian")){
            val data = intent.getParcelableExtra<KajianItem>(EXTRA_DATA)
            tv_detail_judul?.text = data?.judul
            tv_detail_tempat?.text = data?.tempat
            tv_detail_tempat2?.text = data?.alamat
            val deskripsi = data?.deskripsi?.let { HtmlToStringGenerator.generate(it) }
            Log.d("isi deskripsi", deskripsi)
            tv_detail_deskripsi?.text = deskripsi
            val tglAwal = DateGenerator.getTanggal("yy-MM-dd HH:mm:ss","E dd MMMM yyyy",data?.sdate)
            val tglAkhir = DateGenerator.getTanggal("yy-MM-dd HH:mm:ss","E dd MMMM yyyy",data?.edate)
            if(tglAkhir.equals(tglAwal)){
                tv_detail_tanggal?.text = DateGenerator.getTanggal("yy-MM-dd HH:mm:ss","E dd MMMM yyyy",data?.sdate)
                val waktuAwal = DateGenerator.getTanggal("yy-MM-dd HH:mm:ss","HH:mm",data?.sdate)
                val waktuAkhir = DateGenerator.getTanggal("yy-MM-dd HH:mm:ss","HH:mm",data?.edate)
                tv_detail_tanggal2?.text = "$waktuAwal - $waktuAkhir"
            }else{
                tv_detail_tanggal?.text = DateGenerator.getTanggal("yy-MM-dd HH:mm:ss","E dd MMMM yyyy HH:mm",data?.sdate) + " s/d"
                tv_detail_tanggal2?.text = DateGenerator.getTanggal("yy-MM-dd HH:mm:ss","E dd MMMM yyyy HH:mm",data?.edate)
            }
            Glide.with(this)
                .load(data?.gambar)
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                .into(img_kegiatan_detail)
            latitude = data?.latitude?.toDouble()
            longitude = data?.longitude?.toDouble()
            namaTempat = data?.tempat
            if (latitude.toString() != "0.00000000000" || longitude.toString() != "0.00000000000") {
               // map?.getMapAsync(this)
            }else{
                fab.hide()
            }
            img_kegiatan_detail?.setOnClickListener {
                val intent = Intent(this, ImageFullScreenActivity::class.java)
                intent.putExtra(ImageFullScreenActivity.EXTRA_IMAGE, data?.gambar)
                intent.putExtra(ImageFullScreenActivity.EXTRA_TEXT, data?.judul)
                startActivity(intent)
            }
        }else{
            val data = intent.getParcelableExtra<KegiatanItem>(EXTRA_DATA)
            tv_detail_judul?.text = data?.judul
            tv_detail_tempat?.text = data?.tempat
            tv_detail_tempat2?.text = data?.alamat
            val deskripsi = data?.deskripsi?.let { HtmlToStringGenerator.generate(it) }
            tv_detail_deskripsi?.text = deskripsi
            val tglAwal = DateGenerator.getTanggal("yy-MM-dd HH:mm:ss","E dd MMMM yyyy",data?.sdate)
            val tglAkhir = DateGenerator.getTanggal("yy-MM-dd HH:mm:ss","E dd MMMM yyyy",data?.edate)
            if(tglAkhir.equals(tglAwal)){
                tv_detail_tanggal?.text = DateGenerator.getTanggal("yy-MM-dd HH:mm:ss","E dd MMMM yyyy",data?.sdate)
                val waktuAwal = DateGenerator.getTanggal("yy-MM-dd HH:mm:ss","HH:mm",data?.sdate)
                val waktuAkhir = DateGenerator.getTanggal("yy-MM-dd HH:mm:ss","HH:mm",data?.edate)
                tv_detail_tanggal2?.text = "$waktuAwal - $waktuAkhir"
            }else{
                tv_detail_tanggal?.text = DateGenerator.getTanggal("yy-MM-dd HH:mm:ss","E dd MMMM yyyy HH:mm",data?.sdate) + " s/d"
                tv_detail_tanggal2?.text = DateGenerator.getTanggal("yy-MM-dd HH:mm:ss","E dd MMMM yyyy HH:mm",data?.edate)
            }
            Glide.with(this)
                .load(data?.gambar)
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                .into(img_kegiatan_detail)
            latitude = data?.latitude?.toDouble()
            longitude = data?.longitude?.toDouble()
            namaTempat = data?.tempat
            if (latitude.toString() != "0.00000000000" || longitude.toString() != "0.00000000000") {
                //map?.getMapAsync(this)
            }else{
                fab.hide()
            }
            img_kegiatan_detail.setOnClickListener {
                val intent = Intent(this, ImageFullScreenActivity::class.java)
                intent.putExtra(ImageFullScreenActivity.EXTRA_IMAGE, data?.gambar)
                intent.putExtra(ImageFullScreenActivity.EXTRA_TEXT, data?.judul)
                startActivity(intent)
            }
        }


    }

    private fun getCurrentLocation(view: View) {

        // GET CURRENT LOCATION
        val mFusedLocation = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocation.lastLocation
            .addOnSuccessListener(
                this
            ) { location ->
                if (location != null) {
                    // Do it all with location
                    Log.d(
                        "My Current location",
                        "Lat : " + location.latitude + " Long : " + location.longitude
                    )
                    val latLng = location.latitude.toString() + "," + location.longitude
                    goToDirection(view, latLng)
                }
            }
    }

    private fun goToDirection(view: View, latLng: String) {
        val gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=$latLng&daddr=$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Snackbar.make(
                    view,
                    "Anda belum menginstal aplikasi Google Map",
                    Snackbar.LENGTH_SHORT
                )
                .setDuration(800)
                .show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val zoomLevel = 16.0f
        val location = latitude?.let { longitude?.let { it1 -> LatLng(it, it1) } }
        googleMap?.addMarker(location?.let { MarkerOptions().position(it).title(namaTempat) })
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))
    }
}
