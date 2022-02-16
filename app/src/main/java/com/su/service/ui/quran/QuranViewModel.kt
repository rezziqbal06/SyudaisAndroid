package com.su.service.ui.quran

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.su.service.model.quran.Surat
import com.su.service.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuranViewModel : ViewModel(){
    val mutableLiveData =  MutableLiveData<ArrayList<Surat>>()
    fun getListSurat(context: Context): LiveData<ArrayList<Surat>>{
        var listSurat: ArrayList<Surat>? = null
            GlobalScope.launch(Dispatchers.Default) {
            val jsonFileString = Utils(context).getJsonDataFromAsset("list.json")
            val gson = Gson()
            val type = object: TypeToken<List<Surat>>() {}.type
            listSurat = gson.fromJson(jsonFileString, type)
        }
        mutableLiveData.value = listSurat
        return mutableLiveData
    }
}