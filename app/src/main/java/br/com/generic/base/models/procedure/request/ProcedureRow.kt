package br.com.generic.base.models.procedure.request

import com.google.gson.annotations.SerializedName

class ProcedureRow {

    @SerializedName("field") var procedureField = arrayListOf(ProcedureFields())

    constructor()

    constructor(
        procedureField : ArrayList<ProcedureFields>
    ) {
        this.procedureField = procedureField
    }

}