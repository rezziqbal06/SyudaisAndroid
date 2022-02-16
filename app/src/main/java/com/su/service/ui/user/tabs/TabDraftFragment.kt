package com.su.service.ui.user.tabs


import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import com.su.service.R
import com.su.service.model.artikel.ArtikelLocal
import com.su.service.ui.artikel.buateditartikel.ArtikelLocalViewModel
import com.su.service.ui.artikel.buateditartikel.BuatEditArtikelActivity
import com.su.service.ui.user.ArtikelLocalAdapter
import com.su.service.ui.user.UserFragment
import com.su.service.ui.user.UserViewModel
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_tab_draft.*

/**
 * A simple [Fragment] subclass.
 */
class TabDraftFragment : Fragment() {
    private lateinit var viewModel: ArtikelLocalViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var sharedPrefManager: SharedPrefManager
    private val TAG = UserFragment::class.java.simpleName
    private val REQUEST_BUAT_CODE = 100
    private val REQUEST_EDIT_CODE = 110
    private val adapter = ArtikelLocalAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_tab_draft, container, false)
        viewModel = ViewModelProvider(this).get(ArtikelLocalViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        Log.d(TAG,"di halaman draft")
        fab_artikel.setOnClickListener {
            val intent = Intent(requireActivity(), BuatEditArtikelActivity::class.java)
            startActivityForResult(intent, REQUEST_BUAT_CODE)
        }
    }


    private fun showEmptyList(show: Boolean) {
        if(show){
            img_empty_draft?.visibility = View.VISIBLE
            rv_draft?.visibility =View.INVISIBLE
        }else{
            rv_draft?.visibility = View.VISIBLE
            img_empty_draft?.visibility = View.GONE
        }
    }

    private fun initAdapter() {
        rv_draft?.layoutManager = GridLayoutManager(requireActivity(), 2)
        rv_draft?.adapter = adapter
        viewModel.artikelLocal.observe(requireActivity(), Observer {
            Log.d(TAG, "List Artikel Draft: ${it?.size}")
            showEmptyList(it?.size == 0)
            adapter.setListItem(it)
            Log.d(TAG, "item adapter "+ adapter.itemCount.toString())
        })
        adapter.setOnItemClickListener(object: ArtikelLocalAdapter.OnItemClickListener{
            override fun onItemClick(
                data: ArtikelLocal,
                tag: String
            ) {
                if(tag == "edit"){
                    Log.d(TAG, "artikel id: ${data.id}")
                    val intent = Intent(requireActivity(), BuatEditArtikelActivity::class.java)
                    intent.putExtra(UserFragment.EXTRA_DATA, data)
                    startActivityForResult(intent, REQUEST_EDIT_CODE)
                }else{
                    val builder = AlertDialog.Builder(requireContext())
                    val dialog = builder.setIcon(R.drawable.logo_syudais_merah).setMessage("Apakah artikel ingin dihapus?")
                        .setTitle("Hapus Artikel")
                        .setNegativeButton("Tidak",DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.dismiss()
                        }).setPositiveButton("Ya",DialogInterface.OnClickListener { dialogInterface, i ->
                            hapusArtikel(data)
                        }).create()

                    dialog.show()
                    val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    positive.setTextColor(resources.getColor(R.color.dark))
                    positive.setBackgroundColor(resources.getColor(R.color.white))
                    val negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    negative.setTextColor(resources.getColor(R.color.dark))
                    negative.setBackgroundColor(resources.getColor(R.color.white))
                }

            }
        })
    }

    private fun hapusArtikel(data: ArtikelLocal) {
        viewModel.delete(data)
        Toast.makeText(requireActivity(), "Berhasil", Toast.LENGTH_SHORT).show()
        initAdapter()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, datas: Intent?) {
        super.onActivityResult(requestCode, resultCode, datas)
        Log.d(TAG, "requestCode: $requestCode")
        if(requestCode == REQUEST_BUAT_CODE && resultCode == Activity.RESULT_OK){
            val artikel = datas?.getParcelableExtra<ArtikelLocal>(UserFragment.EXTRA_DATA)
            Log.d(TAG, "inserting artikel: ${artikel?.title}")
            Log.d(TAG, "inserting artikel id: ${artikel?.id}")
            Log.d(TAG, "inserting artikel: ${artikel?.kategori}")

            artikel?.let { viewModel.insert(it) }
            Toast.makeText(requireActivity(), "Berhasil", Toast.LENGTH_SHORT).show()

        }else if(requestCode == REQUEST_EDIT_CODE && resultCode == Activity.RESULT_OK){
            val artikel = datas?.getParcelableExtra<ArtikelLocal>(UserFragment.EXTRA_DATA)
            Log.d(TAG, "updating artikel: $artikel")
            artikel?.let{ viewModel.update(it)}
            Toast.makeText(requireActivity(), "Berhasil diedit", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireActivity(), "Batal" , Toast.LENGTH_SHORT).show()
        }
    }
}
