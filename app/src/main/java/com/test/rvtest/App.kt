package com.test.rvtest

import android.app.Application
import com.test.rvtest.api.ApiClient
import com.test.rvtest.api.ApiInterface

class App : Application(){
    companion object{
        lateinit var api: ApiInterface
    }

    override fun onCreate() {
        super.onCreate()

        api = ApiClient.getRetrofit().create(ApiInterface::class.java)
    }
}