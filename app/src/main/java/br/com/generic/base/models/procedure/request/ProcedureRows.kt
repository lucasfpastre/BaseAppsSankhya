package br.com.generic.base.models.procedure.request

import com.google.gson.annotations.SerializedName

class ProcedureRows {

    @SerializedName("row") var procedureRow = arrayListOf(ProcedureRow())

    constructor()

    constructor(
        procedureRow: ArrayList<ProcedureRow>
    ) {
        this.procedureRow = procedureRow
    }

}