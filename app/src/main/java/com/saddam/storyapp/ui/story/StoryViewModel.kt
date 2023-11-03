package com.saddam.storyapp.ui.story

import androidx.lifecycle.ViewModel
import com.saddam.storyapp.data.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val repository: Repository): ViewModel() {

    fun postStory(
        file: MultipartBody.Part,
        description: RequestBody,
        latitude: RequestBody,
        longitude: RequestBody) = repository.sendStory(file, description, latitude, longitude)

}