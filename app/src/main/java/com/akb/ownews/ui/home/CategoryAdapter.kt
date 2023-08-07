package com.akb.ownews.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akb.ownews.data.model.CategoryModel
import com.akb.ownews.databinding.ItemCategoryBinding
import com.akb.ownews.utils.loadImage
import com.akb.ownews.utils.textOrNull

class CategoryAdapter() : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryModel) {
            binding.run {
                ivCategory.loadImage(item.image)
                tvTitle.textOrNull = item.title

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

}