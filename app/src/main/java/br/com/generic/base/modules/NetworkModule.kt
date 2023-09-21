package br.com.generic.base.modules

import br.com.generic.base.data.extensions.serverURL
import br.com.generic.base.data.remote.SankhyaApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Módulo do Retrofit para conexão com as APIs
    // Usado pelo Hilt/Dagger

    // Cliente de conexão com API sem interceptador - Comentar durantes os testes
    /*
    @Singleton
    @Provides
    fun provideHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }
    */

    // Cliente de conexão com API com interceptador - Descomentar durante testes
    @Singleton
    @Provides
    fun provideHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(getInterceptor())
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    // Interceptador - Descomentar durante testes
    @Singleton
    @Provides
    fun getInterceptor() : HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(serverURL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory.apply {
                GsonBuilder()
                    .serializeNulls()
                    .setLenient()
                    .create()
            })
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): SankhyaApi {
        return retrofit.create(SankhyaApi::class.java)
    }

}