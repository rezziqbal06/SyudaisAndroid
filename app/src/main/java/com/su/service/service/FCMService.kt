package com.su.service.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.su.service.R
import com.su.service.data.source.remote.Api
import com.su.service.model.pelanggan.PelangganResponse
import com.su.service.ui.artikel.admin.ApprovalActivity
import com.su.service.ui.diskusi.DiskusiActivity
import com.su.service.ui.main.MainActivity
import com.su.service.utils.Constants
import com.su.service.utils.SharedPrefManager
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutionException

class FCMService: FirebaseMessagingService(){
    private val TAG: String = FCMService::class.java.simpleName
    private var sharedPrefManager: SharedPrefManager? = null
    val CHANNEL_NAME = "notif_channel_nama"
    val CHANNEL_ID = "notif_channel_id"
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sharedPrefManager = SharedPrefManager.getInstance(applicationContext)
        if(sharedPrefManager?.isLoggedIn!!){
            sendRegistrationToServer(token)
        }
    }

    private fun sendRegistrationToServer(token: String) {
        sharedPrefManager = SharedPrefManager.getInstance(applicationContext)
        Api.service.updateFcm(Constants.APIKEY, sharedPrefManager?.user?.apiMobileToken, Constants.DEVICE, token).enqueue(object: Callback<PelangganResponse>{
            override fun onFailure(call: Call<PelangganResponse>, t: Throwable) {
                Log.d(TAG, "Update failure")
            }

            override fun onResponse(
                call: Call<PelangganResponse>,
                response: Response<PelangganResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Berhasil update")
                } else {
                    Log.d(TAG, "Gagal update")
                }
            }

        })
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val data = remoteMessage.data
        Log.d(TAG, data.toString())
        val payload = data["extras"]

        if(remoteMessage.data.isNotEmpty()){
            Log.e("TAG", "Message data payload: " + remoteMessage.data)
            if(payload != null){
                try {
                    val jsonParse = JSONObject(payload)
                    showNotif(jsonParse.getString("judul"),jsonParse.getString("gambar")
                        ,jsonParse.getString("deskripsi"),jsonParse.getString("jenis"),jsonParse.getString("id"))
                }catch (e: JSONException){
                    e.printStackTrace()
                }
            }
        }

        if(remoteMessage.notification != null){
            Log.e(
                "TAG",
                "Message Notification Body: " + remoteMessage.notification!!.body
            )
            showNotif(
                remoteMessage.notification!!.title!!,
                remoteMessage.notification!!.body!!,
                "",
                "",
                ""
            )
        }
    }

    private fun showNotif(judul: String, gambar: String, deskripsi: String, jenis: String,id:String) {

        // passing data title dan message ke MainActivity
        var image: Bitmap? = null
        try {
            val imageBitmap = Glide.with(this)
                .asBitmap()
                .load(gambar)
                .submit()
                .get()
            image = imageBitmap
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        val bundle = Bundle()
        bundle.putString("title", judul)
        bundle.putString("message", deskripsi)
        bundle.putString("jenis", jenis)
        val intent = when (jenis) {
            "approval" -> Intent(this, ApprovalActivity::class.java)
            //donasi activity
            "diskusi" -> Intent(this, DiskusiActivity::class.java).putExtra("extra_id", id)
            else -> Intent(this, MainActivity::class.java)
        }

        // setup intent
        intent.putExtras(bundle)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val notifBuilder =
            NotificationCompat.Builder(
                this,
                CHANNEL_ID
            )
                .setContentTitle(judul)
                .setContentText(deskripsi)
                .setAutoCancel(true) // menghapus notif ketika user melakukan tap pada notif
                .setLights(200, 200, 200) // light button
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI) // set sound
                .setOnlyAlertOnce(true) // set alert sound notif
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(NotificationCompat.BigTextStyle().bigText(deskripsi))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent) // action notif ketika di tap

        if (image != null) {
            notifBuilder.setLargeIcon(image)
        } else {
            notifBuilder.setLargeIcon(null)
        }
        if (jenis == "artikel" || jenis == "donasi") {
            notifBuilder.setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(image)
            )
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            notifBuilder.setSmallIcon(R.drawable.ic_notifications_black_24dp)
        } else {
            notifBuilder.setSmallIcon(R.drawable.icon_syudais)
        }

        val notificationManager = NotificationManagerCompat.from(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notifBuilder.setChannelId(CHANNEL_ID)
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableLights(true)
            channel.lightColor = Color.BLUE
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(1, notifBuilder.build())
    }
}