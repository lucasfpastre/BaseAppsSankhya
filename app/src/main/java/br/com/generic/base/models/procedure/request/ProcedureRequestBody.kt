package br.com.generic.base.models.procedure.request

import com.google.gson.annotations.SerializedName

class ProcedureRequestBody {

    @SerializedName("stpCall") var procedureCall = ProcedureCall()

    constructor()

    constructor(
        procedureCall : ProcedureCall
    ) {
        this.procedureCall = procedureCall
    }

}