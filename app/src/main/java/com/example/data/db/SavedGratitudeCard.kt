package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.AiGuruResponse

@Entity(tableName = "saved_gratitude_cards")
data class SavedGratitudeCard(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val teacherName: String,
    val subject: String,
    val keyTraits: String,
    val personalizedMessage: String,
    val rhymingStanzaJson: String, // newline or pipe-separated or JSON list of lines
    val badgeNamesJson: String, // comma-separated or JSON list of badge names
    val studentSignature: String = "Aarav Sharma",
    val studentRole: String = "Class of 2026",
    val backNote: String = "",
    val templateIndex: Int = 0,
    val themeIndex: Int = 0,
    val accentIndex: Int = 0,
    val tone: String = "Inspiring",
    val language: String = "English",
    val createdAt: Long = System.currentTimeMillis(),
    val isAiGenerated: Boolean = true
) {
    fun getStanzaLines(): List<String> {
        return if (rhymingStanzaJson.isBlank()) emptyList()
        else rhymingStanzaJson.split("\n").filter { it.isNotBlank() }
    }

    fun getBadges(): List<String> {
        return if (badgeNamesJson.isBlank()) emptyList()
        else badgeNamesJson.split("|||").filter { it.isNotBlank() }
    }

    fun toAiGuruResponse(): AiGuruResponse {
        return AiGuruResponse(
            module = "AI_GURU",
            teacherName = teacherName,
            subject = subject,
            tone = tone,
            language = language,
            personalizedMessage = personalizedMessage,
            rhymingStanza = getStanzaLines(),
            badgeNames = getBadges()
        )
    }

    companion object {
        fun fromResponse(
            response: AiGuruResponse,
            traits: String,
            signature: String,
            role: String,
            backNote: String,
            templateIndex: Int,
            themeIndex: Int,
            accentIndex: Int,
            isAi: Boolean = true
        ): SavedGratitudeCard {
            return SavedGratitudeCard(
                teacherName = response.teacherName,
                subject = response.subject,
                keyTraits = traits,
                personalizedMessage = response.personalizedMessage,
                rhymingStanzaJson = response.rhymingStanza.joinToString("\n"),
                badgeNamesJson = response.badgeNames.joinToString("|||"),
                studentSignature = signature,
                studentRole = role,
                backNote = backNote,
                templateIndex = templateIndex,
                themeIndex = themeIndex,
                accentIndex = accentIndex,
                tone = response.tone,
                language = response.language,
                createdAt = System.currentTimeMillis(),
                isAiGenerated = isAi
            )
        }
    }
}
