package br.com.generic.base.models.view.request

import com.google.gson.annotations.SerializedName

class ViewData {

    @SerializedName("query") var query = ViewRequestBody()

    constructor()

    constructor (
        query: ViewRequestBody
    ) {
        this.query = query
    }

}