package com.raiyansoft.sweetsapp.interactors

import android.util.Log
import com.raiyansoft.sweetsapp.util.TAG
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.net.SocketFactory

object DoesNetworkHaveInternet {

    fun execute(socketFactory: SocketFactory) : Boolean {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            Log.e(TAG, "execute: Connect Success")
            true
        }catch (e : IOException){
            Log.e(TAG, "execute: No Internet Connection")
            false
        }
    }

}