package com.example.covidmap.Splash

import androidx.lifecycle.ViewModel
import com.example.covidmap.API.ApiInterface
import com.example.covidmap.API.CovidCenterApi
import com.example.covidmap.Data.CovidCenterData
import com.example.covidmap.Data.CovidCentersInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SplashViewModel: ViewModel() {
    fun loadCenters(
        page: Int,
        onSuccess : () -> Unit,
        onFail : () -> Unit,
    ): ArrayList<CovidCenterData>{
        val data = arrayListOf<CovidCenterData>()
        val retrofit = Retrofit.Builder()
            .baseUrl(CovidCenterApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ApiInterface = retrofit.create(ApiInterface::class.java)
        service.loadCenterRequest(CovidCenterApi.API_AUTH_KEY, CovidCenterApi.API_KEY, page, 10)
            .enqueue(object : Callback<CovidCentersInfo> {
                override fun onFailure(call: Call<CovidCentersInfo>, t: Throwable) {
                    onFail()
                }
                override fun onResponse(call: Call<CovidCentersInfo>, response: Response<CovidCentersInfo>) {
                    for (res in response.body()?.data!!){
                        println("$res")
                        data.add(res)
                    }
                    onSuccess()
                }
            })
        return data
    }
}