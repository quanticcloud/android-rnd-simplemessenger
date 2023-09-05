package com.groot.simplemessenger.service

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.telephony.SmsManager


class HeadlessSmsSendService : Service() {
    override fun onBind(intent: Intent?) = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            if (intent == null) {
                return START_NOT_STICKY
            }

            val number = Uri.decode(intent.dataString!!.removePrefix("sms:").removePrefix("smsto:").removePrefix("mms").removePrefix("mmsto:").trim())
            val text = intent.getStringExtra(Intent.EXTRA_TEXT)
            val extras = intent.extras

            if (!text.isNullOrEmpty()) {
                val smsManager: SmsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(number, null, text, null, null)
                //put to db
            }

        } catch (ignored: Exception) {

        }

        return super.onStartCommand(intent, flags, startId)
    }



}