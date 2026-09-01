package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

enum class EduModule(val title: String, val subtitle: String) {
    AI_GURU("AI Guru", "Appreciation Card & Poem"),
    VOICE_OF_GRATITUDE("Voice of Gratitude", "TTS Audio Tribute Script"),
    TRIVIA_BATTLE("Trivia Battle", "Teacher Persona & Quiz"),
    API_ENGINE("API Engine", "JSON Playground & Inspector")
}

// -------------------------------------------------------------
// Module 1: AI_GURU Models
// -------------------------------------------------------------

@JsonClass(generateAdapter = true)
data class AiGuruRequest(
    val teacherName: String,
    val subject: String,
    val keyTraits: String,
    val language: String = "English",
    val tone: String = "Inspiring" // Poetic, Humorous, Inspiring, Formal
)

@JsonClass(generateAdapter = true)
data class AiGuruResponse(
    @Json(name = "module") val module: String = "AI_GURU",
    @Json(name = "teacherName") val teacherName: String = "",
    @Json(name = "subject") val subject: String = "",
    @Json(name = "tone") val tone: String = "",
    @Json(name = "language") val language: String = "English",
    @Json(name = "personalizedMessage") val personalizedMessage: String = "",
    @Json(name = "rhymingStanza") val rhymingStanza: List<String> = emptyList(),
    @Json(name = "badgeNames") val badgeNames: List<String> = emptyList()
)

// -------------------------------------------------------------
// Module 2: VOICE_OF_GRATITUDE Models
// -------------------------------------------------------------

@JsonClass(generateAdapter = true)
data class VoiceGratitudeRequest(
    val studentName: String,
    val teacherName: String,
    val rawMessage: String,
    val targetLanguage: String = "English"
)

@JsonClass(generateAdapter = true)
data class PronunciationGuideItem(
    @Json(name = "term") val term: String = "",
    @Json(name = "guide") val guide: String = ""
)

@JsonClass(generateAdapter = true)
data class VoiceGratitudeResponse(
    @Json(name = "module") val module: String = "VOICE_OF_GRATITUDE",
    @Json(name = "studentName") val studentName: String = "",
    @Json(name = "teacherName") val teacherName: String = "",
    @Json(name = "targetLanguage") val targetLanguage: String = "English",
    @Json(name = "ttsScript") val ttsScript: String = "",
    @Json(name = "pronunciationGuides") val pronunciationGuides: List<PronunciationGuideItem> = emptyList(),
    @Json(name = "englishSummary") val englishSummary: String = "",
    @Json(name = "pacingMarkers") val pacingMarkers: List<String> = emptyList()
)

// -------------------------------------------------------------
// Module 3: TRIVIA_BATTLE Models
// -------------------------------------------------------------

@JsonClass(generateAdapter = true)
data class TriviaBattleRequest(
    val subjectArea: String,
    val gradeDepartment: String,
    val personaArchetype: String
)

@JsonClass(generateAdapter = true)
data class TriviaQuestion(
    @Json(name = "id") val id: Int = 1,
    @Json(name = "question") val question: String = "",
    @Json(name = "options") val options: List<String> = emptyList(),
    @Json(name = "correctOptionIndex") val correctOptionIndex: Int = 0,
    @Json(name = "explanation") val explanation: String = "",
    @Json(name = "personaComment") val personaComment: String = ""
)

@JsonClass(generateAdapter = true)
data class TriviaBattleResponse(
    @Json(name = "module") val module: String = "TRIVIA_BATTLE",
    @Json(name = "subjectArea") val subjectArea: String = "",
    @Json(name = "gradeDepartment") val gradeDepartment: String = "",
    @Json(name = "personaArchetype") val personaArchetype: String = "",
    @Json(name = "digitalBadgeTitle") val digitalBadgeTitle: String = "",
    @Json(name = "questions") val questions: List<TriviaQuestion> = emptyList()
)

// -------------------------------------------------------------
// Safety & General Engine States
// -------------------------------------------------------------

@JsonClass(generateAdapter = true)
data class SafetyRejectionResponse(
    @Json(name = "status") val status: String = "REJECTED_DISRESPECTFUL_CONTENT",
    @Json(name = "reason") val reason: String = "Input violates the dignity, warmth, and respect guidelines for educators.",
    @Json(name = "politeAlternative") val politeAlternative: String = ""
)

sealed class EngineUiState<out T> {
    object Idle : EngineUiState<Nothing>()
    object Loading : EngineUiState<Nothing>()
    data class Success<T>(val data: T, val rawJson: String, val isAiGenerated: Boolean = true) : EngineUiState<T>()
    data class SafetyRejection(val reason: String, val politeAlternative: String, val rawJson: String) : EngineUiState<Nothing>()
    data class Error(val message: String) : EngineUiState<Nothing>()
}
