package com.groot.simplemessenger.service


import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast


/**
 * A broadcast receiver who listens for incoming SMS
 */
class SmsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Get SMS map from Intent
        // Get SMS map from Intent
        val extras = intent.extras
        var messages = ""

        if (extras != null) {
            val smsExtra = extras[SMS_EXTRA_NAME] as Array<Any>?
            // Get received SMS array

            // Get ContentResolver object for pushing encrypted SMS to incoming folder
            val contentResolver = context.contentResolver
            Log.d(TAG, " smsExtra ${(smsExtra?.size ?: 0)}")
            Toast.makeText(context, "message received ", Toast.LENGTH_LONG).show()

            for (i in 0 until smsExtra?.size!!) {
                val sms = SmsMessage.createFromPdu(smsExtra[i] as ByteArray)
                val body = sms.messageBody.toString()
                val address = sms.originatingAddress
                messages += "SMS from $address :\n"
                messages += body + "\n"

                Log.d(TAG, sms.displayMessageBody)
                // Here you can add any your code to work with incoming SMS
                // I added encrypting of all received SMS
                Toast.makeText(context, "message received $messages", Toast.LENGTH_LONG).show()
                putSmsToDatabase(contentResolver, sms)
            }
        }
    }

    private fun putSmsToDatabase(contentResolver: ContentResolver, sms: SmsMessage) {
        // Construct a ContentValues object with the SMS details
        val values = ContentValues()
        values.put(ADDRESS, sms.originatingAddress)
        values.put(DATE, sms.timestampMillis)
        values.put(READ, MESSAGE_IS_NOT_READ)
        values.put(STATUS, sms.status)
        values.put(TYPE, MESSAGE_TYPE_INBOX)
        values.put(SEEN, MESSAGE_IS_NOT_SEEN)
        try {
            // Create SMS row
            values.put(BODY, sms.messageBody.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Push row into the SMS table
        contentResolver.insert(Uri.parse(SMS_URI), values)
    }


    companion object {
        private const val TAG = "SmsBroadcastReceiver"
    }
}


