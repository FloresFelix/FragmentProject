package com.example.fragmentproject.ui.home.misasistencias

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fragmentproject.R
import com.example.fragmentproject.databinding.FragmentMisAsistenciasBinding
import com.example.fragmentproject.injection.ViewModulFactoryModule
import com.example.fragmentproject.model.MiAsistencia
import com.example.fragmentproject.ui.home.adapters.MisAsistenciasAdapter
import com.example.fragmentproject.utils.Constants
import com.example.fragmentproject.utils.Constants.DIA
import com.example.fragmentproject.utils.Constants.PRESENTES
import com.example.fragmentproject.viewmodel.AppViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class MisAsistenciasFragment : Fragment() {

    private val viewModel by activityViewModels<AppViewModel>{ ViewModulFactoryModule.loginViewModelFactory}
    private var _binding : FragmentMisAsistenciasBinding? = null
    private val binding get() = _binding!!
    val lista = ArrayList<MiAsistencia>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMisAsistenciasBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = activity?.getSharedPreferences(Constants.SHAREDPREFERENCES_NAME_APP, Context.MODE_PRIVATE)?.getString(Constants.USER_UID, "nada")
        val clases = ArrayList<MiAsistencia>()
        viewModel.getAsistencias()
        viewModel.misAsistencias.observe(viewLifecycleOwner, Observer { result ->
            result.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(data: DataSnapshot) {
                    clases.clear()
                    for (value :DataSnapshot in data.children){
                        val dia = value.child(DIA).getValue(String::class.java)?:"error"
                        val lista_presentes = value.child(PRESENTES).value
                        val presente = lista_presentes.toString().contains(uid.toString())
                        clases.add(MiAsistencia(dia,presente))
                    }
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
        _binding = null
    }
}