package com.example.javewheels.User

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.javewheels.Data.DataSingleton2
import com.example.javewheels.Data.UserModel
import com.example.javewheels.R
import com.example.javewheels.Start.StartActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AcountActivityU : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acount_u)
        val nombre = findViewById<EditText>(R.id.editText1)
        val contrasena = findViewById<EditText>(R.id.editText2)
        val actualizar = findViewById<AppCompatButton>(R.id.buttonSubmit)
        val logOut  = findViewById<AppCompatButton>(R.id.buttonSubmit2)

        CargarDatos(nombre, contrasena)

        CargarFoto();

        acualizar(nombre, contrasena, actualizar)




        logOut.setOnClickListener(){
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun CargarFoto() {
        TODO("Not yet implemented")
    }

    private fun acualizar(nombre: EditText?, contrasena: EditText?, actualizar: AppCompatButton?) {
        actualizar!!.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val userRef = database.getReference("users/$userId")

            userRef.child("name").setValue(nombre!!.text.toString())
            userRef.child("password").setValue(contrasena!!.text.toString())
            updatePassword(contrasena!!.text.toString())
            Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePassword(password: String) {
        val user = FirebaseAuth.getInstance().currentUser
        user!!.updatePassword(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "User password updated.")
                }
            }
    }

    private fun CargarDatos(nombre: EditText?, contrasena: EditText?) {
        val database = FirebaseDatabase.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = database.getReference("users/$userId")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(UserModel::class.java)
                nombre!!.setText(user!!.name)
                contrasena!!.setText(user.password)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.")
            }
        })
    }
}