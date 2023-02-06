package com.example.currentsnews.ui.screens

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.currentsnews.ui.theme.CurrentsNewsTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewsWebViewContainer() {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "News Title") }) },
        content = { WebContent(url = "https://www.dailysabah.com/sports/football/barca-dismantle-sevilla-going-8-points-clear-after-madrid-stumble") }
    )
}

@Composable
fun WebContent(url: String) {

    // Adding a WebView inside AndroidView
    // with layout as full screen
    AndroidView(
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        },
        update = { it.loadUrl(url) }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewWebView() {
    CurrentsNewsTheme {
        NewsWebViewContainer()
    }
}