package com.akb.ownews.ui.search.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akb.ownews.R
import com.akb.ownews.data.model.CategoryModel
import com.akb.ownews.databinding.ItemFilterBinding
import com.akb.ownews.utils.emptyString
import com.akb.ownews.utils.textOrNull

class FilterCategoryAdapter() : RecyclerView.Adapter<FilterCategoryAdapter.ViewHolder>() {

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
        fun bind(item: CategoryModel) {
            binding.run {
                tvFilter.textOrNull = item.title

                if(slug == item.slug) {
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

    private val differCallBack = object : DiffUtil.ItemCallback<CategoryModel>() {
        override fun areItemsTheSame(
            oldExampleModel: CategoryModel, newExampleModel: CategoryModel
        ): Boolean {
            return oldExampleModel.title == newExampleModel.title
        }

        override fun areContentsTheSame(
            oldExampleModel: CategoryModel, newExampleModel: CategoryModel
        ): Boolean {
            return oldExampleModel == newExampleModel
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    private var onItemClickListener: ((CategoryModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (CategoryModel) -> Unit) {
        onItemClickListener = listener
    }

    fun setChosen(slug: String) {
        this.slug = slug
    }

}