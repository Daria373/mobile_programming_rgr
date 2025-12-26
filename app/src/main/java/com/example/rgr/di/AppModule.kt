package com.example.rgr.di

import android.content.Context
import com.example.rgr.data.local.database.BookDatabase
import com.example.rgr.data.remote.api.GoogleBooksApi
import com.example.rgr.data.repository.BooksRepository
import com.example.rgr.data.repository.BooksRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {
    private const val BASE_URL = "https://www.googleapis.com/books/v1/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val googleBooksApi: GoogleBooksApi = retrofit.create(GoogleBooksApi::class.java)

    fun provideDatabase(context: Context): BookDatabase {
        return BookDatabase.getDatabase(context)
    }

    fun provideBooksRepository(context: Context): BooksRepository {
        val database = provideDatabase(context)
        return BooksRepositoryImpl(
            api = googleBooksApi,
            bookDao = database.bookDao(), // перейменуємо нижче
            wishlistDao = database.wishlistDao()
        )
    }
}