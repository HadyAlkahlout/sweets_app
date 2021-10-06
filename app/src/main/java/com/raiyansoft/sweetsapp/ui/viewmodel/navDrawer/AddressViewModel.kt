package com.raiyansoft.sweetsapp.ui.viewmodel.navDrawer

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.address.CreateLocation
import com.raiyansoft.sweetsapp.models.address.UpdateAddressResponse
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.repository.AppRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddressViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataAddress = MutableLiveData<GeneralResponse<List<UpdateAddressResponse>>>()
    val dataDelete = MutableLiveData<GeneralResponse<String>>()
    val dataUpdate = MutableLiveData<GeneralResponse<UpdateAddressResponse>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.LANGUAGE, "ar"
    )!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.SERVER_TOKEN, ""
    )!!
    private val location = Commons.getLocation(application.applicationContext)

    fun getAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.myAddress(lang, token, location.area_id)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main){
                    dataAddress.value = response.body()
                }
            }
        }
    }

    fun deleteAddress(id : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.deleteAddress(lang, token, location.area_id, id)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main){
                    dataDelete.value = response.body()
                }
            }
        }
    }

    fun updateAddress(id : Int, location : CreateLocation) {
        viewModelScope.launch(Dispatchers.IO) {

            Log.e("TAG", "updateAddress: send update")
            val response = repository.updateAddress(lang, token, id, location)
            Log.e("TAG", "updateAddress: response : $response")
            if (response.isSuccessful) {
                withContext(Dispatchers.Main){
                    dataUpdate.value = response.body()
                }
            }
        }
    }

}