package com.books.gamewepapp.screens.navigation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.books.gamewepapp.databinding.ActivityMainGameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainGameActivity : AppCompatActivity() {

    private var _binding: ActivityMainGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainGameBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }

}