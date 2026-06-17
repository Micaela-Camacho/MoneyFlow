package com.example.moneyflow

import android.app.Application
import com.example.moneyflow.data.MoneyFlowDatabase

class MoneyFlowApp : Application(){
    // La base de datos se inicializa la primera vez que se la necesita (lazy)
    val database: MoneyFlowDatabase by lazy {
        MoneyFlowDatabase.obtenerBaseDatos(this)
    }
}