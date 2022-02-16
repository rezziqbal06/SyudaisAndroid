package com.su.service.ui.diskusi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.su.service.model.diskusi.Detail.DetailDiskusiResponse
import com.su.service.model.diskusi.DiskusiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DiskusiViewModel : ViewModel(){
    val repository = DiskusiRepository()
    fun getDiskusi(apisess: String, page: String, pageSize:String, keyword:String): LiveData<DiskusiResponse>{
        return repository.getDiskusi(apisess,page,pageSize,keyword)
    }

    fun getDetailDiskusi(blogId:String, apisess: String,page:String, pagesize: String, keyword: String): LiveData<DetailDiskusiResponse>{
        return repository.getDetail(blogId, apisess, page, pagesize, keyword)
    }

    fun tambahDiskusi(apisess: String,title: RequestBody, dBlogId: RequestBody, komen: RequestBody, gambar: MultipartBody.Part?): LiveData<DetailDiskusiResponse>{
        return repository.tambahDiskusi(apisess, title, dBlogId, komen, gambar)
    }

    fun komen(id:String, apisess: String,komen: RequestBody, gambar: MultipartBody.Part?): LiveData<DetailDiskusiResponse>{
        return repository.komen(id,apisess, komen, gambar)
    }
    fun calculateTotalPage(produkCount: String): LiveData<Int>{
        val result = MutableLiveData<Int>()
        var hasilPembagian: Int = produkCount.toInt().div(12)
        val sisa: Int = produkCount.toInt().rem(12)
        if (hasilPembagian != 0) {
            if (sisa > 0) {
                hasilPembagian++
            }
        }
        result.setValue(hasilPembagian)
        return result
    }
}