package com.books.gamewepapp.screens.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.books.gamewepapp.databinding.ActivitySplashBinding
import com.books.gamewepapp.screens.navigation.MainGameActivity
import com.books.gamewepapp.screens.web.WebActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()

        lifecycleScope.launch {
            delay(2000L)
            startActivity(Intent(this@SplashScreenActivity, MainGameActivity::class.java))
        }

    }
}