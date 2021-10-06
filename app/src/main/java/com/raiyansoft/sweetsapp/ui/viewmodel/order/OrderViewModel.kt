package com.raiyansoft.sweetsapp.ui.viewmodel.order

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.genral.GeneralPaginateResponse
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.models.home.Category
import com.raiyansoft.sweetsapp.models.order.OrderDetails
import com.raiyansoft.sweetsapp.models.order.PrevOrder
import com.raiyansoft.sweetsapp.models.product.Product
import com.raiyansoft.sweetsapp.repository.AppRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataOrders = MutableLiveData<GeneralPaginateResponse<PrevOrder>>()
    val dataOrder = MutableLiveData<GeneralResponse<OrderDetails>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.LANGUAGE, "ar")!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.SERVER_TOKEN, "")!!
    private val location = Commons.getLocation(application.applicationContext)

    fun getOrders(page : Int, type : String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getPrevOrders(lang, token, location.area_id, type, page)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataOrders.value = response.body()
                }
            }
        }
    }

    fun getOrder(id : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("TAG", "getOrder: ${location.area_id}, $id")
            val response = repository.getOrderDetails(lang, token, location.area_id, id)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    dataOrder.value = response.body()
                }
            }
        }
    }


}