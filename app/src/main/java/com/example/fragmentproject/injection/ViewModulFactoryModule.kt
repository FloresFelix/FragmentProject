package com.example.fragmentproject.injection

import com.example.fragmentproject.interactor.LoginInteractor

object ViewModulFactoryModule {

    val loginViewModelFactory : LoginViewModelFactory get() = LoginViewModelFactory(LoginInteractor())
}