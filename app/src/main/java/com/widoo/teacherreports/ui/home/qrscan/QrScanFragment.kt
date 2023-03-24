package com.widoo.teacherreports.ui.home.qrscan

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.widoo.teacherreports.databinding.FragmentQrScanBinding
import com.widoo.teacherreports.extension.getDataSharedPreferences
import com.widoo.teacherreports.extension.replaceFragment
import com.widoo.teacherreports.injection.ViewModulFactoryModule
import com.widoo.teacherreports.interactor.GoogleSheetHelper
import com.widoo.teacherreports.model.AlumnosDatos
import com.widoo.teacherreports.ui.home.HomeFragment
import com.widoo.teacherreports.utils.Constants
import com.widoo.teacherreports.utils.Constants.FECHA
import com.widoo.teacherreports.utils.Constants.USER_UID
import com.widoo.teacherreports.viewmodel.AppViewModel


class QrScanFragment : Fragment() {

    private val viewModel by activityViewModels<AppViewModel> { ViewModulFactoryModule.loginViewModelFactory }

    private var _binding: FragmentQrScanBinding? = null
    private val binding get() = _binding!!
    private var user_id: String = ""
    private var token_qr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrScanBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val sharedPreferences = activity?.getSharedPreferences(
            Constants.SHAREDPREFERENCES_NAME_APP,
            Context.MODE_PRIVATE
        )
        user_id = sharedPreferences?.getString(USER_UID, "") ?: ""
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {
            scanQrIniciar.setOnClickListener {
                val scan = ScanOptions()
                scan.setPrompt("").setBeepEnabled(true).setOrientationLocked(true)
                barcodeLauncher.launch(scan)
            }
        }
        //viewModel.isConnected()
        viewModel.getTokenQR()
        addObservers()
    }

    private fun addObservers() {
        /*
        viewModel.setAsistencia.observe(viewLifecycleOwner, Observer { res ->
            res.addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(activity, "Asistencia Registrada Con Exito", Toast.LENGTH_LONG).show()
                    replaceFragment(HomeFragment(),false)
                    viewModel.setAsistencia.removeObserver{viewLifecycleOwner}
                }
            }
        })

         */



        viewModel.setAsistencia.observe(viewLifecycleOwner,object: Observer<Boolean>{
            override fun onChanged(t: Boolean) {
                println("viemodel es: "+t.toString())
                Toast.makeText(activity, "Asistencia Registrada Con Exito", Toast.LENGTH_LONG).show()
                replaceFragment(HomeFragment(),false)
                requireActivity().viewModelStore.clear()

            }
        })

        viewModel.tokenQR.observe(viewLifecycleOwner, Observer { res ->

            res.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(data: DataSnapshot) {
                    token_qr = data.value.toString()
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        })

        viewModel.isconnected.observe(viewLifecycleOwner, Observer { res ->

            res.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(data: DataSnapshot) {
                    val value = data.getValue(Boolean::class.java)!!
                    if (value) {
                        println("internet ON")
                    } else {
                        println("internet OFF")
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            QrScanFragment().apply {
                arguments = Bundle().apply {
                    putString("value", param1)
                }
            }
    }

    private val barcodeLauncher =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->

           when (result.contents){
               null -> Toast.makeText(activity, "Scan Cancelado", Toast.LENGTH_SHORT).show()
               else -> {
                   when(result.contents.equals(token_qr)){
                       true -> {
                           val uri = Uri.parse(result.contents.toString())
                           val fecha = uri.getQueryParameter(FECHA).toString()
                           val userDni = getDataSharedPreferences(activity as AppCompatActivity)?:"0"
                           GoogleSheetHelper.conectionGoogleSheet(requireActivity(), AlumnosDatos("update",userDni,null,null,fecha,null))
                           viewModel.setAsistencia(user_id, fecha)
                       }
                       else -> Toast.makeText(activity, "Codigo QR Invaliio", Toast.LENGTH_LONG).show()
                   }
               }
           }
        }


    override fun onPause() {
        viewModel.setAsistencia.removeObserver { viewLifecycleOwner}
        super.onPause()
    }

    override fun onStop() {
        viewModel.setAsistencia.removeObserver { viewLifecycleOwner}
        super.onStop()
    }
}