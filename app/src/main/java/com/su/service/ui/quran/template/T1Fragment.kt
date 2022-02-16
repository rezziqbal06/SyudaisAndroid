package com.su.service.ui.quran.template

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.su.service.R
import kotlinx.android.synthetic.main.fragment_t1.*
import java.io.ByteArrayOutputStream


/**
 * A simple [Fragment] subclass.
 * Use the [T1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class T1Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var arab: String? = null
    private var artinya: String? = null
    private var judul: String? = null
    private var surat: String? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arab = it.getString("arab")
            artinya = it.getString("artinya")
            judul = it.getString("judul")
            surat = it.getString("surat")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val btnShare = activity?.findViewById<ImageView>(R.id.btn_share)
        progressBar = activity?.findViewById(R.id.progress_bar)
        val view =  inflater.inflate(R.layout.fragment_t1, container, false)
        btnShare?.setOnClickListener {
            progressBar?.visibility = View.VISIBLE
            val bitmap = buatGambar(view)
            shareBitmap(bitmap)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_arab?.text = arab
        tv_judul?.text = judul
        tv_artinya?.text = artinya
        tv_surat?.text = surat
    }

    private fun shareBitmap(bitmap: Bitmap?) {
        val intent = Intent(Intent.ACTION_SEND).setType("image/*")
        val bytes = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bytes)

        val path = MediaStore.Images.Media.insertImage(activity?.contentResolver, bitmap, "tempimage", null)
        val uri = Uri.parse(path)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(intent)
        progressBar?.visibility = View.GONE
    }
    private fun buatGambar(view: View): Bitmap?{
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if(bgDrawable != null){
            bgDrawable.draw(canvas)
        }else{
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }
}