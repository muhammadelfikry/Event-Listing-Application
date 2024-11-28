package com.example.eventlistingappv1.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventlistingappv1.data.remote.response.ListEventsItem
import com.example.eventlistingappv1.databinding.ItemEventBinding

class EventAdapter(private val onItemClicked: (ListEventsItem) -> Unit) :
    ListAdapter<ListEventsItem, EventAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class MyViewHolder(val binding: ItemEventBinding, private val onItemClicked: (ListEventsItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem){
            binding.tvItemName.text = event.name
            Glide.with(binding.root.context)
                .load(event.imageLogo)
                .into(binding.tvItemPhoto)

            binding.root.setOnClickListener {
                onItemClicked(event)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}