package com.example.javewheels.Data

import android.content.Context
import android.util.Log
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.osmdroid.util.GeoPoint
import java.io.IOException
import java.nio.charset.Charset

class DataSingleton {
    companion object {
        var ChoseList: MutableList<DatoD1> = mutableListOf()
        var SendList: MutableList<DatoD1> = mutableListOf()
        var notificacionesList: MutableList<DatoD2> = mutableListOf()
        var GeoPointList: MutableList<org.osmdroid.util.GeoPoint> = mutableListOf()
        var GeoPointAux : org.osmdroid.util.GeoPoint = org.osmdroid.util.GeoPoint(0.0,0.0)
        var GeoPointList2: MutableList<GeoPoint> = mutableListOf()

        var User : DatoD5 = DatoD5(0,"","","")

        fun convertListToRoute(currentLocation: GeoPoint): List<GeoPoint> {
            val routePoints = mutableListOf<GeoPoint>()

            // Agrega tu ubicación actual al inicio de la ruta
            routePoints.add(currentLocation)

            // Agrega todos los puntos en SendList a la ruta
            for (dato in SendList) {
                val latitud = dato.latitud.toDoubleOrNull()
                val longitud = dato.longitud.toDoubleOrNull()
                if (latitud != null && longitud != null) {
                    routePoints.add(GeoPoint(longitud, latitud))
                }
            }

            // Agrega el punto final fijo al final de la ruta
            routePoints.add(GeoPoint(4.628436, -74.064669))

            return routePoints
        }

        fun addDatoC(dato: DatoD1) {
            ChoseList.add(dato)
        }

        fun removeDatoC(dato: DatoD1) {
            for(i in ChoseList){
                if(i.name == dato.name){
                    ChoseList.remove(i)
                    break
                }
            }
        }

        fun addDatoS(dato: DatoD1) {
            SendList.add(dato)

        }

        fun removeDatoS(dato: DatoD1) {
            for(i in SendList){
                if(i.name == dato.name){
                    SendList.remove(i)
                    break
                }
            }
        }
        fun loadData(context: Context) {
            Toast.makeText(context, "Loading Data", Toast.LENGTH_SHORT).show()
            val datos = loadNotificacionesFromJSON(context)
            notificacionesList = convertJSONArrayToListN(datos).toMutableList()
            val tempList = mutableListOf<DatoD1>()
            if (User.id == 0){
                val datos3 = loadUserFromJSON(context)
                convertJSONObjectToDatoU(datos3)
            }
            val datos2 = loadDatos2FromJSON(context)
            val check1 = convertJSONArrayToList(datos2).toMutableList()
            GeoPointList = convertJSONArrayToListG(datos2).toMutableList()
            for (i in check1){
                var foundInChoseList = false
                var foundInSendList = false
                for (j in ChoseList){
                    if(i.name == j.name){
                        foundInChoseList = true
                        break
                    }
                }
                for (k in SendList){
                    if(i.name == k.name){
                        foundInSendList = true
                        break
                    }
                }
                if (!foundInChoseList && !foundInSendList){
                    tempList.add(i)
                }
            }
            if (tempList.isNotEmpty()){
                ChoseList.addAll(tempList)
                return
            }else{
                return
            }
        }
        fun convertJSONArrayToListG(jsonArray: JSONArray): List<org.osmdroid.util.GeoPoint> {
            val list = mutableListOf<org.osmdroid.util.GeoPoint>()
            for (i in 0 until jsonArray.length()) {
                try {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val longitude = jsonObject.getString("longitud").replace(',', '.').toDouble()
                    val latitude = jsonObject.getString("latitud").replace(',', '.').toDouble()
                    val geoPoint = org.osmdroid.util.GeoPoint(latitude, longitude)
                    list.add(geoPoint)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            return list
        }
        fun loadDatos2FromJSON(context: Context): JSONArray {
            var json: String? = null
            try {
                val `is` = context.assets.open("driverTests.json")
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
        fun loadUserFromJSON(context: Context): JSONObject {
            var json: String? = null
            try {
                val `is` = context.assets.open("datos5.json")
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

        fun convertJSONArrayToList(jsonArray: JSONArray): List<DatoD1> {
            val list = mutableListOf<DatoD1>()
            for (i in 0 until jsonArray.length()) {
                try {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val id = jsonObject.getInt("id")
                    val nombre = jsonObject.getString("nombre")
                    val precio = jsonObject.getString("precio")
                    val longitud = jsonObject.getString("longitud")
                    val latitud = jsonObject.getString("latitud")
                    val dato = DatoD1(id, nombre, precio, longitud, latitud)
                    list.add(dato)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            return list
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
        fun convertJSONObjectToDatoU(jsonObject: JSONObject) {
            try {
                val id = jsonObject.getInt("id")
                val nombre = jsonObject.getString("nombre")
                val contrasena = jsonObject.getString("contrasena")
                val placa = jsonObject.getString("placa")
                DataSingleton.User = DatoD5(id,nombre, contrasena,placa)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return
        }


    }
}