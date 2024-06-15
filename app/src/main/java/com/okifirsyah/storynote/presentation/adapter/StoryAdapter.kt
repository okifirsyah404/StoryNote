package com.okifirsyah.storynote.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.okifirsyah.data.dto.StoryDto
import com.okifirsyah.storynote.R
import com.okifirsyah.storynote.databinding.ItemStoryBinding
import com.okifirsyah.storynote.utils.extension.formatDate

class StoryAdapter(private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    private var _items: ArrayList<StoryDto> = ArrayList()


    fun setItems(data: ArrayList<StoryDto>) {
        _items.clear()
        _items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = _items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = _items[position]
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StoryDto) {

            binding.apply {
                tvItemName.text = item.name
                tvUploadAt.text = item.createdAt.formatDate()
                tvDescription.text = item.description
                ivItemPhoto.load(item.photoUrl) {
                    placeholder(R.drawable.placehoder)
                }

                root.setOnClickListener {
                    onClick(item.id)
                }
            }

        }
    }
}