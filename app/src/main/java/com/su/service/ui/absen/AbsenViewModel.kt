package com.su.service.ui.absen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.su.service.model.absen.AbsenResponse
import com.su.service.model.santri.SantriResponse

class AbsenViewModel : ViewModel() {
   fun ngabsen(email: String?, apisess: String?): LiveData<AbsenResponse>{
       return AbsenRepository().ngabsen(email, apisess)!!
   }
    fun setAbsen(email: String?, apisess: String?, status: String?, idKegiatan: Int?, utypeKegiatan: String?): LiveData<AbsenResponse>{
        return AbsenRepository().setAbsen(email, apisess, status, idKegiatan, utypeKegiatan)!!
    }
    fun list(apisess: String?, keyword: String?, id: Int, utype: String): LiveData<SantriResponse>{
        return AbsenRepository().list(apisess, keyword, id, utype)!!
    }
}