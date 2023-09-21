package br.com.generic.base.models.view.response.user

import com.google.gson.annotations.SerializedName

data class UserResponseData(
    @SerializedName("records") var records : UserResponseFields?
)