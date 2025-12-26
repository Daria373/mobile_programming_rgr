package com.example.rgr.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistEntity(
    @PrimaryKey val bookId: String,
    val addedAt: Long = System.currentTimeMillis()
)