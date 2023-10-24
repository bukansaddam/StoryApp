package com.saddam.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.saddam.storyapp.R
import com.saddam.storyapp.data.response.ListStoryItem
import com.saddam.storyapp.databinding.ActivityMainBinding
import com.saddam.storyapp.helper.Result
import com.saddam.storyapp.helper.ViewModelFactory
import com.saddam.storyapp.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = StoryAdapter()

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupList()
        setupData()
        setupAction()
    }

    private fun setupToolbar() {
        binding.appBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.menu_add -> {
                    true
                }
                R.id.menu_language -> {
                    true
                }
                R.id.menu_logout -> {
                    viewModel.logout()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupAction() {
        adapter.setOnClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                showSelectedStory(data)
            }
        })
    }

    private fun showSelectedStory(data: ListStoryItem) {
        Toast.makeText(this, data.name, Toast.LENGTH_SHORT).show()
    }


    private fun setupData() {
        viewModel.getAllStories().observe(this){ result ->
            if (result != null){
                when(result){
                    is Result.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is Result.Error -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        binding.progressBar.isVisible = false
                        setStoryData(result.data.listStory)
                    }
                }
            }
        }
    }

    private fun setStoryData(data: List<ListStoryItem?>?) {
        adapter.submitList(data)
        binding.rvStory.adapter = adapter
    }

    private fun setupList() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

    }
}