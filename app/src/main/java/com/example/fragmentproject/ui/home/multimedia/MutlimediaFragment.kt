package com.example.fragmentproject.ui.home.multimedia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fragmentproject.R
import com.example.fragmentproject.databinding.FragmentMutlimediaBinding
import com.example.fragmentproject.injection.ViewModulFactoryModule
import com.example.fragmentproject.model.ItemVideo
import com.example.fragmentproject.ui.home.adapters.VideoMultimediaAdapter
import com.example.fragmentproject.ui.home.multimedia.data.VideoClickListener
import com.example.fragmentproject.viewmodel.AppViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController

class MutlimediaFragment : Fragment(),VideoClickListener {

    private val viewModel by activityViewModels<AppViewModel> {ViewModulFactoryModule.loginViewModelFactory }

    private lateinit var binding: FragmentMutlimediaBinding
    private lateinit var videoPlayer: YouTubePlayerView
    private lateinit var player : YouTubePlayer

    private lateinit var clickListener : VideoClickListener

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = MutlimediaFragment().apply {
            arguments = Bundle().apply {
                //putString(ARG_PARAM1, param1)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMutlimediaBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListener = this
        viewModel.getListaVideos()
        observers()

    }

    private fun observers() {
        val listaVideo =  ArrayList<ItemVideo>()
        viewModel.listaVideos.observe(viewLifecycleOwner) { result ->
            result.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(data: DataSnapshot) {
                    listaVideo.clear()
                    for (value: DataSnapshot in data.children){
                        val video = ItemVideo(
                            value.child("fecha").value.toString(),
                            value.child("idVideo").value.toString()
                        )
                        listaVideo.add(video)
                    }
                    val videosAdapeter = VideoMultimediaAdapter(listaVideo, clickListener)
                    binding.multimediaRV.apply {
                        layoutManager = GridLayoutManager(activity, 2)
                        adapter = videosAdapeter
                    }
                    intVideoPlayer(listaVideo.first().idVideo)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun intVideoPlayer(idVideo : String) {
        videoPlayer = binding.videPlayer
        videoPlayer.enableAutomaticInitialization = false
        lifecycle.addObserver(videoPlayer)
        val listener : YouTubePlayerListener = object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                val defaultPlayerUiController = DefaultPlayerUiController(videoPlayer,youTubePlayer)
                defaultPlayerUiController.showFullscreenButton(false)
                videoPlayer.setCustomPlayerUi(defaultPlayerUiController.rootView)
                player = youTubePlayer
                player.cueVideo(idVideo,0f)

            }
        }

        val option: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).build()
        videoPlayer.initialize(listener,option)
        videoPlayer.removeYouTubePlayerListener(listener)

    }

    override fun onVideoClick(video_id: String?) {
        player.cueVideo(video_id.toString(),0f)
    }
}