package com.example.myapplication.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory  {
    const val BASE_URL = "http://192.168.43.186:8000/api/"
//        "https://restuniv.herokuapp.com/api/"
    fun makeRetrofitService(): RetrofitService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RetrofitService::class.java)
    }
}