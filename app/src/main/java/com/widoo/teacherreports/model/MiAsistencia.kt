package com.widoo.teacherreports.model

data class MiAsistencia (val dia: String,val presente: Boolean, val numero: Int )

data class NuevaClase(val dia: String, var numero: Int)