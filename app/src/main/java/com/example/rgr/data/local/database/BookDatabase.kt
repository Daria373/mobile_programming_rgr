package com.example.rgr.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rgr.data.local.dao.BookDao
import com.example.rgr.data.local.dao.WishlistDao
import com.example.rgr.data.local.model.BookEntity
import com.example.rgr.data.local.model.WishlistEntity

@Database(
    entities = [BookEntity::class, WishlistEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun wishlistDao(): WishlistDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getDatabase(context: Context): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "book_database"  // змінили назву БД
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}