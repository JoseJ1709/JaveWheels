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

class AdapterNoti(private val context: Context?, private val facts: List<DatoD2>) : BaseAdapter() {

    //retorna el tamaño de la lista
    override fun getCount(): Int {
        return facts.size
    }

    //retorna un objeto de la lista en la posicion dada
    override fun getItem(position: Int): DatoD2 {
        return facts[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() // You can use a unique ID if available
    }

    //obtiene los datos y la direccion de donde se va a mostrar la lista y los muestra.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.notificacion, parent, false)

        val tiempo = view.findViewById<TextView>(R.id.tiempo)
        val direccion = view.findViewById<TextView>(R.id.direccion)

        val datoObject = getItem(position)
        tiempo?.text = datoObject.time
        direccion?.text = datoObject.street


        view.setOnClickListener {
            //Meter en la lista de confirmados
            Toast.makeText(context, "Nada", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}