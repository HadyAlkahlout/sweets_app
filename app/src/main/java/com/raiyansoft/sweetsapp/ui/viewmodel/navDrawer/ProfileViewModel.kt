package com.raiyansoft.sweetsapp.ui.viewmodel.navDrawer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.models.login.UserLogin
import com.raiyansoft.sweetsapp.models.profile.UpdateProfile
import com.raiyansoft.sweetsapp.repository.AppRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataProfile = MutableLiveData<GeneralResponse<UserLogin>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.LANGUAGE, "ar"
    )!!
    private val token = Commons.getSharedPreferences(application.applicationContext)
        .getString(Commons.SERVER_TOKEN, "")!!

    private suspend fun update(profile: UpdateProfile) {
        val response = repository.updateProfile(lang, token, profile)
        if (response.isSuccessful) {
            withContext(Dispatchers.Main){
                dataProfile.value = response.body()
            }
        }
    }

    fun updateProfile(profile: UpdateProfile) {
        viewModelScope.launch(Dispatchers.IO) {
            update(profile)
        }
    }


}