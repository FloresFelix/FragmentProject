package com.example.fragmentproject.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.fragmentproject.R
import com.example.fragmentproject.databinding.FragmentHomeBinding
import com.example.fragmentproject.databinding.FragmentMisAsistenciasBinding
import com.example.fragmentproject.extension.openActivity
import com.example.fragmentproject.extension.replaceFragment
import com.example.fragmentproject.injection.ViewModulFactoryModule
import com.example.fragmentproject.ui.home.estadisticas.ClasesFragment
import com.example.fragmentproject.ui.home.misasistencias.MisAsistenciasFragment
import com.example.fragmentproject.ui.home.multimedia.MutlimediaFragment
import com.example.fragmentproject.ui.home.qrscan.QrScanFragment
import com.example.fragmentproject.ui.web.WebViewFragment
import com.example.fragmentproject.utils.Constants
import com.example.fragmentproject.utils.Constants.LOG_IN_APP
import com.example.fragmentproject.utils.Constants.NOMBRE
import com.example.fragmentproject.utils.Constants.USER_UID
import com.example.fragmentproject.viewmodel.AppViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private val viewModel by activityViewModels<AppViewModel> { ViewModulFactoryModule.loginViewModelFactory }

    private lateinit var data: String
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(param1: String) = HomeFragment().apply {
            arguments = Bundle().apply {
                putString("value", param1)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getString("value") ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = activity?.getSharedPreferences(Constants.SHAREDPREFERENCES_NAME_APP, Context.MODE_PRIVATE)?.getString(USER_UID, "nada")
        //binding.loading.root.visibility = View.VISIBLE
        viewModel.getDataUI(uid ?: "nada")
        setObserves()
        binding.design.setOnClickListener {
            viewModel.logOut()
        }

        with(binding){
            panel.imgQr.setOnClickListener {
                replaceFragment(QrScanFragment(),true)
            }

            panel.imgAsitencias.setOnClickListener {
                replaceFragment(MisAsistenciasFragment(),true)
            }

            panel.imgNoticias.setOnClickListener{
                replaceFragment(WebViewFragment(),true)
            }

            panel.imgMultimedia.setOnClickListener{
                replaceFragment(MutlimediaFragment(),true)
            }

            panel.imgEstadisticas.setOnClickListener{
                replaceFragment(ClasesFragment(),true)
            }
        }

    }

    private fun setObserves() {
        viewModel.datosUI.observe(viewLifecycleOwner, Observer { res ->
            res.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(data: DataSnapshot) {
                    binding.homeNombre.text = "${activity?.getString(R.string.hola)}${data.child(NOMBRE).value.toString()}${getString(R.string.emoji)}"
                    //binding.loading.root.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        })

        viewModel.logOut.observe(viewLifecycleOwner, Observer {
            val sharedPreferences = activity?.getSharedPreferences(Constants.SHAREDPREFERENCES_NAME_APP, Context.MODE_PRIVATE)
            sharedPreferences?.edit()?.apply {
                putString(USER_UID,"desconectado")
                putBoolean(LOG_IN_APP,false)
                apply()
            }
            (activity as AppCompatActivity).openActivity()
        })
    }
}
