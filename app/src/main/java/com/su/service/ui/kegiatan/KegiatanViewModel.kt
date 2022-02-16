package com.su.service.ui.kegiatan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.su.service.data.source.local.SyudaisCache
import com.su.service.data.source.remote.DoaService
import com.su.service.data.source.remote.DzikirService
import com.su.service.data.source.remote.queryDoa
import com.su.service.data.source.remote.queryDzikir
import com.su.service.model.absen.AbsenResponse
import com.su.service.model.doa.Doa
import com.su.service.model.doa.DoaResult
import com.su.service.model.dzikir.Dzikir
import com.su.service.model.dzikir.DzikirResult
import com.su.service.model.kegiatan.KegiatanResponse
import com.su.service.model.santri.SantriResponse

class KegiatanViewModel : ViewModel() {
   fun getData(apikey: String?, apisess: String?): LiveData<KegiatanResponse>{
       return KegiatanRepository().getKegiatan(apikey, apisess)!!
   }
}