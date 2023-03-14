package com.example.fragmentproject.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentproject.interactor.LoginInteractor
import com.example.fragmentproject.model.AlumnoUser
import com.example.fragmentproject.model.RequestCall
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.launch

class AppViewModel(private var interactor: LoginInteractor) : ViewModel() {

    private var _setData: MutableLiveData<Boolean> = MutableLiveData()
    val setData: LiveData<Boolean> get() = _setData

    private val _newUser: MutableLiveData<Task<AuthResult>> = MutableLiveData()
    val newUser: LiveData<Task<AuthResult>> = _newUser

    private val _login: MutableLiveData<Task<AuthResult>> = MutableLiveData()
    val login: LiveData<Task<AuthResult>> = _login

    private val _datosPersonales : MutableLiveData<Task<Void>> = MutableLiveData()
    val datosPersonales :  LiveData<Task<Void>> =_datosPersonales

    private val _datosUI : MutableLiveData<DatabaseReference> = MutableLiveData()
    val datosUI : LiveData<DatabaseReference> = _datosUI

    private val _isRegister : MutableLiveData<DatabaseReference> = MutableLiveData()
    val isRegister :  LiveData<DatabaseReference> = _isRegister

    private val _setAsistencia : MutableLiveData<Task<Void>> = MutableLiveData()
    val setAsistencia : LiveData<Task<Void>> = _setAsistencia

    private val _isConnected : MutableLiveData<DatabaseReference> = MutableLiveData()
    val isconnected : LiveData<DatabaseReference> = _isConnected

    private val _logOut :  MutableLiveData<Unit> = MutableLiveData()
    val logOut :  LiveData<Unit> = _logOut

    private val _misAsistencias : MutableLiveData <DatabaseReference> = MutableLiveData()
    val  misAsistencias : LiveData<DatabaseReference> = _misAsistencias

    private val _tokenQR : MutableLiveData<DatabaseReference> = MutableLiveData()
    val tokenQR : LiveData<DatabaseReference> get() = _tokenQR

    fun setValueFirebase(nombre: String, dni: String) {
        viewModelScope.launch {
            _setData.value = interactor.putVelue(nombre, dni)
        }

    }

    fun setNewUser(email: String, password : String) {
        viewModelScope.launch {
            _newUser.postValue(interactor.postNewRegister(email,password))
        }
    }

    fun setLogInUser(email: String,password: String) {
        viewModelScope.launch {
            _login.postValue(interactor.userLogInMailAndPass(email,password))
        }
    }

    fun setDatosPersonales(alumnoUser: AlumnoUser){
        viewModelScope.launch {
            _datosPersonales.postValue(interactor.userDataRegister(alumnoUser))
        }
    }

    fun getDataUI(uid: String){
        viewModelScope.launch {
            _datosUI.value = interactor.getDataUI(uid)
        }
    }

    fun tieneDatos(uid : String){
        viewModelScope.launch {
            _isRegister.value = interactor.tieneDatos(uid)
        }
    }

    fun setAsistencia(user_id: String, fecha: String){
        viewModelScope.launch {
            _setAsistencia.postValue(interactor.setAsistencia(user_id, fecha))
        }
    }

    fun isConnected(){
        viewModelScope.launch{
            _isConnected.postValue(interactor.isConnected())
        }
    }

    fun getAsistencias(){
        viewModelScope.launch {
            _misAsistencias.postValue(interactor.getAsistencias())
        }
    }

    fun logOut(){
        viewModelScope.launch{
            _logOut.value = interactor.logOut()
        }
    }

    fun getTokenQR(){
        viewModelScope.launch {
            _tokenQR.value = interactor.getTokenQR()
        }
    }
}


