package com.groot.simplemessenger.service

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.Telephony


class SMSProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        TODO("Not yet implemented")
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        TODO("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        return ""
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val contentResolver = context!!.contentResolver
        val uri = contentResolver.insert(Telephony.Sms.CONTENT_URI, values)
        return uri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        var numInserted = 0
        // Insert received SMS messages into the SMS content provider
        // using ContentResolver and ContentValues.
        // Ensure you set the appropriate columns like "address" and "body."
        // Use the "Telephony.Sms.CONTENT_URI" URI for SMS messages.

        // Example code:
        val contentResolver = context!!.contentResolver

        return numInserted
    }

}