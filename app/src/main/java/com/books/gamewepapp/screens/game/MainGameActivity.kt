package com.books.gamewepapp.screens.game

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.books.gamewepapp.databinding.ActivityMainGameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.M)
class MainGameActivity : AppCompatActivity() {

    private var _binding: ActivityMainGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainGameBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }

    override fun onStop() {
        super.onStop()
        binding.cannonCustomView.stopGame()
    }
}