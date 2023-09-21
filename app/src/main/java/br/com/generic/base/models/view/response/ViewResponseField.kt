package br.com.generic.base.models.view.response

import com.google.gson.annotations.SerializedName

data class ViewResponseField(
    @SerializedName("$") var viewField : String? = null
)