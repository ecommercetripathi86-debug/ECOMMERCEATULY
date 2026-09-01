package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.EduTributeDatabase
import com.example.data.db.SavedCardRepository
import com.example.data.db.SavedGratitudeCard
import com.example.data.engine.EduTributeEngine
import com.example.data.engine.EngineOutput
import com.example.data.engine.TtsManager
import com.example.data.model.AiGuruRequest
import com.example.data.model.AiGuruResponse
import com.example.data.model.EduModule
import com.example.data.model.EngineUiState
import com.example.data.model.TriviaBattleRequest
import com.example.data.model.TriviaBattleResponse
import com.example.data.model.VoiceGratitudeRequest
import com.example.data.model.VoiceGratitudeResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EduTributeViewModel(application: Application) : AndroidViewModel(application) {

    private val engine = EduTributeEngine()
    val ttsManager = TtsManager(application.applicationContext)

    // Room Database Repository
    private val database = EduTributeDatabase.getDatabase(application)
    val cardRepository = SavedCardRepository(database.savedGratitudeCardDao())

    val savedCards: StateFlow<List<SavedGratitudeCard>> = cardRepository.allSavedCards
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Pre-seed starter appreciation cards if empty
        viewModelScope.launch {
            cardRepository.preseedInitialCardsIfEmpty()
        }
    }

    // Current Active Navigation
    private val _currentModule = MutableStateFlow(EduModule.AI_GURU)
    val currentModule: StateFlow<EduModule> = _currentModule.asStateFlow()

    // ---------------------------------------------------------------------------------------------
    // Module 1: AI GURU Interactive Card Builder State
    // ---------------------------------------------------------------------------------------------
    val guruActiveTab = MutableStateFlow(0) // 0: Studio, 1: Gallery, 2: Info, 3: Style
    val guruTeacherName = MutableStateFlow("Prof. Eleanor Vance")
    val guruSubject = MutableStateFlow("Physics & Astronomy")
    val guruKeyTraits = MutableStateFlow("Passionate about deep space, turns complex relativity into gripping stories, endlessly patient")
    val guruLanguage = MutableStateFlow("English")
    val guruTone = MutableStateFlow("Inspiring") // Poetic, Humorous, Inspiring, Formal
    val guruCardTheme = MutableStateFlow(0) // 0: Dark Obsidian, 1: Champagne Gold, 2: Emerald Academy, 3: Cosmic Navy, 4: Crimson Regal, 5: Violet Nebula
    val guruSelectedTemplate = MutableStateFlow(0) // 0: Luxe Gold Certificate, 1: Modern Luminary Glass, 2: Classic Scroll Parchment, 3: Editorial Minimalist, 4: Cosmic Nebula
    val guruStudentSignature = MutableStateFlow("Aarav Sharma")
    val guruStudentRole = MutableStateFlow("Class of 2026")
    val guruSelectedAccent = MutableStateFlow(0) // 0: Laurel Wreath, 1: Graduation Cap, 2: Quill & Scroll, 3: Star of Excellence, 4: Atom & Spark
    val guruBackNote = MutableStateFlow("Thank you for inspiring our curiosity and always believing in us!")
    val guruIsCardFlipped = MutableStateFlow(false)
    val guruSearchQuery = MutableStateFlow("")

    private val _guruState = MutableStateFlow<EngineUiState<AiGuruResponse>>(EngineUiState.Idle)
    val guruState: StateFlow<EngineUiState<AiGuruResponse>> = _guruState.asStateFlow()

    // ---------------------------------------------------------------------------------------------
    // Module 2: VOICE OF GRATITUDE State
    // ---------------------------------------------------------------------------------------------
    val voiceStudentName = MutableStateFlow("Aarav Sharma")
    val voiceTeacherName = MutableStateFlow("Dr. S. K. Mukherjee")
    val voiceRawMessage = MutableStateFlow("Thank you for never giving up on me when I was struggling with organic chemistry. Your encouragement changed my life.")
    val voiceTargetLanguage = MutableStateFlow("English")
    val voiceOccasion = MutableStateFlow("Teachers' Day Celebration")
    val voiceTone = MutableStateFlow("Heartfelt & Warm")
    val voiceDuration = MutableStateFlow("Standard (~60s)")
    val voiceCustomEditedScript = MutableStateFlow("")
    val ttsSpeed = MutableStateFlow(1.0f)
    val ttsPitch = MutableStateFlow(1.0f)

    val isTtsPlaying: StateFlow<Boolean> = ttsManager.isPlaying
    val ttsSpokenPhrase: StateFlow<String> = ttsManager.currentSpokenPhrase
    val ttsProgress: StateFlow<Float> = ttsManager.speechProgress

    private val _voiceState = MutableStateFlow<EngineUiState<VoiceGratitudeResponse>>(EngineUiState.Idle)
    val voiceState: StateFlow<EngineUiState<VoiceGratitudeResponse>> = _voiceState.asStateFlow()

    // ---------------------------------------------------------------------------------------------
    // Module 3: TRIVIA BATTLE State
    // ---------------------------------------------------------------------------------------------
    val triviaSubject = MutableStateFlow("Mathematics & Calculus")
    val triviaGrade = MutableStateFlow("High School (Grade 11-12)")
    val triviaPersona = MutableStateFlow("The Homework Detective") // The Homework Detective, The Wisdom Sage, The Concept Master

    private val _triviaState = MutableStateFlow<EngineUiState<TriviaBattleResponse>>(EngineUiState.Idle)
    val triviaState: StateFlow<EngineUiState<TriviaBattleResponse>> = _triviaState.asStateFlow()

    // Interactive Quiz Game in-progress state
    val triviaCurrentQuestionIndex = MutableStateFlow(0)
    val triviaUserAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val triviaAnswerRevealed = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val triviaScore = MutableStateFlow(0)
    val triviaQuizFinished = MutableStateFlow(false)

    // ---------------------------------------------------------------------------------------------
    // Module 4: API ENGINE INSPECTOR State
    // ---------------------------------------------------------------------------------------------
    val apiTesterSelectedModule = MutableStateFlow(EduModule.AI_GURU)
    val apiTesterRawJson = MutableStateFlow("")
    val apiTesterIsExecuting = MutableStateFlow(false)

    // ---------------------------------------------------------------------------------------------
    // Navigation
    // ---------------------------------------------------------------------------------------------
    fun selectModule(module: EduModule) {
        _currentModule.value = module
        ttsManager.stop()
    }

    // ---------------------------------------------------------------------------------------------
    // Module 1 Execution
    // ---------------------------------------------------------------------------------------------
    fun generateAiGuru() {
        viewModelScope.launch {
            _guruState.value = EngineUiState.Loading
            val req = AiGuruRequest(
                teacherName = guruTeacherName.value.trim(),
                subject = guruSubject.value.trim(),
                keyTraits = guruKeyTraits.value.trim(),
                language = guruLanguage.value,
                tone = guruTone.value
            )
            when (val result = engine.processAiGuru(req)) {
                is EngineOutput.Success -> {
                    _guruState.value = EngineUiState.Success(result.data, result.rawJson, result.isAi)
                }
                is EngineOutput.SafetyRejection -> {
                    _guruState.value = EngineUiState.SafetyRejection(result.reason, result.politeAlternative, result.rawJson)
                }
                is EngineOutput.Failure -> {
                    _guruState.value = EngineUiState.Error(result.errorMessage)
                }
            }
        }
    }

    fun loadGuruPreset(
        name: String,
        subject: String,
        traits: String,
        tone: String,
        lang: String = "English",
        templateIndex: Int = 0,
        themeIndex: Int = 0,
        accentIndex: Int = 0,
        signature: String = "Aarav Sharma",
        role: String = "Class of 2026"
    ) {
        guruTeacherName.value = name
        guruSubject.value = subject
        guruKeyTraits.value = traits
        guruTone.value = tone
        guruLanguage.value = lang
        guruSelectedTemplate.value = templateIndex
        guruCardTheme.value = themeIndex
        guruSelectedAccent.value = accentIndex
        guruStudentSignature.value = signature
        guruStudentRole.value = role
        guruIsCardFlipped.value = false
    }

    fun toggleCardFlip() {
        guruIsCardFlipped.value = !guruIsCardFlipped.value
    }

    fun updateGuruMessage(newMessage: String) {
        val current = _guruState.value
        if (current is EngineUiState.Success) {
            val updatedData = current.data.copy(personalizedMessage = newMessage)
            _guruState.value = EngineUiState.Success(updatedData, current.rawJson, current.isAiGenerated)
        }
    }

    fun updateGuruStanza(newStanza: List<String>) {
        val current = _guruState.value
        if (current is EngineUiState.Success) {
            val updatedData = current.data.copy(rhymingStanza = newStanza)
            _guruState.value = EngineUiState.Success(updatedData, current.rawJson, current.isAiGenerated)
        }
    }

    fun addGuruBadge(badgeName: String) {
        val current = _guruState.value
        if (current is EngineUiState.Success && badgeName.isNotBlank()) {
            val currentBadges = current.data.badgeNames.toMutableList()
            if (!currentBadges.contains(badgeName.trim())) {
                currentBadges.add(badgeName.trim())
                val updatedData = current.data.copy(badgeNames = currentBadges)
                _guruState.value = EngineUiState.Success(updatedData, current.rawJson, current.isAiGenerated)
            }
        }
    }

    fun removeGuruBadge(badgeName: String) {
        val current = _guruState.value
        if (current is EngineUiState.Success) {
            val currentBadges = current.data.badgeNames.filter { it != badgeName }
            val updatedData = current.data.copy(badgeNames = currentBadges)
            _guruState.value = EngineUiState.Success(updatedData, current.rawJson, current.isAiGenerated)
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Gallery & Local Persistence Operations
    // ---------------------------------------------------------------------------------------------
    fun saveCurrentCard(onSuccess: (Long) -> Unit = {}, onError: (String) -> Unit = {}) {
        val current = _guruState.value
        if (current is EngineUiState.Success) {
            viewModelScope.launch {
                try {
                    val entity = SavedGratitudeCard.fromResponse(
                        response = current.data,
                        traits = guruKeyTraits.value,
                        signature = guruStudentSignature.value,
                        role = guruStudentRole.value,
                        backNote = guruBackNote.value,
                        templateIndex = guruSelectedTemplate.value,
                        themeIndex = guruCardTheme.value,
                        accentIndex = guruSelectedAccent.value,
                        isAi = current.isAiGenerated
                    )
                    val insertedId = cardRepository.saveCard(entity)
                    onSuccess(insertedId)
                } catch (e: Exception) {
                    onError(e.localizedMessage ?: "Failed to save card")
                }
            }
        } else {
            onError("Please generate or load a card before saving to gallery.")
        }
    }

    fun loadSavedCard(card: SavedGratitudeCard) {
        guruTeacherName.value = card.teacherName
        guruSubject.value = card.subject
        guruKeyTraits.value = card.keyTraits
        guruLanguage.value = card.language
        guruTone.value = card.tone
        guruCardTheme.value = card.themeIndex
        guruSelectedTemplate.value = card.templateIndex
        guruStudentSignature.value = card.studentSignature
        guruStudentRole.value = card.studentRole
        guruSelectedAccent.value = card.accentIndex
        guruBackNote.value = card.backNote
        guruIsCardFlipped.value = false

        val response = card.toAiGuruResponse()
        val jsonOutput = engine.formatGuruOutputJson(response)
        _guruState.value = EngineUiState.Success(response, jsonOutput, isAiGenerated = card.isAiGenerated)
        guruActiveTab.value = 0 // Switch directly to Card Studio canvas
    }

    fun deleteSavedCard(cardId: Long) {
        viewModelScope.launch {
            cardRepository.deleteCard(cardId)
        }
    }

    fun clearAllSavedCards() {
        viewModelScope.launch {
            cardRepository.clearAll()
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Module 2 Execution
    // ---------------------------------------------------------------------------------------------
    fun generateVoiceGratitude() {
        viewModelScope.launch {
            _voiceState.value = EngineUiState.Loading
            ttsManager.stop()
            val rawWithOccasion = buildString {
                if (voiceOccasion.value.isNotBlank()) {
                    append("[Occasion: ${voiceOccasion.value}] ")
                }
                if (voiceTone.value.isNotBlank()) {
                    append("[Tone: ${voiceTone.value}] ")
                }
                if (voiceDuration.value.isNotBlank()) {
                    append("[Duration: ${voiceDuration.value}] ")
                }
                append(voiceRawMessage.value.trim())
            }
            val req = VoiceGratitudeRequest(
                studentName = voiceStudentName.value.trim(),
                teacherName = voiceTeacherName.value.trim(),
                rawMessage = rawWithOccasion,
                targetLanguage = voiceTargetLanguage.value
            )
            when (val result = engine.processVoiceGratitude(req)) {
                is EngineOutput.Success -> {
                    voiceCustomEditedScript.value = result.data.ttsScript
                    _voiceState.value = EngineUiState.Success(result.data, result.rawJson, result.isAi)
                }
                is EngineOutput.SafetyRejection -> {
                    _voiceState.value = EngineUiState.SafetyRejection(result.reason, result.politeAlternative, result.rawJson)
                }
                is EngineOutput.Failure -> {
                    _voiceState.value = EngineUiState.Error(result.errorMessage)
                }
            }
        }
    }

    fun playTts(script: String, language: String) {
        ttsManager.speak(script, language, ttsPitch.value, ttsSpeed.value)
    }

    fun stopTts() {
        ttsManager.stop()
    }

    fun playCardTts(teacherName: String, message: String, poemStanza: List<String>, language: String) {
        val poemText = poemStanza.joinToString(", ")
        val speechText = "A tribute to $teacherName. $message. $poemText."
        ttsManager.speak(speechText, language, ttsPitch.value, ttsSpeed.value)
    }

    fun loadVoicePreset(
        student: String,
        teacher: String,
        message: String,
        lang: String,
        occasion: String = "Teachers' Day Celebration",
        tone: String = "Heartfelt & Warm",
        duration: String = "Standard (~60s)"
    ) {
        voiceStudentName.value = student
        voiceTeacherName.value = teacher
        voiceRawMessage.value = message
        voiceTargetLanguage.value = lang
        voiceOccasion.value = occasion
        voiceTone.value = tone
        voiceDuration.value = duration
    }

    // ---------------------------------------------------------------------------------------------
    // Module 3 Execution
    // ---------------------------------------------------------------------------------------------
    fun generateTriviaBattle() {
        viewModelScope.launch {
            _triviaState.value = EngineUiState.Loading
            resetTriviaGameState()
            val req = TriviaBattleRequest(
                subjectArea = triviaSubject.value.trim(),
                gradeDepartment = triviaGrade.value.trim(),
                personaArchetype = triviaPersona.value.trim()
            )
            when (val result = engine.processTriviaBattle(req)) {
                is EngineOutput.Success -> {
                    _triviaState.value = EngineUiState.Success(result.data, result.rawJson, result.isAi)
                }
                is EngineOutput.SafetyRejection -> {
                    _triviaState.value = EngineUiState.SafetyRejection(result.reason, result.politeAlternative, result.rawJson)
                }
                is EngineOutput.Failure -> {
                    _triviaState.value = EngineUiState.Error(result.errorMessage)
                }
            }
        }
    }

    fun selectQuizOption(questionIndex: Int, optionIndex: Int, correctIndex: Int) {
        val currentAnswers = triviaUserAnswers.value.toMutableMap()
        if (currentAnswers.containsKey(questionIndex)) return // Already selected

        currentAnswers[questionIndex] = optionIndex
        triviaUserAnswers.value = currentAnswers

        val currentRevealed = triviaAnswerRevealed.value.toMutableMap()
        currentRevealed[questionIndex] = true
        triviaAnswerRevealed.value = currentRevealed

        if (optionIndex == correctIndex) {
            triviaScore.value += 100
        }
    }

    fun nextQuestion(totalQuestions: Int) {
        if (triviaCurrentQuestionIndex.value < totalQuestions - 1) {
            triviaCurrentQuestionIndex.value += 1
        } else {
            triviaQuizFinished.value = true
        }
    }

    fun resetTriviaGameState() {
        triviaCurrentQuestionIndex.value = 0
        triviaUserAnswers.value = emptyMap()
        triviaAnswerRevealed.value = emptyMap()
        triviaScore.value = 0
        triviaQuizFinished.value = false
    }

    fun loadTriviaPreset(subject: String, grade: String, persona: String) {
        triviaSubject.value = subject
        triviaGrade.value = grade
        triviaPersona.value = persona
    }

    // ---------------------------------------------------------------------------------------------
    // Direct Engine / JSON Playground
    // ---------------------------------------------------------------------------------------------
    fun executeApiTester() {
        viewModelScope.launch {
            apiTesterIsExecuting.value = true
            when (apiTesterSelectedModule.value) {
                EduModule.AI_GURU -> {
                    val req = AiGuruRequest(
                        teacherName = guruTeacherName.value,
                        subject = guruSubject.value,
                        keyTraits = guruKeyTraits.value,
                        language = guruLanguage.value,
                        tone = guruTone.value
                    )
                    when (val res = engine.processAiGuru(req)) {
                        is EngineOutput.Success -> apiTesterRawJson.value = res.rawJson
                        is EngineOutput.SafetyRejection -> apiTesterRawJson.value = res.rawJson
                        is EngineOutput.Failure -> apiTesterRawJson.value = "{\"error\": \"${res.errorMessage}\"}"
                    }
                }
                EduModule.VOICE_OF_GRATITUDE -> {
                    val req = VoiceGratitudeRequest(
                        studentName = voiceStudentName.value,
                        teacherName = voiceTeacherName.value,
                        rawMessage = voiceRawMessage.value,
                        targetLanguage = voiceTargetLanguage.value
                    )
                    when (val res = engine.processVoiceGratitude(req)) {
                        is EngineOutput.Success -> apiTesterRawJson.value = res.rawJson
                        is EngineOutput.SafetyRejection -> apiTesterRawJson.value = res.rawJson
                        is EngineOutput.Failure -> apiTesterRawJson.value = "{\"error\": \"${res.errorMessage}\"}"
                    }
                }
                EduModule.TRIVIA_BATTLE -> {
                    val req = TriviaBattleRequest(
                        subjectArea = triviaSubject.value,
                        gradeDepartment = triviaGrade.value,
                        personaArchetype = triviaPersona.value
                    )
                    when (val res = engine.processTriviaBattle(req)) {
                        is EngineOutput.Success -> apiTesterRawJson.value = res.rawJson
                        is EngineOutput.SafetyRejection -> apiTesterRawJson.value = res.rawJson
                        is EngineOutput.Failure -> apiTesterRawJson.value = "{\"error\": \"${res.errorMessage}\"}"
                    }
                }
                EduModule.API_ENGINE -> {}
            }
            apiTesterIsExecuting.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.release()
    }
}
