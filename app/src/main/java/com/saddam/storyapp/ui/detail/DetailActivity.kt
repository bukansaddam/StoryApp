package com.saddam.storyapp.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.saddam.storyapp.R
import com.saddam.storyapp.data.response.Story
import com.saddam.storyapp.databinding.ActivityDetailBinding
import com.saddam.storyapp.helper.Result
import com.saddam.storyapp.helper.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    companion object{
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID)

        setupViewModel(id.toString())
    }

    private fun setupViewModel(id: String) {
        viewModel.getUser(id).observe(this){result ->
            when(result){
                is Result.Loading -> {
                    binding.progressBar.isVisible = true
                    true
                }
                is Result.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(this, getString(R.string.error_data), Toast.LENGTH_SHORT).show()
                    true
                }
                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(this, result.data.story?.name, Toast.LENGTH_SHORT).show()
                    setupData(result.data.story)
                }
                else -> false
            }
        }
    }

    private fun setupData(data: Story?) {
        Glide.with(this)
            .load(data?.photoUrl)
            .into(binding.ivDetailPhoto)
        binding.tvDetailName.text = data?.name
        binding.tvDetailDescription.text = data?.description
    }
}