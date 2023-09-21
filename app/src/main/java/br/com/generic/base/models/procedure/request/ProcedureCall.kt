package br.com.generic.base.models.procedure.request

import com.google.gson.annotations.SerializedName

class ProcedureCall {

    @SerializedName("actionID") var actionID : String? = null
    @SerializedName("procName") var procedureName : String? = null
    @SerializedName("rootEntity") var rootEntity : String? = null
    @SerializedName("refreshType") var refreshType : String? = null
    @SerializedName("rows") var rows = ProcedureRows()
    @SerializedName("params") var procedureParams : ProcedureParams? = ProcedureParams()

    constructor()

    constructor(
        actionID: String,
        procedureName: String,
        rootEntity: String,
        refreshType: String,
        rows: ProcedureRows,
        procedureParams: ProcedureParams?
    ) {
        this.actionID = actionID
        this.procedureName = procedureName
        this.rootEntity = rootEntity
        this.refreshType = refreshType
        this.rows = rows
        this.procedureParams = procedureParams
    }

}