package com.example.javewheels.Data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.javewheels.Data.DataSingleton.Companion.GeoPointAux
import com.example.javewheels.R
import org.osmdroid.util.GeoPoint
class AdapterD1(private val context: Context?, private var facts: List<DatoD1>) : BaseAdapter() {

    //retorna el tamaño de la lista
    var onDataChangeListener: OnDataChangeListener? = null
    override fun getCount(): Int {
        return facts.size
    }

    //retorna un objeto de la lista en la posicion dada
    override fun getItem(position: Int): DatoD1 {
        return facts[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() // You can use a unique ID if available
    }

    //obtiene los datos y la direccion de donde se va a mostrar la lista y los muestra.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.view_user1, parent, false)

        val precio = view.findViewById<TextView>(R.id.precio)
        val distancia = view.findViewById<TextView>(R.id.distancia)

        val datoObject = getItem(position)
        val name = datoObject.name
        precio?.text = datoObject.price


        view.setOnClickListener {
            val datoTo = DatoD1(datoObject.name, datoObject.price, datoObject.latitud, datoObject.longitud) // Reemplaza esto con el objeto DatoD1 que deseas agregar
            DataSingleton.addDatoS(datoTo)
            DataSingleton.removeDatoC(datoTo)
            facts = DataSingleton.ChoseList
            this@AdapterD1.notifyDataSetChanged()

            val latitud = datoObject.latitud.toDoubleOrNull()
            val longitud = datoObject.longitud.toDoubleOrNull()

            if (latitud != null && longitud != null) {
                GeoPointAux = GeoPoint(latitud, longitud)
                onDataChangeListener?.onDataChanged()
            } else {
            }
        }

        return view
    }
    interface OnDataChangeListener {
        fun onDataChanged()
    }
}