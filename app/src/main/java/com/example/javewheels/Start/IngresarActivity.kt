package com.example.javewheels.Start

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.javewheels.Driver.MenuActivityD
import com.example.javewheels.R
import com.example.javewheels.User.MenuActivityU

class IngresarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ingresar)
        val userText = findViewById<EditText>(R.id.editText1)
        val pwdText = findViewById<EditText>(R.id.editText2)
        val buttonSubmit = findViewById<AppCompatButton>(R.id.buttonSubmit)
        val userButton = findViewById<RadioButton>(R.id.radio_button_1)
        val driverButton = findViewById<RadioButton>(R.id.radio_button_2)
        checks(userText, pwdText,buttonSubmit,userButton,driverButton)
        checksRadioButton(userButton,driverButton)
    }
    private fun checks(userText: EditText,pwdText: EditText,buttonSubmit : AppCompatButton, userButton : RadioButton, driverButton: RadioButton) {
        buttonSubmit.setOnClickListener {
            //aca se mira la bd
            val isEmptyEditText1 = userText.text.isNullOrEmpty()
            val isEmptyEditText2 = pwdText.text.isNullOrEmpty()


            if (isEmptyEditText1) {
                userText.setBackgroundResource(R.drawable.custom_rectangle2)
            } else {
                userText.setBackgroundResource(R.drawable.custom_rectangle)
            }

            if (isEmptyEditText2) {
                pwdText.setBackgroundResource(R.drawable.custom_rectangle2)
            } else {
                pwdText.setBackgroundResource(R.drawable.custom_rectangle)
            }

            if (!isEmptyEditText1 && !isEmptyEditText2) {
                if(driverButton.isChecked){
                    startActivity(Intent(this, MenuActivityD::class.java))
                }
                else if(userButton.isChecked){
                    startActivity(Intent(this, MenuActivityU::class.java))
                }
                else{
                    Toast.makeText(this,"Selecciona un tipo de usuario", Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(this,"Completa todos los campos", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun checksRadioButton(userButton : RadioButton, driverButton: RadioButton){
        var radioButtonSelected = false
        userButton.isChecked = true

        userButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                radioButtonSelected = true
                driverButton.isChecked = false
            } else {
                userButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                radioButtonSelected = false
            }
        }

        driverButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                radioButtonSelected = false
                userButton.isChecked = false
            } else {
                radioButtonSelected = true
                driverButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
            }
        }
    }
}