package com.example.takeaway.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.takeaway.data.local.dao.ChengyuDao
import com.example.takeaway.data.local.dao.HanziDao
import com.example.takeaway.data.local.entity.Chengyu
import com.example.takeaway.data.local.entity.Hanzi

@Database(entities = [Hanzi::class, Chengyu::class], version = 1, exportSchema = false)
abstract class HanziDatabase : RoomDatabase() {
    abstract fun hanziDao(): HanziDao

    abstract fun chengyuDao(): ChengyuDao

    companion object {
        private const val DATABASE_NAME = "hanzi.db"
        @Volatile
        private var instance: HanziDatabase? = null

        fun getInstance(context: Context): HanziDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): HanziDatabase {
            return Room.databaseBuilder(context, HanziDatabase::class.java, DATABASE_NAME)
                .createFromAsset(DATABASE_NAME)
                .build()
        }
    }
}
