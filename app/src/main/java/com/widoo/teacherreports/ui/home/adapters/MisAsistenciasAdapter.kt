package com.widoo.teacherreports.ui.home.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.widoo.teacherreports.R
import com.widoo.teacherreports.model.MiAsistencia

class MisAsistenciasAdapter (private val mList: List<MiAsistencia>) : RecyclerView.Adapter<MisAsistenciasAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clase_asistencia, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listaOrdenada = mList.sortedBy {
            it.numero
        }
        val item = listaOrdenada[position]
        holder.tv_fecha_asistencia.isSingleLine = false
        holder.tv_fecha_asistencia.text = HtmlCompat.fromHtml(item.dia,HtmlCompat.FROM_HTML_MODE_LEGACY)
        when(item.presente){
            true -> {
                with(holder){
                    tv_estado_asistencia.text = "presente"
                    img_sistencia.setImageResource(R.drawable.ic_success)
                    card.setCardBackgroundColor(Color.parseColor("#FFBB86FC"))
                }
            }
            else -> {}
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val tv_fecha_asistencia = itemView.findViewById<TextView>(R.id.asistencia_fecha)
        val img_sistencia = itemView.findViewById<ImageView>(R.id.img_succes)
        val tv_estado_asistencia = itemView.findViewById<TextView>(R.id.asistencia_estado)
        val card = itemView.findViewById<CardView>(R.id.item_card)

    }

}