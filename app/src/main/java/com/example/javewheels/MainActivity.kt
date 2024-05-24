package com.example.javewheels

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.javewheels.Start.StartActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val handler = Handler()

        handler.postDelayed({
            startActivity(Intent(this, StartActivity::class.java))
        }, 1000) // 1000 milisegundos = 1 segundo

    }
}