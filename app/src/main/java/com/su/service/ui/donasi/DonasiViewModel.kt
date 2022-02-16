package com.su.service.ui.donasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.su.service.model.detaildonasi.DetailDonasiResponse
import com.su.service.model.donasi.DonasiResponse
import com.su.service.model.pelanggan.PelangganResponse
import com.su.service.model.update.UpdateResponse
import com.su.service.utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DonasiViewModel : ViewModel(){
    private lateinit var data: MutableLiveData<DonasiResponse>
    private lateinit var dataDetail: MutableLiveData<DetailDonasiResponse>

    fun getDonasi(page: String, pagesize: String, keyword: String): LiveData<DonasiResponse>{
        val repository =  DonasiRepository()
        data = repository.getDonasi(Constants.APIKEY, page, pagesize, keyword)!!
        return data
    }

    fun detailDonasi(id: String): LiveData<DetailDonasiResponse>{
        val repository = DonasiRepository()
        return repository.getDetailDonasi(id, Constants.APIKEY)!!
    }

    fun postDonasi(apikey: String, apisess: String?, gambar1: MultipartBody.Part?,
                   gambar2: MultipartBody.Part?,
                   gambar3: MultipartBody.Part?, isi: Map<String, RequestBody>?): LiveData<DonasiResponse>{
        val repository = DonasiRepository()
        return repository.postDonasi(apikey, apisess, gambar1, gambar2, gambar3, isi)!!
    }

    fun editDonasi(id: String, apikey: String, apisess: String?, isi: Map<String, RequestBody>?): LiveData<DonasiResponse>{
        val repository = DonasiRepository()
        return repository.editDonasi(id,apikey, apisess, isi)!!
    }

    fun hapusDonasi(id: String?, apikey: String?, apisess: String?): LiveData<DonasiResponse>{
        val repository = DonasiRepository()
        return repository.hapusDonasi(id, apikey, apisess)!!
    }

    fun updateGambar(donasiId: String?, id: String?, apikey: String?, apisess: String?, gambar: MultipartBody.Part?): LiveData<DonasiResponse>{
        val repository = DonasiRepository()
        return repository.updateGambarDonasi(donasiId, id, apikey, apisess, gambar)!!
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