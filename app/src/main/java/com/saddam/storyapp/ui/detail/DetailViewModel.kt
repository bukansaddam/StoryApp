package com.saddam.storyapp.ui.detail

import androidx.lifecycle.ViewModel
import com.saddam.storyapp.data.Repository

class DetailViewModel(private val repository: Repository): ViewModel() {

    fun getUser(id: String) = repository.getDetail(id)
}