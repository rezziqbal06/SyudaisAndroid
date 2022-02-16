package com.su.service.ui.user.tabs


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

import com.su.service.R
import com.su.service.ui.user.PublishArtikelAdapter
import com.su.service.ui.user.UserViewModel
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_tab_publish.*

/**
 * A simple [Fragment] subclass.
 */
class TabPublishFragment : Fragment() {
    private val TAG = TabPublishFragment::class.java.simpleName
    private lateinit var userViewModel: UserViewModel
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var adapter: PublishArtikelAdapter
    private var apisess = ""
    private var snackbar: Snackbar? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_tab_publish, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PublishArtikelAdapter()
        rv_publish?.layoutManager = LinearLayoutManager(requireContext())
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(requireContext())!!
        Log.d(TAG,"di halaman publish")
        apisess = sharedPrefManager.user.apiMobileToken.toString()
    }

    override fun onResume() {
        super.onResume()
        snackbar = Snackbar.make(container, resources.getString(R.string.failed),Snackbar.LENGTH_SHORT)
        initData()
    }

    override fun onPause() {
        super.onPause()
        snackbar?.dismiss()
    }

    private fun initData() {
        userViewModel.getByUser(apisess).observe(requireActivity(), Observer {
            if(it != null){
                if(it.status == 200){
                    it.result?.artikel?.let { it1 -> adapter.setList(it1) }
                    rv_publish?.adapter = adapter
                    setEmptyList(it.result?.artikel?.size == 0)
                }else{
                    Log.d(TAG, "message ambil data: ${it.message}")
                }
            }else{
                snackbar?.show()
            }
        })
    }

    private fun setEmptyList(show: Boolean) {
        if(show){
            rv_publish?.visibility = View.GONE
            img_empty_publish?.visibility = View.VISIBLE
        }else{
            rv_publish?.visibility = View.VISIBLE
            img_empty_publish?.visibility = View.GONE
        }
    }


}
