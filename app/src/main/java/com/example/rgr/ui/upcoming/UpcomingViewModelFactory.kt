package com.example.rgr.ui.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rgr.data.repository.BooksRepository

class UpcomingViewModelFactory(
    private val repository: BooksRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpcomingViewModel::class.java)) {
            return UpcomingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}