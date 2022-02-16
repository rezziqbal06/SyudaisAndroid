package com.su.service.ui.absen

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.su.service.R
import com.su.service.utils.SharedPrefManager
import com.su.service.utils.Utils
import kotlinx.android.synthetic.main.activity_absen.*

class AbsenActivity : AppCompatActivity() {
    private val TAG = AbsenActivity::class.java.simpleName
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var viewModel: AbsenViewModel
    private var ranValue = ""
    private var handler: Handler? = null
    private var firestore: FirebaseFirestore? = null
    private var reference: CollectionReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absen)
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        viewModel = ViewModelProvider(this).get(AbsenViewModel::class.java)
        handler = Handler()
        initFirebase()
        initData()
    }

    private fun initFirebase() {
        firestore = FirebaseFirestore.getInstance()
        reference = firestore?.collection("pengguna")
    }

    fun initData() {
        val runnable = Runnable { initData() }
        ranValue = getAlphaNumericString(21)
        Log.d(TAG, ranValue)
        reference?.document(ranValue)
            ?.addSnapshotListener(EventListener<DocumentSnapshot?> { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    //Toast.makeText(GenerateActivity.this, "Get Data Failed", Toast.LENGTH_SHORT).show();
                    return@EventListener
                }
                if (snapshot != null && snapshot.exists()) {
                    showLoading()
                    Log.d(TAG, "Current data: " + snapshot.data)
                    val email = snapshot.getString("email")
                    ngabsen(snapshot, email)
                    handler?.removeCallbacks(runnable)
                    handler?.postDelayed(runnable, 1000)
                    handler?.postDelayed(Runnable { reference?.document(ranValue)!!.delete() }, 1000)
                } else {
                    Log.d(TAG, "Current data: null")
                }
            })
        generateCode(ranValue)
        //handler.postDelayed(runnable,1000*20);
    }

    private fun ngabsen(snapshot: DocumentSnapshot, email: String?) {
        viewModel.ngabsen(email, sharedPrefManager.user.apiMobileToken).observe(this, Observer {
            hideLoading()
            if(it != null){
                Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                if(it.status == 200) Utils(this).vibratePhone()
            }else{
                Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun generateCode(ranValue: String) {
        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix = multiFormatWriter.encode(
                ranValue,
                BarcodeFormat.QR_CODE, 200, 200
            )
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            img_qrcode.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    private fun showLoading() {
        progress_bar?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progress_bar?.visibility = View.GONE
    }

    private fun getAlphaNumericString(n: Int): String {
        // chose a Character random from this String
        val AlphaNumericString = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz")

        // create StringBuffer size of AlphaNumericString
        val sb = StringBuilder(n)
        for (i in 0 until n) {
            // generate a random number between
            // 0 to AlphaNumericString variable length
            val index = (AlphaNumericString.length
                    * Math.random()).toInt()

            // add Character one by one in end of sb
            sb.append(
                AlphaNumericString[index]
            )
        }
        return sb.toString()
    }
}
