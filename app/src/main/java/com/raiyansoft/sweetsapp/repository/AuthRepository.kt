package com.raiyansoft.sweetsapp.repository

import com.raiyansoft.sweetsapp.models.address.CreateLocation
import com.raiyansoft.sweetsapp.models.address.Location
import com.raiyansoft.sweetsapp.models.login.Login
import com.raiyansoft.sweetsapp.models.verification.Verification
import com.raiyansoft.sweetsapp.network.ServiceBuilder

class AuthRepository {

    suspend fun login(
        lang : String,
        areaId: Int,
        login : Login
    ) = ServiceBuilder.apis!!.login(lang, areaId, login)

    suspend fun logout(
        token : String,
        areaId: Int
    ) = ServiceBuilder.apis!!.logout(token, areaId,)

    suspend fun activateAccount(
        lang : String,
        areaId: Int,
        verification : Verification
    ) = ServiceBuilder.apis!!.activateAccount(lang, areaId, verification)

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