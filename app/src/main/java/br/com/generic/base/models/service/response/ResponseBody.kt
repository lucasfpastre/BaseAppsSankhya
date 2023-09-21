package br.com.generic.base.models.service.response

import com.google.gson.annotations.SerializedName

data class ResponseBody (

    @SerializedName("jsessionid" ) var jsessionid : JSessionID? = JSessionID()

)