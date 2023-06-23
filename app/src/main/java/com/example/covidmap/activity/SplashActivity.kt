package com.example.covidmap.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.room.Room
import com.example.covidmap.ApiInterface
import com.example.covidmap.CovidCenterApi
import com.example.covidmap.CovidCenterData
import com.example.covidmap.CovidCentersInfo
import com.example.covidmap.databinding.ActivitySplashBinding
import com.example.covidmap.CovidCenterDB
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Thread.sleep
import kotlin.concurrent.timer

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(
            this,
            CovidCenterDB::class.java, "CovidCenterData"
        ).build()

        GlobalScope.launch {
            db.covidCenterDAO().deleteCovidCenter()
            var loadCenterFinish = false
            var progress = 0
            val loadCenter = async {
                for (page in 1..10) {
                    loadCenters(page)
                        .forEach {
                            db.covidCenterDAO()
                                .insertCovidCenter(it)
                        }
                }
                loadCenterFinish = true
            }
            timer(period = 10) {
                // 100%
                if (progress == 100) {
                    startActivity(
                        Intent(this@SplashActivity, MapsActivity::class.java)
                    )
                    cancel()
                    finish()

                }else{
                    progress++
                }
                // 80%
                if (progress  == 80) {
                    sleep(2000)
                    if (!loadCenterFinish){
                        runBlocking {
                            loadCenter.join()
                        }
                    }
                }
                runOnUiThread {binding.loadingProgressBar.progress = progress}
            }
        }
    }

    private fun loadCenters(page: Int): ArrayList<CovidCenterData>{
        val data = arrayListOf<CovidCenterData>()
        val retrofit = Retrofit.Builder()
            .baseUrl(CovidCenterApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ApiInterface = retrofit.create(ApiInterface::class.java)
        service.loadCenterRequest(CovidCenterApi.API_AUTH_KEY, CovidCenterApi.API_KEY, page, 10)
            .enqueue(object : Callback<CovidCentersInfo> {
            override fun onFailure(call: Call<CovidCentersInfo>, t: Throwable) {
                Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<CovidCentersInfo>, response: Response<CovidCentersInfo>) {
                Toast.makeText(applicationContext, "성공", Toast.LENGTH_SHORT).show()
                for (res in response.body()?.data!!){
                    println("$res")
                    data.add(res)
                }
            }
        })
        return data
    }
}