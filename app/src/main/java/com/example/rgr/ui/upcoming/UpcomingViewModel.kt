package com.example.rgr.ui.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rgr.data.repository.BooksRepository
import com.example.rgr.ui.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UpcomingViewModel(
    private val repository: BooksRepository
) : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _info = MutableStateFlow<String?>(null)
    val info: StateFlow<String?> = _info.asStateFlow()

    init {
        loadUpcomingBooks()
    }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.refreshUpcomingBooks()
                _info.value = "Список новинок оновлено"
            } catch (e: Exception) {
                _error.value = "Помилка оновлення: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadUpcomingBooks() {
        viewModelScope.launch {
            repository.getUpcomingBooks().collect { bookList ->
                _books.value = bookList
            }
        }
    }

    fun toggleFavorite(bookId: String) {
        viewModelScope.launch {
            try {
                repository.toggleWishlist(bookId)
            } catch (e: Exception) {
                android.util.Log.e("UpcomingViewModel", "Error toggling favorite", e)
            }
        }
    }

    fun clearError() { _error.value = null }
    fun clearInfo() { _info.value = null }
}