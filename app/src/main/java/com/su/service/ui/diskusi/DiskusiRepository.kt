package com.su.service.ui.diskusi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.su.service.data.source.remote.Api
import com.su.service.model.diskusi.Detail.DetailDiskusiResponse
import com.su.service.model.diskusi.DiskusiResponse
import com.su.service.model.dzikir.DzikirResponse
import com.su.service.utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart

class DiskusiRepository {
    val data = MutableLiveData<DiskusiResponse>()
    val detail = MutableLiveData<DetailDiskusiResponse>()
    val TAG = DiskusiRepository::class.java.simpleName
    fun getDiskusi(apisess:String, page:String, pagesize: String, keyword: String ) : LiveData<DiskusiResponse> {
        Api.service.getDiskusi(Constants.APIKEY, apisess,page, pagesize, keyword).enqueue(object:
            Callback<DiskusiResponse> {
            override fun onFailure(call: Call<DiskusiResponse>, t: Throwable) {
                Log.d(TAG, call.request().toString())
                data.value = null
            }

            override fun onResponse(call: Call<DiskusiResponse>, response: Response<DiskusiResponse>) {
                Log.d(TAG, call.request().toString())
                if(response.isSuccessful){
                    data.value = response.body()
                }else{
                    data.value = null
                }
            }

        })
        return data
    }

    fun getDetail(blogId:String, apisess:String, page:String, pagesize: String, keyword: String ) : LiveData<DetailDiskusiResponse> {
        Api.service.getDetailDiskusi(blogId,Constants.APIKEY, apisess,page, pagesize, keyword).enqueue(object:
            Callback<DetailDiskusiResponse> {
            override fun onFailure(call: Call<DetailDiskusiResponse>, t: Throwable) {
                Log.d(TAG, call.request().toString())
                detail.value = null
            }

            override fun onResponse(call: Call<DetailDiskusiResponse>, response: Response<DetailDiskusiResponse>) {
                Log.d(TAG, call.request().toString())
                if(response.isSuccessful){
                    detail.value = response.body()
                }else{
                    detail.value = null
                }
            }

        })
        return detail
    }

    fun tambahDiskusi(apisess:String,title: RequestBody, dBlogId:RequestBody,komen:RequestBody,gambar:MultipartBody.Part? ) : LiveData<DetailDiskusiResponse> {
        Api.service.tambahDiskusi(Constants.APIKEY,apisess,title,dBlogId,komen,gambar).enqueue(object:
            Callback<DetailDiskusiResponse> {
            override fun onFailure(call: Call<DetailDiskusiResponse>, t: Throwable) {
                Log.d(TAG, call.request().toString())
                detail.value = null
            }

            override fun onResponse(call: Call<DetailDiskusiResponse>, response: Response<DetailDiskusiResponse>) {
                Log.d(TAG, call.request().toString())
                if(response.isSuccessful){
                    detail.value = response.body()
                }else{
                    detail.value = null
                }
            }

        })
        return detail
    }

    fun komen(id:String, apisess:String,komen:RequestBody,gambar:MultipartBody.Part? ) : LiveData<DetailDiskusiResponse> {
        Api.service.komen(id,Constants.APIKEY,apisess,komen,gambar).enqueue(object:
            Callback<DetailDiskusiResponse> {
            override fun onFailure(call: Call<DetailDiskusiResponse>, t: Throwable) {
                Log.d(TAG, call.request().toString())
                detail.value = null
            }

            override fun onResponse(call: Call<DetailDiskusiResponse>, response: Response<DetailDiskusiResponse>) {
                Log.d(TAG, call.request().toString())
                if(response.isSuccessful){
                    detail.value = response.body()
                }else{
                    detail.value = null
                }
            }

        })
        return detail
    }

}