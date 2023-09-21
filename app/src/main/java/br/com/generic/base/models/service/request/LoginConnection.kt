package br.com.generic.base.models.service.request

import com.google.gson.annotations.SerializedName

class LoginConnection {

    @SerializedName("$") var connection: String? = null

    fun getLoginConnection() : String? {
        return connection
    }

    fun setLoginConnection(connection: String) {
        this.connection = connection
    }

    constructor()

    constructor(
        connection: String
    ) {
        this.connection = connection
    }

}