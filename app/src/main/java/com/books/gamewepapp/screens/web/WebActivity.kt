package com.books.gamewepapp.screens.web

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.books.gamewepapp.databinding.ActivityWebViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebActivity : AppCompatActivity() {

    private var _binding: ActivityWebViewBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<WebViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWebViewBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        viewModel.getPinterestUrl()
        binding.webView.settings.javaScriptEnabled = true

        viewModel.urlLiveData.observe(this) {
            binding.webView.loadUrl(it)
        }
    }

}