package com.raiyansoft.sweetsapp.ui.viewmodel.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.address.Location
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.models.location.LocationUpdate
import com.raiyansoft.sweetsapp.models.login.Login
import com.raiyansoft.sweetsapp.models.login.UserLogin
import com.raiyansoft.sweetsapp.models.token.DeleteToken
import com.raiyansoft.sweetsapp.models.token.SetToken
import com.raiyansoft.sweetsapp.models.verification.Verification
import com.raiyansoft.sweetsapp.repository.AppRepository
import com.raiyansoft.sweetsapp.repository.AuthRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TokenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataSet = MutableLiveData<GeneralResponse<String>>()
    val dataDelete = MutableLiveData<GeneralResponse<String>>()

    private val location = Commons.getLocation(application.applicationContext)

    fun setToken(setToken : SetToken) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.setToken(location.area_id, setToken)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataSet.value = response.body()
                }
            }
        }
    }

    fun deleteToken(deleteToken : DeleteToken) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.deleteToken(location.area_id, deleteToken)
            Log.e("TAG", "deleteToken: $response")
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataDelete.value = response.body()
                }
            }
        }
    }

}