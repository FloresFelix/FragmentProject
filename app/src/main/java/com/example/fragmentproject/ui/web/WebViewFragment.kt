package com.example.fragmentproject.ui.web

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.example.fragmentproject.R
import com.example.fragmentproject.databinding.FragmentWebViewBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class WebViewFragment : Fragment() {

    private lateinit var binding: FragmentWebViewBinding
    val urlWepPage = "https://virtual.unju.edu.ar/course/view.php?id=2698"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWebViewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openWeb(urlWepPage)
    }

    private fun openWeb(url: String) {
        with(binding){
            webview.apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                canGoBack()
                loadUrl(url)
                setOnKeyListener( View.OnKeyListener { _, _, keyEvent  ->
                    if (keyEvent.keyCode == KeyEvent.KEYCODE_BACK && !webview.canGoBack()) {
                        false
                    } else if (keyEvent.keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == MotionEvent.ACTION_UP) {
                        webview.goBack()
                        true
                    } else true
                })
            }
        }
    }
}