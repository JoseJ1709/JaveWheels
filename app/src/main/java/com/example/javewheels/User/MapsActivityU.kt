package com.example.javewheels.User

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.javewheels.Data.AdapterDriver
import com.example.javewheels.Data.DataSingleton2
import com.example.javewheels.R
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

class MapsActivityU : AppCompatActivity(), SensorEventListener {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private lateinit var adapterDriver: AdapterDriver
    lateinit var roadManager: RoadManager
    private lateinit var map: MapView
    private lateinit var sensorManager : SensorManager
    private var lightSensor : Sensor? = null
    private lateinit var locationOverlay: MyLocationNewOverlay
    private var roadOverlay: Polyline? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_u)
        roadManager = OSRMRoadManager(this, "ANDROID")
        Configuration.getInstance().setUserAgentValue(BuildConfig.BUILD_TYPE)
        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)
        setUpSensorStuff()

        val sheet = findViewById<FrameLayout>(R.id.sheet)
        val precioText = findViewById<TextView>(R.id.precioText)
        val precio = findViewById<EditText>(R.id.precio)
        val ViajeCheck = findViewById<TextView>(R.id.ViajeCheck)
        val ViajeCheck2 = findViewById<TextView>(R.id.ViajeCheck2)
        val Conductor = findViewById<LinearLayout>(R.id.Conductor)
        val ButtonContinuar = findViewById<AppCompatButton>(R.id.buttonContinuar)
        val ButtonCancelar = findViewById<AppCompatButton>(R.id.buttonCancelar)
        val ButtonCheck = findViewById<AppCompatButton>(R.id.buttonCheck)
        val name = findViewById<TextView>(R.id.name)
        val placa1 = findViewById<TextView>(R.id.placa)

        bottomSheetBehavior = BottomSheetBehavior.from(sheet)
        bottomSheetBehavior.peekHeight = 500
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        ButtonContinuar.setOnClickListener {
            val precio1 = precio.text.toString()

            ButtonContinuar.visibility = INVISIBLE
            precio.visibility = INVISIBLE
            precioText.visibility = INVISIBLE
            Conductor.visibility = INVISIBLE
            ViajeCheck.visibility = VISIBLE
            ViajeCheck2.visibility = VISIBLE
            ButtonCheck.visibility = VISIBLE
            ButtonContinuar.visibility = INVISIBLE
            ButtonCancelar.visibility = VISIBLE

        }

        ButtonCancelar.setOnClickListener {
            ButtonContinuar.visibility = VISIBLE
            precio.visibility = VISIBLE
            precioText.visibility = VISIBLE
            ViajeCheck.visibility = INVISIBLE
            ViajeCheck2.visibility = INVISIBLE
            ButtonCancelar.visibility = INVISIBLE
            ButtonCheck.visibility = INVISIBLE
        }

        ButtonCheck.setOnClickListener {
            ButtonCheck.visibility = INVISIBLE
            ViajeCheck.visibility = VISIBLE
            Conductor.visibility = VISIBLE
            ViajeCheck.visibility = INVISIBLE
            ViajeCheck2.visibility = INVISIBLE
            ButtonCancelar.visibility = INVISIBLE
            cargarJson(ButtonCheck,Conductor,ButtonCancelar,name,placa1)
            drawOnePoint(DataSingleton2.GeoPointList);
            drawRoute(DataSingleton2.GeoPointList)
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
    private fun setupLocationOverlay(mapController: IMapController) {
        val locationProvider = GpsMyLocationProvider(this)
        locationOverlay = MyLocationNewOverlay(locationProvider, map)
        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()

        map.overlays.add(locationOverlay)

        locationOverlay.runOnFirstFix {
            runOnUiThread {
                if (locationOverlay.myLocation != null) {
                    val currentLocation = locationOverlay.myLocation
                    val latitude = currentLocation.latitude
                    val longitude = currentLocation.longitude
                    mapController.setZoom(15.0)
                    mapController.setCenter(locationOverlay.myLocation)
                }
            }
        }
    }
    private fun drawRoute(points: MutableList<GeoPoint>) {
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

                    Toast.makeText(this@MapsActivityU, "Ruta dibujada con éxito", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun drawOnePoint(points: MutableList<GeoPoint>) {
        if (points.isNotEmpty()) {
            val firstPoint = points[0]
            val startMarker = Marker(map)
            startMarker.position = firstPoint
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            map.overlays.add(startMarker)
            map.invalidate() // Refresh the map
        } else {
            Toast.makeText(this, "La lista de puntos está vacía", Toast.LENGTH_SHORT).show()
        }
    }

    private fun marcarRuta() {
        Toast.makeText(this, "Ruta marcada", Toast.LENGTH_SHORT).show()
        val road = Polyline()   //create empty polyline
        road.setPoints(DataSingleton2.GeoPointList)
        map.overlays.add(road)   //add polyline to the map
        map.invalidate()   //refresh the map
    }

    private fun cargarJson(ButtonCheck: AppCompatButton, Conductor: LinearLayout, ButtonCancelar: AppCompatButton, name: TextView, placa1: TextView){
        ButtonCancelar.visibility = INVISIBLE
        Conductor.visibility = VISIBLE
        ButtonCheck.visibility = VISIBLE
        val nombre = DataSingleton2.Driver?.nombre
        val placa = DataSingleton2.Driver?.placa
        name.text = nombre
        placa1.text = placa
    }

}