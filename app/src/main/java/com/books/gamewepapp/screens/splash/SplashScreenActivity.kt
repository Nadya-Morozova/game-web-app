package com.books.gamewepapp.screens.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.books.gamewepapp.databinding.ActivitySplashBinding
import com.books.gamewepapp.screens.game.MainGameActivity
import com.books.gamewepapp.screens.web.WebActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()

        viewModel.getValueVisibility()

        viewModel.isWebViewVisibleOnScreen.observe(this) {
            if (it == false) {
                startActivity(Intent(this@SplashScreenActivity, MainGameActivity::class.java))
            } else {
                startActivity(Intent(this@SplashScreenActivity, WebActivity::class.java))
            }
            finish()
        }
    }
}