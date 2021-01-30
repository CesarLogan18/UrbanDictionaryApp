package com.test.nikeapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.nikeapplication.data.entities.Word
import com.test.nikeapplication.databinding.ItemViewBinding

class MainAdapter : RecyclerView.Adapter<WordViewHolder>() {

    private val items = ArrayList<Word>()

    fun setItems(items: List<Word>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding: ItemViewBinding =
            ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) =
        holder.bind(items[position])
}

