package com.raiyansoft.sweetsapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ActivityMainBinding
import com.raiyansoft.sweetsapp.util.Commons
import com.raiyansoft.sweetsapp.util.ConnectionLiveData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
                connectionLiveData.observe(this@MainActivity, { isNetworkAvailable ->
                    binding.internetConnection = isNetworkAvailable
                })
            }
        }
        val lang = Commons.getSharedPreferences(this).getString(Commons.LANGUAGE, "ar")
        Commons.setLocale(lang!!, this)
    }


}