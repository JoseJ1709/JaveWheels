package com.example.javewheels.Start

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.javewheels.R
import com.example.javewheels.Start.IngresarActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.File

class RegisterActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        database = FirebaseDatabase.getInstance().reference
        auth = Firebase.auth

        val userImgButton = findViewById<ImageButton>(R.id.imageButton3)
        val userText = findViewById<EditText>(R.id.editText1)
        val pwdText = findViewById<EditText>(R.id.editText2)
        val userButton = findViewById<RadioButton>(R.id.radio_button_1)
        val driverButton = findViewById<RadioButton>(R.id.radio_button_2)
        val linearGrande = findViewById<LinearLayout>(R.id.LinearGrande)
        val carImgButton = findViewById<ImageButton>(R.id.imageButton1)
        val LicImgButton = findViewById<ImageButton>(R.id.imageButton2)
        val placaText = findViewById<EditText>(R.id.editText3)
        val buttonSubmit = findViewById<AppCompatButton>(R.id.buttonSubmit)

        checks(userText, pwdText, placaText, buttonSubmit, carImgButton, LicImgButton, userImgButton, userButton, driverButton)
        checksRadioButton(userButton, driverButton, linearGrande)

        val tempImageUri = initTempUri()
        registerTakePictureLauncher(tempImageUri, carImgButton, LicImgButton, userImgButton)
    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.CAMERA
            ) -> {
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA),
                    MY_PERMISSION_REQUEST_READ_CAMERA
                )
            }
            else -> {
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA),
                    MY_PERMISSION_REQUEST_READ_CAMERA
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSION_REQUEST_READ_CAMERA -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, you can perform your operation here
                } else {
                    // Permission denied, inform the user and ask them to grant the permission
                    Toast.makeText(this, "Camera permission is required for this feature", Toast.LENGTH_LONG).show()
                }
                return
            }
            else -> {
                // Handle other permission results
            }
        }
    }

    private fun initTempUri(): Uri {
        val tempImagesDir = File(
            applicationContext.filesDir,
            getString(R.string.temp_images_dir)
        )
        tempImagesDir.mkdir()
        val tempImage = File(
            tempImagesDir,
            getString(R.string.temp_image)
        )
        return FileProvider.getUriForFile(
            applicationContext,
            getString(R.string.authorities),
            tempImage
        )
    }

    private fun registerTakePictureLauncher(path: Uri, carImgButton: ImageButton, LicImgButton: ImageButton, userImgButton: ImageButton) {
        val resultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            carImgButton.setImageBitmap(null)
            handleImageCapture(path, carImgButton)
        }

        val resultLauncher2 = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            LicImgButton.setImageBitmap(null)
            handleImageCapture(path, LicImgButton)
        }

        val resultLauncher3 = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            userImgButton.setImageBitmap(null)
            handleImageCapture(path, userImgButton)
        }

        carImgButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                resultLauncher.launch(path)
            } else {
                requestCameraPermission()
                Toast.makeText(this, "Camera permission is required for this feature", Toast.LENGTH_LONG).show()
            }
        }

        LicImgButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                resultLauncher2.launch(path)
            } else {
                requestCameraPermission()
                Toast.makeText(this, "Camera permission is required for this feature", Toast.LENGTH_LONG).show()
            }
        }

        userImgButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                resultLauncher3.launch(path)
            } else {
                requestCameraPermission()
                Toast.makeText(this, "Camera permission is required for this feature", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleImageCapture(path: Uri, imgButton: ImageButton) {
        try {
            contentResolver.openInputStream(path)?.use { inputStream ->

                val bitmap = BitmapFactory.decodeStream(inputStream)
                val matrix = Matrix()
                matrix.postRotate(90F)
                val rotatedBitmap = Bitmap.createBitmap(
                    bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
                )

                val desiredWidth = imgButton.width
                val desiredHeight = imgButton.height

                val scaleWidth = desiredWidth.toFloat() / rotatedBitmap.width
                val scaleHeight = desiredHeight.toFloat() / rotatedBitmap.height

                val scaleFactor = if (scaleWidth < scaleHeight) scaleWidth else scaleHeight

                val scaledBitmap = Bitmap.createScaledBitmap(
                    rotatedBitmap,
                    (rotatedBitmap.width * scaleFactor).toInt(),
                    (rotatedBitmap.height * scaleFactor).toInt(), true
                )

                imgButton.setImageBitmap(scaledBitmap)
            }
        } catch (e: Exception) {
            Log.e("TAG", "Error loading image: ${e.message}")
        }
    }

    private fun checks(
        userText: EditText, pwdText: EditText, placaText: EditText,
        buttonSubmit: AppCompatButton, carImgButton: ImageButton, LicImgButton: ImageButton,
        userImgButton: ImageButton, userButton: RadioButton, driverButton: RadioButton
    ) {
        buttonSubmit.setOnClickListener {
            val isEmptyuserText = userText.text.isNullOrEmpty()
            val isEmptypwdText = pwdText.text.isNullOrEmpty()
            val isEmptyplacaText = placaText.text.isNullOrEmpty()
            val carImgButtonHasImage = hasImage(carImgButton)
            val LicImgButtonHasImage = hasImage(LicImgButton)
            val userImgButtonHasImage = hasImage(userImgButton)

            if (!isEmptyuserText && !isEmptypwdText && userImgButtonHasImage &&
                (userButton.isChecked || (driverButton.isChecked && !isEmptyplacaText && carImgButtonHasImage && LicImgButtonHasImage))
            ) {
                // Register user in Firebase Authentication
                val email = "${userText.text}@example.com"  // Convert user name to an email format
                val password = pwdText.text.toString()
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser

                            val userData = HashMap<String, Any>()
                            userData["name"] = userText.text.toString()
                            userData["password"] = pwdText.text.toString()
                            userData["type"] = if (userButton.isChecked) "user" else "driver"

                            database.child("users").child(user!!.uid).setValue(userData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "User registered successfully", Toast.LENGTH_LONG).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Failed to register user", Toast.LENGTH_LONG).show()
                                }

                            if (driverButton.isChecked) {
                                val driverData = HashMap<String, Any>()
                                driverData["placa"] = placaText.text.toString()

                                database.child("drivers").child(user.uid).setValue(driverData)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Driver registered successfully", Toast.LENGTH_LONG).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Failed to register driver", Toast.LENGTH_LONG).show()
                                    }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }

            } else {
                Toast.makeText(this, "Please fill in all fields and select images", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checksRadioButton(userButton: RadioButton, driverButton: RadioButton, linearGrande: LinearLayout) {
        userButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                linearGrande.visibility = LinearLayout.GONE
            }
        }

        driverButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                linearGrande.visibility = LinearLayout.VISIBLE
            }
        }
    }

    private fun hasImage(imageButton: ImageButton): Boolean {
        return imageButton.drawable != null
    }

    companion object {
        const val MY_PERMISSION_REQUEST_READ_CAMERA = 101
    }
}