package com.saddam.storyapp.ui.register

import androidx.lifecycle.ViewModel
import com.saddam.storyapp.data.Repository

class RegisterViewModel(private val repository: Repository): ViewModel() {

    fun register(name: String, email: String, password: String) = repository.register(name, email, password)
}