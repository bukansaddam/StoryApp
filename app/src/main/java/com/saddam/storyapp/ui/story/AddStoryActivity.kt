package com.saddam.storyapp.ui.story

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.saddam.storyapp.databinding.ActivityAddStoryBinding
import com.saddam.storyapp.helper.Result
import com.saddam.storyapp.helper.ViewModelFactory
import com.saddam.storyapp.ui.main.MainActivity
import com.saddam.storyapp.utils.getImageUri
import com.saddam.storyapp.utils.reduceFileImage
import com.saddam.storyapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private val viewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var currentImageUri: Uri? = null

    private var token: String = ""

    companion object{
        const val EXTRA_TOKEN = "extra_token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = intent.getStringExtra(EXTRA_TOKEN).toString()

        binding.btnGalleryFile.setOnClickListener { startGallery() }
        binding.btnGalleryCamera.setOnClickListener { startCamera() }
        binding.btnGallerySend.setOnClickListener { uploadStory() }
    }

    private fun uploadStory() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.etGalleryDescription.text.toString()
            showLoading(true)

            val requestBodyDescription = description.toRequestBody("text/plain".toMediaType())
            val requestBodyImage = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestBodyImage
            )

            viewModel.postStory(token, multipartBody, requestBodyDescription).observe(this){ result ->
                when(result){
                    is Result.Loading -> {
                        binding.progressIndicator.visibility = View.VISIBLE
                        true
                    }
                    is Result.Error -> {
                        binding.progressIndicator.visibility = View.GONE
                        showToast(result.error)
                        true
                    }
                    is Result.Success -> {
                        binding.progressIndicator.visibility = View.GONE
                        showToast("Berhasil diupload")
                        startActivity(Intent(this@AddStoryActivity, MainActivity::class.java))
                        finish()
                        true
                    }
                    else -> false
                }
            }

        } ?: showToast("Gambar tidak ditemukan")
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(teks: String) {
        Toast.makeText(this, teks, Toast.LENGTH_SHORT).show()
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ){isSuccess ->
        if (isSuccess){
            showImage()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){uri: Uri? ->
        if (uri != null){
            currentImageUri = uri
            showImage()
        }else{
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivGalleryPreview.setImageURI(it)
        }
    }
}