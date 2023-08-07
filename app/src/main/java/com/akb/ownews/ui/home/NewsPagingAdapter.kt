package com.akb.ownews.ui.home

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akb.ownews.data.model.ArticleModel
import com.akb.ownews.databinding.ItemNewsBinding
import com.akb.ownews.utils.dateFormatter
import com.akb.ownews.utils.loadImage
import com.akb.ownews.utils.textOrNull
import java.time.Instant

class NewsPagingAdapter :
    PagingDataAdapter<ArticleModel, NewsPagingAdapter.ViewHolder>(
    differ
) {

    private val formatDate = "dd MMMM, yyyy"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if(item != null) {
            holder.bind(item)
        }
    }

    inner class ViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArticleModel) {
            binding.run {
                val instant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Instant.parse(item.publishedAt)
                } else {
                    TODO("VERSION.SDK_INT < O")
                }

                val date = java.util.Date.from(instant)

                ivNews.loadImage(item.urlToImage)
                tvTitle.textOrNull = item.title
                tvSource.textOrNull = item.source?.name
                tvDate.textOrNull = dateFormatter(formatDate).format(date)

                binding.ibShare.setOnClickListener {
                    onShareClickListener?.invoke(item.url.toString())
                }

                binding.root.setOnClickListener {
                    onItemClickListener?.invoke(item)
                }
            }
        }
    }

    private var onItemClickListener: ((ArticleModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (ArticleModel) -> Unit) {
        onItemClickListener = listener
    }

    private var onShareClickListener: ((String) -> Unit)? = null

    fun setOnShareClickListener(listener: (String) -> Unit) {
        onShareClickListener = listener
    }

    companion object {
        private val differ = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean =
                oldItem.content == newItem.content

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean =
                oldItem == newItem
        }
    }

}