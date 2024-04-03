package com.harrrshith.signify

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.ActionBar.LayoutParams
import android.graphics.drawable.AnimatedVectorDrawable
import android.icu.text.ListFormatter.Width
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.TranslateAnimation
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.animation.AnimatableView.Listener
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
        //everytime the isClicked variable becomes false that is when I close the toolbar I need to manually make all the animations to reset to its original position.
        initialScreenSetup()
        setOnSignifyToolBarListeners()
    }
    private fun initialScreenSetup(){
        //initial screen contents are set here
        avdBtn = binding.avdButton.setAnimateResource(R.drawable.animated_vector_button_from)
        binding.avdButton.setOnClickListener {
            isClicked = !isClicked // when clicked for the first time isClicked will be true
            onAVDButtonClick(isClicked)
            avdBtn.start()
        }
    }

    private fun onAVDButtonClick(isClicked: Boolean){
        binding.avdButton.apply {
            if(isClicked){
                binding.signifyOptions.also {view->
                    view.visibility = View.VISIBLE
                    view.animate()
                        .setDuration(250)
                        .translationY(0f)
                        .alpha(1f)
                        .setListener(object : AnimatorListenerAdapter(){
                            override fun onAnimationEnd(
                                animation: Animator,
                                isReverse: Boolean
                            ) {
                                super.onAnimationEnd(animation, false)//Animation was repeating so isReverse is false
                            }
                        })

                }
                avdBtn = binding.avdButton.setAnimateResource(R.drawable.animated_vector_button_from)
            }else{
                binding.signifyOptions.also {view ->
                    view.animate()
                        .setDuration(250)
                        .translationY(40f)
                        .alpha(0.1f)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                view.visibility = View.GONE
                            }
                        })

                }
                avdBtn = binding.avdButton.setAnimateResource(R.drawable.animated_vector_button_to)
            }
        }
    }

    private fun setOnSignifyToolBarListeners(){
        binding.lineWeightToolbarBtn.setOnClickListener {
            binding.signifyToolbar.visibility = View.GONE
            binding.lineWeightToolbarWrapper.lineWeightToolbar.also {
                it.visibility = View.VISIBLE
                it.layoutParams.width = LayoutParams.MATCH_PARENT
                it.animate()
                    .setDuration(200)
                    .alpha(1f)
                    .translationX(0f)
                    .setListener(object : AnimatorListenerAdapter(){
                        override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                            super.onAnimationEnd(animation, false)
                        }
                    })
            }
            onLineHeightOptionsClicked()
        }
    }

    private fun onLineHeightOptionsClicked(){
        binding.lineWeightToolbarWrapper.oneX.setOnClickListener {
            binding.lineWeightToolbarWrapper.lineWeightToolbar.visibility = View.GONE
            binding.signifyToolbar.visibility = View.VISIBLE
        }
    }

    private fun ImageButton.setAnimateResource(drawableId: Int): AnimatedVectorDrawable{
        this.apply {
            setBackgroundResource(drawableId)
            return background as AnimatedVectorDrawable
        }
    }
    private fun Int.thisToFloat(): Float {
        val scale = resources.displayMetrics.density
        return this * scale
    }
}
