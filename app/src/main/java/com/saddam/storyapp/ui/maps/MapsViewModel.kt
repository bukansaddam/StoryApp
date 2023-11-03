package com.saddam.storyapp.ui.maps

import androidx.lifecycle.ViewModel
import com.saddam.storyapp.data.Repository

class MapsViewModel(private val repository: Repository): ViewModel() {

    fun getStoryLocation() = repository.getStoryLocation()

}