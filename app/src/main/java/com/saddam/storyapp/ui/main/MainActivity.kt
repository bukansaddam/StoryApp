package com.saddam.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.saddam.storyapp.R
import com.saddam.storyapp.data.response.ListStoryItem
import com.saddam.storyapp.databinding.ActivityMainBinding
import com.saddam.storyapp.helper.Result
import com.saddam.storyapp.helper.ViewModelFactory
import com.saddam.storyapp.ui.detail.DetailActivity
import com.saddam.storyapp.ui.login.LoginActivity
import com.saddam.storyapp.ui.story.AddStoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var token: String = ""
    private val adapter = StoryAdapter()

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()
        setupToolbar()
        setupList()
    }

    private fun setupToolbar() {
        binding.appBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.menu_add -> {
                    startActivity(Intent(this, AddStoryActivity::class.java))
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

    private fun showSelectedUser(data: ListStoryItem, item: StoryAdapter.MyViewHolder) {

            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID, data.id)

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    Pair(item.binding.ivItemPhoto, "image"),
                    Pair(item.binding.tvItemName, "name"),
                    Pair(item.binding.tvItemDescription, "description"),
                )

            startActivity(intent, optionsCompat.toBundle())

    }

    private fun setStoryData(data: List<ListStoryItem?>?) {
        adapter.submitList(data)
        binding.rvStory.adapter = adapter

        adapter.setOnClickCallback(object : StoryAdapter.OnItemClickCallback{
            override fun onItemClicked(
                data: ListStoryItem,
                item: StoryAdapter.MyViewHolder
            ) {
                showSelectedUser(data, item)
            }
        })
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