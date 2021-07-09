package com.raiyansoft.sweetsapp.ui.viewmodel.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.address.CreateLocation
import com.raiyansoft.sweetsapp.models.address.Location
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.models.location.LocationUpdate
import com.raiyansoft.sweetsapp.models.login.Login
import com.raiyansoft.sweetsapp.models.login.UserLogin
import com.raiyansoft.sweetsapp.models.verification.Verification
import com.raiyansoft.sweetsapp.repository.AuthRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()

    val dataLogin = MutableLiveData<GeneralResponse<String>>()
    val dataLogout = MutableLiveData<GeneralResponse<String>>()
    val dataActive = MutableLiveData<GeneralResponse<UserLogin>>()
    val dataLocation = MutableLiveData<GeneralResponse<LocationUpdate>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(Commons.LANGUAGE, "ar")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(Commons.SERVER_TOKEN, "")!!

    private suspend fun login(login: Login) {
        val response = repository.login(lang, login)
        if (response.isSuccessful){
            withContext(Dispatchers.Main){
                dataLogin.value = response.body()
            }
        }
    }

    private suspend fun logout() {
        val response = repository.logout(token)
        if (response.isSuccessful){
            withContext(Dispatchers.Main){
                dataLogout.value = response.body()
            }
        }
    }

    private suspend fun active(verification : Verification) {
        val response = repository.activateAccount(lang, verification)
        if (response.isSuccessful){
            withContext(Dispatchers.Main){
                dataActive.value = response.body()
            }
        }
    }

    private suspend fun location(location : Location) {
        val response = repository.saveLocation(lang, token, location)
        if (response.isSuccessful){
            withContext(Dispatchers.Main){
                dataLocation.value = response.body()
            }
        }
    }

    private suspend fun location(location : CreateLocation) {
        val response = repository.saveLocation(lang, token, location)
        if (response.isSuccessful){
            withContext(Dispatchers.Main){
                dataLocation.value = response.body()
            }
        }
    }

    fun mackAccount(login: Login) {
        viewModelScope.launch(Dispatchers.IO) {
            login(login)
        }
    }

    fun leaveAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            logout()
        }
    }

    fun activateAccount(verification : Verification) {
        viewModelScope.launch(Dispatchers.IO) {
            active(verification)
        }
    }

    fun saveLocation(location : Location) {
        viewModelScope.launch(Dispatchers.IO) {
            location(location)
        }
    }

    fun saveLocation(location : CreateLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            location(location)
        }
    }

}