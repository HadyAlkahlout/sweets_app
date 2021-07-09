package com.raiyansoft.sweetsapp.repository

import com.raiyansoft.sweetsapp.models.address.CreateLocation
import com.raiyansoft.sweetsapp.models.address.Location
import com.raiyansoft.sweetsapp.models.login.Login
import com.raiyansoft.sweetsapp.models.verification.Verification
import com.raiyansoft.sweetsapp.network.ServiceBuilder

class AuthRepository {

    suspend fun login(
        lang : String,
        login : Login
    ) = ServiceBuilder.apis!!.login(lang, login)

    suspend fun logout(
        token : String
    ) = ServiceBuilder.apis!!.logout(token)

    suspend fun activateAccount(
        lang : String,
        verification : Verification
    ) = ServiceBuilder.apis!!.activateAccount(lang, verification)

    suspend fun saveLocation(
        lang : String,
        token : String,
        location : Location
    ) = ServiceBuilder.apis!!.saveLocation(lang, token, location)

    suspend fun saveLocation(
        lang : String,
        token : String,
        location : CreateLocation
    ) = ServiceBuilder.apis!!.saveLocation(lang, token, location)

}