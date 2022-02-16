package com.su.service.ui.absen2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.su.service.R
import com.su.service.model.kegiatan.KegiatanItem
import com.su.service.ui.absen.AbsenViewModel
import com.su.service.ui.kegiatan.KegiatanViewModel
import com.su.service.utils.Constants
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.activity_absen2.*

class Absen2Activity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var viewModel: AbsenViewModel
    private lateinit var kegiatanViewModel: KegiatanViewModel
    private lateinit var sharedPrefManager: SharedPrefManager
    private var apisess: String? = null
    private var adapter: AbsensiAdapter? = null
    private var idKegiatan : Int? = null
    private var utypeKegiatan: String? = null
    private var kegiatans: List<KegiatanItem?>? = null
    private var listKegiatan = arrayOf("Pilih kegiatan")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AbsenViewModel::class.java)
        kegiatanViewModel = ViewModelProvider(this).get(KegiatanViewModel::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        setContentView(R.layout.activity_absen2)

        apisess = sharedPrefManager.user.apiMobileToken
        img_back.setOnClickListener {
            onBackPressed()
        }

        dd_kegiatan?.setLabel("Kegiatan")
        dd_kegiatan?.getSpinner()?.onItemSelectedListener = this

        getDataKegiatan()

        getData("", idKegiatan, utypeKegiatan)

        swipeRefresh.setOnRefreshListener {
            getDataKegiatan()
            getData("", idKegiatan, utypeKegiatan)
            swipeRefresh.isRefreshing = false
        }

        cv_search.setOnClickListener {
            sv_search_absen?.isIconified = false
        }

        sv_search_absen?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                getData(query, idKegiatan, utypeKegiatan)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Handler().postDelayed(Runnable {
                    getData(newText, idKegiatan, utypeKegiatan)
                },500)
                return true
            }

        })
    }

    private fun getDataKegiatan() {
        kegiatanViewModel.getData(Constants.APIKEY, apisess).observe(this, Observer {
            if(it != null){
                if(it.status == 200){
                    kegiatans = it.result?.kegiatan

                    for (kegiatan in this!!.kegiatans!!){
                        listKegiatan = kegiatan?.judul?.let { it1 -> append(listKegiatan, it1) }!!
                    }

                    setDataToDropdown(listKegiatan)
                }else{
                    Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }else{
                Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun setDataToDropdown(listKegiatan: Array<String>) {
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, listKegiatan)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dd_kegiatan?.setAdapter(aa)
    }

    fun append(arr: Array<String>, element: String): Array<String> {
        val list: MutableList<String> = arr.toMutableList()
        list.add(element)
        return list.toTypedArray()
    }

    private fun showLoading(){
        progress_bar?.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        progress_bar?.visibility = View.GONE
    }

    private fun getData(keyword: String?,id: Int?, utype: String?) {
        showLoading()
        id?.let {
            utype?.let { it1 ->
                viewModel.list(apisess, keyword, it, it1).observe(this, Observer {
                    hideLoading()
                    if(it != null){
                        if(it.status != 200){
                            Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                        }else{
                            tv_hadir?.text = it.result?.hadir.toString()
                            tv_berhalangan?.text = it.result?.berhalangan.toString()
                            rv_absen?.layoutManager = LinearLayoutManager(this)
                            rv_absen?.setHasFixedSize(true)
                            adapter = it.result?.santri?.let { it1 -> AbsensiAdapter(it1) }
                            rv_absen.adapter = adapter
                            adapter?.setOnItemClickListener(object : AbsensiAdapter.OnItemClickListener{
                                override fun onItemClick(
                                    position: Int,
                                    email: String?,
                                    view: View,
                                    utype: String
                                ) {
                                    if(utype == "hadir"){
                                        absen(position, email, utype, idKegiatan, utypeKegiatan)
                                    }else{
                                        val actionSheetDialog = ActionListDialogFragment()
                                        val bundle = Bundle()
                                        bundle.putString("email",email)
                                        bundle.putString("utype", utype)
                                        bundle.putInt("position", position)
                                        bundle.putString("utypeKegiatan", utypeKegiatan)
                                        idKegiatan?.let { it2 -> bundle.putInt("idKegiatan", it2) }
                                        actionSheetDialog.arguments = bundle
                                        actionSheetDialog.show(supportFragmentManager,"action")
                                    }
                                }

                            })
                        }
                    }else{
                        Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    fun absen(position: Int, email: String?, status: String?, idKegiatan: Int?, utypeKegiatan: String?) {
        showLoading()
        viewModel.setAbsen(email, apisess, status, idKegiatan, utypeKegiatan).observe(this, Observer {
            hideLoading()
            if(it != null){
                if(it.status != 200){
                    Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }else{
                    adapter?.updateItem(position, status, it.result?.absen?.jamMasuk)
                    tv_hadir?.text = it.result?.hadir.toString()
                    tv_berhalangan?.text = it.result?.berhalangan.toString()
                }
            }else{
                Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        if(position == 0){
            dd_kegiatan?.setError("Pilih kegiatan terlebih dahulu")
        }else{
            idKegiatan = kegiatans?.get(position-1)?.id
            utypeKegiatan = kegiatans?.get(position-1)?.utype
            getData("",idKegiatan,utypeKegiatan)
        }
    }
}
