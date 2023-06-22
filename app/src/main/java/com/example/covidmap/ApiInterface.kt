package com.example.covidmap

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiInterface {
    @GET("centers")
    fun loadCenterRequest(
        @Header("Authorization") authorization: String,
        @Query("serviceKey") serviceKey: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int = 10,
    ) : Call<CovidCentersInfo>
}