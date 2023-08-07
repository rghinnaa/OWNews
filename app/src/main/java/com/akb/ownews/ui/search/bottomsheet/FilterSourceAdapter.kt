package com.akb.ownews.ui.search.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akb.ownews.R
import com.akb.ownews.data.model.SourceModel
import com.akb.ownews.databinding.ItemFilterBinding
import com.akb.ownews.utils.emptyString
import com.akb.ownews.utils.textOrNull

class FilterSourceAdapter() : RecyclerView.Adapter<FilterSourceAdapter.ViewHolder>() {

    private var id = emptyString

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
        fun bind(item: SourceModel) {
            binding.run {
                tvFilter.textOrNull = item.name

                if(id == item.id) {
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

    private val differCallBack = object : DiffUtil.ItemCallback<SourceModel>() {
        override fun areItemsTheSame(
            oldExampleModel: SourceModel, newExampleModel: SourceModel
        ): Boolean {
            return oldExampleModel.name == newExampleModel.name
        }

        override fun areContentsTheSame(
            oldExampleModel: SourceModel, newExampleModel: SourceModel
        ): Boolean {
            return oldExampleModel == newExampleModel
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    private var onItemClickListener: ((SourceModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (SourceModel) -> Unit) {
        onItemClickListener = listener
    }

    fun setChosen(id: String) {
        this.id = id
    }

}