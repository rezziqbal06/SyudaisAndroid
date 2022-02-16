package com.su.service.ui.absen

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Constraints
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.su.service.R
import com.su.service.utils.SharedPrefManager
import com.su.service.utils.Utils
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : AppCompatActivity() {
    private lateinit var sharedPrefManager: SharedPrefManager
    val data = HashMap<String, String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        val db = FirebaseFirestore.getInstance()

        qr_view?.setStatusText(" ")
        qr_view?.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                val res = result.text
                data.set("email",sharedPrefManager.user.email.toString())
                data["nama"] = sharedPrefManager.user.fnama.toString()
                data["foto"] = sharedPrefManager.user.image.toString()
                db.collection("pengguna")
                    .document(res)
                    .set(data as Map<String, Any>)
                    .addOnSuccessListener(OnSuccessListener<Void?> {
                        Log.d(
                            Constraints.TAG,
                            "DocumentSnapshot successfully written!"
                        )
                        //vibrate();
                        Utils(this@ScanActivity).vibratePhone()
                        qr_view?.pause()
                        finish()
                    })
                    .addOnFailureListener(OnFailureListener { e ->
                        Log.w(
                            Constraints.TAG,
                            "Error writing document",
                            e
                        )
                        Toast.makeText(this@ScanActivity, e.message, Toast.LENGTH_SHORT).show()
                    })
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
        })
        qr_view?.resume()
    }

    override fun onPause() {
        super.onPause()
        qr_view.pause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        qr_view.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        qr_view.pause()
    }


}
