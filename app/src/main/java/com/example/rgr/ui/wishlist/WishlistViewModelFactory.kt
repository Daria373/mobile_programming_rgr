package com.example.rgr.ui.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rgr.data.repository.BooksRepository

class WishlistViewModelFactory(
    private val repository: BooksRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WishlistViewModel::class.java)) {
            return WishlistViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}