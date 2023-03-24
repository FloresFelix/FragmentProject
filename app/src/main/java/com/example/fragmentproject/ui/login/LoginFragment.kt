package com.example.fragmentproject.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fragmentproject.R
import com.example.fragmentproject.databinding.FragmentLoginBinding
import com.example.fragmentproject.extension.replaceFragment
import com.example.fragmentproject.extension.setDataSharedPreferences
import com.example.fragmentproject.injection.ViewModulFactoryModule
import com.example.fragmentproject.model.AlumnoUser
import com.example.fragmentproject.ui.home.HomeFragment
import com.example.fragmentproject.ui.personales.DatosPersonalesFragment
import com.example.fragmentproject.ui.register.RegisterFragment
import com.example.fragmentproject.utils.Constants
import com.example.fragmentproject.utils.Constants.DATA_ENTRY
import com.example.fragmentproject.utils.Constants.DATOS_PERSONALES
import com.example.fragmentproject.utils.Constants.DNI
import com.example.fragmentproject.utils.Constants.LOG_IN_APP
import com.example.fragmentproject.utils.Constants.USER_UID
import com.example.fragmentproject.viewmodel.AppViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class LoginFragment : Fragment() {


    private val viewModel by viewModels<AppViewModel> { ViewModulFactoryModule.loginViewModelFactory }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    companion object {
        @JvmStatic
        fun newInstance(param1: String) = LoginFragment().apply {
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addObservers()
        resultGoogleLogin()
        addListeners()
    }

    private fun addListeners() {
        with(binding) {
            loginBtnAceptar.setOnClickListener {
                viewModel.setLogInUser(correo.text.toString(), password.text.toString())
            }

            tvNuevo.setOnClickListener {
                replaceFragment(RegisterFragment(), true)
            }

            btnGoogle.setOnClickListener {
                val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                val googleClient = view?.let { it1 -> GoogleSignIn.getClient(it1.context, googleConf) }
                startForResult.launch(googleClient?.signInIntent)
            }
        }
    }

    private fun addObservers() {
        viewModel.setData.observe(viewLifecycleOwner, Observer { res ->

            if (res) {
                Toast.makeText(activity, "exito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "algo salio mal", Toast.LENGTH_SHORT).show()

            }
        })

        viewModel.login.observe(viewLifecycleOwner, Observer { res ->
            res.addOnCompleteListener {
                if (it.isSuccessful) {
                    val sharedPreferences = activity?.getSharedPreferences(Constants.SHAREDPREFERENCES_NAME_APP, Context.MODE_PRIVATE)
                    sharedPreferences?.edit()?.apply {
                        putBoolean(LOG_IN_APP,true)
                        putString(USER_UID, it.result.user?.uid)
                       // putBoolean(LOG_IN_APP,true)
                        apply()
                    }
                    viewModel.tieneDatos(it.result.user?.uid.toString())
                } else {
                    Toast.makeText(activity, "Datos Ingresados Incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.isRegister.observe(viewLifecycleOwner, Observer { res ->

            res.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(data: DataSnapshot) {
                    val isDataEntry = data.child(DATA_ENTRY).getValue(Boolean::class.java)?:false
                    val dni = data.child(DNI).value
                    if(isDataEntry){
                        val sharedPreferences = activity?.getSharedPreferences(Constants.SHAREDPREFERENCES_NAME_APP, Context.MODE_PRIVATE)
                        sharedPreferences?.edit()?.apply {
                            putBoolean(LOG_IN_APP,true)
                            putBoolean(DATOS_PERSONALES,true)
                            setDataSharedPreferences(activity as AppCompatActivity,dni.toString())
                            apply()
                        }
                        replaceFragment(HomeFragment(),false)

                    }else{
                        replaceFragment(DatosPersonalesFragment(),false)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

        })
    }

    private fun resultGoogleLogin() {
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
                    val account = task.getResult(ApiException::class.java)
                    try {
                        if (account != null) {
                            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                            FirebaseAuth.getInstance().signInWithCredential(credential)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Toast.makeText(activity, "algo", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(activity, "no algo", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                        }
                    } catch (e: ApiException) {
                        println("error")
                    }
                }
            }
    }

    private fun goToHome() {
        val fragment = activity?.supportFragmentManager?.beginTransaction()
        fragment?.apply {
            this.replace(R.id.fragment_host, HomeFragment())
            this.commit()
        }
    }
}