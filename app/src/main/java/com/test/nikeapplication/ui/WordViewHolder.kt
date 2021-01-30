package com.test.nikeapplication.ui

import androidx.recyclerview.widget.RecyclerView
import com.test.nikeapplication.data.entities.Word
import com.test.nikeapplication.databinding.ItemViewBinding

class WordViewHolder(private val itemBinding: ItemViewBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var word: Word

    fun bind(item: Word) {
        this.word = item
        itemBinding.tvName.text = item.word
        itemBinding.tvDefinition.text = item.definition
        itemBinding.tvThumbsUp.text = item.thumbs_up.toString()
        itemBinding.tvThumbsDown.text = item.thumbs_down.toString()
    }
}