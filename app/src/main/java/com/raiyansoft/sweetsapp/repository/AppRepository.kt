package com.raiyansoft.sweetsapp.repository

import com.raiyansoft.sweetsapp.models.address.CreateLocation
import com.raiyansoft.sweetsapp.models.address.Location
import com.raiyansoft.sweetsapp.models.appInfo.ContactUs
import com.raiyansoft.sweetsapp.models.cart.SubmitCart
import com.raiyansoft.sweetsapp.models.cart.SubmitDetails
import com.raiyansoft.sweetsapp.models.product.AddCart
import com.raiyansoft.sweetsapp.models.product.Fav
import com.raiyansoft.sweetsapp.models.profile.UpdateProfile
import com.raiyansoft.sweetsapp.models.token.DeleteToken
import com.raiyansoft.sweetsapp.models.token.SetToken
import com.raiyansoft.sweetsapp.network.ServiceBuilder
import okhttp3.RequestBody

class AppRepository {

    suspend fun updateProfile(
        lang: String,
        token: String,
        profile: UpdateProfile
    ) = ServiceBuilder.apis!!.updateProfile(lang, token, profile)

    suspend fun getHome(lang: String) = ServiceBuilder.apis!!.getHome(lang)

    suspend fun getAllCategories(
        lang: String,
        page: Int
    ) = ServiceBuilder.apis!!.getAllCategories(lang, page)

    suspend fun getCategoryProducts(
        lang: String,
        id: Int,
        page: Int,
        query : String
    ) = ServiceBuilder.apis!!.getCategoryProducts(lang, id, page, query)

    suspend fun getAllOccasions(
        lang: String,
        page: Int
    ) = ServiceBuilder.apis!!.getAllOccasions(lang, page)

    suspend fun getOccasionProducts(
        lang: String,
        id: Int,
        page: Int,
        query : String
    ) = ServiceBuilder.apis!!.getOccasionProducts(lang, id, page, query)

    suspend fun getAllStores(
        lang: String,
        page: Int,
        query : String
    ) = ServiceBuilder.apis!!.getAllStores(lang, page, query)

    suspend fun getStoreProducts(
        lang: String,
        id: Int,
    ) = ServiceBuilder.apis!!.getStoreProducts(lang, id)

    suspend fun getStoreData(
        lang: String,
        id: Int,
    ) = ServiceBuilder.apis!!.getStoreData(lang, id)

    suspend fun getProduct(
        lang: String,
        id: Int
    ) = ServiceBuilder.apis!!.getProduct(lang, id)

    suspend fun getProduct(
        lang: String,
        token: String,
        id: Int
    ) = ServiceBuilder.apis!!.getProduct(lang, token, id)

    suspend fun setFav(
        lang: String,
        token: String,
        id: Fav
    ) = ServiceBuilder.apis!!.setFav(lang, token, id)

    suspend fun contactPages() = ServiceBuilder.apis!!.contactPages()

    suspend fun contactUs(
        lang: String,
        token: String,
        contactUs: ContactUs
    ) = ServiceBuilder.apis!!.contactUs(lang, token, contactUs)

    suspend fun aboutUs(lang: String) = ServiceBuilder.apis!!.aboutUs(lang)

    suspend fun terms(lang: String) = ServiceBuilder.apis!!.terms(lang)

    suspend fun myFav(
        lang: String,
        token: String
    ) = ServiceBuilder.apis!!.myFav(lang, token)

    suspend fun myAddress (
        lang: String,
        token: String
    ) = ServiceBuilder.apis!!.myAddress(lang, token)

    suspend fun deleteAddress (
        lang: String,
        token: String,
        id : Int
    ) = ServiceBuilder.apis!!.deleteAddress(lang, token, id)

    suspend fun updateAddress (
        lang: String,
        token: String,
        id : Int,
        location : CreateLocation
    ) = ServiceBuilder.apis!!.updateAddress(lang, token, id, location)

    suspend fun addToCart (
        lang: String,
        token: String,
        id : Int,
        addCart : AddCart
    ) = ServiceBuilder.apis!!.addToCart(lang, token, id, addCart)

    suspend fun removeFromCart (
        lang: String,
        token: String,
        id : Int
    ) = ServiceBuilder.apis!!.removeFromCart(lang, token, id)

    suspend fun getCart (
        lang: String,
        token: String
    ) = ServiceBuilder.apis!!.getCart(lang, token)

    suspend fun submitDetails (
        lang: String,
        token: String,
        submitDetails : SubmitDetails
    ) = ServiceBuilder.apis!!.submitDetails(lang, token, submitDetails)

    suspend fun getPrevOrders (
        lang: String,
        token: String,
        type : String,
        page : Int
    ) = ServiceBuilder.apis!!.getPrevOrders(lang, token, type, page)

    suspend fun getOrderDetails (
        lang: String,
        token: String,
        id : Int
    ) = ServiceBuilder.apis!!.getOrderDetails(lang, token, id)

    suspend fun setToken (
        setToken : SetToken
    ) = ServiceBuilder.apis!!.setToken(setToken)

    suspend fun deleteToken (
        deleteToken : DeleteToken
    ) = ServiceBuilder.apis!!.deleteToken(deleteToken)

    suspend fun getFilterData (
        lang: String
    ) = ServiceBuilder.apis!!.getFilterData(lang)

    suspend fun getFilterProduct (
        lang: String,
        page: Int,
        params: Map<String, RequestBody>
    ) = ServiceBuilder.apis!!.getFilterProduct(lang, page, params)

    suspend fun getAreas (
        lang: String,
        token: String
    ) = ServiceBuilder.apis!!.getAreas(lang, token)

    suspend fun submitCart (
        lang: String,
        token: String,
        submitCart: SubmitCart
    ) = ServiceBuilder.apis!!.submitCart(lang, token, submitCart)

}