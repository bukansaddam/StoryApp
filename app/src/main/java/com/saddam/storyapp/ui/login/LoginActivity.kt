package com.saddam.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.isVisible
import com.saddam.storyapp.R
import com.saddam.storyapp.data.pref.UserModel
import com.saddam.storyapp.databinding.ActivityLoginBinding
import com.saddam.storyapp.helper.Result
import com.saddam.storyapp.helper.ViewModelFactory
import com.saddam.storyapp.ui.main.MainActivity
import com.saddam.storyapp.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(1000)
        val subTitle = ObjectAnimator.ofFloat(binding.tvSubtitle, View.ALPHA, 1f).setDuration(500)
        val dirRegister = ObjectAnimator.ofFloat(binding.tvDirRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, subTitle, dirRegister)
            start()
        }
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()
            loginUser(email, password)

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, getString(R.string.validasi_email), Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        binding.tvRegister.setOnClickListener {
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    Pair(binding.edLoginName, "name"),
                    Pair(binding.edLoginEmail, "email"),
                    Pair(binding.edLoginPassword, "password"),
                )

            startActivity(
                Intent(this@LoginActivity, RegisterActivity::class.java),
                optionsCompat.toBundle()
            )
        }
    }

    private fun loginUser(email: String, password: String) {
        viewModel.login(email, password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is Result.Error -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }

                    is Result.Success -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                        val token = result.data.loginResult?.token.toString()
                        val user = UserModel(email, token)
                        viewModel.saveSession(user)
                        Log.i(TAG, "loginUser: ${user.token}")
                        ViewModelFactory.clearInstance()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.title_keluar_aplikasi))
            setMessage(getString(R.string.message_keluar_aplikasi))
            setPositiveButton(getString(R.string.yakin)){ _, _ ->
                super.onBackPressed()
            }
            setNegativeButton(getString(R.string.batal)) { _, _ ->

            }
            create()
            show()
        }
    }

    companion object{
        const val TAG = "Login Activity"
    }
}