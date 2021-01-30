package com.test.nikeapplication.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.test.nikeapplication.data.remote.UrbanDictionaryRemoteDataSource
import com.test.nikeapplication.data.remote.UrbanDictionaryService
import com.test.nikeapplication.data.repository.WordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    fun provideHttpClient(): OkHttpClient =
        OkHttpClient.Builder().addInterceptor { chain ->
            val request: Request =
                chain.request().newBuilder().addHeader("x-rapidapi-key", "4207ca15e9msh3a263ec7dcfc67cp15de23jsn00df39bfa3a7").build()
            chain.proceed(request)
        }.build()


    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://mashape-community-urban-dictionary.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideUrbanDictionaryService(retrofit: Retrofit): UrbanDictionaryService =
        retrofit.create(UrbanDictionaryService::class.java)



//    @Singleton
//    @Provides
//    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

//    @Singleton
//    @Provides
//    fun provideCharacterDao(db: AppDatabase) = db.characterDao()


}