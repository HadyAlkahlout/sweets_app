package com.raiyansoft.sweetsapp.ui.viewmodel.navDrawer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.appInfo.AboutUs
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.repository.AppRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AboutViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataAbout = MutableLiveData<GeneralResponse<AboutUs>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.LANGUAGE, "ar")!!
    private val location = Commons.getLocation(application.applicationContext)

    fun getAbout() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.aboutUs(lang, location.area_id)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main){
                    dataAbout.value = response.body()
                }
            }
        }
    }

}