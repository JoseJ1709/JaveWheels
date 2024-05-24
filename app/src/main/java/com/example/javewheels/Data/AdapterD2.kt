package com.example.javewheels.Data

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.javewheels.R

class AdapterD2(private val context: Context?, private var facts: List<DatoD1>) : BaseAdapter() {

    //retorna el tamaño de la lista
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
            .inflate(R.layout.view_user2, parent, false)

        val nombre = view.findViewById<TextView>(R.id.name)
        val precio = view.findViewById<TextView>(R.id.price)
        val distancia = view.findViewById<TextView>(R.id.distancia)


        val datoObject = getItem(position)
        nombre?.text = datoObject.name
        precio?.text = datoObject.price
        Toast.makeText(context, "Tryng Adapter2", Toast.LENGTH_SHORT).show()

        view.setOnClickListener {
            val datoTo = DatoD1(datoObject.name, datoObject.price, datoObject.latitud, datoObject.longitud)
            DataSingleton.addDatoC(datoTo)
            DataSingleton.removeDatoS(datoTo)
            facts = DataSingleton.SendList
            this@AdapterD2.notifyDataSetChanged()
        }

        return view
    }
}