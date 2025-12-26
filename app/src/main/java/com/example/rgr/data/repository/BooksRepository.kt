package com.example.rgr.data.repository

import com.example.rgr.ui.model.Book
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    fun getPopularBooks(): Flow<List<Book>>
    fun getUpcomingBooks(): Flow<List<Book>>
    suspend fun getBookById(bookId: String): Book?
    suspend fun refreshPopularBooks()
    suspend fun refreshUpcomingBooks()
    suspend fun toggleWishlist(bookId: String)
    fun getWishlistBooks(): Flow<List<Book>>
    suspend fun isInWishlist(bookId: String): Boolean
}