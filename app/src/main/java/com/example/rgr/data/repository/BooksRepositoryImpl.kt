package com.example.rgr.data.repository

import android.util.Log
import com.example.rgr.data.local.dao.BookDao
import com.example.rgr.data.local.dao.WishlistDao
import com.example.rgr.data.local.model.BookEntity
import com.example.rgr.data.local.model.WishlistEntity  // ДОДАНО!
import com.example.rgr.data.remote.api.GoogleBooksApi
import com.example.rgr.ui.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class BooksRepositoryImpl(
    private val api: GoogleBooksApi,
    private val bookDao: BookDao,
    private val wishlistDao: WishlistDao  // виправлено з "wis..."
) : BooksRepository {

    override fun getPopularBooks(): Flow<List<Book>> {
        return bookDao.getPopularBooks().combine(wishlistDao.getAllWishlistItems()) { books, wishlist ->
            books.map { it.toBook(wishlist.any { w -> w.bookId == it.id }) }
        }
    }

    override fun getUpcomingBooks(): Flow<List<Book>> {
        return bookDao.getUpcomingBooks().combine(wishlistDao.getAllWishlistItems()) { books, wishlist ->
            books.map { it.toBook(wishlist.any { w -> w.bookId == it.id }) }
        }
    }

    override suspend fun getBookById(bookId: String): Book? {
        return bookDao.getBookById(bookId)?.toBook(isInWishlist(bookId))
    }

    override suspend fun refreshPopularBooks() {
        try {
            bookDao.clearPopular()
            val response = api.getPopularBooks()
            val entities = response.items.map { it.toEntity(isNowPlaying = true) }
            bookDao.insertBooks(entities)
        } catch (e: Exception) {
            Log.e("BooksRepo", "Refresh popular failed", e)
            throw e
        }
    }

    override suspend fun refreshUpcomingBooks() {
        try {
            bookDao.clearUpcoming()
            val response = api.getUpcomingBooks()
            val entities = response.items.map { it.toEntity(isUpcoming = true) }
            bookDao.insertBooks(entities)
        } catch (e: Exception) {
            Log.e("BooksRepo", "Refresh upcoming failed", e)
            throw e
        }
    }

    override suspend fun toggleWishlist(bookId: String) {
        if (isInWishlist(bookId)) {
            wishlistDao.getWishlistItem(bookId)?.let { wishlistDao.deleteWishlistItem(it) }
        } else {
            wishlistDao.insertWishlistItem(WishlistEntity(bookId))
        }
    }

    override fun getWishlistBooks(): Flow<List<Book>> {
        return wishlistDao.getAllWishlistItems().map { wishlistItems ->
            wishlistItems.mapNotNull { item ->
                bookDao.getBookById(item.bookId)?.toBook(true)
            }
        }
    }

    override suspend fun isInWishlist(bookId: String): Boolean {
        return wishlistDao.isInWishlist(bookId)
    }

    private fun BookEntity.toBook(isFavorite: Boolean = false): Book {
        return Book(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath,
            backdropPath = backdropPath,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            voteCount = voteCount,
            pageCount = pageCount,
            authors = authors,
            isFavorite = isFavorite
        )
    }
}