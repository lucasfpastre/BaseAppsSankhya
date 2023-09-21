package br.com.generic.base.models.procedure.request

import com.google.gson.annotations.SerializedName

class ProcedureFields {

    @SerializedName("fieldName") var fieldName : String? = null
    @SerializedName("$") var fieldValue : String? = null

    constructor()

    constructor(
        fieldName: String,
        fieldValue: String
    ) {
        this.fieldName = fieldName
        this.fieldValue = fieldValue
    }

}