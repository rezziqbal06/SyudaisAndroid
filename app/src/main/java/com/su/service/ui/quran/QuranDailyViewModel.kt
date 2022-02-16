package com.su.service.ui.quran

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.su.service.model.qddetail.QDDetailResponse
import com.su.service.model.qurandaily.QDResponse
import com.su.service.utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody

class QuranDailyViewModel : ViewModel(){
    private lateinit var data: MutableLiveData<QDResponse>
    private lateinit var dataDetail: MutableLiveData<QDDetailResponse>

    fun getAll(apisess: String?): LiveData<QDResponse>{
        val repository =  QuranDailyRepository()
        data = repository.getAll(Constants.APIKEY, apisess)!!
        return data
    }

    fun getActive(): LiveData<QDDetailResponse>{
        val repository = QuranDailyRepository();
        dataDetail = repository.getPicked(Constants.APIKEY)!!
        return dataDetail
    }

    fun tambah(apisess: String?, judul: String?, namaSurat: String?, noSurat: String?, noAyat: String?): LiveData<QDResponse>{
        val repository = QuranDailyRepository()
        return repository.tambah(Constants.APIKEY, apisess, judul, namaSurat, noSurat, noAyat)!!
    }

    fun edit(id: String?, apikey: String?, apisess: String?, judul: String?, namaSurat: String?, noSurat: String?, noAyat: String?): LiveData<QDResponse>{
        val repository = QuranDailyRepository()
        return repository.edit(id, apikey, apisess, judul, namaSurat, noSurat, noAyat)!!
    }

    fun hapus(id: String?, apikey: String?, apisess: String?): LiveData<QDResponse>{
        val repository = QuranDailyRepository()
        return repository.hapus(id,apikey, apisess)!!
    }

    fun pick(id: String, apikey: String, apisess: String?): LiveData<QDResponse>{
        val repository = QuranDailyRepository()
        return repository.pick(id,apikey, apisess)!!
    }

}