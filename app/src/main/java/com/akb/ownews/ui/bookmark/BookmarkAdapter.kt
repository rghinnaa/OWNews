package com.akb.ownews.ui.bookmark

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akb.ownews.R
import com.akb.ownews.data.local.entity.Bookmark
import com.akb.ownews.data.model.CategoryModel
import com.akb.ownews.databinding.ItemCategoryBinding
import com.akb.ownews.databinding.ItemNewsBinding
import com.akb.ownews.utils.dateFormatter
import com.akb.ownews.utils.loadImage
import com.akb.ownews.utils.textOrNull
import java.time.Instant

class BookmarkAdapter() : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    private val formatDate = "dd MMMM, yyyy"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Bookmark) {
            binding.run {
                ibShare.loadImage(R.drawable.ic_bookmark_news_active_16dp)

                val instant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Instant.parse(item.publishedAt)
                } else {
                    TODO("VERSION.SDK_INT < O")
                }

                val date = java.util.Date.from(instant)

                ivNews.loadImage(item.urlToImage)
                tvTitle.textOrNull = item.title
                tvSource.textOrNull = item.source
                tvDate.textOrNull = dateFormatter(formatDate).format(date)

                binding.ibShare.setOnClickListener {
                    onBookmarkClickListener?.invoke(item.id)
                }
                binding.root.setOnClickListener {
                    onItemClickListener?.invoke(item)
                }
            }
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Bookmark>() {
        override fun areItemsTheSame(
            oldExampleModel: Bookmark, newExampleModel: Bookmark
        ): Boolean {
            return oldExampleModel.title == newExampleModel.title
        }

        override fun areContentsTheSame(
            oldExampleModel: Bookmark, newExampleModel: Bookmark
        ): Boolean {
            return oldExampleModel == newExampleModel
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    private var onItemClickListener: ((Bookmark) -> Unit)? = null

    fun setOnItemClickListener(listener: (Bookmark) -> Unit) {
        onItemClickListener = listener
    }

    private var onBookmarkClickListener: ((Int?) -> Unit)? = null

    fun setOnBookmarkClickListener(listener: (Int?) -> Unit) {
        onBookmarkClickListener = listener
    }

}