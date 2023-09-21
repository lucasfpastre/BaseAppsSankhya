package br.com.generic.base.models.view.response.user

import com.google.gson.annotations.SerializedName

data class UserResponseFields(
    @SerializedName("record") var viewNames : UserResponseNames?
)