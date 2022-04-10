package com.su.service.data.source.remote

import com.su.service.model.NonData
import com.su.service.model.absen.AbsenResponse
import com.su.service.model.artikel.ArtikelResponse
import com.su.service.model.beranda.BerandaResponse
import com.su.service.model.detaildonasi.DetailDonasiResponse
import com.su.service.model.diskusi.Detail.DetailDiskusiResponse
import com.su.service.model.diskusi.DiskusiResponse
import com.su.service.model.doa.DoaResponse
import com.su.service.model.donasi.DonasiResponse
import com.su.service.model.dzikir.DzikirResponse
import com.su.service.model.kategori.KategoriResponse
import com.su.service.model.kegiatan.KegiatanResponse
import com.su.service.model.pelanggan.PelangganResponse
import com.su.service.model.qddetail.QDDetailResponse
import com.su.service.model.qurandaily.QDResponse
import com.su.service.model.santri.SantriResponse
import com.su.service.model.update.UpdateResponse
import com.su.service.model.youtube.YoutubeResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService{
    @FormUrlEncoded
    @POST("user/login/")
    fun login(
        @Query("apikey") apikey: String?,
        @Field("username") email: String?,
        @Field("password") password: String?,
        @Field("device") device: String?,
        @Field("fcm_token") fcm_token: String?
    ): Call<PelangganResponse>

    @FormUrlEncoded
    @POST("user/daftar/")
    fun daftar(
        @Query("apikey") apikey: String?,
        @Query("nation_code") nation_code: String?,
        @Field("fnama") fnama: String?,
        @Field("password") password: String?,
        @Field("telp") telp: String?,
        @Field("email") email: String?,
        @Field("device") device: String?,
        @Field("fcm_token") fcm_token: String?
    ): Call<PelangganResponse>

    @FormUrlEncoded
    @POST("user/daftar/")
    fun daftarBySosmed(
        @Query("apikey") apikey: String?,
        @Field("fnama") nama: String?,
        @Field("google_id") google_id: String?,
        @Field("email") email: String?,
        @Field("gambar") gambar: String?,
        @Field("device") device: String?,
        @Field("fcm_token") fcm_token: String?
    ): Call<PelangganResponse>

    @FormUrlEncoded
    @POST("user/lupa/")
    fun lupa(
        @Query("apikey") apikey: String?,
        @Field("email") email: String?
    ): Call<PelangganResponse>

    @FormUrlEncoded
    @POST("user/reset/{token}/")
    fun resetPassword(
        @Path("token") token: String?,
        @Field("password") password: String?,
        @Field("repassword") repassword: String?
    ): Call<NonData>

    @GET("user/")
    fun getUser(
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess: String?
    ): Call<PelangganResponse>

    @FormUrlEncoded
    @POST("user/login_sosmed/")
    fun loginByGoogle(
        @Query("apikey") apikey: String?,
        @Field("google_id") googleId: String?,
        @Field("telp") telp: String?,
        @Field("email") email: String?,
        @Field("device") device: String?,
        @Field("fcm_token") fcm_token: String?
    ): Call<PelangganResponse>

    @FormUrlEncoded
    @POST("user/edit_password/")
    fun editPassword(@Query("apikey") apikey: String?,
                  @Query("apisess") apisess: String?,
                     @Field("oldpassword") oldpassword: String?,
                     @Field("newpassword") newpassword: String?,
                     @Field("confirm_newpassword") confirmpassword: String?) : Call<PelangganResponse>

    @FormUrlEncoded
    @POST("user/update_fcm/")
    fun updateFcm(@Query("apikey") apikey: String,
                  @Query("apisess") apisess: String?,
                  @Field("device") device: String,
                  @Field("token") token: String) : Call<PelangganResponse>


    @GET("beranda/")
    fun beranda(@Query("apikey") apikey: String?) : Call<BerandaResponse>

    @GET("artikel/")
    fun getArtikel(@Query("apikey") apikey: String?,
                   @Query("kategori") kategori:String?,
                   @Query("page") page:String?,
                   @Query("pagesize") pagesize:String?,
                   @Query("keyword") keyword:String?
                   ) : Call<ArtikelResponse>

    @GET("artikel/populer/")
    fun getPopulerArtikel(@Query("apikey") apikey: String?,
                   @Query("kategori") kategori:String?,
                   @Query("page") page:String?,
                   @Query("pagesize") pagesize:String?,
                   @Query("keyword") keyword:String?
    ) : Call<ArtikelResponse>

    @GET("artikel/user/")
    fun getByUser(@Query("apikey") apikey: String?,
                  @Query("apisess") apisess:String?
    ) : Call<ArtikelResponse>

    @GET("artikel/approval/")
    fun getApprovalArtikel(@Query("apikey") apikey: String?,
                    @Query("apisess") apisess:String?,
                   @Query("page") page:String?,
                   @Query("pagesize") pagesize:String?,
                   @Query("keyword") keyword:String?
    ) : Call<ArtikelResponse>

    @GET("artikel/kategori/")
    fun getKategoriArtikel(@Query("apikey") apikey: String?,
                          @Query("kategori") kategori:String?,
                          @Query("page") page:String?,
                          @Query("pagesize") pagesize:String?,
                          @Query("keyword") keyword:String?
    ) : Call<KategoriResponse>

    @FormUrlEncoded
    @POST("artikel/upload/")
    fun uploadArtikel(@Query("apikey") apikey: String?,
                      @Query("apisess") apisess: String?,
                      @Field("title") title: String?,
                      @Field("content") content: String?,
                      @Field("kategori") kategori: String?,
                      @Field("slug") slug: String?,
                      @Field("mdescription") mdescription: String?
    ) : Call<ArtikelResponse>

    @FormUrlEncoded
    @POST("artikel/edit/{id}")
    fun editArtikel(@Path("id") id: String?,
                    @Query("apikey") apikey: String?,
                    @Query("apisess") apisess: String?,
                    @Field("title") title: String?,
                    @Field("content") content: String?,
                    @Field("kategori") kategori: String?,
                    @Field("slug") slug: String?,
                    @Field("mdescription") mdescription: String?
    ) : Call<ArtikelResponse>

    @GET("artikel/hapus/{id}")
    fun hapusArtikel(@Path("id") id: String?,
                    @Query("apikey") apikey: String?,
                    @Query("apisess") apisess: String?
    ) : Call<ArtikelResponse>

    @JvmSuppressWildcards
    @Multipart
    @POST("artikel/updateGambar/{id}/")
    fun updateGambarArtikel(
        @Path("id") id: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess:String?,
        @Part gambar: MultipartBody.Part?
    ) : Call<ArtikelResponse>

    @GET("artikel/get_read/{id}")
    fun bacaArtikel(@Path("id") id: String?,
                    @Query("apikey") apikey: String?,
                    @Query("apisess") apisess: String?
    ) : Call<ArtikelResponse>

    @FormUrlEncoded
    @POST("artikel/approve_to/{id}")
    fun approveStatus(@Path("id") id: String?,
                    @Query("apikey") apikey: String?,
                    @Query("apisess") apisess: String?,
                    @Field("status") status: String?
    ) : Call<ArtikelResponse>

    @GET("diskusi/")
    fun getDiskusi(@Query("apikey") apikey: String?,
                      @Query("apisess") apisess: String?,
                      @Query("page") page: String?,
                      @Query("pagesize") pagesize: String?,
                      @Query("keyword") keyword: String?
    ) : Call<DiskusiResponse>

    @GET("diskusi/detail/{id}/")
    fun getDetailDiskusi(@Path("id") id: String?,
                   @Query("apikey") apikey: String?,
                   @Query("apisess") apisess: String?,
                   @Query("page") page: String?,
                   @Query("pagesize") pagesize: String?,
                   @Query("keyword") keyword: String?
    ) : Call<DetailDiskusiResponse>

    @JvmSuppressWildcards
    @Multipart
    @POST("diskusi/tambah/")
    fun tambahDiskusi(@Query("apikey") apikey: String?,
                      @Query("apisess") apisess: String?,
                      @Part("title") title: RequestBody?,
                      @Part("d_blog_id") d_blog_id: RequestBody?,
                      @Part("komen") komen: RequestBody?,
                      @Part gambar: MultipartBody.Part?
    ) : Call<DetailDiskusiResponse>

    @JvmSuppressWildcards
    @Multipart
    @POST("diskusi/komen/{id}/")
    fun komen(@Path("id") id: String?,
              @Query("apikey") apikey: String?,
              @Query("apisess") apisess: String?,
              @Part("komen") komen: RequestBody?,
              @Part gambar: MultipartBody.Part?
    ) : Call<DetailDiskusiResponse>

    @GET("search?part=snippet,id&order=date")
    fun youtubeChannel(@Query("key") apiKey: String,
                       @Query("channelId") channelId: String,
                       @Query("maxResults") maxResult: String,
                       @Query("pageToken") pageToken: String?) : Call<YoutubeResponse>

    //Doa
    @JvmSuppressWildcards
    @Multipart
    @POST("doa/upload/")
    fun uploadDoa(
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess:String?,
        @Part gambar: MultipartBody.Part?,
        @PartMap(encoding = "utf-8") isi: Map<String,RequestBody>?
    ) : Call<DoaResponse>

    @JvmSuppressWildcards
    @Multipart
    @POST("doa/edit/{id}/")
    fun editDoa(
        @Path("id") id: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess:String?,
        @Part gambar: MultipartBody.Part?,
        @PartMap(encoding = "utf-8") isi: Map<String,RequestBody>?
    ) : Call<DoaResponse>

    @GET("doa/hapus/{id}/")
    fun hapusDoa(
        @Path("id") id: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess:String?
    ) : Call<DoaResponse>

    //Dzikir
    @JvmSuppressWildcards
    @Multipart
    @POST("dzikir/upload/")
    fun uploadDzikir(
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess:String?,
        @Part gambar: MultipartBody.Part?,
        @PartMap isi: Map<String,RequestBody>?
    ) : Call<DzikirResponse>

    @JvmSuppressWildcards
    @Multipart
    @POST("dzikir/edit/{id}/")
    fun editDzikir(
        @Path("id") id: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess:String?,
        @Part gambar: MultipartBody.Part?,
        @PartMap isi: Map<String,RequestBody>?
    ) : Call<DzikirResponse>

    @GET("dzikir/hapus/{id}/")
    fun hapusDzikir(
        @Path("id") id: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess:String?
    ) : Call<DzikirResponse>

    //kegiatan
    @GET("kegiatan/general/")
    fun kegiatanGeneral(
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess:String?
    ) : Call<KegiatanResponse>

    @JvmSuppressWildcards
    @Multipart
    @POST("kegiatan/tambah/")
    fun tambahKegiatan(
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess:String?,
        @Part gambar: MultipartBody.Part?,
        @PartMap isi: Map<String,RequestBody>?
    ) : Call<NonData>

    @JvmSuppressWildcards
    @Multipart
    @POST("kegiatan/edit/{id}/")
    fun editKegiatan(
        @Path("id") id: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess:String?,
        @Part gambar: MultipartBody.Part?,
        @PartMap isi: Map<String,RequestBody>?
    ) : Call<NonData>

    @GET("kegiatan/hapus/{id}/")
    fun hapusKegiatan(
        @Path("id") id: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess:String?
    ) : Call<NonData>

    @JvmSuppressWildcards
    @Multipart
    @POST("kajian/tambah/")
    fun tambahKajian(
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess:String?,
        @Part gambar: MultipartBody.Part?,
        @PartMap isi: Map<String,RequestBody>?
    ) : Call<NonData>

    @JvmSuppressWildcards
    @Multipart
    @POST("kajian/edit/{id}/")
    fun editKajian(
        @Path("id") id: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess:String?,
        @Part gambar: MultipartBody.Part?,
        @PartMap isi: Map<String,RequestBody>?
    ) : Call<NonData>

    @GET("kajian/hapus/{id}/")
    fun hapusKajian(
        @Path("id") id: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess:String?
    ) : Call<NonData>

    //check update
    @FormUrlEncoded
    @POST("update/")
    fun checkUpdate(
        @Query("apikey") apikey: String?,
        @Field("version_name") versionName: String?
    ): Call<UpdateResponse>?

    //donasi

    @GET("donasi/")
    fun getDonasi(@Query("apikey") apikey: String?,
                   @Query("page") page:String?,
                   @Query("pagesize") pagesize:String?,
                   @Query("keyword") keyword:String?
    ) : Call<DonasiResponse>

    @GET("donasi/detail/{id}/")
    fun detailDonasi(@Path("id") id: String?,
                     @Query("apikey") apikey: String?): Call<DetailDonasiResponse>

    @JvmSuppressWildcards
    @Multipart
    @POST("donasi/upload/")
    fun postDonasi(
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess: String?,
        @Part gambar1: MultipartBody.Part?,
        @PartMap isi: Map<String,RequestBody>?
    ): Call<DonasiResponse>

    @JvmSuppressWildcards
    @Multipart
    @POST("donasi/edit/{id}/")
    fun editDonasi(
        @Path("id") id: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess: String?,
        @PartMap isi: Map<String,RequestBody>?
    ): Call<DonasiResponse>

    @Multipart
    @POST("donasi/updateGambar/{donasi_id}/{id}/")
    fun updateGambarDonasi(
        @Path("donasi_id") donasi_id: String?,
        @Path("id") id: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess: String?,
        @Part gambar: MultipartBody.Part?
    ): Call<DonasiResponse>


    @GET("donasi/hapus/{id}/")
    fun hapusDonasi(
        @Path("id") id: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess: String?
    ): Call<DonasiResponse>

    //absen
    @GET("absensi/ngabsen/{email}/")
    fun ngabsen(
        @Path("email") email: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess: String?
    ): Call<AbsenResponse>

    @FormUrlEncoded
    @POST("absensi/ngabsen/{email}/")
    fun setAbsen(
        @Path("email") email: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess: String?,
        @Field("sia") status: String?,
        @Field("id_kegiatan") idKegiatan: Int?,
        @Field("utype_kegiatan") utypeKegiatan: String?
    ): Call<AbsenResponse>

    @GET("absensi/")
    fun absensi(
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess: String?,
        @Query("keyword") keyword: String?,
        @Query("id") id: Int,
        @Query("utype") utype: String
    ): Call<SantriResponse>

    //qurandaily
    @GET("qurandaily/")
    fun getQuranDaily(
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess: String?
    ): Call<QDResponse>

    @GET("qurandaily/getPicked/")
    fun getQDPicked(
        @Query("apikey") apikey: String?
    ): Call<QDDetailResponse>

    @FormUrlEncoded
    @POST("qurandaily/tambah/")
    fun tambahQD(
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess: String?,
        @Field("judul") judul: String?,
        @Field("nama_surat") namaSurat: String?,
        @Field("no_surat") noSurat: String?,
        @Field("no_ayat") noAyat: String?
    ): Call<QDResponse>

    @FormUrlEncoded
    @POST("qurandaily/edit/{id}/")
    fun editQD(
        @Path("id") id: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess: String?,
        @Field("judul") judul: String?,
        @Field("nama_surat") namaSurat: String?,
        @Field("no_surat") noSurat: String?,
        @Field("no_ayat") noAyat: String?
    ): Call<QDResponse>

    @GET("qurandaily/hapus/{id}/")
    fun hapusQD(
        @Path("id") id: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess: String?
    ): Call<QDResponse>

    @GET("qurandaily/pick_this/{id}/")
    fun pickQD(
        @Path("id") id: String?,
        @Query("apikey") apikey: String?,
        @Query("apisess") apisess: String?
    ): Call<QDResponse>

}