package com.wtw.whattheweather.util

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wtw.whattheweather.databinding.HomeFeedItemLayoutBinding
import com.wtw.whattheweather.model.FeedItemData
import com.wtw.whattheweather.ui.home.FeedDetailInfoActivity

class HomeFeedRecyclerAdapter : ListAdapter<FeedItemData, HomeFeedRecyclerAdapter.ViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<FeedItemData>() {

            override fun areContentsTheSame(
                oldItem: FeedItemData,
                newItem: FeedItemData
            ): Boolean = oldItem == newItem

            override fun areItemsTheSame(
                oldItem: FeedItemData,
                newItem: FeedItemData
            ): Boolean = oldItem.id == newItem.id

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(currentList[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    = ViewHolder(HomeFeedItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    inner class ViewHolder(private val binding : HomeFeedItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : FeedItemData) {

            binding.item = item

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, FeedDetailInfoActivity::class.java)

                intent.putExtra("data",item)
                binding.root.context.startActivity(intent)
                

            }

        }
    }
}