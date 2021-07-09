package com.raiyansoft.sweetsapp.ui.viewmodel.navDrawer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.appInfo.Terms
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.repository.AppRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TermsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataTerms = MutableLiveData<GeneralResponse<Terms>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.LANGUAGE, "ar"
    )!!

    fun getTerms() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.terms(lang)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main){
                    dataTerms.value = response.body()
                }
            }
        }
    }

}