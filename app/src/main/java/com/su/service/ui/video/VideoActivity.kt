package com.su.service.ui.video

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.su.service.R
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.content_video.*
import kotlin.properties.Delegates


class VideoActivity : AppCompatActivity(){
    private lateinit var viewModel: VideoViewModel
    private var isLoading by Delegates.notNull<Boolean>()
    private var page by Delegates.notNull<Int>()
    private var totalPage by Delegates.notNull<Int>()
    private var totalResult:Int? = 0
    private var itemResult:Int? = 0
    private lateinit var adapter: SyudaisVideoAdapter
    var token: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        btn_back.setOnClickListener {
            onBackPressed()
        }

        webView.loadUrl("https://www.youtube.com/channel/UCzvk1s89CiEK7BOceA6X9ng")
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        swipeRefresh.setOnRefreshListener {
            webView.loadUrl("https://www.youtube.com/channel/UCzvk1s89CiEK7BOceA6X9ng")
            swipeRefresh.isRefreshing = false
        }

        //setSupportActionBar(toolbar)
        //supportActionBar?.title = " "

        /*viewModel = ViewModelProviders.of(this).get(VideoViewModel::class.java)
        page = 1
        totalPage = 0
        showLoader()
        initAdapter()
        tv_load_more?.visibility = View.GONE
        getData()
        //initListener()
        tv_load_more?.setOnClickListener {
            tv_load_more?.visibility = View.GONE
            if(token?.length!! >= 6) getData()
        }*/


    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Check if the key event was the Back button and if there's history
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event)
    }

    /*private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(this)
        rv_video?.layoutManager = linearLayoutManager
        rv_video?.setHasFixedSize(true)
        rv_video?.itemAnimator = DefaultItemAnimator()
        adapter = SyudaisVideoAdapter(this)
        rv_video?.adapter = adapter
    }

    private fun showLoader() {
        isLoading = true
        progressbar_video.visibility = View.VISIBLE
    }
    private fun hideLoader() {
        isLoading = false
        progressbar_video.visibility = View.GONE
    }

    private fun getData() {
        showLoader()
        viewModel.getContext(this)
        viewModel.getData(token).observe(this, Observer {
            if(itemResult != 0){
                if(itemResult!! >= this.totalResult!!) tv_load_more?.visibility = View.GONE else tv_load_more?.visibility = View.VISIBLE
            }else{
                tv_load_more?.visibility = View.VISIBLE
            }
            if(it != null){
                hideLoader()
                Log.d("VideoActivity", it?.items?.size.toString())
                Log.d("VideoActivity", "token ${it?.nextPageToken}")
                it?.items?.let { it1 -> adapter.addListItem(it1) }
                this.token = it?.nextPageToken
                totalResult = it.pageInfo?.totalResults
                itemResult = it.pageInfo?.resultsPerPage?.let { it1 -> itemResult?.plus(it1) }
                viewModel.calculateTotalPage(it?.pageInfo?.totalResults.toString()).observe(this, Observer { result ->
                    totalPage = result
                })
            }else{
                tv_load_more?.visibility = View.VISIBLE
                hideLoader()
                Snackbar.make(container, "Gagal, coba beberapa saat lagi", Snackbar.LENGTH_SHORT).show()
            }

        })
    }

    private fun getNextPage(tokenPage: String?) {
        Log.d("VideoActivity", "token $tokenPage")
        viewModel.getData(tokenPage).observe(this, Observer {
            if(itemResult != 0){
                if(itemResult!! >= this.totalResult!!) tv_load_more?.visibility = View.GONE else tv_load_more?.visibility = View.VISIBLE
            }else{
                tv_load_more?.visibility = View.VISIBLE
            }
            if(it != null){
                hideLoader()
                Log.d("VideoActivity", it?.items?.size.toString())
                Log.d("VideoActivity", "token ${it?.nextPageToken}")
                it?.items?.let { it1 -> adapter.addListItem(it1) }
                this.token = it?.nextPageToken
            }else{
                tv_load_more?.visibility = View.VISIBLE
                hideLoader()
                Snackbar.make(container, "Gagal, coba beberapa saat lagi", Snackbar.LENGTH_SHORT).show()
            }

        })
    }*/


}
