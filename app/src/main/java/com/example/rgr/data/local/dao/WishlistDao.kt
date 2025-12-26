package com.example.rgr.data.local.dao

import androidx.room.*
import com.example.rgr.data.local.model.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist ORDER BY addedAt DESC")
    fun getAllWishlistItems(): Flow<List<WishlistEntity>>

    @Query("SELECT * FROM wishlist WHERE bookId = :bookId")
    suspend fun getWishlistItem(bookId: String): WishlistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistItem(item: WishlistEntity)

    @Delete
    suspend fun deleteWishlistItem(item: WishlistEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE bookId = :bookId)")
    suspend fun isInWishlist(bookId: String): Boolean
}