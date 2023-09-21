package br.com.generic.base.models.procedure.request

class ProcedureBody {

    var serviceName: String = ""
    var requestBody = ProcedureRequestBody()

    constructor()

    constructor(
        serviceName : String,
        requestBody : ProcedureRequestBody
    ) {
        this.serviceName = serviceName
        this.requestBody = requestBody
    }

}