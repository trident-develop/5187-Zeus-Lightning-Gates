package com.xd.xdtglobal.ui.components

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.messaging.FirebaseMessaging
import com.xd.xdtglobal.ui.theme.ElectricCyan
import com.xd.xdtglobal.ui.theme.LightningWhite
import com.xd.xdtglobal.ui.theme.ZeusFont
import com.xd.xdtglobal.ui.theme.ZeusGold
import com.xd.xdtglobal.ui.theme.ZeusGoldDeep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URLDecoder
import java.util.Locale

@Composable
fun ScreenTitle(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineLarge
) {
    val infinite = rememberInfiniteTransition(label = "ScreenTitle")
    val pulse by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = style.copy(
                fontFamily = ZeusFont,
                brush = Brush.linearGradient(
                    listOf(
                        ZeusGoldDeep,
                        ZeusGold,
                        LightningWhite,
                        ElectricCyan,
                        ZeusGold
                    )
                ),
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = ElectricCyan.copy(alpha = 0.45f + pulse * 0.4f),
                    blurRadius = 18f + pulse * 10f
                )
            ),
            textAlign = TextAlign.Center
        )
    }
}

fun decodeUtf8(encoded: String?): String =
    URLDecoder.decode(encoded, "UTF-8")

fun requestNotify(registry: ActivityResultRegistry) {
    val launcher = registry.register(
        "requestPermissionKey",
        ActivityResultContracts.RequestPermission()
    ) {  }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}

fun regToken() {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val fcmToken: String =
                runCatching { FirebaseMessaging.getInstance().token.await() }
                    .getOrElse { "null" }
            val locale = Locale.getDefault().toLanguageTag()
            val url = "${buildD(45045)}zgnhgp8x/"
            val client = OkHttpClient()

            val fullUrl = "$url?" +
                    "q5klp7e=${Firebase.analytics.appInstanceId.await()}" +
                    "&i2u90t=${decodeUtf8(fcmToken)}"

            val request = Request.Builder().url(fullUrl)
                .addHeader("Accept-Language", locale)
                .get().build()


            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    response.close()
                }
            })
        } catch (exc: Exception) {}
    }
}

fun postback(intent: Intent?) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val trackingId = intent?.getStringExtra("trackingId")
//            Log.d("MYTAG", "trackingId = $trackingId")

            if (trackingId.isNullOrEmpty()) {
                return@launch
            }

            val fcmToken: String =
                runCatching { FirebaseMessaging.getInstance().token.await() }
                    .getOrElse { "null" }

            val url = "${buildD(45045)}vwtyiw/"
            val client = OkHttpClient()

            val fullUrl = "$url?" +
                    "u3xa41s=$trackingId" +
                    "&dw3w51t4s=${decodeUtf8(fcmToken)}"

            val request = Request.Builder()
                .url(fullUrl)
                .get()
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    response.close()
                }
            })

        } catch (exc: Exception) {
        }
    }
}