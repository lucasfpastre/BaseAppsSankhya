package br.com.generic.base.data.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.generic.base.models.user.RecordedUserData
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    // Insere usuário e substitui quando um novo é usado
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userData : RecordedUserData)

    // Busca o usuário
    @Query("SELECT * FROM user_database WHERE userId = 1")
    fun getMonitorUser(): Flow<RecordedUserData>

}