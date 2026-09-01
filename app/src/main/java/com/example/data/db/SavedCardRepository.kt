package com.example.data.db

import kotlinx.coroutines.flow.Flow

class SavedCardRepository(private val dao: SavedGratitudeCardDao) {

    val allSavedCards: Flow<List<SavedGratitudeCard>> = dao.getAllCards()

    suspend fun saveCard(card: SavedGratitudeCard): Long {
        return dao.insertCard(card)
    }

    suspend fun deleteCard(cardId: Long) {
        dao.deleteCardById(cardId)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }

    suspend fun getCardById(cardId: Long): SavedGratitudeCard? {
        return dao.getCardById(cardId)
    }

    suspend fun preseedInitialCardsIfEmpty() {
        if (dao.getCount() == 0) {
            val sampleCards = listOf(
                SavedGratitudeCard(
                    teacherName = "Prof. Eleanor Vance",
                    subject = "Physics & Astronomy",
                    keyTraits = "Passionate about astrophysics, explains relativity through cosmic stories",
                    personalizedMessage = "To Prof. Eleanor Vance: Like gravitational lensing revealing distant galaxies, your brilliant lectures illuminated the cosmos of astrophysics for us. Your endless patience turned daunting equations into stellar journeys of wonder.",
                    rhymingStanzaJson = "Across the cosmos of equations deep and grand,\nYou guided every telescope with gentle hand.\nFrom orbiting horizons to the stellar light,\nYou sparked in us a flame that shines forever bright.",
                    badgeNamesJson = "Cosmic Navigator|||Gravitational Beacon|||Stellar Mentor",
                    studentSignature = "Aarav Sharma",
                    studentRole = "Astronomy Club Lead",
                    backNote = "Thank you for inspiring our curiosity and always believing in us!",
                    templateIndex = 4, // Cosmic Nebula
                    themeIndex = 3, // Cosmic Navy
                    accentIndex = 3, // Star of Honor
                    tone = "Inspiring",
                    language = "English",
                    createdAt = System.currentTimeMillis() - 86400000L * 2,
                    isAiGenerated = true
                ),
                SavedGratitudeCard(
                    teacherName = "Dr. Alan Cooper",
                    subject = "Mathematics & Calculus",
                    keyTraits = "Makes integration intuitive, shows mathematical beauty in nature",
                    personalizedMessage = "To Dr. Alan Cooper: You showed us that calculus is not just formulas on a chalkboard, but the vibrant rhythm of change and nature. Thank you for proving that every complex derivative holds elegance and understanding.",
                    rhymingStanzaJson = "With limits and integrals woven like art,\nYou placed mathematical joy in each heart.\nNo function too steep and no tangent too wide,\nWith wisdom and clarity right by our side.",
                    badgeNamesJson = "Calculus Luminary|||Proof of Excellence|||Master of Derivatives",
                    studentSignature = "Meera Krishnan",
                    studentRole = "Batch of 2026",
                    backNote = "Your lessons in logic shaped how I solve real-world problems. Grateful forever!",
                    templateIndex = 0, // Luxe Gold Certificate
                    themeIndex = 1, // Champagne Luxe
                    accentIndex = 0, // Laurel Wreath
                    tone = "Poetic",
                    language = "English",
                    createdAt = System.currentTimeMillis() - 86400000L,
                    isAiGenerated = true
                ),
                SavedGratitudeCard(
                    teacherName = "Mrs. Sunita Verma",
                    subject = "Chemistry & Lab Sciences",
                    keyTraits = "Memorable chemical reaction demos, catalyst for curiosity, cheerful mentor",
                    personalizedMessage = "To Mrs. Sunita Verma: Like the perfect chemical catalyst, you accelerated our passion for scientific discovery without ever letting our enthusiasm deplete. Your lab experiments brought atomic magic to everyday life!",
                    rhymingStanzaJson = "In flasks of bright color and bonds made secure,\nYou made every principle sparkling and pure.\nA catalyst teacher with warmth in your soul,\nYou inspired each student to reach every goal.",
                    badgeNamesJson = "Curiosity Catalyst|||Lab Alchemist|||Inspiring Element",
                    studentSignature = "Rohan Mehta",
                    studentRole = "Chemistry Olympiad Team",
                    backNote = "Thank you for making science feel like magic every single morning!",
                    templateIndex = 1, // Modern Luminary Glass
                    themeIndex = 2, // Emerald Academy
                    accentIndex = 1, // Graduation Cap
                    tone = "Humorous",
                    language = "English",
                    createdAt = System.currentTimeMillis() - 3600000L * 4,
                    isAiGenerated = true
                )
            )
            dao.insertAll(sampleCards)
        }
    }
}
