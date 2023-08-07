package com.akb.ownews.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akb.ownews.databinding.ItemPagingFooterBinding

class PagingLoadStateAdapter(
    private val retryAction: () -> Unit
) : LoadStateAdapter<PagingLoadStateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding =
            ItemPagingFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class ViewHolder(private val binding: ItemPagingFooterBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.apply {
                errorView.isVisible = loadState !is LoadState.Loading
                loadingProgress.isVisible = loadState is LoadState.Loading

                if (loadState is LoadState.Error){
                    errorText.text = loadState.error.localizedMessage
                }

                retryButton.setOnClickListener {
                    retryAction()
                }
            }
        }
    }
}