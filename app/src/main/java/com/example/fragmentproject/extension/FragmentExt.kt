package com.example.fragmentproject.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.replaceFragment(fragment: Fragment, backEnable: Boolean){

    activity?.let { ac ->
        (ac as AppCompatActivity).replaceFragment(fragment,backEnable)
    }
}