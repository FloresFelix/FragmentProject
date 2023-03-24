package com.example.fragmentproject.interactor

import android.content.Context
import android.widget.Toast
import com.example.fragmentproject.R
import com.example.fragmentproject.model.AlumnoUser
import com.example.fragmentproject.model.AsistenciaRegistro
import com.example.fragmentproject.model.NuevaClase
import com.example.fragmentproject.model.RequestCall
import com.example.fragmentproject.utils.Constants
import com.example.fragmentproject.utils.Constants.ALUMNOS
import com.example.fragmentproject.utils.Constants.CLASES
import com.example.fragmentproject.utils.Constants.DATOS_PERSONALES
import com.example.fragmentproject.utils.Constants.NOMBRE
import com.example.fragmentproject.utils.Constants.PRESENTES
import com.example.fragmentproject.utils.Constants.USER_UID
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

open class LoginInteractor : LoginInteractorInterface{

    override suspend fun putVelue(nombre : String, dni : String): Boolean {
        val susses = true
        val database = FirebaseDatabase.getInstance().reference
        database.child("alumno").setValue(nombre)
        database.child("alumno").setValue(dni)

        return susses
    }

    override suspend fun postNewRegister(email: String, password: String): Task<AuthResult> {
      return FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
    }

    override suspend fun userLogInMailAndPass(email: String,password: String): Task<AuthResult> {
       return FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
    }

    override suspend fun userDataRegister(alumnoUser: AlumnoUser): Task<Void> {
        val uid =  FirebaseAuth.getInstance().uid
        return FirebaseDatabase.getInstance().reference.child(ALUMNOS).child(uid.toString()).child(DATOS_PERSONALES).setValue(alumnoUser)
    }

    override suspend fun getDataUI(uid: String): DatabaseReference {
        val bd = FirebaseDatabase.getInstance().reference
        return bd.child(ALUMNOS).child(uid).child(DATOS_PERSONALES)
    }

    override suspend fun tieneDatos(uid: String): DatabaseReference {
        val bd = FirebaseDatabase.getInstance().reference
        return bd.child(ALUMNOS).child(uid).child(DATOS_PERSONALES)
    }

    override suspend fun setAsistencia(user_id: String, fecha: String): Boolean {
        val fireBaseDB = FirebaseDatabase.getInstance().reference
        return fireBaseDB.child(CLASES).child(fecha).child(PRESENTES).child(user_id).setValue(true).isSuccessful
    }

    override suspend fun isConnected(): DatabaseReference  = FirebaseDatabase.getInstance().getReference(".info/connected")

    override suspend fun getAsistencias(): DatabaseReference {
        val firebaseDB = FirebaseDatabase.getInstance().reference
        return firebaseDB.child(CLASES)
    }

    override suspend fun getTokenQR(): DatabaseReference {
        val firebaseDB = FirebaseDatabase.getInstance().reference
        return firebaseDB.child("token_qr")
    }

    override suspend fun logOut(): Unit {
        FirebaseAuth.getInstance().signOut()
        return
    }

    override suspend fun getListaVideos() = FirebaseDatabase.getInstance().reference.child("lista_videos")

    override suspend fun setNuevoDia(fecha: String, value:NuevaClase): Task<Void> {
       val firebaseDB = FirebaseDatabase.getInstance().reference
       return firebaseDB.child(CLASES).child(fecha).setValue(value)
    }


    override suspend fun dummyTest(): String {
       val firebaseDB = FirebaseDatabase.getInstance().reference
        var nombre = ""
        firebaseDB.child(ALUMNOS).child("").child(DATOS_PERSONALES).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(data: DataSnapshot) {
              nombre = data.child(NOMBRE).value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return "nombre"
    }
}