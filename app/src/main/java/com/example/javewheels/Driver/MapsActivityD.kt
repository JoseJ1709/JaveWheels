package com.example.javewheels.Driver

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.javewheels.Data.AdapterD1
import com.example.javewheels.Data.AdapterD2
import com.example.javewheels.Data.DataSingleton
import com.example.javewheels.Data.DataSingleton.Companion.GeoPointAux
import com.example.javewheels.R
import com.google.android.gms.common.internal.Objects.ToStringHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.api.IMapController
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.TilesOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.ArrayList


class MapsActivityD : AppCompatActivity(), SensorEventListener, AdapterD1.OnDataChangeListener {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private lateinit var adapterDis: AdapterD1
    private lateinit var adapterConf: AdapterD2
    lateinit var roadManager: RoadManager
    private lateinit var map: MapView
    private lateinit var sensorManager : SensorManager
    private var lightSensor : Sensor? = null
    private lateinit var locationOverlay: MyLocationNewOverlay
    private var roadOverlay: Polyline? = null
    val locationsWithinRadius = mutableListOf<GeoPoint>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_d)
        roadManager = OSRMRoadManager(this, "ANDROID")
        Configuration.getInstance().setUserAgentValue(BuildConfig.BUILD_TYPE)
        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)
        setUpSensorStuff()
        val buttonDispo = findViewById<AppCompatButton>(R.id.buttonSubmit)
        val buttonConf = findViewById<AppCompatButton>(R.id.buttonSubmit2)
        val buttonAtras = findViewById<AppCompatButton>(R.id.buttonSubmit3)
        val textView = findViewById<TextView>(R.id.ViajeCheck)
        val sheet = findViewById<FrameLayout>(R.id.sheet)
        val distList = mutableListOf<String>()
        val confirmList = mutableListOf<String>()
        val disList = findViewById<ListView>(R.id.lista)
        val confimList = findViewById<ListView>(R.id.lista2)

        buttonDispo.setOnClickListener {
            confimList.visibility = VISIBLE
            buttonConf.visibility = VISIBLE
            buttonAtras.visibility = VISIBLE
            disList.visibility = INVISIBLE
            buttonDispo.visibility = INVISIBLE
            adapterConf.notifyDataSetChanged()
            val routePoints = DataSingleton.convertListToRoute(locationOverlay.myLocation)
            drawRoute(routePoints)
        }

        buttonConf.setOnClickListener {
            confimList.visibility = INVISIBLE
            buttonConf.visibility = INVISIBLE
            buttonAtras.visibility = INVISIBLE
            textView.visibility = VISIBLE
        }

        buttonAtras.setOnClickListener {
            confimList.visibility = INVISIBLE
            buttonConf.visibility = INVISIBLE
            buttonAtras.visibility = INVISIBLE

            disList.visibility = VISIBLE
            buttonDispo.visibility = VISIBLE
            adapterDis.notifyDataSetChanged()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(sheet)
        bottomSheetBehavior.peekHeight = 500
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        // Crear instancias de los adaptadores personalizados
        adapterDis = AdapterD1(this, DataSingleton.ChoseList)
        adapterDis.onDataChangeListener = this
        adapterConf = AdapterD2(this, DataSingleton.SendList)


        disList.adapter = adapterDis
        confimList.adapter = adapterConf


    }
    private fun setupLocationOverlay(mapController: IMapController) {
        val locationProvider = GpsMyLocationProvider(this)
        locationOverlay = MyLocationNewOverlay(locationProvider, map)
        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()

        map.overlays.add(locationOverlay)

        locationOverlay.runOnFirstFix {
            runOnUiThread {
                if (locationOverlay.myLocation != null) {
                    if(DataSingleton.GeoPointAux.latitude == 0.0 && DataSingleton.GeoPointAux.longitude == 0.0 ){
                        val currentLocation = locationOverlay.myLocation
                        val latitude = currentLocation.latitude
                        val longitude = currentLocation.longitude
                        mapController.setZoom(15.0)
                        mapController.setCenter(locationOverlay.myLocation)
                        GeoPointAux = GeoPoint(latitude, longitude)
                        makeRadio(GeoPointAux, 1000.0)

                    }else{
                        makeRadio(GeoPointAux, 1000.0)
                    }


                }
            }
        }
    }
    private fun drawRoute(points: List<GeoPoint>) {
        GlobalScope.launch(Dispatchers.IO) {
            val road = roadManager.getRoad(points as ArrayList<GeoPoint>?)
            withContext(Dispatchers.Main) {
                Log.i("OSM_acticity", "Route length: ${road.mLength} klm")
                Log.i("OSM_acticity", "Duration: ${road.mDuration / 60} min")
                if (map != null) {
                    roadOverlay?.let { map.overlays.remove(it) }
                    roadOverlay = RoadManager.buildRoadOverlay(road)
                    roadOverlay?.outlinePaint?.color = Color.RED
                    roadOverlay?.outlinePaint?.strokeWidth = 10f
                    map.overlays.add(roadOverlay)

                    // Add a marker for each point on the route
                    for (point in points) {
                        val marker = Marker(map)
                        marker.position = point
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        map.overlays.add(marker)
                    }

                    Toast.makeText(this@MapsActivityD, "Ruta dibujada con éxito", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun setUpSensorStuff() {
        sensorManager = getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            if (event.values[0] < 10.0) {
                map.overlayManager.tilesOverlay.setColorFilter(TilesOverlay.INVERT_COLORS)
            } else {
                map.overlayManager.tilesOverlay.setColorFilter(null)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }
    private fun makeRadio(center: GeoPoint, radius: Double): List<GeoPoint> {
        locationsWithinRadius.clear()

        for (location in DataSingleton.GeoPointList) {
            val distance = center.distanceToAsDouble(location)
            Toast.makeText(this, "entre a makeRadio ${distance}", Toast.LENGTH_SHORT).show()
            if (distance <= radius) {
                locationsWithinRadius.add(location)
            }
        }
        Toast.makeText(this, "entre a makeRadio ${locationsWithinRadius.size}", Toast.LENGTH_SHORT).show()
        for (location in locationsWithinRadius) {
            val marker = Marker(map)
            marker.position = location
            marker.title = "Location ${location.latitude}, ${location.longitude}"
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            map.overlays.add(marker)
        }

        return locationsWithinRadius
    }
        override fun onPause() {
        super.onPause()
            map.overlays.remove(locationOverlay)
            sensorManager.unregisterListener(this)
        }
    override fun onResume() {
        super.onResume()
        val mapController = map.controller
        setupLocationOverlay(mapController)
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    interface OnDataChangeListener {
        fun onDataChanged()
    }

    override fun onDataChanged() {
        val latitud = GeoPointAux.latitude
        val longitud = GeoPointAux.longitude
        GeoPointAux = GeoPoint(latitud, longitud)
        makeRadio(GeoPointAux, 1000.0)
    }

}