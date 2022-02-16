package com.su.service.ui.artikel

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.su.service.Injection
import com.su.service.R
import com.su.service.data.source.remote.ArtikelService
import com.su.service.model.artikel.Artikel
import kotlinx.android.synthetic.main.activity_searching_artikel.*


class SearchingArtikelActivity : AppCompatActivity() {
    private var TAG = SearchingArtikelActivity::class.java.simpleName
    private lateinit var rvArtikelSearch : RecyclerView
    private lateinit var viewModel: ArtikelViewModel
    private lateinit var imgEmpty: ImageView
    private lateinit var progressBar: ProgressBar
    private var query = ""
    private var kategori = ""
    private var adapter = ArtikelAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searching_artikel)
        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this) ).get(ArtikelViewModel::class.java)
        adapter.setContext(this)
        sv_searh_artikel.isIconified = false

        setSupportActionBar(search_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rvArtikelSearch = findViewById(R.id.rv_artikel_search)
        imgEmpty = findViewById(R.id.img_empty_search_artikel)
        progressBar = findViewById(R.id.progressbar_search)
        initAdapter()

        sv_searh_artikel.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
                sv_searh_artikel.isIconified = false
                return true
            }

        })

        sv_searh_artikel.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query?.length!! >= 3){
                    showLoader()
                    updateDataFromSearch(query)
                    this@SearchingArtikelActivity.query = query
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if(query?.length!! >= 3){
                    showLoader()
                    updateDataFromSearch(query)
                    this@SearchingArtikelActivity.query = query
                }
                return true
            }

        })

        swipe_refresh_search_artikel.setOnRefreshListener {
            val handler = Handler()
            handler.postDelayed(Runnable {
                swipe_refresh_search_artikel.isRefreshing = false
                updateDataFromSearch(this@SearchingArtikelActivity.query)
            }, 500)
        }
    }

    private fun updateDataFromSearch(query: String) {
        rvArtikelSearch.scrollToPosition(0)
        viewModel.getQuery(query)
        adapter.submitList(null)
    }

    private fun initAdapter() {
        rvArtikelSearch.layoutManager = LinearLayoutManager(this)
        rvArtikelSearch.adapter = adapter
        viewModel.artikelSearch.observe(this, Observer<PagedList<Artikel>> {
            Log.d("ArtikelFragment", "List Artikel: ${it?.size}")
            hideLoader()
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
            Log.d(TAG, "item adapter "+ adapter.itemCount.toString())
        })
        viewModel.searchNetworkError.observe(this, Observer {
            Log.d("ArtikelFragment", "Error : ${it}")
        })
    }

    private fun showEmptyList(show: Boolean) {
        if(show){
            rvArtikelSearch.visibility = View.INVISIBLE
            imgEmpty.visibility = View.VISIBLE
        }else{
            rvArtikelSearch.visibility = View.VISIBLE
            imgEmpty.visibility = View.INVISIBLE
        }
    }

    private fun showLoader(){
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader(){
        progressBar.visibility = View.INVISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (!sv_searh_artikel.isIconified()) {
            sv_searh_artikel.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }
}
