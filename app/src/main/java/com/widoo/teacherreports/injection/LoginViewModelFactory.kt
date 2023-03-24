package com.widoo.teacherreports.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.widoo.teacherreports.interactor.LoginInteractor
import com.widoo.teacherreports.viewmodel.AppViewModel

class LoginViewModelFactory (private val interactor: LoginInteractor) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppViewModel(interactor) as T
    }
}