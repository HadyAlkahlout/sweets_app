package com.raiyansoft.sweetsapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ActivitySplashBinding
import com.raiyansoft.sweetsapp.models.token.SetToken
import com.raiyansoft.sweetsapp.ui.viewmodel.auth.TokenViewModel
import com.raiyansoft.sweetsapp.util.Commons
import com.raiyansoft.sweetsapp.util.ConnectionLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : AppCompatActivity() {

    private lateinit var connectionLiveData: ConnectionLiveData
    private lateinit var binding: ActivitySplashBinding

    private val tokenViewModel by lazy {
        ViewModelProvider(this)[TokenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        doInitialization()
    }

    private fun doInitialization() {
        connectionLiveData = ConnectionLiveData(this)
        val videoPath = "android.resource://" + packageName + "/" + R.raw.splash_video
        binding.vvSplash.setVideoPath(videoPath)
        binding.vvSplash.start()
        binding.vvSplash.setOnCompletionListener{
            connectionLiveData.observe(this@SplashActivity, { isNetworkAvailable ->
                if (isNetworkAvailable) {
                    val id = Commons.getSharedPreferences(this@SplashActivity)
                        .getInt(Commons.USER_ID, 0)
                    val changeToken = Commons.getSharedPreferences(this@SplashActivity)
                        .getBoolean(Commons.CHANGE_TOKEN, false)
                    Log.e("TAG", "doInitialization: ${Commons.getSharedPreferences(this@SplashActivity)
                        .getString(Commons.DEVICE_TOKEN, "")}")
                    Log.e("TAG", "doInitialization: $id")
                    if (id == 0) {
                        doSomething()
                    } else {
                        Log.e("TAG", "doInitialization: changeToken : $changeToken")
                        if (changeToken) {
                            getFCMToken(id) {
                                Commons.getSharedEditor(this).putBoolean(Commons.CHANGE_TOKEN, false).apply()
                                doSomething()
                            }
                        } else {
                            doSomething()
                        }
                    }
                } else {
                    val builder = AlertDialog.Builder(this@SplashActivity)
                    builder.setTitle(getString(R.string.internet_issue))
                    builder.setMessage(getString(R.string.internet_issue_message))
                    builder.setPositiveButton(getString(R.string.ok)) { _, _ -> finish() }
                    val dialog = builder.create()
                    dialog.show()
                }
            })
        }
    }

    private fun doSomething() {
        val login = Commons.getSharedPreferences(this).getBoolean(Commons.LOGIN, false)
        if (login) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
            finish()
        }
    }

    private fun getFCMToken(id: Int, onSuccess: () -> Unit) {
        val token = Commons.getSharedPreferences(this)
            .getString(Commons.DEVICE_TOKEN, "")
        tokenViewModel.setToken(SetToken(id, token!!))
        tokenViewModel.dataSet.observe(this,
            {
                if (it.status == 200) {
                    onSuccess()
                } else {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

}