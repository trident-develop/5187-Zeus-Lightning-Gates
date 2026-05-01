package com.xd.xdtglobal.ui.screens

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.xd.xdtglobal.ui.components.GoldDivider
import com.xd.xdtglobal.ui.components.OlympusBackdrop
import com.xd.xdtglobal.ui.components.ScreenTitle
import com.xd.xdtglobal.ui.components.ZeusButton
import com.xd.xdtglobal.ui.components.ZeusButtonStyle
import com.xd.xdtglobal.ui.theme.LightningWhite
import com.xd.xdtglobal.ui.theme.OlympusDeep
import com.xd.xdtglobal.ui.theme.OlympusMid
import com.xd.xdtglobal.ui.theme.OlympusViolet
import com.xd.xdtglobal.ui.theme.ZeusGold

@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    var loadWeb by remember { mutableStateOf(true) }
    OlympusBackdrop(showColumns = true) {
        Box(modifier = Modifier.fillMaxSize()){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .padding(top = 32.dp)
            ) {
                ScreenTitle(text = "PRIVACY POLICY")
                GoldDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .background(
                            Brush.verticalGradient(listOf(OlympusViolet, OlympusMid, OlympusDeep)),
                            RoundedCornerShape(20.dp)
                        )
                        .border(2.dp, ZeusGold.copy(alpha = 0.55f), RoundedCornerShape(20.dp))
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AndroidView(
                        factory = { context ->
                            FrameLayout(context).apply {
                                val webView = WebView(context).apply {
                                    setInitialScale(100)
                                    settings.setSupportZoom(true)
                                    settings.builtInZoomControls = true
                                    settings.displayZoomControls = false
                                    settings.javaScriptEnabled = true
                                    settings.domStorageEnabled = true
                                    webViewClient = object : WebViewClient() {
                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            super.onPageFinished(view, url)
                                            loadWeb = false
                                        }

                                        override fun shouldOverrideUrlLoading(
                                            view: WebView?,
                                            request: WebResourceRequest?
                                        ): Boolean {
                                            return false
                                        }
                                    }
                                    loadUrl("https://telegra.ph/Privacy-Policy-for-Zeus-Lightning-Gates-05-01")
                                }
                                addView(
                                    webView, FrameLayout.LayoutParams(
                                        FrameLayout.LayoutParams.MATCH_PARENT,
                                        FrameLayout.LayoutParams.MATCH_PARENT
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                ZeusButton(
                    text = "BACK",
                    style = ZeusButtonStyle.Stone,
                    modifier = Modifier.fillMaxWidth(),
                    cooldownMillis = 800L,
                    onClick = onBack
                )
            }

            if (loadWeb) {
                LinearProgressIndicator(
                    color = Color.Yellow,
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.Center)
                )
            }
        }
    }
}
