package br.com.generic.base.models.service.request

import com.google.gson.annotations.SerializedName

class ServiceBody {

    @SerializedName("NOMUSU") var serviceUser: LoginUser? = null
    @SerializedName("INTERNO2") var serviceCode: LoginCode? = null
    @SerializedName("KEEPCONNECTED") var serviceConnection: LoginConnection? = null

    fun getPostUsu() : LoginUser? {
        return serviceUser
    }

    fun setPostUsu(serviceUser: LoginUser) {
        this.serviceUser = serviceUser
    }

    fun getPostIntern() : LoginCode? {
        return serviceCode
    }

    fun setPostIntern(serviceCode: LoginCode) {
        this.serviceCode = serviceCode
    }

    fun getConnection() : LoginConnection? {
        return serviceConnection
    }

    fun setConnection(serviceConnection: LoginConnection) {
        this.serviceConnection = serviceConnection
    }

    constructor()

    constructor(
        serviceUser: LoginUser,
        serviceCode: LoginCode,
        serviceConnection: LoginConnection
    ) {
        this.serviceUser = serviceUser
        this.serviceCode = serviceCode
        this.serviceConnection = serviceConnection
    }

}