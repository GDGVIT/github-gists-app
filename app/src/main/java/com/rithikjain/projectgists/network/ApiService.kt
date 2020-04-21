package com.rithikjain.projectgists.network

import android.content.Context
import com.rithikjain.projectgists.util.Constants
import com.rithikjain.projectgists.util.PrefHelper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiService {
    fun createRetrofit(context: Context): ApiInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient(context))
            .build()

        return retrofit.create(ApiInterface::class.java)
    }

    private fun getOkHttpClient(context: Context): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        val sharedPref = PrefHelper.customPrefs(context, Constants.PREF_NAME)

        httpClient.connectTimeout(25, TimeUnit.SECONDS)
        httpClient.readTimeout(25, TimeUnit.SECONDS)

        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .addHeader(
                    "Content-Type",
                    "application/json"
                )
                .addHeader(
                    "Authorization",
                    "Bearer ${sharedPref.getString(Constants.PREF_AUTH_TOKEN, "")}"
                )
            val request = requestBuilder.build()
            return@addInterceptor chain.proceed(request)
        }
        return httpClient.build()
    }
}