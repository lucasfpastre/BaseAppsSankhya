package br.com.generic.base.models.view.response.generic.multi

import br.com.generic.base.models.view.response.generic.GenericResponseNames
import com.google.gson.annotations.SerializedName

data class MultiResponseFields(
    @SerializedName("record") var viewNames : ArrayList<GenericResponseNames?>? = arrayListOf(GenericResponseNames())
)