package com.su.service.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.su.service.model.kategori.KategoriItem
import com.su.service.model.pelanggan.Data
import com.su.service.model.pelanggan.Pelanggan
import com.su.service.model.qddetail.Qurandaily
import com.su.service.model.quran.Ayat
import com.su.service.model.quran.Bookmark
import com.su.service.model.qurandaily.QurandailyItem


/**
 * Created by Belal on 9/5/2017.
 */
//here for this class we are using a singleton pattern
class SharedPrefManager(private val mCtx: Context) {
    // set
    fun userLogin(data: Data) {
        val sharedPreferences =
            mCtx.getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE
            )
        val editor = sharedPreferences.edit()
        val user: Pelanggan = data.pelanggan!!
        Log.d("SharedPrefManager", "save session: ${user.email}")
        editor.putString(KEY_GAMBAR, user.image)
        editor.putString(KEY_GOOGLE_ID, user.googleId)
        editor.putString(KEY_TELEPON, user.telp)
        editor.putString(KEY_IS_ACTIVE, user.isActive)
        editor.putString(KEY_IS_CONFIRM, user.isConfirmed)
        editor.putString(KEY_KELAMIN, user.kelamin)
        editor.putString(KEY_APISESS, user.apiMobileToken)
        editor.putString(KEY_FB_ID, user.fbId)
        editor.putString(KEY_LNAMA, user.lnama)
        editor.putString(KEY_FCM_TOKEN, user.fcmToken)
        editor.putString(KEY_ID, user.id)
        editor.putString(KEY_IS_AGREE, user.isAgree)
        editor.putString(KEY_FNAMA, user.fnama)
        editor.putString(KEY_DEVICE, user.device)
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_UTYPE, user.utype)
        editor.apply()
    }

    //this method will checker whether user is already logged in or not
    val isLoggedIn: Boolean
        get() {
            val sharedPreferences =
                mCtx.getSharedPreferences(
                    SHARED_PREF_NAME,
                    Context.MODE_PRIVATE
                )
            return sharedPreferences.getString(KEY_APISESS, null) != null
        }

    fun setKategori(item: ArrayList<KategoriItem?>?){
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(item)
        editor.putString(KEY_KATEGORI, json)
        editor.apply()
    }

    fun setBookmark(item: Bookmark){
        val sharedPreferences =
            mCtx.getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE
            )
        val editor = sharedPreferences.edit()
        editor.putString(KEY_AYAT_BOOK, item.ayat)
        editor.putString(KEY_NO_SURAT_BOOK, item.no_surat)
        editor.putString(KEY_SURAT_BOOK, item.surat)
        item.position?.let { editor.putInt(KEY_POSITION_AYAT_BOOK_BOOK, it) }
        editor.apply()
    }

    fun setQuranDaily(data: Qurandaily) {
        val sharedPreferences =
            mCtx.getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE
            )
        val editor = sharedPreferences.edit()
        editor.putString(KEY_QD_ID, data.id)
        editor.putString(KEY_JUDUL_QD, data.judul)
        editor.putString(KEY_NAMA_SURAT, data.namaSurat)
        editor.putString(KEY_NO_SURAT, data.noSurat)
        editor.putString(KEY_NO_AYAT, data.noAyat)
        editor.apply()
    }

    //get
    val listKategori: ArrayList<KategoriItem?>?
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString(KEY_KATEGORI, "")
            val type=
                object : TypeToken<ArrayList<KategoriItem?>?>() {}.type
            return gson.fromJson(json, type)
        }

    val user: Pelanggan
        get() {
            val sharedPreferences =
                mCtx.getSharedPreferences(
                    SHARED_PREF_NAME,
                    Context.MODE_PRIVATE
                )
            val pelanggan = Pelanggan()
            pelanggan.image = sharedPreferences.getString(KEY_GAMBAR, null)
            pelanggan.googleId = sharedPreferences.getString(KEY_GOOGLE_ID, null)
            pelanggan.telp = sharedPreferences.getString(KEY_TELEPON, null)
            pelanggan.isActive = sharedPreferences.getString(KEY_IS_ACTIVE, null)
            pelanggan.isConfirmed = sharedPreferences.getString(KEY_IS_CONFIRM, null)
            pelanggan.kelamin = sharedPreferences.getString(KEY_KELAMIN, null)
            pelanggan.apiMobileToken = sharedPreferences.getString(KEY_APISESS, null)
            pelanggan.fbId = sharedPreferences.getString(KEY_FB_ID, null)
            pelanggan.lnama = sharedPreferences.getString(KEY_LNAMA, null)
            pelanggan.fcmToken = sharedPreferences.getString(KEY_FCM_TOKEN, null)
            pelanggan.id = sharedPreferences.getString(KEY_ID, null)
            pelanggan.isAgree = sharedPreferences.getString(KEY_IS_AGREE, null)
            pelanggan.fnama = sharedPreferences.getString(KEY_FNAMA, null)
            pelanggan.device = sharedPreferences.getString(KEY_DEVICE, null)
            pelanggan.email = sharedPreferences.getString(KEY_EMAIL, null)
            pelanggan.utype = sharedPreferences.getString(KEY_UTYPE, null)
            pelanggan.poin = sharedPreferences.getInt(KEY_POIN, 0)

            return pelanggan
        }

    val quranDaily: Qurandaily?
        get() {
            val sharedPreferences =
                mCtx.getSharedPreferences(
                    SHARED_PREF_NAME,
                    Context.MODE_PRIVATE
                )
            val qd = Qurandaily()
            qd.id = sharedPreferences.getString(KEY_QD_ID, null)
            qd.judul = sharedPreferences.getString(KEY_JUDUL_QD, null)
            qd.namaSurat = sharedPreferences.getString(KEY_NAMA_SURAT, null)
            qd.noSurat = sharedPreferences.getString(KEY_NO_SURAT, null)
            qd.noAyat = sharedPreferences.getString(KEY_NO_AYAT, null)
            qd.isPick = sharedPreferences.getString(KEY_IS_PICK, null)
            return qd
        }

    val getBookmark: Bookmark?
        get() {
            val sharedPreferences =
                mCtx.getSharedPreferences(
                    SHARED_PREF_NAME,
                    Context.MODE_PRIVATE
                )
            val item = Bookmark()
            item.no_surat = sharedPreferences.getString(KEY_NO_SURAT_BOOK, null)
            item.surat = sharedPreferences.getString(KEY_SURAT_BOOK, null)
            item.ayat = sharedPreferences.getString(KEY_AYAT_BOOK, null)
            item.position = sharedPreferences.getInt(KEY_POSITION_AYAT_BOOK_BOOK,0)
            return item
        }

    //this method will logout the user
    fun logout() {
        val sharedPreferences =
            mCtx.getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE
            )
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        //mCtx.startActivity(Intent(mCtx, MainActivity::class.java))
    }

    companion object {
        //the constants
        private const val SHARED_PREF_NAME = "customgrosir"
        private const val KEY_NAMA = "keynama"
        private const val KEY_USERNAME = "keyusername"
        private const val KEY_ID = "keyid"
        private const val KEY_APISESS = "apissess"
        private const val KEY_NOMOR = "keynomor"
        private const val KEY_ICON = "keyicon"
        private const val KEY_ALAMAT = "keyalamat"
        private const val KEY_KELURAHAN = "keykelurahan"
        private const val KEY_KECAMATAN = "keykecamatan"
        private const val KEY_KABKOTA = "keykabkota"
        private const val KEY_PROVINSI = "keyprovinsi"
        private const val KEY_UTYPE = "utype"
        private const val KEY_GAMBAR = "gambar"
        private const val KEY_GOOGLE_ID = "google_id"
        private const val KEY_API_SOCIAL_ID = "apisocialid"
        private const val KEY_TELEPON = "telepon"
        private const val KEY_IS_ACTIVE = "isactive"
        private const val KEY_IS_CONFIRM = "isconfirm"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_NATION_CODE = "nationcode"
        private const val KEY_KELAMIN = "kelamin"
        private const val KEY_FB_ID = "fbid"
        private const val KEY_LNAMA = "lnama"
        private const val KEY_FCM_TOKEN = "fcmtoken"
        private const val KEY_INTRO_TEKS = "introteks"
        private const val KEY_IS_AGREE = "isagree"
        private const val KEY_FNAMA = "fnama"
        private const val KEY_DEVICE = "device"
        private const val KEY_EMAIL = "email"
        private const val KEY_LONGITUDE = "longitude"
        private const val KEY_POIN = "poin"

        //kategori
        private const val KEY_KATEGORI = "kategori"
        private const val KEY_KATEGORI_NAMA = "kategoriNama"
        private const val KEY_KATEGORI_ID = "kategoriId"
        private const val KEY_KATEGORI_IS_ACTIVE = "kategoriIsactive"
        private const val KEY_KATEGORI_IS_VISIBLE = "kategoriIsVisible"
        private const val KEY_KATEGORI_UTYPE = "kategoriUtype"

        //qurandaily
        private const val KEY_QD_ID = "QDId"
        private const val KEY_JUDUL_QD = "judulQD"
        private const val KEY_NO_SURAT = "noSurat"
        private const val KEY_NAMA_SURAT = "namaSurat"
        private const val KEY_NO_AYAT = "noAyat"
        private const val KEY_IS_PICK = "isPick"

        private const val KEY_AYAT_BOOK = "ayat_book"
        private const val KEY_NO_SURAT_BOOK = "no_surat_book"
        private const val KEY_SURAT_BOOK = "surat_book"
        private const val KEY_POSITION_AYAT_BOOK_BOOK = "position_ayat_book"


        private var mInstance: SharedPrefManager? = null

        @Synchronized
        fun getInstance(context: Context): SharedPrefManager? {
            if (mInstance == null) {
                mInstance = SharedPrefManager(context)
            }
            return mInstance
        }
    }

}