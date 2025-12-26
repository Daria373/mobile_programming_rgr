package com.example.rgr.data.remote.api

import com.example.rgr.data.remote.model.BookResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleBooksApi {
    @GET("volumes")
    suspend fun getPopularBooks(
        @Query("q") query: String = "subject:fiction",
        @Query("orderBy") orderBy: String = "relevance",
        @Query("maxResults") maxResults: Int = 20,
        @Query("printType") printType: String = "books"
    ): BookResponse

    @GET("volumes")
    suspend fun getUpcomingBooks(
        @Query("q") query: String = "bestsellers OR new york times OR fantasy OR thriller OR romance 2024 OR 2025",
        @Query("orderBy") orderBy: String = "newest",
        @Query("maxResults") maxResults: Int = 40,
        @Query("printType") printType: String = "books"
    ): BookResponse

    @GET("volumes/{volumeId}")
    suspend fun getBookDetails(
        @Path("volumeId") volumeId: String
    ): com.example.rgr.data.remote.model.BookDto
}