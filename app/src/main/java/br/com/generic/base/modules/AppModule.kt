package br.com.generic.base.modules

import android.content.Context
import androidx.room.Room
import br.com.generic.base.data.local.server.ServerDatabase
import br.com.generic.base.data.local.user.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesServerDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, ServerDatabase::class.java, "server_database").build()

    @Singleton
    @Provides
    fun providesUserDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(app, UserDatabase::class.java, "user_database").build()

    @Singleton
    @Provides
    fun providesServerDao(db: ServerDatabase) = db.serverDao()

    @Singleton
    @Provides
    fun providesUserDao(db: UserDatabase) = db.userDao()
}