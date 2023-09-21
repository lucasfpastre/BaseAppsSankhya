package br.com.generic.base.data.repository

import br.com.generic.base.data.local.server.ServerDataSource
import br.com.generic.base.data.local.user.UserDataSource
import br.com.generic.base.data.remote.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RequestRepository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    serverDataSource: ServerDataSource,
    userDataSource: UserDataSource
) {
    val remote = remoteDataSource
    val server = serverDataSource
    val recordedUser = userDataSource
}