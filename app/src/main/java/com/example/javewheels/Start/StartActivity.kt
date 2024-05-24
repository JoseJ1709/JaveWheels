package com.example.javewheels.Start

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.javewheels.R

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start)
        val welcomeImg = findViewById<ImageView>(R.id.welcome_img)
        val buttonCrear = findViewById<AppCompatButton>(R.id.buttonCrear)
        val buttonIngresar = findViewById<AppCompatButton>(R.id.buttonIngresar)
        setupClickListeners(buttonCrear,buttonIngresar)
    }
    private fun setupClickListeners(buttonCrear: AppCompatButton, buttonIngresar: AppCompatButton) {
        buttonCrear.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        buttonIngresar.setOnClickListener {
            startActivity(Intent(this, IngresarActivity::class.java))
        }
    }
}