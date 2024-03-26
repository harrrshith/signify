package com.harrrshith.signify

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.harrrshith.signify.databinding.ActivityMainBinding
import com.harrrshith.signify.util.seNavigationBarColor
import com.harrrshith.signify.util.setStatusBarColor

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var avdBtn: AnimatedVectorDrawable
    private var isClicked: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.apply {
            statusBarColor = this@MainActivity.setStatusBarColor(R.color.primary)
            navigationBarColor = this@MainActivity.seNavigationBarColor(R.color.primary)
        }
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setView()
    }

    private fun setView(){
        avdBtn = binding.avdButton.setAnimateResource(R.drawable.animated_vector_button_from)
        binding.avdButton.setOnClickListener {
            isClicked = !isClicked
            binding.avdButton.apply {
                avdBtn = if(isClicked){
                    binding.avdButton.setAnimateResource(R.drawable.animated_vector_button_from)
                }else{
                    binding.avdButton.setAnimateResource(R.drawable.animated_vector_button_to)
                }
            }
            avdBtn.start()
        }
    }

    private fun ImageButton.setAnimateResource(drawableId: Int): AnimatedVectorDrawable{
        this.apply {
            setBackgroundResource(drawableId)
            return background as AnimatedVectorDrawable
        }
    }


}