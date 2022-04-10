package com.su.service.utils

import android.app.Activity
import android.content.*
import androidx.appcompat.app.AlertDialog


class Alerts {
    companion object{
        fun register(activity: Activity?) {
            activity?.let { AlertReceiver.register(it) }
        }

        fun unregister(activity: Activity?) {
            activity?.let { AlertReceiver.unregister(it) }
        }

        fun displayError(context: Context, msg: String?) {
            val intent = Intent("MyApplication.INTENT_DISPLAYERROR")
            intent.putExtra(Intent.EXTRA_TEXT, msg)
            context.sendOrderedBroadcast(intent, null)
        }

        private fun displayErrorInternal(context: Context, msg: String) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Error").setMessage(msg).setCancelable(false)
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
            val alert: AlertDialog = builder.create()
            alert.show()
        }

        private class AlertReceiver private constructor(activity: Activity) : BroadcastReceiver() {
            companion object {
                private var registrations: HashMap<Activity, AlertReceiver>? = null
                fun register(activity: Activity) {
                    val receiver = AlertReceiver(activity)
                    activity.registerReceiver(
                        receiver,
                        IntentFilter("MyApplication.INTENT_DISPLAYERROR")
                    )
                    registrations!![activity] = receiver
                }

                fun unregister(activity: Activity) {
                    val receiver = registrations!![activity]
                    if (receiver != null) {
                        activity.unregisterReceiver(receiver)
                        registrations!!.remove(activity)
                    }
                }

                init {
                    registrations = HashMap()
                }
            }

            private val activityContext: Context
            override fun onReceive(context: Context?, intent: Intent) {
                abortBroadcast()
                val msg = intent.getStringExtra(Intent.EXTRA_TEXT)
                msg?.let { displayErrorInternal(activityContext, it) }
            }

            init {
                activityContext = activity
            }
        }
    }

}