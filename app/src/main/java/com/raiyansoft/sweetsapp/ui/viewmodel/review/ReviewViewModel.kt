package com.raiyansoft.sweetsapp.ui.viewmodel.review

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.models.reviews.Review
import com.raiyansoft.sweetsapp.models.store.UserRate
import com.raiyansoft.sweetsapp.repository.AppRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataReview = MutableLiveData<GeneralResponse<Review>>()
    val dataSend = MutableLiveData<GeneralResponse<String>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.LANGUAGE, "ar")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.SERVER_TOKEN, "")!!
    private val location = Commons.getLocation(application.applicationContext)

    fun getRates(storeID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getRates(lang, token, location.area_id, storeID)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataReview.value = response.body()
                }
            }
        }
    }

    fun sendRate(storeID: Int, userRate: UserRate) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.sendRate(lang, token, location.area_id, storeID, userRate)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataSend.value = response.body()
                }
            }
        }
    }


}