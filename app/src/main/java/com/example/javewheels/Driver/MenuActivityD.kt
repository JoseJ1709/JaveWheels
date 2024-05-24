package com.example.javewheels.Driver

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.javewheels.Data.DataSingleton
import com.example.javewheels.Data.DataSingleton2
import com.example.javewheels.Data.Datos
import com.example.javewheels.R
import com.example.javewheels.User.AcountActivityU
import com.example.javewheels.User.MapsActivityU
import com.example.javewheels.User.NotificationActivityU
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class MenuActivityD : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_d)
        val buttonMap = findViewById<AppCompatButton>(R.id.buttonMap)
        val buttonNoti = findViewById<AppCompatButton>(R.id.buttonNoti)
        val buttonAcount = findViewById<AppCompatButton>(R.id.buttonAcount)
        seters(buttonMap, buttonNoti, buttonAcount)
        DataSingleton.loadData(this)
    }
    private fun seters (buttonMap: Button, buttonNoti: Button, buttonAcount: Button) {
        buttonMap.setOnClickListener {
            mapLocation()
        }
        buttonNoti.setOnClickListener {
            startActivity(Intent(this, NotificationActivityD::class.java))
        }
        buttonAcount.setOnClickListener {
            startActivity(Intent(this, AcountActivityD::class.java))
        }
    }
    fun mapLocation() {

        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                startActivity(Intent(this, MapsActivityD::class.java))
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