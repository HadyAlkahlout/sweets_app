package com.raiyansoft.sweetsapp.ui.viewmodel.cart

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.cart.CartResponse
import com.raiyansoft.sweetsapp.models.cart.DetailsResponse
import com.raiyansoft.sweetsapp.models.cart.SubmitCart
import com.raiyansoft.sweetsapp.models.cart.SubmitDetails
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.models.product.*
import com.raiyansoft.sweetsapp.repository.AppRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataCart = MutableLiveData<GeneralResponse<CartResponse>>()
    val dataRemove = MutableLiveData<GeneralResponse<String>>()
    val dataSubmit = MutableLiveData<GeneralResponse<DetailsResponse>>()
    val dataSubmitCart = MutableLiveData<GeneralResponse<String>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.LANGUAGE, "ar")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.SERVER_TOKEN, "")!!

    fun getCart() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getCart(lang, token)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataCart.value = response.body()
                }
            }
        }
    }

    fun removeFromCart(id : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.removeFromCart(lang, token, id)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataRemove.value = response.body()
                }
            }
        }
    }

    fun submitDetails(submitDetails : SubmitDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.submitDetails(lang, token, submitDetails)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataSubmit.value = response.body()
                }
            }
        }
    }

    fun submitCart(submitCart: SubmitCart) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.submitCart(lang, token, submitCart)
            Log.e("TAG", "submitCart: $response")
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataSubmitCart.value = response.body()
                }
            }
        }
    }

}