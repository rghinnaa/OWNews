package com.akb.ownews.ui.home.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akb.ownews.R
import com.akb.ownews.databinding.ItemFilterBinding
import com.akb.ownews.utils.emptyString
import com.akb.ownews.utils.textOrNull

class FilterCountryAdapter() : RecyclerView.Adapter<FilterCountryAdapter.ViewHolder>() {

    private var slug = emptyString

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFilterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(private val binding: ItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.run {
                tvFilter.textOrNull = item

                if(slug == item) {
                    binding.tvFilter.setBackgroundColor(root.context.getColor(R.color.zinnwaldite))
                } else {
                    binding.tvFilter.setBackgroundColor(root.context.getColor(R.color.white))
                }

                binding.root.setOnClickListener {
                    onItemClickListener?.invoke(item)
                }
            }
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldExampleModel: String, newExampleModel: String
        ): Boolean {
            return oldExampleModel == newExampleModel
        }

        override fun areContentsTheSame(
            oldExampleModel: String, newExampleModel: String
        ): Boolean {
            return oldExampleModel == newExampleModel
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    fun setChosen(slug: String) {
        this.slug = slug
    }

}