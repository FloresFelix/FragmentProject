package com.widoo.teacherreports

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.widoo.teacherreports.databinding.ActivityMainBinding
import com.widoo.teacherreports.extension.replaceFragment
import com.widoo.teacherreports.ui.home.HomeFragment
import com.widoo.teacherreports.ui.login.LoginFragment
import com.widoo.teacherreports.utils.Constants.DATOS_PERSONALES
import com.widoo.teacherreports.utils.Constants.LOG_IN_APP
import com.widoo.teacherreports.utils.Constants.SHAREDPREFERENCES_NAME_APP

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(SHAREDPREFERENCES_NAME_APP,Context.MODE_PRIVATE)
        val value = sharedPreferences.getBoolean(LOG_IN_APP,false)
        val datos = sharedPreferences.getBoolean(DATOS_PERSONALES,false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        when(value && datos){
            true -> {
                replaceFragment(HomeFragment(),false)
            }
            else-> {
                replaceFragment(LoginFragment(),false)
            }
        }
    }
}