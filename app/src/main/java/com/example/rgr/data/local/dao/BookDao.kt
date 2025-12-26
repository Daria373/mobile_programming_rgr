package com.example.rgr.data.local.dao

import androidx.room.*
import com.example.rgr.data.local.model.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books WHERE isNowPlaying = 1 ORDER BY popularity DESC")
    fun getPopularBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE isUpcoming = 1 ORDER BY releaseDate DESC")
    fun getUpcomingBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): BookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Query("DELETE FROM books WHERE isNowPlaying = 1")
    suspend fun clearPopular()

    @Query("DELETE FROM books WHERE isUpcoming = 1")
    suspend fun clearUpcoming()

    @Query("UPDATE books SET isNowPlaying = 0, isUpcoming = 0")
    suspend fun clearAllFlags()
}