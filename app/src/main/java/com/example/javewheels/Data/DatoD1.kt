package com.example.javewheels.Data

class DatoD1 {
    var id: Int = 0
    lateinit var photo: String
    lateinit var name: String
    var price: String
    var longitud: String = ""
    var latitud: String = ""

    constructor(id:Int,name: String, price: String, latitud: String, longitud: String) {
        this.id = id
        this.name = name
        this.price = price
        this.longitud = longitud
        this.latitud = latitud
    }
    constructor(name: String, price: String, latitud: String, longitud: String) {
        this.id = id
        this.name = name
        this.price = price
        this.longitud = longitud
        this.latitud = latitud
    }

}