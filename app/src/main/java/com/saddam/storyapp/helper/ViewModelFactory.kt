package com.saddam.storyapp.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.saddam.storyapp.data.Repository
import com.saddam.storyapp.di.Injection
import com.saddam.storyapp.ui.detail.DetailViewModel
import com.saddam.storyapp.ui.login.LoginViewModel
import com.saddam.storyapp.ui.main.MainViewModel
import com.saddam.storyapp.ui.register.RegisterViewModel
import com.saddam.storyapp.ui.splash.SplashViewModel
import com.saddam.storyapp.ui.story.StoryViewModel

class ViewModelFactory private constructor(private val repository: Repository) :
ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }

        fun clearInstance(){
            Repository.clearInstance()
            instance = null
        }
    }
}