package com.widoo.teacherreports.ui.register

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.widoo.teacherreports.databinding.FragmentRegisterBinding
import com.widoo.teacherreports.extension.replaceFragment
import com.widoo.teacherreports.injection.ViewModulFactoryModule
import com.widoo.teacherreports.ui.personales.DatosPersonalesFragment
import com.widoo.teacherreports.utils.Constants
import com.widoo.teacherreports.viewmodel.AppViewModel


class RegisterFragment : Fragment() {

    private val viewModel by activityViewModels<AppViewModel> { ViewModulFactoryModule.loginViewModelFactory }

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    companion object {
        @JvmStatic
        fun newInstance(param1: String) = RegisterFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObserver()

        with(binding) {
            textWatcher.apply {
                registerEmail.addTextChangedListener(this)
                registerPassword.addTextChangedListener(this)
                registerRepPassword.addTextChangedListener(this)
            }

            registerBtnAceptar.setOnClickListener {

                if (registerEmail.text.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(registerEmail.text).matches()) {
                    if (registerPassword.text.toString().equals(registerRepPassword.text.toString())) {
                        //binding.loadingRegister.root.visibility = View.VISIBLE
                        viewModel.setNewUser(registerEmail.text.toString(), registerPassword.text.toString())
                    } else {
                        Toast.makeText(
                            activity,
                            "las contraseñas deben ser iguales",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }else{
                    registerEmail.requestFocus()
                    registerEmail.setError("formato de emial invalido")
                }
            }
        }
    }

    private fun addObserver() {
        viewModel.newUser.observe(viewLifecycleOwner, Observer { resp ->

            resp.addOnCompleteListener {
                if (resp.isSuccessful) {
                    //binding.loadingRegister.root.visibility = View.GONE
                    val uid = resp.result.user?.uid
                    val sharedPreferences = activity?.getSharedPreferences(Constants.SHAREDPREFERENCES_NAME_APP, Context.MODE_PRIVATE)
                    sharedPreferences?.edit()?.apply {
                        putString(Constants.USER_UID,uid)
                        apply()
                    }
                    goToDatosPerosonales(uid?:"vacio")
                } else {
                    Toast.makeText(
                        context,
                        "La contraseña debe tener almenos 6 caracteres",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })

    }

    private fun goToDatosPerosonales(uid: String) {
        replaceFragment(DatosPersonalesFragment(), false)
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            with(binding) {
                val email = registerEmail.text
                val password = registerPassword.text
                val repPassword = registerRepPassword.text
                val value = !email.isEmpty() && !password.isEmpty() && !repPassword.isEmpty()
                registerBtnAceptar.isEnabled = value
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    }
}