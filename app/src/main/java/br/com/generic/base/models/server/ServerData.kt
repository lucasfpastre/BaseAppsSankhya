package br.com.generic.base.models.server

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.generic.base.utils.Constants.Companion.SERVER_DATA

@Entity(tableName = SERVER_DATA)
data class ServerData(

    @PrimaryKey(autoGenerate = false)
    var serverId   : Int,
    var serverData : String

)