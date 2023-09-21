package br.com.generic.base.models.service.request

import com.google.gson.annotations.SerializedName

class LoginUser {

    @SerializedName("$") var user: String? = null

    fun getLoginUser() : String? {
        return user
    }

    fun setLoginUser(user: String) {
        this.user = user
    }

    constructor()

    constructor(
        user: String
    ) {
        this.user = user
    }

}