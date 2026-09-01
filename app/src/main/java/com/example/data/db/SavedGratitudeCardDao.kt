package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedGratitudeCardDao {

    @Query("SELECT * FROM saved_gratitude_cards ORDER BY createdAt DESC")
    fun getAllCards(): Flow<List<SavedGratitudeCard>>

    @Query("SELECT * FROM saved_gratitude_cards WHERE id = :id LIMIT 1")
    suspend fun getCardById(id: Long): SavedGratitudeCard?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: SavedGratitudeCard): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<SavedGratitudeCard>)

    @Update
    suspend fun updateCard(card: SavedGratitudeCard)

    @Query("DELETE FROM saved_gratitude_cards WHERE id = :id")
    suspend fun deleteCardById(id: Long)

    @Query("DELETE FROM saved_gratitude_cards")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM saved_gratitude_cards")
    suspend fun getCount(): Int
}
