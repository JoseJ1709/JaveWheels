package com.example.javewheels.User

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.javewheels.Data.DataSingleton2
import com.example.javewheels.R
import com.example.javewheels.Start.StartActivity

class AcountActivityU : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acount_u)
        val nombre = findViewById<EditText>(R.id.editText1)
        val contrasena = findViewById<EditText>(R.id.editText2)
        val actualizar = findViewById<AppCompatButton>(R.id.buttonSubmit)
        val cancelar  = findViewById<AppCompatButton>(R.id.buttonSubmit2)

        nombre.setText(DataSingleton2.User.nombre)
        contrasena.setText(DataSingleton2.User.contrasena)

        actualizar.setOnClickListener {
            DataSingleton2.User.nombre = nombre.text.toString()
            DataSingleton2.User.contrasena = contrasena.text.toString()
        }
        cancelar.setOnClickListener(){
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }
    }
}