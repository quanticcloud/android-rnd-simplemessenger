package com.groot.simplemessenger.ui.screen

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Telephony
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.groot.messenger.ui.component.CustomAlertDialogue
import com.groot.messenger.ui.component.SmsPermissionDisabledAlert
import com.groot.messenger.ui.component.setAsDefaultSmsApp
import com.groot.simplemessenger.service.BODY
import com.groot.simplemessenger.service.MESSAGE_IS_NOT_READ
import com.groot.simplemessenger.service.MESSAGE_IS_NOT_SEEN
import com.groot.simplemessenger.service.MESSAGE_TYPE_INBOX
import com.groot.simplemessenger.service.MESSAGE_TYPE_SENT
import com.groot.simplemessenger.service.SMS_URI
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMessageScreen() {

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    // view model objects


    // text fields objects


    //permissions

    //flag
    val phoneNumber = remember { mutableStateOf<String>("") }
    val message = remember { mutableStateOf<String?>(null) }
    val showSetDefaultMessageAppAlert = remember { mutableStateOf(false) }
    val showSmsPermissionDisabledAlert = remember { mutableStateOf(false) }

    BackHandler {

    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                /* to close keyboard on tap outside*/
                this.detectTapGestures {
                    focusManager.clearFocus()
                }
            },
        topBar = {

        },
        snackbarHost = {

        },
        content = {
            if (showSetDefaultMessageAppAlert.value) {
                CustomAlertDialogue(
                    title = "Make Messenger Default SMS App",
                    textMessage = "To enable, go to Settings & Apps then Choose default apps & SMS App then Select Messenger.",
                    clickOk = {
                        showSetDefaultMessageAppAlert.value = true
                        setAsDefaultSmsApp(context)
                    }, clickCancel = {
                        showSetDefaultMessageAppAlert.value = false
                    })
            }

            if (showSmsPermissionDisabledAlert.value) {
                SmsPermissionDisabledAlert(context = context, showAlert = showSmsPermissionDisabledAlert)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    value = phoneNumber.value,
                    onValueChange = { newText ->
                        //length check < 10
                        phoneNumber.value = newText
                    },
                    label = {
                        Text(text = "Phone Number")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                ) //end OutlinedTextField

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    value = message.value ?: "",
                    onValueChange = { newText ->
                        message.value = newText
                    },
                    label = {
                        Text(text = "Message")
                    },
                ) //end OutlinedTextField

                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
//                        if (Telephony.Sms.getDefaultSmsPackage(context) == context.packageName) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                            sendSMS(context, phoneNumber.value, message.value ?: "Hello There!!!")
                        } else {
                            showSmsPermissionDisabledAlert.value = true
                        }
//                        } else {
//                            showSetDefaultMessageAppAlert.value = true
//                        }
                    },
                    content = {
                        Text(text = "Send Message")
                    }
                ) //end button
            }
        })

}

fun sendSMS(context: Context, phoneNo: String?, msg: String?) {
    try {
        val smsManager: SmsManager = SmsManager.getDefault()
        val contentResolver = context.contentResolver
        msg?.let {
            smsManager.sendTextMessage(phoneNo, null, it, null, null)
            putSmsToDatabase(contentResolver, it, phoneNo)
        }
    } catch (ex: Exception) {
        Toast.makeText(context, ex.message.toString(), Toast.LENGTH_LONG).show()
        ex.printStackTrace()
    }
}

fun putSmsToDatabase(contentResolver: ContentResolver, sms: String, phoneNo: String?) {
    // Construct a ContentValues object with the SMS details
    val values = ContentValues()
    values.put(Telephony.Sms.ADDRESS, phoneNo)
    values.put(Telephony.Sms.DATE_SENT, Date().time)
    values.put(Telephony.Sms.READ, MESSAGE_IS_NOT_READ)
    values.put(Telephony.Sms.STATUS, "")
    values.put(Telephony.Sms.TYPE, MESSAGE_TYPE_SENT)
    values.put(Telephony.Sms.SEEN, MESSAGE_IS_NOT_SEEN)
    try {
        // Create SMS row
        values.put(BODY, sms)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    // Push row into the SMS table
    contentResolver.insert(Uri.parse(SMS_URI), values)
}


/* **********************************************  Preview ************************************************************/
@Preview(showBackground = true)
@Composable
fun SendMessageScreenPreview() {
    SendMessageScreen()
}


