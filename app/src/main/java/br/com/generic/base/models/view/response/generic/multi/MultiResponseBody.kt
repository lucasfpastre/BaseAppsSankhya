package br.com.generic.base.models.view.response.generic.multi

import com.google.gson.annotations.SerializedName

data class MultiResponseBody (
    @SerializedName("serviceName"    ) var serviceName     : String?        = null,
    @SerializedName("status"         ) var status          : String?        = null,
    @SerializedName("pendingPrinting") var pendingPrinting : String?        = null,
    @SerializedName("transactionId"  ) var transactionId   : String?        = null,
    @SerializedName("responseBody"   ) var responseBody    : MultiResponseData?
)