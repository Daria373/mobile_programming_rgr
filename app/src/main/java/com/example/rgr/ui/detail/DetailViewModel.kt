package com.example.rgr.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rgr.data.repository.BooksRepository
import com.example.rgr.ui.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: BooksRepository
) : ViewModel() {

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadBook(bookId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val loadedBook = repository.getBookById(bookId)
                _book.value = loadedBook
            } catch (e: Exception) {
                _error.value = "Помилка завантаження книги: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(bookId: String) {
        viewModelScope.launch {
            try {
                repository.toggleWishlist(bookId)
                val currentBook = _book.value
                if (currentBook != null) {
                    val isFavorite = repository.isInWishlist(bookId)
                    _book.value = currentBook.copy(isFavorite = isFavorite)
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Помилка додавання до списку бажаного"
            }
        }
    }

    fun clearError() { _error.value = null }
}