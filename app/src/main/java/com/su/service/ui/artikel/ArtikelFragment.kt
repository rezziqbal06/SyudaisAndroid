package com.su.service.ui.artikel

import KategoriAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.su.service.Injection
import com.su.service.R
import com.su.service.model.artikel.Artikel
import com.su.service.model.kategori.KategoriItem
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_artikel.*


class ArtikelFragment : Fragment() {
    private val TAG = ArtikelFragment::class.java.simpleName
    private lateinit var artikelViewModel: ArtikelViewModel
    private lateinit var kategoriViewModel: KategoriViewModel
    private val adapter = ArtikelAdapter()
    private val adapterPopular = ArtikelPopularAdapter()
    private var pageKategori = 1
    private lateinit var imgEmpty: ImageView
    private lateinit var rvArtikel: RecyclerView
    private lateinit var sharedPrefManager: SharedPrefManager
    private var kat = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_artikel, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kategoriViewModel = ViewModelProviders.of(this).get(KategoriViewModel::class.java)
        artikelViewModel = ViewModelProviders.of(this,
            context?.let { Injection.provideViewModelFactory(it) }).get(ArtikelViewModel::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(requireActivity())!!
        val decoration = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        rv_artikel?.addItemDecoration(decoration)
        activity?.let { adapter.setContext(it) }
        context?.let { adapterPopular.setContext(it) }
        showLoader()
        imgEmpty = view.findViewById(R.id.img_empty_artikel)
        rvArtikel = view.findViewById(R.id.rv_artikel)
        initAdapter()
        initRvKategoriFromNetwork()
        initSwipeRefresh()
        sv_artikel?.setOnClickListener {
            val intent = Intent(activity, SearchingArtikelActivity::class.java)
            context?.startActivity(intent)
        }
    }

    private fun initSwipeRefresh() {
        refresh_container?.setOnRefreshListener {
            updateDataFromKategori(kat)
            Handler().postDelayed(Runnable { refresh_container?.isRefreshing = false },500)
        }
    }

    private fun showLoader(){
        progress_bar?.visibility = View.VISIBLE
    }

    private fun hideLoader(){
        progress_bar?.visibility = View.GONE
    }

    private fun initRvKategoriFromNetwork() {
        artikelViewModel.getKategori("")
        kategoriViewModel.getDataKategori(pageKategori.toString(),"","","").observe(requireActivity(), Observer {
            if(it?.status == 200){
                Log.d(TAG, "kategori size: ${it.result?.kategori?.size}")
                val adapter : KategoriAdapter
                if(!it.result?.kategori.isNullOrEmpty()){
                    sharedPrefManager.setKategori(it.result?.kategori)
                    Log.d(TAG, "sharedkategori size: ${sharedPrefManager.listKategori?.size}")
                    adapter = KategoriAdapter(sharedPrefManager.listKategori)
                }else{
                    adapter = KategoriAdapter(sharedPrefManager.listKategori)
                }
                rv_kategori?.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                rv_kategori?.setHasFixedSize(true)
                rv_kategori?.adapter = adapter
                adapter.setOnItemClickListener(object : KategoriAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int, kategori: String) {
                        if(kategori == "terbaru"){
                            updateDataFromKategori("")
                        }else{
                            this@ArtikelFragment.kat = kategori
                            updateDataFromKategori(kategori)
                        }
                    }

                })
            }else{
                val kategories = sharedPrefManager.listKategori
                val adapter = KategoriAdapter(kategories)
                rv_kategori?.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                rv_kategori?.setHasFixedSize(true)
                rv_kategori?.adapter = adapter
                adapter.setOnItemClickListener(object : KategoriAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int, kategori: String) {
                        if(kategori == "terbaru"){
                            updateDataFromKategori("")
                        }else{
                            updateDataFromKategori(kategori)
                        }
                    }

                })
            }
        })
    }

    private fun initAdapter() {
        artikelViewModel.getKategori("")
        artikelViewModel.getPopular("popular")
        rv_artikel?.layoutManager = LinearLayoutManager(activity)
        rv_popular_artikel?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_artikel?.adapter = adapter
        rv_popular_artikel?.adapter = adapterPopular
        artikelViewModel.artikelKategori.observe(requireActivity(), Observer<PagedList<Artikel>> {
            hideLoader()
            Log.d("ArtikelFragment", "List Artikel: ${it?.size}")
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
            Log.d(TAG, "item adapter "+ adapter.itemCount.toString())
        })
        artikelViewModel.kategoriNetworkError.observe(requireActivity(), Observer {
            Log.d("ArtikelFragment", "Error : ${it}")
        })
        artikelViewModel.artikelPopular.observe(requireActivity(), Observer {
            Log.d("ArtikelFragment", "List Artikel: ${it?.size}")
            showEmptyList(it?.size == 0)
            adapterPopular.submitList(it)
            Log.d(TAG, "item adapter "+ adapterPopular.itemCount.toString())
        })
        artikelViewModel.popularNetwork.observe(requireActivity(), Observer {
            Log.d("ArtikelFragment", "Error : ${it}")
        })

    }

    private fun showEmptyList(show: Boolean){
        if(show){
            Log.d(TAG, "show empty")
            img_empty_artikel?.visibility = View.VISIBLE
            rv_artikel?.visibility = View.INVISIBLE
        }else {
            img_empty_artikel?.visibility = View.INVISIBLE
            rv_artikel?.visibility = View.VISIBLE
        }
    }

    fun updateDataFromKategori(kategori: String){
        rvArtikel?.scrollToPosition(0)
        artikelViewModel.getKategori(kategori)
        adapter.submitList(null)
    }
}