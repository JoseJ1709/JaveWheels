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
import com.example.javewheels.Data.Datos.Companion.MY_PERMISSION_REQUEST_READ_CAMERA
import com.example.javewheels.Driver.MenuActivityD
import com.example.javewheels.R
import com.example.javewheels.User.MenuActivityU
import java.io.File

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
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


        checks(userText, pwdText, placaText, buttonSubmit,carImgButton,LicImgButton,userImgButton,userButton,driverButton)

        checksRadioButton(userButton,driverButton,linearGrande)


        val tempImageUri = initTempUri()
        registerTakePictureLauncher(tempImageUri,carImgButton,LicImgButton,userImgButton)
    }
    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.CAMERA) -> {
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA),
                    MY_PERMISSION_REQUEST_READ_CAMERA)
            }
            else -> {
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA),
                    MY_PERMISSION_REQUEST_READ_CAMERA)
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
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
            getString(R.string.temp_images_dir))

        tempImagesDir.mkdir()

        val tempImage = File(
            tempImagesDir,
            getString(R.string.temp_image))

        return FileProvider.getUriForFile(
            applicationContext,
            getString(R.string.authorities),
            tempImage)
    }
    private fun registerTakePictureLauncher(path: Uri, carImgButton: ImageButton,LicImgButton: ImageButton, userImgButton: ImageButton) {

        val resultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){
            carImgButton.setImageBitmap(null)
            try {
                contentResolver.openInputStream(path)?.use { inputStream ->

                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val matrix = Matrix()
                    matrix.postRotate(90F)
                    val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

                    val desiredWidth = carImgButton.width
                    val desiredHeight = carImgButton.height

                    val scaleWidth = desiredWidth.toFloat() / rotatedBitmap.width
                    val scaleHeight = desiredHeight.toFloat() / rotatedBitmap.height

                    val scaleFactor = if (scaleWidth < scaleHeight) scaleWidth else scaleHeight

                    val scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap,
                        (rotatedBitmap.width * scaleFactor).toInt(),
                        (rotatedBitmap.height * scaleFactor).toInt(), true)

                    carImgButton.setImageBitmap(scaledBitmap)
                }
            } catch (e: Exception) {
                // Maneja cualquier error al cargar la imagen
                Log.e("TAG", "Error cargando la imagen: ${e.message}")
            }

        }
        val resultLauncher2 = registerForActivityResult(ActivityResultContracts.TakePicture()){
            LicImgButton.setImageBitmap(null)
            try {
                contentResolver.openInputStream(path)?.use { inputStream ->

                    // Decodifica el flujo de entrada en un objeto Bitmap
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val matrix = Matrix()
                    matrix.postRotate(90F) // Replace degreesToRotate with the appropriate value
                    val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

                    val desiredWidth = LicImgButton.width
                    val desiredHeight = LicImgButton.height

                    val scaleWidth = desiredWidth.toFloat() / rotatedBitmap.width
                    val scaleHeight = desiredHeight.toFloat() / rotatedBitmap.height

                    val scaleFactor = if (scaleWidth < scaleHeight) scaleWidth else scaleHeight

                    val scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap,
                        (rotatedBitmap.width * scaleFactor).toInt(),
                        (rotatedBitmap.height * scaleFactor).toInt(), true)

                    LicImgButton.setImageBitmap(scaledBitmap)
                }
            } catch (e: Exception) {
                // Maneja cualquier error al cargar la imagen
                Log.e("TAG", "Error cargando la imagen: ${e.message}")
            }

        }
        val resultLauncher3 = registerForActivityResult(ActivityResultContracts.TakePicture()){
            userImgButton.setImageBitmap(null)
            try {
                contentResolver.openInputStream(path)?.use { inputStream ->

                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val matrix = Matrix()
                    matrix.postRotate(90F)
                    val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

                    val desiredWidth = userImgButton.width
                    val desiredHeight = userImgButton.height

                    val scaleWidth = desiredWidth.toFloat() / rotatedBitmap.width
                    val scaleHeight = desiredHeight.toFloat() / rotatedBitmap.height

                    val scaleFactor = if (scaleWidth < scaleHeight) scaleWidth else scaleHeight

                    val scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap,
                        (rotatedBitmap.width * scaleFactor).toInt(),
                        (rotatedBitmap.height * scaleFactor).toInt(), true)

                    userImgButton.setImageBitmap(scaledBitmap)
                }
            } catch (e: Exception) {
                // Maneja cualquier error al cargar la imagen
                Log.e("TAG", "Error cargando la imagen: ${e.message}")
            }

        }

        carImgButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED) {
                resultLauncher.launch(path)
            } else {
                requestCameraPermission()
                Toast.makeText(this, "Camera permission is required for this feature", Toast.LENGTH_LONG).show()
            }
        }

        LicImgButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED) {
                resultLauncher2.launch(path)
            } else {
                requestCameraPermission()
                Toast.makeText(this, "Camera permission is required for this feature", Toast.LENGTH_LONG).show()
            }
        }

        userImgButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED) {
                resultLauncher3.launch(path)
            } else {
                requestCameraPermission()
                Toast.makeText(this, "Camera permission is required for this feature", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun checks(userText: EditText, pwdText: EditText, placaText: EditText, buttonSubmit: AppCompatButton, carImgButton: ImageButton, LicImgButton: ImageButton,userImgButton: ImageButton,userButton : RadioButton, driverButton: RadioButton) {
        buttonSubmit.setOnClickListener {
            val isEmptyuserText = userText.text.isNullOrEmpty()
            val isEmptypwdText = pwdText.text.isNullOrEmpty()
            val isEmptyplacaText = pwdText.text.isNullOrEmpty()
            val carImgButtonHasImage = hasImage(carImgButton)
            val LicImgButtonHasImage = hasImage(LicImgButton)
            val userImgButton = hasImage(LicImgButton)


            if (isEmptyuserText) {
                userText.setBackgroundResource(R.drawable.custom_rectangle2)
            } else {
                userText.setBackgroundResource(R.drawable.custom_rectangle)
            }
            if (isEmptypwdText) {
                pwdText.setBackgroundResource(R.drawable.custom_rectangle2)
            } else {
                pwdText.setBackgroundResource(R.drawable.custom_rectangle)
            }
            if (isEmptyplacaText) {
                placaText.setBackgroundResource(R.drawable.custom_rectangle2)
            } else {
                placaText.setBackgroundResource(R.drawable.custom_rectangle)
            }
            if (carImgButtonHasImage) {
                carImgButton.setBackgroundResource(R.drawable.custom_rectangle2)
            } else {
                carImgButton.setBackgroundResource(R.drawable.custom_rectangle)
            }
            if (LicImgButtonHasImage) {
                LicImgButton.setBackgroundResource(R.drawable.custom_rectangle2)
            } else {
                LicImgButton.setBackgroundResource(R.drawable.custom_rectangle)
            }
            if (userImgButton) {
                LicImgButton.setBackgroundResource(R.drawable.custom_rectangle2)
            } else {
                LicImgButton.setBackgroundResource(R.drawable.custom_rectangle)
            }
            if(driverButton.isChecked){
                if (!isEmptyuserText && !isEmptypwdText && !isEmptyplacaText && carImgButtonHasImage && LicImgButtonHasImage && userImgButton) {
                    startActivity(Intent(this, MenuActivityD::class.java))
                }

                else{
                    Toast.makeText(this,"Completa todos los campos",Toast.LENGTH_LONG).show()
                }
            }else if(userButton.isChecked){
                if (!isEmptyuserText && !isEmptypwdText && userImgButton) {
                    startActivity(Intent(this, MenuActivityU::class.java))
                }

                else{
                    Toast.makeText(this,"Completa todos los campos",Toast.LENGTH_LONG).show()

            }
        }
    } }
    private fun hasImage(imgButton: ImageButton): Boolean {
        val defaultImageResource = R.drawable.cameralog
        val currentImageResource = imgButton.drawable?.constantState?.hashCode()
        return currentImageResource != defaultImageResource
    }
    private fun checksRadioButton(userButton : RadioButton, driverButton: RadioButton, linearGrande: LinearLayout){
        var radioButtonSelected = false
        userButton.isChecked = true
        linearGrande.visibility = View.INVISIBLE

        userButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                radioButtonSelected = true
                driverButton.isChecked = false
                mostarComponentes(linearGrande,radioButtonSelected)
            } else {
                userButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                radioButtonSelected = false
            }
        }

        driverButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                radioButtonSelected = false
                userButton.isChecked = false
                mostarComponentes(linearGrande,radioButtonSelected)
            } else {
                radioButtonSelected = true
                driverButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
            }
        }
    }
    private fun mostarComponentes( linearGrande: LinearLayout, radioButtonSelected :Boolean ) {
        if (radioButtonSelected) {
            linearGrande.visibility = View.INVISIBLE
        } else {
            linearGrande.visibility = View.VISIBLE

        }
    }

}