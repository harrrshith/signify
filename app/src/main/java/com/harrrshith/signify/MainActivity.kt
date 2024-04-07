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
import androidx.core.content.ContextCompat
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
                                initialAnimPosition()//this will reset the nested toolbar to their initial position
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
            binding.lineWeightToolbarWrapper.lineWeightToolbar.apply {
                visibility = View.VISIBLE
                layoutParams.width = LayoutParams.MATCH_PARENT
                animate()
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

        binding.colorSelectorToolbarBtn.setOnClickListener {
            binding.signifyToolbar.visibility = View.GONE
            binding.colorSelectorToolbarWrapper.colorSelectorToolbar .apply {
                visibility = View.VISIBLE
                layoutParams.width = LayoutParams.MATCH_PARENT
                animate()
                    .setDuration(200)
                    .alpha(1f)
                    .translationX(0f)
                    .setListener(object : AnimatorListenerAdapter(){
                        override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                            super.onAnimationEnd(animation, false)
                        }
                    })
            }
            onColorSelectorClicked()
        }
    }

    private fun initialAnimPosition(){
        binding.lineWeightToolbarWrapper.lineWeightToolbar.apply {
            visibility = View.GONE
            translationX = (-80).thisToFloat()
        }
        binding.colorSelectorToolbarWrapper.colorSelectorToolbar.apply {
            visibility = View.GONE
            translationX = (-80).thisToFloat()
        }
        binding.signifyToolbar.visibility = View.VISIBLE
    }

    private fun onLineHeightOptionsClicked(){
        val signatureView = binding.signatureView
        binding.lineWeightToolbarWrapper.apply {
            oneX.setOnClickListener{
                signatureView.settingBrushSize(1f)
            }
            twoX.setOnClickListener{
                signatureView.settingBrushSize(2f)
            }
            fiveX.setOnClickListener{
                signatureView.settingBrushSize(5f)
            }
            sevenX.setOnClickListener {
                signatureView.settingBrushSize(7f)
            }
        }
    }

    private fun onColorSelectorClicked(){
        val signatureView = binding.signatureView
        val context = this
        binding.colorSelectorToolbarWrapper.apply {
            firstColor.setOnClickListener{
                signatureView.settingBrushColor(ContextCompat.getColor(context, R.color.black2))
            }
            secondColor.setOnClickListener{
                signatureView.settingBrushColor(ContextCompat.getColor(context, R.color.red))
            }
            thirdColor.setOnClickListener{
                signatureView.settingBrushColor(ContextCompat.getColor(context, R.color.green))
            }
            fourthColor.setOnClickListener{
                signatureView.settingBrushColor(ContextCompat.getColor(context, R.color.blue))
            }
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

    private fun SignatureView.settingBrushSize(brushSize: Float){
        isClicked = !isClicked
        this.setBrushSize(brushSize)
        onAVDButtonClick(isClicked)
    }

    private fun SignatureView.settingBrushColor(color: Int){
        isClicked = !isClicked
        this.setBrushColor(color)
        binding.colorSelectorToolbarBtn.setCardBackgroundColor(color)
        onAVDButtonClick(isClicked)
    }

}
