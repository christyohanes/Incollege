package com.example.incollege.main.ui.ormawa

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface Get {
    @GET("{name}")
    fun getData(
    @Path("name") name:String?

    ): Call<ResponseOrmawa>
}