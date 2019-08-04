package com.test.rvtest.api

import com.test.rvtest.model.ResultResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @GET("search?")
    fun search(
        @Query("page") page: Int,
        @Query("pagesize") pagesize: Int,
        @Query("order") order: String,
        @Query("sort") sort: String,
        @Query("tagged") tagged: String,
        @Query("site") site: String
    ) : Call<ResultResponse>

}