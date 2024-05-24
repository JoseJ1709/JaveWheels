package com.example.javewheels.User

import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.javewheels.Data.AdapterNoti
import com.example.javewheels.Data.DataSingleton2
import com.example.javewheels.R

class NotificationActivityU : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_u)
        val listNoti = findViewById<ListView>(R.id.lista)
        val mNotiAdapter = AdapterNoti(this, DataSingleton2.notificacionesList)
        listNoti.adapter = mNotiAdapter
    }
}