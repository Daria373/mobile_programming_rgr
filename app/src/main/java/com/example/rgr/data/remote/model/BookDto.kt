package com.example.rgr.data.remote.model

import com.example.rgr.data.local.model.BookEntity
import com.google.gson.annotations.SerializedName

data class BookDto(
    @SerializedName("id") val id: String,
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfo
) {
    data class VolumeInfo(
        @SerializedName("title") val title: String,
        @SerializedName("authors") val authors: List<String>?,
        @SerializedName("description") val description: String?,
        @SerializedName("imageLinks") val imageLinks: ImageLinks?,
        @SerializedName("publishedDate") val publishedDate: String?,
        @SerializedName("averageRating") val averageRating: Double?,
        @SerializedName("ratingsCount") val ratingsCount: Int?,
        @SerializedName("pageCount") val pageCount: Int?
    ) {
        data class ImageLinks(
            @SerializedName("thumbnail") val thumbnail: String?,
            @SerializedName("smallThumbnail") val smallThumbnail: String?
        )
    }

    fun toEntity(isNowPlaying: Boolean = false, isUpcoming: Boolean = false): BookEntity {
        return BookEntity(
            id = id,
            title = volumeInfo.title,
            overview = volumeInfo.description ?: "Опис відсутній",
            posterPath = volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://"),
            backdropPath = volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://"),
            releaseDate = volumeInfo.publishedDate,
            voteAverage = volumeInfo.averageRating ?: 0.0,
            voteCount = volumeInfo.ratingsCount ?: 0,
            popularity = 0.0, // Google Books не дає popularity
            pageCount = volumeInfo.pageCount ?: 0,
            authors = volumeInfo.authors?.joinToString(", ") ?: "Невідомий автор",
            isNowPlaying = isNowPlaying,
            isUpcoming = isUpcoming
        )
    }
}