package br.com.generic.base.models.view.response.generic.single

import br.com.generic.base.models.view.response.generic.GenericResponseNames
import com.google.gson.annotations.SerializedName

data class SingleResponseFields(
    @SerializedName("record") var viewNames : GenericResponseNames?
)