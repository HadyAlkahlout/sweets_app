package com.raiyansoft.sweetsapp.ui.viewmodel.navDrawer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raiyansoft.sweetsapp.models.favorite.FavProduct
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.repository.AppRepository
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository()

    val dataFavourite = MutableLiveData<GeneralResponse<List<FavProduct>>>()

    private val lang = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.LANGUAGE, "ar"
    )!!
    private val token = Commons.getSharedPreferences(application.applicationContext).getString(
        Commons.SERVER_TOKEN, ""
    )!!

    fun getFav() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.myFav(lang, token)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main){
                    dataFavourite.value = response.body()
                }
            }
        }
    }

}