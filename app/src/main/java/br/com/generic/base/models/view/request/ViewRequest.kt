package br.com.generic.base.models.view.request

class ViewRequest {

    var serviceName: String = ""
    var requestBody = ViewData()

    constructor()

    constructor(
        serviceName: String,
        requestBody: ViewData
    ) {
        this.serviceName = serviceName
        this.requestBody = requestBody
    }

}