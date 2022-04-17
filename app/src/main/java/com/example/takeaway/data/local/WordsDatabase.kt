package com.example.takeaway.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.takeaway.data.local.dao.WordDao
import com.example.takeaway.data.local.entity.Word

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordsDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        private const val DATABASE_NAME = "starred.db"
        @Volatile private var instance: WordsDatabase? = null

        fun getInstance(context: Context): WordsDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): WordsDatabase {
            return Room.databaseBuilder(context, WordsDatabase::class.java, DATABASE_NAME).build()
        }
    }
}
