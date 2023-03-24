package com.example.fragmentproject.extension

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fragmentproject.model.AlumnosDatos

fun Fragment.replaceFragment(fragment: Fragment, backEnable: Boolean){

    activity?.let { ac ->
        (ac as AppCompatActivity).replaceFragment(fragment,backEnable)
    }
}

fun Fragment.setDataSharedPreferences(activity: AppCompatActivity, dni : String ){

    val sharedPreferences = activity.getSharedPreferences("base", Context.MODE_PRIVATE)
    sharedPreferences.edit().apply{
        putString("dni",dni)
        apply()
    }
}

fun Fragment.getDataSharedPreferences(activity: AppCompatActivity) : String?{

    val sharedPreferences = activity.getSharedPreferences("base",Context.MODE_PRIVATE)?:null
    return sharedPreferences?.getString("dni","desconocido")

}