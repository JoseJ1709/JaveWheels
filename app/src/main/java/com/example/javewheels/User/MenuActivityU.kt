package com.example.javewheels.User

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.javewheels.Data.DataSingleton2
import com.example.javewheels.Data.Datos
import com.example.javewheels.R

class MenuActivityU : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_u)
        val buttonMap = findViewById<AppCompatButton>(R.id.buttonMap)
        val buttonNoti = findViewById<AppCompatButton>(R.id.buttonNoti)
        val buttonAcount = findViewById<AppCompatButton>(R.id.buttonAcount)
        seters(buttonMap, buttonNoti, buttonAcount)
        DataSingleton2.loadData(this)
    }
    private fun seters (buttonMap: Button, buttonNoti: Button, buttonAcount: Button) {
        buttonMap.setOnClickListener {
            mapLocation()
        }
        buttonNoti.setOnClickListener {
            startActivity(Intent(this, NotificationActivityU::class.java))
        }
        buttonAcount.setOnClickListener {
            startActivity(Intent(this, AcountActivityU::class.java))
        }
    }
    fun mapLocation() {

        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                startActivity(Intent(this, MapsActivityU::class.java))
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    Datos.MY_PERMISSION_REQUEST_LOCATION
                )
            }

            else -> {
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    Datos.MY_PERMISSION_REQUEST_LOCATION
                )
            }
        }
    }
}