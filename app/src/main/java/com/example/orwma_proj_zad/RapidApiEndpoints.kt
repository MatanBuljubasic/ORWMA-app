package com.example.orwma_proj_zad

import okhttp3.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface RapidApiEndpoints {
    @Headers("X-RapidAPI-Key: dffc98cc86msh69d250ab605b43bp15c1dcjsn186e150347c8", "X-RapidAPI-Host: odds.p.rapidapi.com")
    @GET("/v4/sports")
    fun getSports(): retrofit2.Call<ArrayList<Sport>>

    @Headers("X-RapidAPI-Key: dffc98cc86msh69d250ab605b43bp15c1dcjsn186e150347c8", "X-RapidAPI-Host: odds.p.rapidapi.com")
    @GET("/v4/sports/{sport}/scores")
    fun getMatches(@Path("sport") sportKey: String): retrofit2.Call<ArrayList<Match>>
}