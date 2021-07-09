package com.raiyansoft.sweetsapp.ui.viewmodel.filter

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.filter.FilterData
import com.raiyansoft.sweetsapp.models.genral.GeneralPaginateResponse
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.models.product.Product
import com.raiyansoft.sweetsapp.repository.AppRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import java.util.*
import kotlin.collections.ArrayList

class FilterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataFilters = MutableLiveData<GeneralResponse<FilterData>>()
    val dataFilterProducts = MutableLiveData<GeneralPaginateResponse<Product>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.LANGUAGE, "ar")!!

    fun getFilters() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getFilterData(lang)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataFilters.value = response.body()
                }
            }
        }
    }

    fun getFilterProducts(
        page : Int,
        params: Map<String, RequestBody>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getFilterProduct(lang, page, params)
            Log.e("TAG", "getFilterProducts: $response")
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataFilterProducts.value = response.body()
                }
            }else{
                withContext(Dispatchers.Main){
                    dataFilterProducts.value = null
                }
            }
        }
    }


}