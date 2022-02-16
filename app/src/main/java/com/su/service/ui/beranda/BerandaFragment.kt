package com.su.service.ui.beranda

import KajianAdapter
import KegiatanAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.su.service.R
import com.su.service.model.beranda.KajianItem
import com.su.service.model.beranda.KegiatanItem
import com.su.service.model.beranda.SliderItem
import com.su.service.receiver.PopupReceiver
import com.su.service.ui.doa.DoaActivity
import com.su.service.ui.donasi.DonasiActivity
import com.su.service.ui.dzikir.DzikirActivity
import com.su.service.ui.kegiatan.DetailKegiatanActivity
import com.su.service.ui.lembaga.LembagaActivity
import com.su.service.ui.quran.QuranActivity
import com.su.service.ui.video.VideoActivity
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_beranda.*
import kotlinx.android.synthetic.main.item_kajian.*
import java.util.*
import kotlin.collections.ArrayList

class BerandaFragment : Fragment(), View.OnClickListener {
    companion object{
        private var currentPage = 0
        private var NUM_PAGES = 0
    }
    private lateinit var popupReceiver: PopupReceiver
    private lateinit var berandaViewModel: BerandaViewModel
    private lateinit var rvKajianRutin: RecyclerView
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        berandaViewModel =
            ViewModelProviders.of(this).get(BerandaViewModel::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(requireContext())!!
        val root = inflater.inflate(R.layout.fragment_beranda, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menu_doa.setOnClickListener(this)
        menu_dzikir.setOnClickListener(this)
        menu_quran.setOnClickListener(this)
        menu_video.setOnClickListener(this)
        menu_lembaga.setOnClickListener(this)
        menu_marketplace.setOnClickListener(this)
        rvKajianRutin = view.findViewById(R.id.rv_kajian_rutin)

        setAlarmManager()

        initKajianRecylerview()
        getData()
        initSwipeListener()
    }

    private fun setAlarmManager() {
        popupReceiver = PopupReceiver()
        if(!popupReceiver.isAlarmSet(requireContext())){
            popupReceiver.setRepeatingAlarm(requireContext())
        }
    }

    private fun initSwipeListener() {
        swipeRefresh.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                val handler = Handler()
                handler.postDelayed({
                    getData()
                    sharedPrefManager.user.apiMobileToken?.let { getUser(it) }
                    swipeRefresh.setRefreshing(false) }, 1500)
            }

        })
    }

    private fun getUser(apisess: String) {
        berandaViewModel.getUser(apisess).observe(requireActivity(), Observer {
            it?.data?.let { it1 -> sharedPrefManager.userLogin(it1) }
        })
    }

    private fun getData() {
        context?.let { berandaViewModel.getContext(it) }
        berandaViewModel.getDataBeranda().observe(requireActivity(), Observer {
            if(it?.status == 200){
                val kajian = it.result?.kajian
                val kegiatan = it.result?.kegiatan
                val slider = it.result?.slider
                setDataKajian(kajian)
                setDataKegiatan(kegiatan)
                setDataSlider(slider)
            }
        })
    }

    private fun setDataSlider(slider: List<SliderItem?>?) {
        Log.d("data slider ", slider?.size.toString())
        val adapter = context?.let{SliderAdapter(it, slider)}
        vp_slider?.adapter = adapter
        vp_slider?.measure(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        slider_indicator?.setViewPager(vp_slider)
        NUM_PAGES = slider?.size!!
        /*val handler = Handler()
        val update = Runnable {
            if(currentPage == NUM_PAGES){
                currentPage = 0
            }
            vp_slider.setCurrentItem(currentPage++, true)
        }
        swipeTimer.schedule(object: TimerTask(){
            override fun run() {
                handler.post(update)
            }

        },3000,3000)*/

    }

    private fun setDataKegiatan(kegiatan: List<KegiatanItem?>?) {
        showEmptyKegiatan(kegiatan?.size == 0)
        Log.d("data kegiatan ", kegiatan?.size.toString())
        val adapter = context?.let { KegiatanAdapter(it,kegiatan) }
        vp_kajian?.adapter = adapter
        indicatorKegiatan?.setViewPager(vp_kajian)
        adapter?.setOnItemClickListener(object : KegiatanAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, kegiatan: KegiatanItem) {
                val intent = Intent(activity, DetailKegiatanActivity::class.java)
                intent.putExtra(DetailKegiatanActivity.EXTRA_DATA, kegiatan)
                intent.putExtra(DetailKegiatanActivity.EXTRA_TAG,"kegiatan")
                startActivity(intent)
            }
        })
    }

    private fun showEmptyKegiatan(show: Boolean) {
        if(show){
            vp_kajian?.visibility = View.GONE
            indicatorKegiatan.visibility = View.GONE
        }else{
            vp_kajian?.visibility = View.VISIBLE
            indicatorKegiatan.visibility = View.VISIBLE
        }
    }

    private fun setDataKajian(kajian: ArrayList<KajianItem?>?) {
        val adapter = KajianAdapter(kajian)
        rv_kajian_rutin?.adapter = adapter
        showEmptyList(kajian?.size == 0)
    }

    private fun showEmptyList(show: Boolean) {
        if(show){
            tv_title_kajian?.text = "Belum Ada Kajian Rutin"
            img_event_empty?.visibility = View.VISIBLE
            rv_kajian_rutin?.visibility = View.GONE
        }else{
            tv_title_kajian?.text = "Kajian Rutin"
            img_event_empty?.visibility = View.GONE
            rv_kajian_rutin?.visibility = View.VISIBLE
        }
    }

    private fun initKajianRecylerview() {
        rv_kajian_rutin?.layoutManager = LinearLayoutManager(activity)
        rv_kajian_rutin?.setHasFixedSize(true)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.menu_doa -> {
                val intent = Intent(activity, DoaActivity::class.java)
                activity?.startActivity(intent)
            }
            R.id.menu_dzikir ->{
                val intent = Intent(activity, DzikirActivity::class.java)
                activity?.startActivity(intent)
            }
            R.id.menu_quran ->{
                val intent = Intent(activity, QuranActivity::class.java)
                activity?.startActivity(intent)
            }
            R.id.menu_lembaga -> {
                val intent = Intent(activity, LembagaActivity::class.java)
                activity?.startActivity(intent)
            }
            R.id.menu_video -> {
                val intent = Intent(activity, VideoActivity::class.java)
                activity?.startActivity(intent)
            }
            R.id.menu_marketplace -> {
                showUnderConstruction()
            }
        }
    }

    private fun showUnderConstruction() {
        Snackbar.make(swipeRefresh, "Dalam pengembangan", Snackbar.LENGTH_SHORT).show()
    }
}