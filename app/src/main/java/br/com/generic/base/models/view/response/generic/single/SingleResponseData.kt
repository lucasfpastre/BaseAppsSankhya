package br.com.generic.base.models.view.response.generic.single

import com.google.gson.annotations.SerializedName

data class SingleResponseData(
    @SerializedName("records") var records : SingleResponseFields?
)