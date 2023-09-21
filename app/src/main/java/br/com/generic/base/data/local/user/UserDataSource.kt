package br.com.generic.base.data.local.user

import br.com.generic.base.models.user.RecordedUserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val userDao: UserDao
){
    suspend fun insertUser(monitorUserData: RecordedUserData){
        userDao.insertUser(monitorUserData)
    }

    fun getMonitorUser() : Flow<RecordedUserData> {
        return userDao.getMonitorUser()
    }
}