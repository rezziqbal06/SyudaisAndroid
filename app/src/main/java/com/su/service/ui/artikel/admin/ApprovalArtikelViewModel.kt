package com.su.service.ui.artikel.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.su.service.model.artikel.ArtikelResponse

class ApprovalArtikelViewModel : ViewModel() {
    val repository =
        ApprovalArtikelRepository()
    fun getApproval(apisess: String,page:String, pagesize: String, keywoard: String):LiveData<ArtikelResponse>{
        return repository.getApprovalArtikel(apisess,page, pagesize,keywoard)
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