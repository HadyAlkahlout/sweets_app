package com.raiyansoft.sweetsapp.ui.viewmodel.product

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.genral.GeneralPaginateResponse
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.models.home.Store
import com.raiyansoft.sweetsapp.models.product.*
import com.raiyansoft.sweetsapp.repository.AppRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.RequestBody

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataProduct = MutableLiveData<GeneralResponse<ProductDetails>>()
    val dataFav = MutableLiveData<GeneralResponse<FavResponse>>()
    val dataAdd = MutableLiveData<GeneralResponse<String>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.LANGUAGE, "ar")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.SERVER_TOKEN, "")!!
    private val location = Commons.getLocation(application.applicationContext)

    fun getProduct(id : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getProduct(lang, location.area_id, id)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataProduct.value = response.body()
                }
            }
        }
    }

    fun getProduct(token: String, id : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getProduct(lang, token, location.area_id, id)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataProduct.value = response.body()
                }
            }
        }
    }

    fun setFav(id : Fav) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.setFav(lang, token, location.area_id, id)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataFav.value = response.body()
                }
            }else{
                Log.e("TAG", "setFav: fail fav")
            }
        }
    }

    fun addToCart(id : Int, addCart: AddCart) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.addToCart(lang, token, location.area_id, id, addCart)
            Log.e("TAG", "addToCart: $response")
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataAdd.value = response.body()
                }
            }
        }
    }

}