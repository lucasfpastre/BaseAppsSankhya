package br.com.generic.base.data.local.server

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.generic.base.models.server.ServerData

@Database(entities = [ServerData::class], version = 1, exportSchema = true)
abstract class ServerDatabase: RoomDatabase() {
    abstract fun serverDao() : ServerDao
}