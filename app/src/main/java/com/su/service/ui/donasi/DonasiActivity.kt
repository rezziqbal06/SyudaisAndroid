package com.su.service.ui.donasi

import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_donasi.*
import kotlin.properties.Delegates

class DonasiActivity : AppCompatActivity() {
    private lateinit var viewModel: DonasiViewModel
    private lateinit var sharedPrefManager: SharedPrefManager
    private var isLoading by Delegates.notNull<Boolean>()
    private var page by Delegates.notNull<Int>()
    private var totalPage by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donasi)
        page = 0
        isLoading = true
        totalPage = 0
        hideLoading()
        viewModel = ViewModelProvider(this).get(DonasiViewModel::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Donasi"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(sharedPrefManager.isLoggedIn){
            if(sharedPrefManager.user.utype == "admin"){
                btn_tambah?.visibility = View.VISIBLE
                btn_tambah?.setOnClickListener {
                    val intent = Intent(this@DonasiActivity, BuatEditDonasiActivity::class.java)
                    startActivity(intent)
                }
            } else{
                btn_tambah?.visibility = View.GONE
            }
        }

        initData()
    }

    private fun hideLoading() {
        isLoading = false
        progress_bar?.visibility = View.GONE
    }

    private fun showLoading() {
        isLoading = true
        progress_bar?.visibility = View.VISIBLE
    }

    private fun initData() {
        showLoading()
        viewModel.getDonasi(page.toString(),"12","").observe(this, Observer {
            hideLoading()
            if(it != null){
                if(it.status == 200){
                    Log.d("DonasiActivity", it.result?.donasi.toString())
                    val adapter = it.result?.donasi?.let { it1 -> DonasiAdapter(it1) }
                    rv_donasi.layoutManager = LinearLayoutManager(this)
                    rv_donasi?.adapter = adapter
                    it.result?.donasiCount?.let { it1 -> viewModel.calculateTotalPage(it1).observe(this, Observer { result ->
                        totalPage = result
                    }) }

                    rv_donasi?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                            val countItem = linearLayoutManager.itemCount
                            val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                            val isLastPosition = countItem.minus(1) == lastVisiblePosition
                            if(!isLoading && isLastPosition && page < totalPage){
                                showLoading()
                                page = page.plus(1)
                                nextPage(adapter)
                            }
                        }
                    })
                }else{
                    Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }else{
                Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun nextPage(adapter: DonasiAdapter?) {
        showLoading()
        viewModel.getDonasi(page.toString(),"12","").observe(this, Observer {
            hideLoading()
            if(it != null){
                if(it.status == 200){
                    it.result?.donasi?.let { it1 -> adapter?.addListItem(it1) }
                }else{
                    Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }else{
                Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }
}
