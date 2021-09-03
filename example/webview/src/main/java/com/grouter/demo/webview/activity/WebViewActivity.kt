package com.grouter.demo.webview.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.grouter.GRouter

import com.grouter.RouterActivity
import com.grouter.RouterField
import com.grouter.demo.webview.R
import com.grouter.hybrid.GRouterJavascriptInterface
import kotlinx.android.synthetic.main.activity_web_view.*

@Suppress("DEPRECATION")
@SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
@RouterActivity("/web_view")
class WebViewActivity : AppCompatActivity() {
    @RouterField
    var url: String? = null
    var javascriptInterface = GRouterJavascriptInterface(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GRouter.inject(this)
        title = javaClass.simpleName
        setContentView(R.layout.activity_web_view)
        web_view.settings.javaScriptEnabled = true
        web_view.addJavascriptInterface(javascriptInterface, "grouter")
        web_view.webChromeClient = WebChromeClient()
        web_view.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                javascriptInterface.setUrl(url)
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
        web_view.loadUrl("file:///android_asset/grouter_test.html")
        if (url != null) {
            web_view.loadUrl(url!!)
        }
    }

    override fun onBackPressed() {
        if (web_view.canGoBack()) {
            web_view.goBack()
            return
        }
        super.onBackPressed()
    }
}
