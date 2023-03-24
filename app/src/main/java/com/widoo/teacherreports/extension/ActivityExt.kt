package com.widoo.teacherreports.extension

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.widoo.teacherreports.R

fun AppCompatActivity.replaceFragment(fragment: Fragment, backEnable : Boolean){
    supportFragmentManager.popBackStack()
    /*
    supportFragmentManager.beginTransaction().apply {
        if (backEnable) addToBackStack(null)
        replace(R.id.fragment_host, fragment)
    }

     */
    supportFragmentManager.commit {
        setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
        )
        if (backEnable) addToBackStack(null)
        replace(R.id.fragment_host, fragment)
    }
}

fun AppCompatActivity.openActivity(){
    try {
        startActivity(Intent(applicationContext, com.widoo.teacherreports.MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }).apply {
            finish()
        }

    }catch (e: ActivityNotFoundException){
        finishAffinity()
    }
}



