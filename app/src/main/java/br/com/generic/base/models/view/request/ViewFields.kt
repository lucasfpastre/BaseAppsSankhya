package br.com.generic.base.models.view.request

import com.google.gson.annotations.SerializedName

class ViewFields {

    @SerializedName("field") var queryField = arrayListOf(ViewFieldName())

    constructor()

    constructor(
        queryField : ArrayList<ViewFieldName>
    ) {
        this.queryField = queryField
    }

}