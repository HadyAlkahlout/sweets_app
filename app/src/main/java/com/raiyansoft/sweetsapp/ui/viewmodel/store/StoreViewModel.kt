package com.raiyansoft.sweetsapp.ui.viewmodel.store

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.genral.GeneralPaginateResponse
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.models.home.Store
import com.raiyansoft.sweetsapp.models.store.StoreCategory
import com.raiyansoft.sweetsapp.models.store.StoreDetails
import com.raiyansoft.sweetsapp.repository.AppRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoreViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataStores = MutableLiveData<GeneralPaginateResponse<Store>>()
    val dataStore = MutableLiveData<GeneralResponse<List<StoreCategory>>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.LANGUAGE, "ar")!!
    private val location = Commons.getLocation(application.applicationContext)

    fun getStores(page : Int, query : String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getAllStores(lang, location.area_id, page, query)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataStores.value = response.body()
                }
            }
        }
    }

    fun getStoreProducts(id : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getStoreProducts(lang, location.area_id, id)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataStore.value = response.body()
                }
            }
        }
    }


}