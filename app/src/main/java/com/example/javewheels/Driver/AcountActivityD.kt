package com.example.javewheels.Driver

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.javewheels.Data.DataSingleton
import com.example.javewheels.R
import com.example.javewheels.Start.StartActivity

class AcountActivityD : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acount_d)
        val logOutBottom = findViewById<AppCompatButton>(R.id.buttonSubmit2)
        val actualizarBottom = findViewById<AppCompatButton>(R.id.buttonSubmit)
        val userText = findViewById<EditText>(R.id.editText1)
        val pwdText = findViewById<EditText>(R.id.editText2)
        val placaText = findViewById<EditText>(R.id.editText3)

        userText.setText(DataSingleton.User.nombre)
        pwdText.setText(DataSingleton.User.contrasena)
        placaText.setText(DataSingleton.User.placa)

        actualizarBottom.setOnClickListener {
            DataSingleton.User.nombre = userText.text.toString()
            DataSingleton.User.contrasena = pwdText.text.toString()
            DataSingleton.User.placa = placaText.text.toString()
        }
        logOutBottom.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }
    }
}