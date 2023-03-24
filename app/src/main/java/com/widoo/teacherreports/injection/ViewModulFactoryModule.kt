package com.widoo.teacherreports.injection

import com.widoo.teacherreports.interactor.LoginInteractor


object ViewModulFactoryModule {

    val loginViewModelFactory : LoginViewModelFactory get() = LoginViewModelFactory(LoginInteractor())
}