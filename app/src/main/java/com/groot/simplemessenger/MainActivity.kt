package com.groot.simplemessenger

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.provider.Telephony
import android.view.ContextThemeWrapper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.groot.simplemessenger.ui.screen.SendMessageScreen
import com.groot.simplemessenger.ui.theme.SimpleMessengerTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(applicationContext)
        val showSetDefaultMessageAppAlert = defaultSmsApp != applicationContext.packageName

        if (showSetDefaultMessageAppAlert) {
            showCustomAlertDialog(
                context = this,
                message = "To enable, go to Settings & Apps then Choose default apps & SMS App then Select Messenger.",
                title = "Make Messenger Default SMS App",
                onOkListener = {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    this.startActivity(intent)

                    println("xcxcxc hello work done...")
                    println("xcxcxc moving on to new tasks")
                }, onCancelListener = {
                    println("xcxcxc hello sms default app cancelled...")
                    println("xcxcxc moving on to new tasks")


                }
            )
        }

        setContent {
            SimpleMessengerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    Greeting("Android")
                    SendMessageScreen()
                }
            }
        }
    }


}

fun showCustomAlertDialog(context: Context, message: String, title: String, onOkListener: () -> Unit, onCancelListener: () -> Unit) {
    AlertDialog.Builder(ContextThemeWrapper(context, R.style.Theme_SimpleMessenger))
        .setMessage(message)
        .setTitle(title)
        .setCancelable(true)
        .setPositiveButton("Open settings") { _, _ ->
            onOkListener()
        }
        .setNegativeButton("Cancel") { _, _ ->
            onCancelListener()
        }
        .setOnCancelListener {
            onCancelListener()
        }
        .create()
        .show()
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimpleMessengerTheme {
        Greeting("Android")
    }
}