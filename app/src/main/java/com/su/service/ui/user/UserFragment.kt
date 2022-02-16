package com.su.service.ui.user

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.su.service.R
import com.su.service.ui.artikel.buateditartikel.ArtikelLocalViewModel
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment() {
    companion object{
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_TITLE = "extra_title"
    }
    private lateinit var userViewModel: UserViewModel
    private lateinit var viewModel: ArtikelLocalViewModel
    private lateinit var sharedPrefManager: SharedPrefManager
    private val TAG = UserFragment::class.java.simpleName
    private val REQUEST_BUAT_CODE = 100
    private val REQUEST_EDIT_CODE = 110
    private val adapter = ArtikelLocalAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        viewModel = ViewModelProvider(this).get(ArtikelLocalViewModel::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(requireActivity())!!
        val root = inflater.inflate(R.layout.fragment_user, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUi()
        iniTabLayout()

    }

    private fun iniTabLayout() {
        val sectionsPagerAdapter = SectionsPagerAdapter(requireContext(),childFragmentManager)
        view_pager?.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }

    private fun updateUi() {
        val user = sharedPrefManager.user
        if(user.email != null){
            img_user?.visibility = View.VISIBLE
            tv_nama_user?.visibility = View.VISIBLE
            panel_statistik?.visibility = View.VISIBLE
            Glide.with(requireActivity())
                .load(user.image)
                .into(img_user)
            if(user.fnama != null) tv_nama_user?.text = user.fnama else tv_nama_user?.text = user.email
            tv_poin?.text = user.poin.toString()
            viewModel.artikelLocal.observe(this, Observer {
                tv_jumlah_artikel?.text = it.size.toString()
            })
        }else{
            img_user?.visibility = View.GONE
            tv_nama_user?.visibility = View.GONE
            panel_statistik?.visibility = View.GONE
        }
    }



}