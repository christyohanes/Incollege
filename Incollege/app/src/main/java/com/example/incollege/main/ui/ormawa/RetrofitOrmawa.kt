package com.example.incollege.main.ui.ormawa

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitOrmawa {
    fun getRetroClientInstance(url :String): Retrofit {
        val gson= GsonBuilder().setLenient().create()
        return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }
}