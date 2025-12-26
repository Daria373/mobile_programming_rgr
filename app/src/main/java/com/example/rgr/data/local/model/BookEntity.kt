package com.example.rgr.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val pageCount: Int,
    val authors: String,
    val isNowPlaying: Boolean = false,
    val isUpcoming: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)