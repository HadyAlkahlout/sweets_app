package com.raiyansoft.sweetsapp.ui.viewmodel.category

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

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataCategories = MutableLiveData<GeneralPaginateResponse<Category>>()
    val dataCategory = MutableLiveData<GeneralPaginateResponse<Product>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.LANGUAGE, "ar"
    )!!

    fun getCategories(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getAllCategories(lang, page)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    dataCategories.value = response.body()
                }
            }
            if (page == 1) {
                withContext(Dispatchers.Main) {
                    dataCategories.value = null
                }
            }
        }
    }

    fun getCategoryProducts(id: Int, page: Int, query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getCategoryProducts(lang, id, page, query)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    dataCategory.value = response.body()
                }
            }
        }
    }


}