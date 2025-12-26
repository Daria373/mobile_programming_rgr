package com.example.rgr.ui.model

data class Book(
    val id: String,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val pageCount: Int,
    val authors: String,
    val isFavorite: Boolean = false
)