package com.googlefinance.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.googlefinance.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure WebView
        val webView: WebView = binding.webview
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true

        // Load default (SET Index) on start (Google Finance may use different symbol conventions)
        binding.symbolField.setText("SET%3ASET") // placeholder; user can change

        fun loadSymbol(symbol: String) {
            // Best-effort Google Finance URL. For tickers that don't work, open google finance homepage.
            val url = if (symbol.trim().isEmpty()) {
                "https://www.google.com/finance"
            } else {
                // attempt: https://www.google.com/finance/quote/{SYMBOL}
                val s = symbol.trim()
                "https://www.google.com/finance/quote/" + s
            }
            webView.loadUrl(url)
        }

        binding.loadBtn.setOnClickListener {
            val symbol = binding.symbolField.text.toString()
            loadSymbol(symbol)
        }

        // allow "enter" to load
        binding.symbolField.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                binding.loadBtn.performClick()
                true
            } else false
        }

        // initial load
        loadSymbol(binding.symbolField.text.toString())
    }
}
