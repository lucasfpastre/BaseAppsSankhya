package br.com.generic.base.data.local.server

import br.com.generic.base.models.server.ServerData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Fornece os dados para serem usados pelo Hilt para injeção
class ServerDataSource @Inject constructor(
    private val serverDao: ServerDao
) {
    suspend fun insertServer(serverData: ServerData) {
        serverDao.insertServer(serverData)
    }

    suspend fun updateServerData(serverName: String) {
        serverDao.updateServerData(serverName)
    }

    fun getServer() : Flow<ServerData> {
        return serverDao.getServer()
    }
}