package neobis.o.ui.post

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web_view.*
import neobis.alier.bilimkana.ui.BaseActivity
import neobis.o.R

/**
 * Created by Alier on 18.07.2018.
 */
class WebViewActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val url = "https://o.kg/ru/chastnym-klientam/"
        webView.loadUrl(url)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
                super.onPageFinished(view, url)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebViewSettings() {
        val settings = webView.settings
        settings.javaScriptEnabled = true


    }

}