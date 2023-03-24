package com.example.fragmentproject.ui.home.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fragmentproject.R
import com.example.fragmentproject.model.ItemVideo
import com.example.fragmentproject.ui.home.multimedia.data.VideoClickListener

public class VideoMultimediaAdapter(val videoList: List<ItemVideo>, private val videoClickListener: VideoClickListener) : RecyclerView.Adapter<VideoMultimediaAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoMultimediaAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_multimedia, parent, false)
        return VideoMultimediaAdapter.ViewHolder(view)

    }
    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: VideoMultimediaAdapter.ViewHolder, position: Int) {
        val item = videoList[position]

        holder.tv_fecha.text = item.fecha
        holder.img_multimedia.setOnClickListener {
            videoClickListener.onVideoClick(item.idVideo)
        }
        println(item.idVideo)

    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val tv_fecha : TextView = itemView.findViewById(R.id.multimedia_fecha)
        val img_multimedia : ImageView = itemView.findViewById(R.id.img_video_multimedia)

    }
}