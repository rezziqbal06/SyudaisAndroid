package com.su.service.utils

import android.app.Application
import android.content.Context

class AppController : Application(){
    companion object{
        private var context: Context? = null
        fun getContext(): Context? {
            return context
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }


}