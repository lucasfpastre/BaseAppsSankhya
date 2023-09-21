package br.com.generic.base.models.view.response.generic.multi

import com.google.gson.annotations.SerializedName

data class MultiResponseData(
    @SerializedName("records") var records : MultiResponseFields?
)