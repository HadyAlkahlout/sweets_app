package com.raiyansoft.sweetsapp.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.models.address.Location
import com.raiyansoft.sweetsapp.ui.activities.AuthActivity
import com.raiyansoft.sweetsapp.ui.dialogs.IndeterminateProgressDialog
import java.lang.ClassCastException
import java.util.*

object Commons {

    const val LANGUAGE = "lang"
    const val LOGIN = "login"
    const val IS_SENT_LOC = "isSentLoc"
    const val DEVICE_TOKEN = "deviceToken"
    const val SERVER_TOKEN = "serverToken"
    const val CHANGE_TOKEN = "changeToken"
    const val USER_ID = "userID"
    const val USERNAME = "userName"
    const val USER_PHONE = "userPhone"
    const val USER_EMAIL = "userEmail"
    const val USER_SEX = "userSex"
    const val USER_BIRTH_DATE = "userBirth"
    const val OPEN_ORDER = "openOrder"
    const val ORDER_ID = "orderID"
    const val KEY_MY_LOCATION = "myLocation"

    val dialog = IndeterminateProgressDialog()

    fun setLocale(lang: String, context: Context) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources
            .updateConfiguration(config, context.resources.displayMetrics)
    }

    fun getSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences("shared", Context.MODE_PRIVATE)

    fun getSharedEditor(context: Context): SharedPreferences.Editor =
        getSharedPreferences(context).edit()

    fun showLoginDialog(activity: Activity) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(activity.getString(R.string.warning))
        builder.setMessage(activity.getString(R.string.should_login))
        builder.setPositiveButton(activity.getString(R.string.go_login)) { _, _ ->
            activity.startActivity(Intent(activity, AuthActivity::class.java))
            activity.finish()
        }
        builder.setNegativeButton(activity.getString(R.string.no)) { view, _ -> view.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    fun showLoadingDialog(activity: FragmentActivity) {
        dialog.isCancelable = false
        dialog.show(activity.supportFragmentManager, "Loading")
    }

    fun dismissLoadingDialog() {
        dialog.dismiss()
    }


    fun getLocation(context: Context): Location {
        val gson = Gson()
        val json: String = getSharedPreferences(context)
            .getString(KEY_MY_LOCATION, "")!!
        return gson.fromJson(json, Location::class.java)
    }

}