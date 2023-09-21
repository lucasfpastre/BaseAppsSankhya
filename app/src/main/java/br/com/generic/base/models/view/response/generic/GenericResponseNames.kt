package br.com.generic.base.models.view.response.generic

import br.com.generic.base.models.view.response.ViewResponseField
import com.google.gson.annotations.SerializedName

data class GenericResponseNames (
    @SerializedName("CODUSU")  var genericUserCode : ViewResponseField? = ViewResponseField(),
    @SerializedName("NOMEUSU") var genericUserName : ViewResponseField? = ViewResponseField()
)