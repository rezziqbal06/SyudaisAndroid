package com.su.service.ui.diskusi

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.su.service.R
import com.su.service.model.diskusi.Detail.KomenItem
import com.su.service.utils.DateGenerator
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.activity_diskusi.*
import okhttp3.MediaType
import okhttp3.RequestBody
import kotlin.properties.Delegates

@Suppress("UNCHECKED_CAST")
class DiskusiActivity : AppCompatActivity() {
    private var is_baru: Boolean = false
    val TAG = DiskusiActivity::class.java.simpleName
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var diskusiViewModel: DiskusiViewModel
    private var isLoading by Delegates.notNull<Boolean>()
    private var page by Delegates.notNull<Int>()
    private var totalPage by Delegates.notNull<Int>()
    private lateinit var diskusiAdapter: DiskusiAdapter
    private var blogId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diskusi)
        page = 1
        totalPage = 1
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        diskusiViewModel = ViewModelProvider(this).get(DiskusiViewModel::class.java)
        blogId = intent.getStringExtra("extra_id")
        showLoading()
        initAdapter()
        getData()
        btn_kirim?.setOnClickListener {
            val pesan = edt_pesan?.text.toString()
            if(pesan.isNotEmpty()){
                kirimKomen(pesan)
                Log.d(TAG, "isBaru: $is_baru")
                if(is_baru){
                    tambahDiskusi(pesan)
                }else{
                    kirimKomenKeServer(pesan)
                }
            }
        }

        initAdapterClickListener()

    }

    private fun tambahDiskusi(pesan: String) {
        Log.d(TAG, "tambah diskusi")
        val komen = stringToRequestBody(pesan)
        val title = stringToRequestBody(pesan)
        val dBlogId = blogId?.let { stringToRequestBody(it) }
        diskusiViewModel.tambahDiskusi(sharedPrefManager.user.apiMobileToken!!,title, komen, dBlogId!!, null).observe(this, Observer {
            if(it != null){
                if(it.status == 200){
                    is_baru = false
                    diskusiAdapter.setStatusTerkirim()
                }else{
                    diskusiAdapter.setStatusTidakTerkirim()
                }
            }else{
                diskusiAdapter.setStatusTidakTerkirim()
            }
        })
    }

    private fun stringToRequestBody(s: String): RequestBody{
        return RequestBody.create(MediaType.parse("text/plain"), s)
    }

    private fun initAdapterClickListener() {
        diskusiAdapter.setOnItemClickListener(object : DiskusiAdapter.OnItemClickListener{
            override fun onItemClick(status: String, komen: String) {
                if(status == "tidak terkirim"){
                    kirimUlangDialog(komen)
                }
            }

        })
    }

    private fun getData() {
        blogId?.let {
            diskusiViewModel.getDetailDiskusi(it,sharedPrefManager.user.apiMobileToken!!,page.toString(),"12","").observe(this,
                Observer {
                    hideLoading()
                    if(it != null){
                        if(it.status == 200){
                            it.result?.diskusi?.komen?.let { it1 -> diskusiAdapter.updateMessages(it1) }
                            rv_diskusi?.adapter = diskusiAdapter
                            it.result?.diskusi?.komenCount?.let { it1 ->
                                diskusiViewModel.calculateTotalPage(
                                    it1
                                ).observe(this, Observer { result -> totalPage = result
                                })
                            }
                            rv_diskusi?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                                    super.onScrolled(recyclerView, dx, dy)
                                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                                    val itemCount = linearLayoutManager.itemCount
                                    val lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                                    val isLastVisible = itemCount.minus(1) == lastVisible
                                    if(!isLoading && isLastVisible && page < totalPage){
                                        page = page.plus(1)
                                        getNextPageData()
                                    }
                                }
                            })
                        }else if(it.status == 104){
                            this@DiskusiActivity.is_baru = true
                            it.message?.let { it1 -> Log.d(TAG, it1) }
                        }else{
                            it.message?.let { it1 -> Log.d(TAG, it1) }
                        }
                    }else{
                        //Snackbar.make(container,resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun getNextPageData(){
        blogId?.let {
            diskusiViewModel.getDetailDiskusi(it,sharedPrefManager.user.apiMobileToken!!,page.toString(),"12","").observe(this,
                Observer {
                    hideLoading()
                    if(it != null){
                        if(it.status == 200){
                            it.result?.diskusi?.komen?.let { it1 -> diskusiAdapter.appendMessages(it1) }
                            it.result?.diskusi?.komenCount?.let { it1 ->
                                diskusiViewModel.calculateTotalPage(
                                    it1
                                ).observe(this, Observer { result -> totalPage = result
                                })
                            }
                            rv_diskusi?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                                    super.onScrolled(recyclerView, dx, dy)
                                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                                    val itemCount = linearLayoutManager.itemCount
                                    val lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                                    val isLastVisible = itemCount.minus(1) == lastVisible
                                    if(!isLoading && isLastVisible && page < totalPage){
                                        page = page.plus(1)
                                        getNextPageData()
                                    }
                                }
                            })
                        }else{
                            it.message?.let { it1 -> Snackbar.make(container, it1, Snackbar.LENGTH_SHORT).show() }
                        }
                    }else{
                        //Snackbar.make(container,resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun initAdapter() {
        val linearLayout = LinearLayoutManager(this)
        linearLayout.stackFromEnd = true
        rv_diskusi?.layoutManager = linearLayout
        diskusiAdapter = DiskusiAdapter(sharedPrefManager.user.id!!, mutableListOf())
        rv_diskusi?.adapter = diskusiAdapter
    }

    private fun showLoading(){
        progress_bar?.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideLoading(){
        progress_bar?.visibility = View.GONE
        isLoading = false
    }

    private fun kirimKomen(pesan: String){
        edt_pesan?.setText("")
        Log.d(TAG, "KirimKomen")
        val komen = KomenItem()
        komen.cdate = DateGenerator.getCurrentDate()
        komen.bUserPengirimId = sharedPrefManager.user.id
        komen.pengirim = sharedPrefManager.user.fnama
        komen.komen = pesan
        diskusiAdapter.appendMessage(komen)
        scrollToBottom()
    }

    private fun kirimKomenKeServer(pesan: String){
        Log.d(TAG, "komen")
        val requestBodyKomen = RequestBody.create(MediaType.parse("multipart/form-data"),pesan)
        blogId?.let {
            diskusiViewModel.komen(it, sharedPrefManager.user.apiMobileToken!!,requestBodyKomen,null).observe(this, Observer {
                if(it != null){
                    if(it.status == 200){
                        diskusiAdapter.setStatusTerkirim()
                    }else{
                        diskusiAdapter.setStatusTidakTerkirim()
                    }
                }else{
                    diskusiAdapter.setStatusTidakTerkirim()
                }
            })
        }
    }

    private fun scrollToBottom() {
        rv_diskusi?.scrollToPosition(diskusiAdapter.itemCount - 1)
    }

    private fun kirimUlangDialog(komen: String){
        val builder = AlertDialog.Builder(this)
        val dialog: AlertDialog = builder.setTitle("Pesan tidak terkirim")
            .setMessage("pesan akan terhapus bila tidak dikirim ulang")
            .setPositiveButton("Kirim ulang", DialogInterface.OnClickListener { dialogInterface, i ->
                kirimKomenKeServer(komen)
                diskusiAdapter.setStatusBelumTerkirim()
            }).create()
        dialog.show()
        val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positive.setTextColor(resources.getColor(R.color.dark))
        positive.setBackgroundColor(resources.getColor(R.color.white))
    }
}
