package br.com.generic.base.data.remote

import br.com.generic.base.models.procedure.request.ProcedureBody
import br.com.generic.base.models.service.request.ServiceRequest
import br.com.generic.base.models.view.request.ViewRequest
import retrofit2.Call
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val sankhyaApi: SankhyaApi) {

    fun getSessionId(serviceRequest: ServiceRequest, newUrl: String) : Call<Any> {
        return sankhyaApi.getSessionId(serviceRequest, newUrl)
    }

    fun getViewData(viewRequest: ViewRequest, newUrl : String, cookie: String) : Call<Any> {
        return sankhyaApi.getViewData(viewRequest, newUrl, cookie)
    }

    fun callProcedure(procedureBody: ProcedureBody, newUrl : String, cookie: String) : Call<Any> {
        return sankhyaApi.callProcedure(procedureBody, newUrl, cookie)
    }

    fun logoutSession(serviceRequest: ServiceRequest, newUrl: String, cookie: String) : Call<Any> {
        return sankhyaApi.logoutSession(serviceRequest, newUrl, cookie)
    }

}