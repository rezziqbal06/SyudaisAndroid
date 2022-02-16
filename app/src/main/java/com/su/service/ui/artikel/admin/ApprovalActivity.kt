package com.su.service.ui.artikel.admin

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
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.activity_approval.*
import kotlinx.android.synthetic.main.content_video.*
import kotlin.properties.Delegates

class ApprovalActivity : AppCompatActivity() {
    val TAG = ApprovalActivity::class.java.simpleName
    private lateinit var viewModel: ApprovalArtikelViewModel
    private lateinit var sharedPrefManager: SharedPrefManager
    private var isLoading by Delegates.notNull<Boolean>()
    private var page by Delegates.notNull<Int>()
    private var totalPage by Delegates.notNull<Int>()
    private var keywoard = ""
    private lateinit var adapter: ApprovalItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approval)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Persetujuan Artikel"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        hideLoader()
        viewModel = ViewModelProvider(this).get(ApprovalArtikelViewModel::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        page = 0
        totalPage = 0
        initAdapter()
    }

    private fun initAdapter() {
        showLoader()
        adapter = ApprovalItemAdapter(sharedPrefManager)
        rv_approval?.layoutManager = LinearLayoutManager(this)
        rv_approval?.adapter = adapter
        viewModel.getApproval(sharedPrefManager.user.apiMobileToken!!, page.toString(),"12",keywoard).observe(this, Observer {
            if(it != null){
                if(it.status == 200){
                    Log.d(TAG, "artikel size ${it.result?.artikelCount}")
                    hideLoader()
                    it.result?.artikel?.let { it1 -> adapter.setList(it1) }
                    it.result?.artikelCount?.let { it1 -> viewModel.calculateTotalPage(it1).observe(this, Observer {result->
                        totalPage = result
                    }) }
                    initListener()
                }else{
                    hideLoader()
                    Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }else{
                hideLoader()
                Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
            }
        })

    }

    private fun initListener() {
        rv_approval?.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val countItem = linearLayoutManager.itemCount
                val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
                if(!isLoading && isLastPosition && page < totalPage){
                    showLoader()
                    page = page.plus(1)
                    getNextPage()
                }
            }
        })
    }

    private fun getNextPage() {
        viewModel.getApproval(sharedPrefManager.user.apiMobileToken!!, page.toString(),"12",keywoard).observe(this, Observer {
            if(it != null){
                if(it.status == 200){
                    hideLoader()
                    it.result?.artikel?.let { it1 -> adapter.addListItem(it1) }
                    initListener()
                }else{
                    hideLoader()
                    Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }else{
                hideLoader()
                Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoader() {
        isLoading = true
        progress_bar?.visibility = View.VISIBLE
    }
    private fun hideLoader() {
        isLoading = false
        progress_bar?.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
