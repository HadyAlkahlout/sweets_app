package com.raiyansoft.sweetsapp.network

import com.raiyansoft.sweetsapp.models.address.CreateLocation
import com.raiyansoft.sweetsapp.models.address.Location
import com.raiyansoft.sweetsapp.models.address.UpdateAddressResponse
import com.raiyansoft.sweetsapp.models.appInfo.AboutUs
import com.raiyansoft.sweetsapp.models.appInfo.ContactPage
import com.raiyansoft.sweetsapp.models.appInfo.ContactUs
import com.raiyansoft.sweetsapp.models.appInfo.Terms
import com.raiyansoft.sweetsapp.models.cart.CartResponse
import com.raiyansoft.sweetsapp.models.cart.DetailsResponse
import com.raiyansoft.sweetsapp.models.cart.SubmitCart
import com.raiyansoft.sweetsapp.models.cart.SubmitDetails
import com.raiyansoft.sweetsapp.models.cities.City
import com.raiyansoft.sweetsapp.models.favorite.FavProduct
import com.raiyansoft.sweetsapp.models.filter.FilterData
import com.raiyansoft.sweetsapp.models.genral.GeneralPaginateResponse
import com.raiyansoft.sweetsapp.models.genral.GeneralResponse
import com.raiyansoft.sweetsapp.models.home.Category
import com.raiyansoft.sweetsapp.models.home.HomeResponse
import com.raiyansoft.sweetsapp.models.home.Store
import com.raiyansoft.sweetsapp.models.location.LocationUpdate
import com.raiyansoft.sweetsapp.models.login.Login
import com.raiyansoft.sweetsapp.models.login.UserLogin
import com.raiyansoft.sweetsapp.models.order.OrderDetails
import com.raiyansoft.sweetsapp.models.order.PrevOrder
import com.raiyansoft.sweetsapp.models.product.*
import com.raiyansoft.sweetsapp.models.profile.UpdateProfile
import com.raiyansoft.sweetsapp.models.store.StoreCategory
import com.raiyansoft.sweetsapp.models.store.StoreDetails
import com.raiyansoft.sweetsapp.models.token.DeleteToken
import com.raiyansoft.sweetsapp.models.token.SetToken
import com.raiyansoft.sweetsapp.models.verification.Verification
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @POST("login")
    suspend fun login(
        @Header("x-localization") lang : String,
        @Body login : Login
    ) : Response<GeneralResponse<String>>

    @POST("logout")
    suspend fun logout(
        @Header("Authorization") token : String
    ) : Response<GeneralResponse<String>>

    @POST("active")
    suspend fun activateAccount(
        @Header("x-localization") lang : String,
        @Body verification : Verification
    ) : Response<GeneralResponse<UserLogin>>

    @POST("save-location")
    suspend fun saveLocation(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String,
        @Body location : Location
    ) : Response<GeneralResponse<LocationUpdate>>

    @POST("save-location")
    suspend fun saveLocation(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String,
        @Body location : CreateLocation
    ) : Response<GeneralResponse<LocationUpdate>>

    @POST("update-profile")
    suspend fun updateProfile(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String,
        @Body profile : UpdateProfile
    ) : Response<GeneralResponse<UserLogin>>

    @GET("home")
    suspend fun getHome(
        @Header("x-localization") lang : String
    ) : Response<GeneralResponse<HomeResponse>>

    @GET("categories")
    suspend fun getAllCategories(
        @Header("x-localization") lang : String,
        @Query("page") page : Int
    ) : Response<GeneralPaginateResponse<Category>>

    @GET("categories/{id}")
    suspend fun getCategoryProducts(
        @Header("x-localization") lang : String,
        @Path("id") id : Int,
        @Query("page") page : Int,
        @Query("q") query : String
    ) : Response<GeneralPaginateResponse<Product>>

    @GET("occasions")
    suspend fun getAllOccasions(
        @Header("x-localization") lang : String,
        @Query("page") page : Int
    ) : Response<GeneralPaginateResponse<Category>>

    @GET("occasions/{id}")
    suspend fun getOccasionProducts(
        @Header("x-localization") lang : String,
        @Path("id") id : Int,
        @Query("page") page : Int,
        @Query("q") query : String
    ) : Response<GeneralPaginateResponse<Product>>

    @GET("stores")
    suspend fun getAllStores(
        @Header("x-localization") lang : String,
        @Query("page") page : Int,
        @Query("q") query : String
    ) : Response<GeneralPaginateResponse<Store>>

    @GET("stores/{id}/show")
    suspend fun getStoreProducts(
        @Header("x-localization") lang : String,
        @Path("id") id : Int,
    ) : Response<GeneralResponse<List<StoreCategory>>>

    @GET("stores/{id}")
    suspend fun getStoreData(
        @Header("x-localization") lang : String,
        @Path("id") id : Int,
    ) : Response<GeneralResponse<StoreDetails>>

    @GET("products/{id}")
    suspend fun getProduct(
        @Header("x-localization") lang : String,
        @Path("id") id : Int
    ) : Response<GeneralResponse<ProductDetails>>

    @GET("products/{id}")
    suspend fun getProduct(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String,
        @Path("id") id : Int
    ) : Response<GeneralResponse<ProductDetails>>

    @POST("add-favorite/product")
    suspend fun setFav(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String,
        @Body id : Fav
    ) : Response<GeneralResponse<FavResponse>>

    @GET("page/contact")
    suspend fun contactPages() : Response<GeneralResponse<ContactPage>>

    @POST("contact-us")
    suspend fun contactUs(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String,
        @Body contactUs: ContactUs
    ) : Response<GeneralResponse<String>>

    @GET("page/about-us")
    suspend fun aboutUs(
        @Header("x-localization") lang : String
    ) : Response<GeneralResponse<AboutUs>>

    @GET("page/terms")
    suspend fun terms(
        @Header("x-localization") lang : String
    ) : Response<GeneralResponse<Terms>>

    @GET("my-favorites")
    suspend fun myFav(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String
    ) : Response<GeneralResponse<List<FavProduct>>>

    @GET("my-addresses")
    suspend fun myAddress(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String
    ) : Response<GeneralResponse<List<UpdateAddressResponse>>>

    @POST("delete-address/{id}")
    suspend fun deleteAddress(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String,
        @Path("id") id : Int
    ) : Response<GeneralResponse<String>>

    @POST("update-address/{id}")
    suspend fun updateAddress(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String,
        @Path("id") id : Int,
        @Body location : CreateLocation
    ) : Response<GeneralResponse<UpdateAddressResponse>>

    @POST("add-to-cart/{id}")
    suspend fun addToCart(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String,
        @Path("id") id : Int,
        @Body addCart : AddCart
    ) : Response<GeneralResponse<String>>

    @POST("remove-from-cart/{id}")
    suspend fun removeFromCart(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String,
        @Path("id") id : Int,
    ) : Response<GeneralResponse<String>>

    @GET("my-cart")
    suspend fun getCart(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String
    ) : Response<GeneralResponse<CartResponse>>

    @POST("submit-details")
    suspend fun submitDetails(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String,
        @Body submitDetails : SubmitDetails
    ) : Response<GeneralResponse<DetailsResponse>>

    @GET("carts/{type}")
    suspend fun getPrevOrders(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String,
        @Path("type") type : String,
        @Query("page") page : Int
    ) : Response<GeneralPaginateResponse<PrevOrder>>

    @GET("carts/{id}/show")
    suspend fun getOrderDetails(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String,
        @Path("id") id : Int
    ) : Response<GeneralResponse<OrderDetails>>

    @POST("set-token")
    suspend fun setToken(
        @Body setToken : SetToken
    ) : Response<GeneralResponse<String>>

    @POST("delete-token")
    suspend fun deleteToken(
        @Body deleteToken : DeleteToken
    ) : Response<GeneralResponse<String>>

    @GET("filter-data")
    suspend fun getFilterData(
        @Header("x-localization") lang : String
    ) : Response<GeneralResponse<FilterData>>

    @Multipart
    @POST("filter-products")
    suspend fun getFilterProduct(
        @Header("x-localization") lang : String,
        @Query("page") page : Int,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>
    ) : Response<GeneralPaginateResponse<Product>>

    @GET("areas")
    suspend fun getAreas(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String
    ) : Response<GeneralResponse<List<City>>>

    @POST("submit-cart")
    suspend fun submitCart(
        @Header("x-localization") lang : String,
        @Header("Authorization") token : String,
        @Body submitCart: SubmitCart
    ) : Response<GeneralResponse<String>>

}