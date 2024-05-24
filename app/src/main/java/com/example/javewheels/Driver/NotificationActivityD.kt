package com.example.javewheels.Driver

import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.javewheels.Data.AdapterNoti
import com.example.javewheels.Data.DataSingleton
import com.example.javewheels.R

class NotificationActivityD : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_d)
        val listNoti = findViewById<ListView>(R.id.lista)
        val mNotiAdapter = AdapterNoti(this, DataSingleton.notificacionesList)
        listNoti.adapter = mNotiAdapter

    }
}