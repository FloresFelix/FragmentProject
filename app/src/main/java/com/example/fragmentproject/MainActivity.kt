package com.example.fragmentproject

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.example.fragmentproject.databinding.ActivityMainBinding
import com.example.fragmentproject.extension.replaceFragment
import com.example.fragmentproject.ui.home.HomeFragment
import com.example.fragmentproject.ui.login.LoginFragment
import com.example.fragmentproject.ui.web.WebViewFragment
import com.example.fragmentproject.utils.Constants.DATOS_PERSONALES
import com.example.fragmentproject.utils.Constants.LOG_IN_APP
import com.example.fragmentproject.utils.Constants.SHAREDPREFERENCES_NAME_APP

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