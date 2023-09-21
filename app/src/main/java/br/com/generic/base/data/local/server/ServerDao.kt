package br.com.generic.base.data.local.server

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.generic.base.models.server.ServerData
import kotlinx.coroutines.flow.Flow

@Dao
interface ServerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServer(serverData: ServerData)

    @Query("UPDATE server_database SET serverData = :newServer WHERE serverId = 1")
    suspend fun updateServerData(newServer : String)

    @Query("SELECT * FROM server_database WHERE serverId = 1")
    fun getServer(): Flow<ServerData>

}