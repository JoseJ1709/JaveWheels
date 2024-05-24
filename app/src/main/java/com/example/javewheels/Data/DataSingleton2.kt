package com.example.javewheels.Data

import android.content.Context
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.osmdroid.util.GeoPoint
import java.io.IOException
import java.nio.charset.Charset

class DataSingleton2 {
    companion object {
        var User : DatoD4 = DatoD4(0,"","")
        var Driver: DatoD3  = DatoD3(0,"","")
        var notificacionesList: MutableList<DatoD2> = mutableListOf()
        var GeoPointList: MutableList<GeoPoint> = mutableListOf()

        fun loadData(context: Context) {
            val datos = loadNotificacionesFromJSON(context)
            notificacionesList = convertJSONArrayToListN(datos).toMutableList()

            if (DataSingleton2.User.id == 0){
                val datos2 = loadUserFromJSON(context)
                convertJSONObjectToDatoU(datos2)
            }

            val jsonObject = loadDriverFromJSON(context)

            val datosDriver = jsonObject.getJSONArray("datos")
            convertJSONObjectToDatoD(datosDriver.getJSONObject(0))

            val route = jsonObject.getJSONArray("route")
            GeoPointList = convertJSONArrayToGeoPointList(route).toMutableList()


        }

        fun convertJSONArrayToGeoPointList(jsonArray: JSONArray): List<GeoPoint> {
            val list = mutableListOf<GeoPoint>()
            for (i in 0 until jsonArray.length()) {
                try {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val longitude = jsonObject.getString("longitud").toDouble()
                    val latitude = jsonObject.getString("latiud").toDouble()
                    val geoPoint = GeoPoint(latitude, longitude)
                    list.add(geoPoint)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            return list
        }
        fun loadNotificacionesFromJSON(context: Context): JSONArray {
            var json: String? = null
            try {
                val `is` = context.assets.open("datos.json")
                val size = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                json = String(buffer, Charset.forName("UTF-8"))
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            val jsonObject = JSONObject(json)
            return jsonObject.getJSONArray("datos")
        }
        fun loadDriverFromJSON(context: Context): JSONObject {
            var json: String? = null
            try {
                val `is` = context.assets.open("userTests.json")
                val size = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                json = String(buffer, Charset.forName("UTF-8"))
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            return JSONObject(json)
        }
        fun loadUserFromJSON(context: Context): JSONObject {
            var json: String? = null
            try {
                val `is` = context.assets.open("datos4.json")
                val size = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                json = String(buffer, Charset.forName("UTF-8"))
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            val jsonObject = JSONObject(json)
            return jsonObject.getJSONArray("datos").getJSONObject(0)
        }

        fun convertJSONArrayToListN(jsonArray: JSONArray): List<DatoD2> {
            val list = mutableListOf<DatoD2>()
            for (i in 0 until jsonArray.length()) {
                try {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val street = jsonObject.getString("time")
                    val distance = jsonObject.getString("direction")
                    val dato = DatoD2(street, distance)
                    list.add(dato)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            return list
        }

        fun convertJSONObjectToDatoD(jsonObject: JSONObject) {
            try {
                val id = jsonObject.getInt("id")
                val nombre = jsonObject.getString("nombre")
                val placa = jsonObject.getString("placa")
                Driver = DatoD3(id,nombre, placa)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return
        }
        fun convertJSONObjectToDatoU(jsonObject: JSONObject) {
            try {
                val id = jsonObject.getInt("id")
                val nombre = jsonObject.getString("nombre")
                val contrasena = jsonObject.getString("contrasena")
                User = DatoD4(id,nombre, contrasena)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return
        }
    }
}