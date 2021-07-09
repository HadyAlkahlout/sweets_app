package com.raiyansoft.sweetsapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.raiyansoft.sweetsapp.databinding.ActivityAuthBinding
import com.raiyansoft.sweetsapp.util.Commons
import com.raiyansoft.sweetsapp.util.ConnectionLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAuthBinding
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
        lifecycleScope.launch(Dispatchers.IO){
            delay(5000)
            withContext(Dispatchers.Main){
                connectionLiveData.observe(this@AuthActivity, { isNetworkAvailable ->
                    binding.internetConnection = isNetworkAvailable
                })
            }
        }
        val lang = Commons.getSharedPreferences(this).getString(Commons.LANGUAGE, "ar")
        Commons.setLocale(lang!!, this)
    }
}