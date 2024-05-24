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

class AdapterDriver(private val context: Context?, private var facts: List<DatoD3>) : BaseAdapter() {

    //retorna el tamaño de la lista
    override fun getCount(): Int {
        return facts.size
    }

    //retorna un objeto de la lista en la posicion dada
    override fun getItem(position: Int): DatoD3 {
        return facts[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() // You can use a unique ID if available
    }

    //obtiene los datos y la direccion de donde se va a mostrar la lista y los muestra.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.view_driver, parent, false)

        val nombre = view.findViewById<TextView>(R.id.name)
        val placa = view.findViewById<TextView>(R.id.placa)

        val datoObject = getItem(position)
        val name = datoObject.nombre
        nombre?.text = datoObject.nombre
        placa?.text = datoObject.placa


        return view
    }
}