package com.example.covidmap.Splash

import android.content.Context
import android.widget.Toast


class ToastManager (context: Context)  {
    val ctx = context
    fun makeToast(msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }
}