package com.su.service.ui.doa

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.su.service.Injection
import com.su.service.R
import com.su.service.model.doa.Doa
import com.su.service.ui.doadzikir.BuatDoaDzikirActivity
import com.su.service.ui.doadzikir.DoaDzikirViewModel
import com.su.service.ui.dzikir.DzikirAdapter
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.activity_doa.*
import kotlinx.android.synthetic.main.activity_doa.toolbar
import kotlinx.android.synthetic.main.content_doa.*

class DoaActivity : AppCompatActivity() {
    private var query = ""
    private lateinit var doaViewModel: DoaViewModel
    private val adapter = DoaAdapter()
    private lateinit var doaDzikirViewModel: DoaDzikirViewModel
    private val TAG = DoaActivity::class.java.simpleName
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doa)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        doaViewModel = ViewModelProviders.of(this,
             Injection.provideDoaViewModelFactory(this) ).get(DoaViewModel::class.java)
        doaDzikirViewModel = ViewModelProvider(this).get(DoaDzikirViewModel::class.java)
        initAdapter()

        if(sharedPrefManager.isLoggedIn){
            Log.d(TAG, "utype user: ${sharedPrefManager?.user?.utype}")
            if(sharedPrefManager.user.utype == "admin"){
                fab_doa?.visibility = View.VISIBLE
            }else{
                fab_doa?.visibility = View.GONE
            }
        }else{
            fab_doa?.visibility = View.GONE
        }

        initSwipeRefresh()

        sv_doa?.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, mv: MotionEvent?): Boolean {
                sv_doa.isIconified = false
                return true
            }
        })
        sv_doa?.setOnCloseListener(object: SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                sv_doa.isIconified = true
                initAdapter()
                return true
            }

        })
        sv_doa?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query?.length!! >= 3){
                    showLoader()
                    updateDataFromSearch(query)
                    this@DoaActivity.query = query
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText?.length!! >= 3){
                    showLoader()
                    updateDataFromSearch(query)
                    this@DoaActivity.query = newText
                }
                return true
            }

        })
        fab_doa?.setOnClickListener {
            val intent = Intent(this, BuatDoaDzikirActivity::class.java)
            intent.putExtra(BuatDoaDzikirActivity.EXTRA_TAG,"doa")
            startActivity(intent)
            finish()
        }
    }

    private fun initSwipeRefresh() {
        refresh_container?.setOnRefreshListener {
            refreshData()
            Handler().postDelayed(Runnable { refresh_container?.isRefreshing = false },300)
        }
    }

    private fun showLoader() {
        progressbar_doa?.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        progressbar_doa?.visibility = View.GONE
    }

    private fun updateDataFromSearch(query: String) {
        rv_doa?.scrollToPosition(0)
        doaViewModel.getQuery(query)
        adapter.submitList(null)
    }

    private fun initAdapter() {
        doaViewModel.getQuery("")
        adapter.setContext(this)
        rv_doa?.layoutManager = LinearLayoutManager(this)
        rv_doa?.adapter = adapter
        doaViewModel.getDoa.observe(this, Observer<PagedList<Doa>> {
            Log.d(TAG, "List Doa: ${it?.size}")
            hideLoader()
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
            Log.d(TAG, "item doa adapter "+ adapter.itemCount.toString())
        })
        doaViewModel.getDoaNetwork.observe(this, Observer {
            Log.d("ArtikelFragment", "Error : ${it}")
        })
    }

    private fun refreshData() {
        updateDataFromSearch("")
    }

    private fun showEmptyList(show: Boolean) {
        if(show){
            img_empty_doa?.visibility = View.VISIBLE
            rv_doa?.visibility = View.GONE
        }else{
            rv_doa?.visibility = View.VISIBLE
            img_empty_doa?.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if (!sv_doa.isIconified()) {
            sv_doa.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
