package br.com.generic.base.models.procedure.request

import com.google.gson.annotations.SerializedName

class ProcedureParams {

    @SerializedName("param") var procedureParam = arrayListOf(ProcedureParam())

    constructor()

    constructor(
        procedureParam: ArrayList<ProcedureParam>
    ) {
        this.procedureParam = procedureParam
    }

}