package br.com.generic.base.models.service.request

class ServiceRequest {

    private var serviceName: String = ""
    private var requestBody = ServiceBody()

    fun getServiceName() : String {
        return serviceName
    }

    fun setServiceName(serviceName: String) {
        this.serviceName = serviceName
    }

    fun getBodyData() : ServiceBody {
        return requestBody
    }

    fun setBodyData(requestBody: ServiceBody) {
        this.requestBody = requestBody
    }

    constructor()

    constructor(
        serviceName: String,
        requestBody: ServiceBody
    ) {
        this.serviceName = serviceName
        this.requestBody = requestBody
    }

}