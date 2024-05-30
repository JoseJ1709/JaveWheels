package com.example.javewheels.Start

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class IngresarActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ingresar)
        val userText = findViewById<EditText>(R.id.editText1)
        val pwdText = findViewById<EditText>(R.id.editText2)
        val buttonSubmit = findViewById<AppCompatButton>(R.id.buttonSubmit)

        checks(userText, pwdText,buttonSubmit);
    }
    private fun checks(userText: EditText,pwdText: EditText,buttonSubmit : AppCompatButton) {
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
                login(userText, pwdText) }
            else{
                Toast.makeText(this,"Completa todos los campos", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun login(userText: EditText, pwdText: EditText) {
        val user = userText.text.toString() + "@example.com"
        val pass = pwdText.text.toString()
        val database = FirebaseDatabase.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val userRef = database.getReference("users/$userId")
                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            startActivity(Intent(this@IngresarActivity, MenuActivityU::class.java))
                        } else {
                            // Si no se encuentra en "users", verifica en "drivers"
                            val driverRef = database.getReference("drivers/$userId")
                            driverRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                       startActivity(Intent(this@IngresarActivity, MenuActivityD::class.java))
                                    } else {
                                        // El usuario no se encuentra ni en "users" ni en "drivers"
                                        Log.d(TAG, "User is not in 'users' or 'drivers'")
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    Log.w(TAG, "Failed to read value.", databaseError.toException())
                                }
                            })
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w(TAG, "Failed to read value.", databaseError.toException())
                    }
                })
            } else
                Toast.makeText(this, "Log In failed " + user  +" "+ pass, Toast.LENGTH_SHORT).show()
        }
    }
}