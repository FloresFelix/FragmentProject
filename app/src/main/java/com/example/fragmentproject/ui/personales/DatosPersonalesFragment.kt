package com.example.fragmentproject.ui.personales

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.fragmentproject.R
import com.example.fragmentproject.databinding.FragmentDatosPersonalesBinding
import com.example.fragmentproject.extension.replaceFragment
import com.example.fragmentproject.extension.setDataSharedPreferences
import com.example.fragmentproject.injection.ViewModulFactoryModule
import com.example.fragmentproject.interactor.GoogleSheetHelper
import com.example.fragmentproject.model.AlumnoUser
import com.example.fragmentproject.model.AlumnosDatos
import com.example.fragmentproject.ui.home.HomeFragment
import com.example.fragmentproject.ui.login.LoginFragment
import com.example.fragmentproject.utils.Constants
import com.example.fragmentproject.utils.Constants.DATOS_PERSONALES
import com.example.fragmentproject.utils.Constants.LOG_IN_APP
import com.example.fragmentproject.viewmodel.AppViewModel
import com.google.firebase.auth.FirebaseAuth


class DatosPersonalesFragment : Fragment() {

    private val viewModel by activityViewModels<AppViewModel> { ViewModulFactoryModule.loginViewModelFactory}

    private var _binding: FragmentDatosPersonalesBinding? = null
    private val binding get() = _binding!!

    private lateinit var datosGuardados : AlumnoUser

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = DatosPersonalesFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
        with(binding){
            textWatcher.apply {
                datoApellido.addTextChangedListener(this)
                datoNombre.addTextChangedListener(this)
                datoDni.addTextChangedListener(this)
            }
            datosBtnAceptar.setOnClickListener {
                val alumno = AlumnoUser(datoNombre.text.toString(),datoApellido.text.toString(),datoDni.text.toString(),true)
                datosGuardados = alumno
                GoogleSheetHelper.conectionGoogleSheet(requireContext(), AlumnosDatos("nuevovalue",datoDni.text.toString(),datoApellido.text.toString(),datoNombre.text.toString(),null,null))
                viewModel.setDatosPersonales(alumno)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDatosPersonalesBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun observers() {
        viewModel.datosPersonales.observe(viewLifecycleOwner, Observer { resut ->
            resut.addOnCompleteListener {
                when (resut.isSuccessful){
                    true ->{
                        val sharedPreferences = activity?.getSharedPreferences(Constants.SHAREDPREFERENCES_NAME_APP, Context.MODE_PRIVATE)
                        sharedPreferences?.edit()?.apply {
                            putBoolean(DATOS_PERSONALES,true)
                            putBoolean(LOG_IN_APP,true)
                            setDataSharedPreferences(activity as AppCompatActivity,datosGuardados.dni)
                            apply()
                        }
                        replaceFragment(HomeFragment(),false)
                    }
                    false -> Toast.makeText(activity, "algo salio mal", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private val textWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            with(binding){
                val email = datoNombre.text
                val password = datoApellido.text
                val repPassword = datoDni.text
                val value = !email.isEmpty() && !password.isEmpty() && !repPassword.isEmpty()
                datosBtnAceptar.isEnabled = value
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }
}