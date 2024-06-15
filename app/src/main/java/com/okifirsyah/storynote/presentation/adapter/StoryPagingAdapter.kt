package com.okifirsyah.storynote.presentation.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.okifirsyah.data.local.model.StoryModel
import com.okifirsyah.storynote.R
import com.okifirsyah.storynote.databinding.ItemStoryBinding
import com.okifirsyah.storynote.utils.extension.formatDate
import com.okifirsyah.storynote.utils.extension.getSubAdminAreaName
import com.okifirsyah.storynote.utils.extension.gone
import com.okifirsyah.storynote.utils.extension.show
import kotlinx.coroutines.runBlocking
import java.util.Date

class StoryPagingAdapter(private val activity: Activity, private val onClick: (String) -> Unit) :
    PagingDataAdapter<StoryModel, StoryPagingAdapter.StoryViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {

        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(story: StoryModel) {
            binding.apply {
                tvItemName.text = story.name
                tvUploadAt.text = Date(story.createdAt).formatDate()
                tvDescription.text = story.description
                ivItemPhoto.load(story.photoUrl) {
                    placeholder(R.drawable.placehoder)
                }

                if (story.latitude != null && story.longitude != null) {

                    runBlocking {
                        chipLoc.text = activity.getSubAdminAreaName(
                            story.latitude!!.toDouble(),
                            story.longitude!!.toDouble()
                        )
                    }

                    chipLoc.show()
                } else {
                    chipLoc.gone()
                }

                root.setOnClickListener {
                    onClick(story.id)
                }
            }
        }


    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryModel>() {
            override fun areItemsTheSame(
                oldItem: StoryModel,
                newItem: StoryModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryModel,
                newItem: StoryModel
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}