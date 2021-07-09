package com.raiyansoft.sweetsapp.ui.viewmodel.occasion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.genral.GeneralPaginateResponse
import com.raiyansoft.sweetsapp.models.home.Category
import com.raiyansoft.sweetsapp.models.product.Product
import com.raiyansoft.sweetsapp.repository.AppRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OccasionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataOccasions = MutableLiveData<GeneralPaginateResponse<Category>>()
    val dataOccasion = MutableLiveData<GeneralPaginateResponse<Product>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.LANGUAGE, "ar")!!

    fun getOccasions(page : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getAllOccasions(lang, page)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataOccasions.value = response.body()
                }
            }else{
                if (page == 1){
                    withContext(Dispatchers.Main){
                        dataOccasions.value = null
                    }
                }
            }
        }
    }

    fun getOccasionProducts(id : Int, page : Int, query : String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getOccasionProducts(lang, id, page, query)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataOccasion.value = response.body()
                }
            }
        }
    }


}