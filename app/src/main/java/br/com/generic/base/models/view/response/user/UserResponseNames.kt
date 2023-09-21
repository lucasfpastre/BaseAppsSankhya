package br.com.generic.base.models.view.response.user

import br.com.generic.base.models.view.response.ViewResponseField
import com.google.gson.annotations.SerializedName

data class UserResponseNames(
    @SerializedName("CODUSU")    var userCode : ViewResponseField? = ViewResponseField(),
    @SerializedName("NOMEUSU")   var userName : ViewResponseField? = ViewResponseField()
)