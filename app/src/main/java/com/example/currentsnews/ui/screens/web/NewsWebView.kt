package com.example.currentsnews.ui.screens

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewsWebViewContainer(url: String, backHandler: () -> Unit) {
    Scaffold(
        content = { WebContent(url = url, backHandler = backHandler, resources = LocalContext.current.resources) }
    )
}

@Composable
fun WebContent(url: String, backHandler: () -> Unit,resources: Resources) {

    BackHandler {
        backHandler()
    }

    val visibility = remember{ mutableStateOf(false) }
    val progress= remember{ mutableStateOf(0.0f) }

    Column {

        if(visibility.value){
            LinearProgressIndicator(
                modifier = Modifier.height(2.dp),
                progress = progress.value
            )
        }

        // Adding a WebView inside AndroidView
        // with layout as full screen
        AndroidView(
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    settings.javaScriptEnabled = false

                    if(WebViewFeature.isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING))
                        WebSettingsCompat.setAlgorithmicDarkeningAllowed(settings,true)

                    webViewClient = object : WebViewClient(){
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            visibility.value = true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            visibility.value = false
                        }
                    }

                    webChromeClient= object : WebChromeClient(){
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            super.onProgressChanged(view, newProgress)
                            progress.value = newProgress.toFloat()
                        }
                    }

                    loadUrl(url)
                }
            },
            update = { it.loadUrl(url) }
        )
    }

}
