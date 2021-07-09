package com.raiyansoft.sweetsapp.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.ui.activities.SplashActivity
import com.raiyansoft.sweetsapp.ui.viewmodel.auth.TokenViewModel
import com.raiyansoft.sweetsapp.util.Commons
import java.util.*
import kotlin.math.log

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var myNotificationManager: MyNotificationManager
    private lateinit var sharedPreferences : SharedPreferences

    override fun onNewToken(token: String) {
        sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(Commons.DEVICE_TOKEN, token).apply()
        sharedPreferences.edit().putBoolean(Commons.CHANGE_TOKEN, true).apply()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("TAG", "onMessageReceived")

        myNotificationManager = MyNotificationManager(this)
        Log.e("TAG", "onMessageReceived: notification : ${remoteMessage.notification!!}\ndata : ${remoteMessage.data}")
        if (remoteMessage.data.isNullOrEmpty()) {
            Log.e("TAG", "onMessageReceived: Title : ${remoteMessage.notification!!.title}\nBody : ${remoteMessage.notification!!.body}")
            Log.e("TAG", "onMessageReceived: ${remoteMessage.notification!!}")
            myNotificationManager.showNotification(
                1, remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!,
                Intent(
                    applicationContext,
                    SplashActivity::class.java
                )
            )
        } else {
            if (remoteMessage.data["type"]!! == "status"){
                sharedPreferences.edit().putInt(Commons.ORDER_ID, remoteMessage.data["cart_id"]!!.toInt()).apply()
                sharedPreferences.edit().putBoolean(Commons.OPEN_ORDER, true).apply()
                myNotificationManager.showNotification(
                    1, remoteMessage.data["title"]!!, remoteMessage.data["message"]!!,
                    Intent(
                        applicationContext,
                        SplashActivity::class.java
                    )
                )
            }else{
                myNotificationManager.showNotification(
                    1, remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!,
                    Intent(
                        applicationContext,
                        SplashActivity::class.java
                    )
                )
            }
        }
    }

}