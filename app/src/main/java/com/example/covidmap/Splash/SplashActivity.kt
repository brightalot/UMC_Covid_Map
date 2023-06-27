package com.example.covidmap.Splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.covidmap.*
import com.example.covidmap.Data.CovidCenterDB
import com.example.covidmap.databinding.ActivitySplashBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep
import kotlin.concurrent.timer

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var splashViewModel: SplashViewModel
    private lateinit var toastManager: ToastManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        val db = Room.databaseBuilder(
            this,
            CovidCenterDB::class.java, "CenterData"
        ).build()
        toastManager = ToastManager(this)
        GlobalScope.launch {
            db.covidCenterDAO().deleteCovidCenter()
            var loadCenterFinish = false
            var progress = 0
            val loadCenter = async {
                for (page in 1..10) {
                    splashViewModel.loadCenters(
                        page,
                        onFail = {
                            toastManager.makeToast("실패")
                        },
                        onSuccess = {
                            toastManager.makeToast("성공")
                        }
                    )
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

    private fun _makeToast(msg : String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}