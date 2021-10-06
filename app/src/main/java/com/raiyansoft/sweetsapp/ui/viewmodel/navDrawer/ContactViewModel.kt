package com.raiyansoft.sweetsapp.ui.viewmodel.navDrawer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.appInfo.ContactPage
import com.raiyansoft.sweetsapp.models.appInfo.ContactUs
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.repository.AppRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataPages = MutableLiveData<GeneralResponse<ContactPage>>()
    val dataContact = MutableLiveData<GeneralResponse<String>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.LANGUAGE, "ar"
    )!!
    private val token = Commons.getSharedPreferences(application.applicationContext)
        .getString(Commons.SERVER_TOKEN, "")!!
    private val location = Commons.getLocation(application.applicationContext)

    private suspend fun contact(contactUs: ContactUs) {
        val response = repository.contactUs(lang, token, location.area_id, contactUs)
        if (response.isSuccessful) {
            withContext(Dispatchers.Main){
                dataContact.value = response.body()
            }
        }
    }

    fun getPages() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.contactPages(location.area_id)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main){
                    dataPages.value = response.body()
                }
            }
        }
    }

    fun contactUs(contactUs: ContactUs) {
        viewModelScope.launch(Dispatchers.IO) {
            contact(contactUs)
        }
    }


}