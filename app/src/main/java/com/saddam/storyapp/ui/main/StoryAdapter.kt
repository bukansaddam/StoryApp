package com.saddam.storyapp.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saddam.storyapp.data.response.ListStoryItem
import com.saddam.storyapp.databinding.ItemStoryBinding
import com.saddam.storyapp.ui.detail.DetailActivity

class StoryAdapter(): ListAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem){
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)
            binding.tvItemName.text = "${story.name}"
            binding.tvItemDescription.text = "${story.description}"

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_ID, story.id)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "image"),
                        Pair(binding.tvItemName, "name"),
                        Pair(binding.tvItemDescription, "description"),
                    )

                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}