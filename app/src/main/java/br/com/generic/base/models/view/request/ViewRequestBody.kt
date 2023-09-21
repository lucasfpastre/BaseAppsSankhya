package br.com.generic.base.models.view.request

import com.google.gson.annotations.SerializedName

class ViewRequestBody {

    @SerializedName("viewName") var viewName : String? = null
    @SerializedName("orderBy") var viewOrderBy : String? = null
    @SerializedName("where") var viewWhere = ViewFieldName()
    @SerializedName("fields") var viewFields = ViewFields()

    constructor()

    constructor(
        viewName: String,
        viewOrderBy : String,
        viewWhere : ViewFieldName,
        viewFields: ViewFields
    ) {
        this.viewName = viewName
        this.viewOrderBy = viewOrderBy
        this.viewWhere = viewWhere
        this.viewFields = viewFields
    }

}