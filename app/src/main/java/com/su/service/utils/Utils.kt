package com.su.service.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import java.io.IOException
import javax.inject.Inject


class Utils(private val context: Context) {

  fun isConnectedToInternet(): Boolean {
    val connectivity = context.getSystemService(
        Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivity != null) {
      val info = connectivity.allNetworkInfo
      if (info != null)
        for (i in info.indices)
          if (info[i].state == NetworkInfo.State.CONNECTED) {
            return true
          }
    }
    return false
  }

  fun vibratePhone() {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
      vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
      vibrator.vibrate(200)
    }
  }

  fun getJsonDataFromAsset(fileName: String): String? {
    val jsonString: String
    try {
      jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
      ioException.printStackTrace()
      return null
    }
    return jsonString
  }


}