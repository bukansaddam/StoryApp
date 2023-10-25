package com.saddam.storyapp.ui.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.saddam.storyapp.R
import com.saddam.storyapp.helper.ViewModelFactory
import com.saddam.storyapp.ui.login.LoginActivity

class SplashScreen : AppCompatActivity() {

    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        setupView()

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
            finish()
        }, 3000)

//        Handler(Looper.getMainLooper()).postDelayed({
//            viewModel.getUser().observe(this){ user ->
//                if (!user.isLogin){
//                    startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
//                    finish()
//                }else{
//                    startActivity(Intent(this@SplashScreen, MainActivity::class.java))
//                    finish()
//                }
//            }
//        }, 3000)
    }
}