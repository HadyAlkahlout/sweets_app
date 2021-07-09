package com.raiyansoft.sweetsapp.ui.viewmodel.areas

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.cities.City
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.repository.AppRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AreaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataAreas = MutableLiveData<GeneralResponse<List<City>>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.LANGUAGE, "ar")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.SERVER_TOKEN, "")!!

    fun getAreas() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getAreas(lang, token)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataAreas.value = response.body()
                }
            }
        }
    }

}