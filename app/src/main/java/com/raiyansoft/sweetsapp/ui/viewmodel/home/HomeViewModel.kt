package com.raiyansoft.sweetsapp.ui.viewmodel.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.models.home.HomeResponse
import com.raiyansoft.sweetsapp.repository.AppRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataHome = MutableLiveData<GeneralResponse<HomeResponse>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.LANGUAGE, "ar")!!
    private val location = Commons.getLocation(application.applicationContext)

    private fun getDataHome() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getHome(lang, location.area_id)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataHome.value = response.body()
                }
            }
        }
    }

    fun regetHome(areaID: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getHome(lang, areaID)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataHome.value = response.body()
                }
            }
        }
    }

    init {
        getDataHome()
    }


}