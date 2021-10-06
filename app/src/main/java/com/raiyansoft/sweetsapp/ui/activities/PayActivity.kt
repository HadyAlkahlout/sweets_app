package com.raiyansoft.sweetsapp.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.webkit.WebViewClient
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ActivityPayBinding
import com.raiyansoft.sweetsapp.util.Commons
import com.raiyansoft.sweetsapp.util.ConnectionLiveData
import com.raiyansoft.sweetsapp.util.LocalHelper
import java.util.*

class PayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPayBinding
    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        doInitialization()
    }

    private fun doInitialization() {
        var link = ""
        if (intent != null) {
            link = intent.getStringExtra("payURL")!!
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.webView.webViewClient = WebViewClient()
        connectionLiveData = ConnectionLiveData(this)
        binding.webView.loadUrl(link)
        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        connectionLiveData.observe(this, { isNetworkAvailable ->
            binding.isInternetConnect = isNetworkAvailable
        })
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {

        var lang = ""
        when (Locale.getDefault().displayLanguage) {
            "العربية" -> {
                lang = "ar"
            }
            "English" -> {
                lang = "en"
            }
        }
        val context = LocalHelper().setLocale(
            this,
            Commons.getSharedPreferences(this).getString(Commons.LANGUAGE, lang)!!
        )
        this.resources.updateConfiguration(
            context!!.resources.configuration,
            this.resources.displayMetrics
        )

        return super.onCreateView(name, context, attrs)
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
