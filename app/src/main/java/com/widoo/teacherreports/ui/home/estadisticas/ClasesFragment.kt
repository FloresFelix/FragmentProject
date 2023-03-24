package com.widoo.teacherreports.ui.home.estadisticas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.widoo.teacherreports.databinding.FragmentClasesBinding
import com.widoo.teacherreports.injection.ViewModulFactoryModule
import com.widoo.teacherreports.model.NuevaClase
import com.widoo.teacherreports.viewmodel.AppViewModel


class ClasesFragment : Fragment() {

    private lateinit var bindng: FragmentClasesBinding

    private val viewModel by activityViewModels<AppViewModel> { ViewModulFactoryModule.loginViewModelFactory}


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = ClasesFragment().apply {
            arguments = Bundle().apply {
                //putString(ARG_PARAM1, param1)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindng = FragmentClasesBinding.inflate(layoutInflater)
        return bindng.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(bindng){


            claseAgregar.setOnClickListener {

                val fecha = claseFecha.text.toString()
                val dia = claseDia.text.toString()
                val orden = claseOrden.text.toString()
                println("el orden es: "+orden)

                val nuevo = NuevaClase(dia,orden.toInt())
                //val value : HashMap<String,Any> = HashMap()
                //value.put("dia",dia)
                //value.put("numero",orden.toInt())

                viewModel.setNuevaClase(fecha.toString(), nuevo)
            }
        }
    }
}