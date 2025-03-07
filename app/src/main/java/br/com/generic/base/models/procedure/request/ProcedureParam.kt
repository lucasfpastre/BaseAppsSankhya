package br.com.generic.base.models.procedure.request

import com.google.gson.annotations.SerializedName

class ProcedureParam {

    @SerializedName("type") var paramType : String? = null
    @SerializedName("paramName") var paramName : String? = null
    @SerializedName("$") var paramValue : Any? = null

    constructor()

    constructor(
        paramType : String,
        paramName : String,
        paramValue : Any
    ) {
        this.paramType = paramType
        this.paramName = paramName
        this.paramValue = paramValue
    }
}