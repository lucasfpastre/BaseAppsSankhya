package br.com.generic.base.models.service.request

import com.google.gson.annotations.SerializedName

class LoginCode {

    @SerializedName("$") var code: String? = null

    fun getLoginCode() : String? {
        return code
    }

    fun setLoginCode(code : String) {
        this.code = code
    }

    constructor()

    constructor(
        code: String
    ) {
        this.code = code
    }

}