package com.groot.messenger.ui.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.groot.simplemessenger.ui.theme.Pink40


@Composable
fun SmsPermissionDisabledAlert(context: Context, showAlert: MutableState<Boolean>) {
    CustomPermissionDisabledAlert(
        context = context,
        showAlert = showAlert,
        title = "Need Sms Access",
        textMessage = "SMS access is required to send message. To enable, go to Settings & Apps & DD Digital & Permissions & SMS & Select allow.",
    )
}


@Composable
fun CustomPermissionDisabledAlert(context: Context, showAlert: MutableState<Boolean>, title: String, textMessage: String) {
    AlertDialog(
        onDismissRequest = {
            showAlert.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // FIXME: requires multiple click
                    showAlert.value = false
                    goToAppInfoSettings(context)
                },
                content = {
                    Text(text = "Open settings")
                })
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showAlert.value = false
                },
                content = {
                    Text(text = "Not Now")
                })
        },
        title = {
            Text(text = title, modifier = Modifier.fillMaxWidth(), color = Pink40)
        },
        text = {
            Text(text = textMessage, modifier = Modifier.fillMaxWidth(), color = Pink40)
        }
    )
}

@Composable
fun CustomAlertDialogue(title: String, textMessage: String, clickOk: () -> Unit, clickCancel: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
        },
        confirmButton = {
            TextButton(onClick = {
                clickOk()
            }) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                clickCancel()
            }) {
                Text(text = "No")
            }
        },
        title = {
            Text(text = title, modifier = Modifier.fillMaxWidth(), color = Pink40)
        },
        text = {
            Text(text = textMessage, modifier = Modifier.fillMaxWidth(), color = Pink40)
        }
    )
}

fun goToAppInfoSettings(context: Context) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri: Uri = Uri.fromParts("package", context.packageName, null)
    intent.data = uri
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

fun setAsDefaultSmsApp(context: Context) {
    val intent = Intent()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.action = Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS
    } else {
        intent.action = Settings.ACTION_DEVICE_INFO_SETTINGS
    }
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}