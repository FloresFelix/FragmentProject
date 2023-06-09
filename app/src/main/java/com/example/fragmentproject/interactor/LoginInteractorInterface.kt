package com.example.fragmentproject.interactor

import com.example.fragmentproject.model.AlumnoUser
import com.example.fragmentproject.model.NuevaClase
import com.example.fragmentproject.model.RequestCall
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

interface LoginInteractorInterface {

    suspend fun putVelue (nombre : String, dni : String) : Boolean

    suspend fun postNewRegister( email: String, password: String) : Task<AuthResult>

    suspend fun userLogInMailAndPass( email: String,password: String) : Task<AuthResult>

    suspend fun userDataRegister(alumnoUser: AlumnoUser): Task<Void>

    suspend fun getDataUI(uid: String): DatabaseReference

    suspend fun tieneDatos(uid: String): DatabaseReference

    suspend fun setAsistencia(user_id: String, fecha: String): Boolean

    suspend fun isConnected(): DatabaseReference

    suspend fun getAsistencias(): DatabaseReference

    suspend fun getTokenQR(): DatabaseReference

    suspend fun logOut() : Unit

    suspend fun getListaVideos() : DatabaseReference
   // suspend fun getCantidadAsistencias() : DatabaseReference

    suspend fun setNuevoDia(fecha: String, value: NuevaClase) : Task<Void>

    suspend fun dummyTest() : String
}