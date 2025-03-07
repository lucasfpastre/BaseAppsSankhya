package br.com.generic.base.data.local.user

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.generic.base.models.user.RecordedUserData

// Cria o banco de dados
@Database(entities = [RecordedUserData::class], version = 1, exportSchema = true)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao() : UserDao
}