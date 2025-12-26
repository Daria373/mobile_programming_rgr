package com.example.rgr.ui.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rgr.data.repository.BooksRepository
import com.example.rgr.ui.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WishlistViewModel(
    private val repository: BooksRepository
) : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    fun loadWishlist() {
        viewModelScope.launch {
            repository.getWishlistBooks().collect { bookList ->
                _books.value = bookList
            }
        }
    }

    fun removeFromWishlist(bookId: String) {
        viewModelScope.launch {
            repository.toggleWishlist(bookId)
        }
    }
}