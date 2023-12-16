package com.abid.githubuserapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abid.githubuserapp.data.response.ItemsItem
import com.abid.githubuserapp.databinding.ItemRowCardBinding
import com.bumptech.glide.Glide

class UserAdapter(private val itemClicked: (ItemsItem) -> Unit): ListAdapter<ItemsItem, UserAdapter.MyViewHolder>(
    DIFF_CALLBACK
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userAcc = getItem(position)
        holder.bind(userAcc)
    }

    inner class MyViewHolder(private val binding: ItemRowCardBinding) : RecyclerView.ViewHolder(binding.root) {

        init{
            itemView.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    val item = getItem(position)
                    itemClicked(item)
                }
            }
        }

        fun bind(user: ItemsItem){
            binding.tvUserName.text = user.login
            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .into(binding.imgItem)
        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>(){
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

        }
    }

}

