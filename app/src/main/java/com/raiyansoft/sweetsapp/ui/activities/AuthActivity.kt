package com.raiyansoft.sweetsapp.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.raiyansoft.sweetsapp.databinding.ActivityAuthBinding
import com.raiyansoft.sweetsapp.util.Commons
import com.raiyansoft.sweetsapp.util.ConnectionLiveData
import com.raiyansoft.sweetsapp.util.LocalHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        doInitialization()
    }

    private fun doInitialization() {
        connectionLiveData = ConnectionLiveData(this)
        binding.internetConnection = true
        lifecycleScope.launch(Dispatchers.IO) {
            delay(5000)
            withContext(Dispatchers.Main) {
                connectionLiveData.observe(this@AuthActivity, { isNetworkAvailable ->
                    binding.internetConnection = isNetworkAvailable
                })
            }
        }
        val lang = Commons.getSharedPreferences(this).getString(Commons.LANGUAGE, "ar")
        Commons.setLocale(lang!!, this)
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
}
