package com.example.fragmentproject.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fragmentproject.interactor.LoginInteractor
import com.example.fragmentproject.viewmodel.AppViewModel

class LoginViewModelFactory (private val interactor: LoginInteractor) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppViewModel(interactor) as T
    }
}