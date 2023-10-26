package com.saddam.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.saddam.storyapp.R
import com.saddam.storyapp.data.response.ListStoryItem
import com.saddam.storyapp.databinding.ActivityMainBinding
import com.saddam.storyapp.helper.Result
import com.saddam.storyapp.helper.ViewModelFactory
import com.saddam.storyapp.ui.login.LoginActivity
import com.saddam.storyapp.ui.story.AddStoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var token: String = ""

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSession()
        setupToolbar()
        setupList()
    }

    private fun getSession(){
        viewModel.getSession().observe(this){user ->
            if (user.token.isNotBlank()){
                token = user.token
                setupData(token)
            }
        }
    }

    private fun setupToolbar() {
        binding.appBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.menu_add -> {
                    val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
                    intent.putExtra(AddStoryActivity.EXTRA_TOKEN, token)
                    startActivity(intent)
                    true
                }
                R.id.menu_language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.menu_logout -> {
                    AlertDialog.Builder(this).apply {
                        setTitle(R.string.title_keluar_akun)
                        setMessage(getString(R.string.message_logout))
                        setPositiveButton(getString(R.string.yakin)){_,_ ->
                            viewModel.logout()
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                        }
                        setNegativeButton(getString(R.string.batal)){_,_ ->

                        }
                        create()
                        show()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun setupData(token: String) {
        viewModel.getAllStories(token).observe(this){ result ->
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
                        setStoryData(token, result.data.listStory)
                    }
                }
            }
        }
    }

    private fun setStoryData(token: String, data: List<ListStoryItem?>?) {
        val adapter = StoryAdapter(token)
        adapter.submitList(data)
        binding.rvStory.adapter = adapter
    }

    private fun setupList() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

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

}