package com.tirupati.vendor.di


import android.content.Context

import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import okhttp3.Interceptor

import okhttp3.OkHttpClient

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {


    /*-----Header for Network Call & BuildConfig.DEBUG----*/
    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context, sessionManager: SessionManager) =
        (run {
            val headerInterceptor = Interceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()

                //  builder.header("", ApiService.DEVICE_TYPE)

                val request = builder.method(original.method, original.body)
                    .build()
                chain.proceed(request)

            }

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = (HttpLoggingInterceptor.Level.BODY)

            OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(loggingInterceptor)
                .addInterceptor { chain ->
                    val request = chain.request()
                    val response = chain.proceed(request)
                    if (response.code == 402) {
                        //logout feature
                        //  LoginActivity.openLogin(context,response.message())
                    }
                    response
                }
                .connectTimeout(0, TimeUnit.SECONDS)
                .callTimeout(0, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS)
                .writeTimeout(0, TimeUnit.SECONDS)
                .build()
        })

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(ApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    fun provideSessionManager(@ApplicationContext context: Context) = SessionManager(context)
}