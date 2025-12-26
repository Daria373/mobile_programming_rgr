package com.example.rgr.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rgr.R
import com.example.rgr.databinding.ItemBookBinding
import com.example.rgr.ui.model.Book

class BookAdapter(
    private val onItemClick: (Book) -> Unit,
    private val onFavoriteClick: (Book) -> Unit
) : ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    class BookViewHolder(private val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book, onItemClick: (Book) -> Unit, onFavoriteClick: (Book) -> Unit) {
            // Назва та автори
            binding.titleText.text = book.title
            binding.authorsText.text = book.authors.ifEmpty { "Автор невідомий" }

            // Рейтинг з зірочкою
            val ratingDisplay = if (book.voteCount > 0) {
                "${String.format("%.1f", book.voteAverage)} ★"
            } else {
                "Без рейтингу"
            }
            binding.ratingText.text = ratingDisplay

            // Обкладинка
            Glide.with(binding.root.context)
                .load(book.posterPath)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.posterImage)

            // Сердечко
            binding.favoriteButton.setImageResource(
                if (book.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_empty
            )

            // Кліки
            binding.favoriteButton.setOnClickListener { onFavoriteClick(book) }
            binding.root.setOnClickListener { onItemClick(book) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick, onFavoriteClick)
    }

    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean = oldItem == newItem
    }
}