package com.su.service.ui.dzikir

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.su.service.Injection
import com.su.service.R
import com.su.service.model.dzikir.Dzikir
import com.su.service.ui.doa.DoaActivity
import com.su.service.ui.doadzikir.BuatDoaDzikirActivity
import com.su.service.ui.doadzikir.DoaDzikirViewModel
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.activity_dzikir.*
import kotlinx.android.synthetic.main.activity_dzikir.toolbar
import kotlinx.android.synthetic.main.content_dzikir.*

class DzikirActivity : AppCompatActivity() {

    private var query = ""
    private lateinit var viewModel: DzikirViewModel
    private lateinit var doaDzikirViewModel: DoaDzikirViewModel
    private val adapter = DzikirAdapter()
    private val TAG = DzikirActivity::class.java.simpleName
    private lateinit var sharedPrefManager: SharedPrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dzikir)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        fab_dzikir.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        viewModel = ViewModelProviders.of(this,
            Injection.provideDzikirViewModelFactory(this) ).get(DzikirViewModel::class.java)
        doaDzikirViewModel = ViewModelProvider(this).get(DoaDzikirViewModel::class.java)
        initAdapter()
        if(sharedPrefManager.isLoggedIn){
            if(sharedPrefManager.user.utype == "admin"){
                fab_dzikir?.visibility = View.VISIBLE
            }else{
                fab_dzikir?.visibility = View.GONE
            }
        }else{
            fab_dzikir?.visibility = View.GONE
        }

        initSwipeRefresh()

        sv_dzikir?.setOnTouchListener(object : View.OnTouchListener{
                override fun onTouch(v: View?, mv: MotionEvent?): Boolean {
                    sv_dzikir.isIconified = false
                    return true
                }
            })
        sv_dzikir?.setOnCloseListener(object: SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                sv_dzikir.isIconified = true
                initAdapter()
                return true
            }

        })
        sv_dzikir?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query?.length!! >= 3){
                    showLoader()
                    updateDataFromSearch(query)
                    this@DzikirActivity.query = query
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText?.length!! >= 3){
                    showLoader()
                    updateDataFromSearch(query)
                    this@DzikirActivity.query = newText
                }
                return true
            }

        })

        fab_dzikir?.setOnClickListener {
            val intent = Intent(this, BuatDoaDzikirActivity::class.java)
            intent.putExtra(BuatDoaDzikirActivity.EXTRA_TAG,"dzikir")
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

    private fun refreshData() {
        viewModel.refreshData().observe(this, Observer {
            if(it){
                Log.d(TAG, "ke refresh")
                updateDataFromSearch("")
            }
        })
    }

    private fun showLoader() {
        progressbar_dzikir?.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        progressbar_dzikir?.visibility = View.GONE
    }

    private fun updateDataFromSearch(query: String) {
        rv_dzikir?.scrollToPosition(0)
        viewModel.getQuery(query)
        adapter.submitList(null)
    }

    private fun initAdapter() {
        viewModel.getQuery("")
        adapter.setContext(this)

        rv_dzikir?.layoutManager = LinearLayoutManager(this)
        rv_dzikir?.adapter = adapter
        viewModel.getDzikir.observe(this, Observer<PagedList<Dzikir>> {
            Log.d(TAG, "List Doa: ${it?.size}")
            hideLoader()
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
            Log.d(TAG, "item doa adapter "+ adapter.itemCount.toString())
        })
        viewModel.getDzikirNetworkError.observe(this, Observer {
            Log.d("ArtikelFragment", "Error : ${it}")
        })
    }

    private fun showEmptyList(show: Boolean) {
        if(show){
            img_empty_dzikir?.visibility = View.VISIBLE
            rv_dzikir?.visibility = View.GONE
        }else{
            rv_dzikir?.visibility = View.VISIBLE
            img_empty_dzikir?.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if (!sv_dzikir.isIconified()) {
            sv_dzikir.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }
}
