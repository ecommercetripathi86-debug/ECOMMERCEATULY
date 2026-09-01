package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [SavedGratitudeCard::class],
    version = 1,
    exportSchema = false
)
abstract class EduTributeDatabase : RoomDatabase() {

    abstract fun savedGratitudeCardDao(): SavedGratitudeCardDao

    companion object {
        @Volatile
        private var INSTANCE: EduTributeDatabase? = null

        fun getDatabase(context: Context): EduTributeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EduTributeDatabase::class.java,
                    "edutribute_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
