package com.widoo.teacherreports.ui.home.misasistencias

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.widoo.teacherreports.R
import com.widoo.teacherreports.databinding.FragmentMisAsistenciasBinding
import com.widoo.teacherreports.injection.ViewModulFactoryModule
import com.widoo.teacherreports.model.MiAsistencia
import com.widoo.teacherreports.ui.home.adapters.MisAsistenciasAdapter
import com.widoo.teacherreports.utils.Constants
import com.widoo.teacherreports.utils.Constants.APELLIDO
import com.widoo.teacherreports.utils.Constants.DIA
import com.widoo.teacherreports.utils.Constants.DNI
import com.widoo.teacherreports.utils.Constants.NOMBRE
import com.widoo.teacherreports.utils.Constants.PRESENTES
import com.widoo.teacherreports.viewmodel.AppViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class MisAsistenciasFragment : Fragment() {

    private val viewModel by activityViewModels<AppViewModel>{ ViewModulFactoryModule.loginViewModelFactory}
    private lateinit var binding : FragmentMisAsistenciasBinding
    private lateinit var uid : String

    val lista = ArrayList<MiAsistencia>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMisAsistenciasBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uid = activity?.getSharedPreferences(Constants.SHAREDPREFERENCES_NAME_APP, Context.MODE_PRIVATE)?.getString(Constants.USER_UID, "nada").toString()
        viewModel.getAsistencias()
        viewModel.getDataUI(uid)
        observers()

    }

    private fun observers() {
        val uid = activity?.getSharedPreferences(Constants.SHAREDPREFERENCES_NAME_APP, Context.MODE_PRIVATE)?.getString(Constants.USER_UID, "nada")
        val clases = ArrayList<MiAsistencia>()
        viewModel.misAsistencias.observe(viewLifecycleOwner, Observer { result ->
            result.addValueEventListener(object : ValueEventListener {
                var cantidad = 0
                override fun onDataChange(data: DataSnapshot) {
                    clases.clear()
                    for (value :DataSnapshot in data.children){

                        val dia = value.child(DIA).getValue(String::class.java)?:"error"
                        val lista_presentes = value.child(PRESENTES).value
                        val presente = lista_presentes.toString().contains(uid.toString())
                        val orden = value.child("numero").getValue(Int::class.java)
                        presente.also {
                            clases.add(MiAsistencia(dia,it, orden?:0))
                            cantidad = if (it) cantidad.plus(1) else cantidad.plus(0)
                        }
                        //clases.add(MiAsistencia(dia,presente))
                    }
                    binding.detAsistencias.text = "${activity?.getString(R.string.cantidad_asistencia)} $cantidad"
                    binding.rvAsistencias.apply {
                        layoutManager = GridLayoutManager(activity,2)
                        adapter = MisAsistenciasAdapter(clases)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        })

        viewModel.datosUI.observe(viewLifecycleOwner) { result ->
            result.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(data: DataSnapshot) {
                    binding.detNombre.text = "Nombre: "+data.child(NOMBRE).value.toString()+" "+ data.child(APELLIDO).value.toString()
                    binding.detDni.text =  "DNI: "+ data.child(DNI).value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) = MisAsistenciasFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}