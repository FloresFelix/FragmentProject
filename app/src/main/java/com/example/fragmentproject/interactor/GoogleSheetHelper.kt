package com.example.fragmentproject.interactor

import android.content.Context
import android.util.Log
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fragmentproject.model.AlumnosDatos
import kotlin.math.log

object GoogleSheetHelper {

    private val url="https://script.google.com/macros/s/AKfycbxop-Zw7IqawB93ZTLVBCmxg7CHQI2vTEZwyp6GzXqVxhUlu38G0pMqheDhOPUruCFnqA/exec"

    fun conectionGoogleSheet(context: Context, objeto : AlumnosDatos ){

        val queue = Volley.newRequestQueue(context)

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST,
            url,
            { res ->

                try {
                    Log.e("scan",res.toString())
                    println("buen trabajo" + res.toString())
                } catch (e: Exception) {
                    Log.e("scan",e.stackTraceToString())
                    println("salio mal")
                }
            },
            { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val params: MutableMap<String, String> = HashMap()

                with(params){
                    put("action",objeto.action )
                    put("dni", objeto.dni);
                    with(objeto){
                        apellido?.let {
                            put("apellido",it)
                        }
                        nombre?.let {
                            put("nombre",it)
                        }
                        dia?.let {
                            put("dia",it)
                        }
                    }

                }
                return params
            }
        }

        queue.add(stringRequest)
    }
}