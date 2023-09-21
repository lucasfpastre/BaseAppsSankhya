package br.com.generic.base.models.view.request

import com.google.gson.annotations.SerializedName

class ViewFieldName {

    @SerializedName("$") var fieldName : String? = null

    constructor()

    constructor(
        fieldName: String
    ) {
        this.fieldName = fieldName
    }

}