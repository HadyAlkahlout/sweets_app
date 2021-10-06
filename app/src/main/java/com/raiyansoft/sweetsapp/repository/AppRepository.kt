package com.raiyansoft.sweetsapp.repository

import com.raiyansoft.sweetsapp.models.address.CreateLocation
import com.raiyansoft.sweetsapp.models.address.Location
import com.raiyansoft.sweetsapp.models.appInfo.ContactUs
import com.raiyansoft.sweetsapp.models.cart.ChangeQuantity
import com.raiyansoft.sweetsapp.models.cart.SubmitCart
import com.raiyansoft.sweetsapp.models.cart.SubmitDetails
import com.raiyansoft.sweetsapp.models.product.AddCart
import com.raiyansoft.sweetsapp.models.product.Fav
import com.raiyansoft.sweetsapp.models.profile.UpdateProfile
import com.raiyansoft.sweetsapp.models.store.UserRate
import com.raiyansoft.sweetsapp.models.token.DeleteToken
import com.raiyansoft.sweetsapp.models.token.SetToken
import com.raiyansoft.sweetsapp.network.ServiceBuilder
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header

class AppRepository {

    suspend fun updateProfile(
        lang: String,
        token: String,
        areaId: Int,
        profile: UpdateProfile
    ) = ServiceBuilder.apis!!.updateProfile(lang, token, areaId, profile)

    suspend fun getHome(lang: String, areaId: Int) = ServiceBuilder.apis!!.getHome(lang, areaId)

    suspend fun getAllCategories(
        lang: String,
        areaId: Int,
        page: Int
    ) = ServiceBuilder.apis!!.getAllCategories(lang, areaId, page)

    suspend fun getCategoryProducts(
        lang: String,
        areaId: Int,
        id: Int,
        page: Int,
        query: String
    ) = ServiceBuilder.apis!!.getCategoryProducts(lang, areaId, id, page, query)

    suspend fun getAllOccasions(
        lang: String,
        areaId: Int,
        page: Int
    ) = ServiceBuilder.apis!!.getAllOccasions(lang, areaId, page)

    suspend fun getOccasionProducts(
        lang: String,
        areaId: Int,
        id: Int,
        page: Int,
        query: String
    ) = ServiceBuilder.apis!!.getOccasionProducts(lang, areaId, id, page, query)

    suspend fun getAllStores(
        lang: String,
        areaId: Int,
        page: Int,
        query: String
    ) = ServiceBuilder.apis!!.getAllStores(lang, areaId, page, query)

    suspend fun getStoreProducts(
        lang: String,
        areaId: Int,
        id: Int,
    ) = ServiceBuilder.apis!!.getStoreProducts(lang, areaId, id)

    suspend fun getProduct(
        lang: String,
        areaId: Int,
        id: Int
    ) = ServiceBuilder.apis!!.getProduct(lang, areaId, id)

    suspend fun getProduct(
        lang: String,
        token: String,
        areaId: Int,
        id: Int
    ) = ServiceBuilder.apis!!.getProduct(lang, token, areaId, id)

    suspend fun setFav(
        lang: String,
        token: String,
        areaId: Int,
        id: Fav
    ) = ServiceBuilder.apis!!.setFav(lang, token, areaId, id)

    suspend fun contactPages(areaId: Int) = ServiceBuilder.apis!!.contactPages(areaId)

    suspend fun contactUs(
        lang: String,
        token: String,
        areaId: Int,
        contactUs: ContactUs
    ) = ServiceBuilder.apis!!.contactUs(lang, token, areaId, contactUs)

    suspend fun aboutUs(lang: String, areaId: Int) = ServiceBuilder.apis!!.aboutUs(lang, areaId)

    suspend fun terms(lang: String, areaId: Int) = ServiceBuilder.apis!!.terms(lang, areaId)

    suspend fun myFav(
        lang: String,
        token: String,
        areaId: Int
    ) = ServiceBuilder.apis!!.myFav(lang, token, areaId)

    suspend fun myAddress(
        lang: String,
        token: String,
        areaId: Int
    ) = ServiceBuilder.apis!!.myAddress(lang, token, areaId)

    suspend fun deleteAddress(
        lang: String,
        token: String,
        areaId: Int,
        id: Int
    ) = ServiceBuilder.apis!!.deleteAddress(lang, token, areaId, id)

    suspend fun updateAddress(
        lang: String,
        token: String,
        id: Int,
        location: CreateLocation
    ) = ServiceBuilder.apis!!.updateAddress(lang, token, id, location)

    suspend fun addToCart(
        lang: String,
        token: String,
        areaId: Int,
        id: Int,
        addCart: AddCart
    ) = ServiceBuilder.apis!!.addToCart(lang, token, areaId, id, addCart)

    suspend fun removeFromCart(
        lang: String,
        token: String,
        areaId: Int,
        id: Int
    ) = ServiceBuilder.apis!!.removeFromCart(lang, token, areaId, id)

    suspend fun getCart(
        lang: String,
        token: String,
        areaId: Int
    ) = ServiceBuilder.apis!!.getCart(lang, token, areaId)

    suspend fun changeQuantity(
        lang: String,
        token: String,
        areaId: Int,
        productID: Int,
        changeQuantity: ChangeQuantity
    ) = ServiceBuilder.apis!!.changeQuantity(lang, token, areaId, productID, changeQuantity)

    suspend fun submitDetails(
        lang: String,
        token: String,
        areaId: Int,
        submitDetails: SubmitDetails
    ) = ServiceBuilder.apis!!.submitDetails(lang, token, areaId, submitDetails)

    suspend fun getPrevOrders(
        lang: String,
        token: String,
        areaId: Int,
        type: String,
        page: Int
    ) = ServiceBuilder.apis!!.getPrevOrders(lang, token, areaId, type, page)

    suspend fun getOrderDetails(
        lang: String,
        token: String,
        areaId: Int,
        id: Int
    ) = ServiceBuilder.apis!!.getOrderDetails(lang, token, areaId, id)

    suspend fun setToken(
        areaId: Int,
        setToken: SetToken
    ) = ServiceBuilder.apis!!.setToken(areaId, setToken)

    suspend fun deleteToken(
        areaId: Int,
        deleteToken: DeleteToken
    ) = ServiceBuilder.apis!!.deleteToken(areaId, deleteToken)

    suspend fun getFilterData(
        lang: String,
        areaId: Int
    ) = ServiceBuilder.apis!!.getFilterData(lang, areaId)

    suspend fun getFilterProduct(
        lang: String,
        areaId: Int,
        page: Int,
        params: Map<String, RequestBody>
    ) = ServiceBuilder.apis!!.getFilterProduct(lang, areaId, page, params)

    suspend fun getAreas(
        lang: String,
        token: String
    ) = ServiceBuilder.apis!!.getAreas(lang, token)

    suspend fun submitCart(
        lang: String,
        token: String,
        areaId: Int,
        submitCart: SubmitCart
    ) = ServiceBuilder.apis!!.submitCart(lang, token, areaId, submitCart)

    suspend fun getRates(
        lang: String,
        token: String,
        areaId: Int,
        storeID: Int
    ) = ServiceBuilder.apis!!.getRates(lang, token, areaId, storeID)

    suspend fun sendRate(
        lang: String,
        token: String,
        areaId: Int,
        storeID: Int,
        userRate: UserRate
    ) = ServiceBuilder.apis!!.sendRate(lang, token, areaId, storeID, userRate)

}