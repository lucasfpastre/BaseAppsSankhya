package br.com.generic.base.data.remote

import br.com.generic.base.models.procedure.request.ProcedureBody
import br.com.generic.base.models.service.request.ServiceRequest
import br.com.generic.base.models.view.request.ViewRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface SankhyaApi {

    @POST()
    fun getSessionId(@Body serviceRequest: ServiceRequest, @Url newUrl : String): Call<Any>

    @POST()
    fun getViewData(@Body queryRequest: ViewRequest, @Url newUrl : String, @Header("Cookie") cookie: String): Call<Any>

    @POST()
    fun callProcedure(@Body procedureBody: ProcedureBody, @Url newUrl : String, @Header("Cookie") cookie: String): Call<Any>

    @POST()
    fun logoutSession(@Body serviceRequest: ServiceRequest, @Url newUrl: String, @Header("Cookie") cookie: String): Call<Any>
}