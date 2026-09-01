package com.example.data.engine

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class TtsManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    private val _currentSpokenPhrase = MutableStateFlow("")
    val currentSpokenPhrase: StateFlow<String> = _currentSpokenPhrase.asStateFlow()

    private val _speechProgress = MutableStateFlow(0f) // 0.0 to 1.0
    val speechProgress: StateFlow<Float> = _speechProgress.asStateFlow()

    private var activeSpeechText: String = ""

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            _isReady.value = true
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isPlaying.value = true
                    _speechProgress.value = 0.05f
                }

                override fun onDone(utteranceId: String?) {
                    _isPlaying.value = false
                    _currentSpokenPhrase.value = ""
                    _speechProgress.value = 1.0f
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isPlaying.value = false
                    _currentSpokenPhrase.value = ""
                    _speechProgress.value = 0f
                }

                override fun onRangeStart(utteranceId: String?, start: Int, end: Int, frame: Int) {
                    if (activeSpeechText.isNotEmpty() && start >= 0 && end <= activeSpeechText.length && start < end) {
                        _currentSpokenPhrase.value = activeSpeechText.substring(start, end).trim()
                        _speechProgress.value = (end.toFloat() / activeSpeechText.length.toFloat()).coerceIn(0.05f, 0.98f)
                    }
                }
            })
        }
    }

    fun speak(text: String, languageCode: String = "en", pitch: Float = 1.0f, speed: Float = 1.0f) {
        if (!isInitialized || tts == null) return

        // Clean out pacing brackets like [pause 0.5s], [warm tone] for natural cadence
        val cleanedText = cleanTextForSynthesis(text)
        activeSpeechText = cleanedText

        val locale = mapLanguageToLocale(languageCode)

        try {
            val result = tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to English US if specific accent is missing
                tts?.language = Locale.US
            }
        } catch (_: Exception) {
            tts?.language = Locale.US
        }

        tts?.setPitch(pitch.coerceIn(0.5f, 2.0f))
        tts?.setSpeechRate(speed.coerceIn(0.5f, 2.0f))

        _isPlaying.value = true
        _speechProgress.value = 0.05f
        tts?.speak(cleanedText, TextToSpeech.QUEUE_FLUSH, null, "edutribute_utterance_${System.currentTimeMillis()}")
    }

    private fun cleanTextForSynthesis(text: String): String {
        return text
            .replace(Regex("\\[pause\\s*([0-9.]+s?)?\\]", RegexOption.IGNORE_CASE), " ... ")
            .replace(Regex("\\[[^\\]]+\\]"), "")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    private fun mapLanguageToLocale(language: String): Locale {
        val l = language.lowercase(Locale.getDefault())
        return when {
            l.contains("hindi") || l.contains("हिंदी") -> Locale("hi", "IN")
            l.contains("spanish") || l.contains("español") -> Locale("es", "ES")
            l.contains("french") || l.contains("français") -> Locale.FRENCH
            l.contains("german") || l.contains("deutsch") -> Locale.GERMAN
            l.contains("japanese") || l.contains("日本語") -> Locale.JAPANESE
            l.contains("italian") || l.contains("italiano") -> Locale.ITALIAN
            l.contains("portuguese") || l.contains("português") -> Locale("pt", "BR")
            l.contains("russian") || l.contains("русский") -> Locale("ru", "RU")
            l.contains("chinese") || l.contains("mandarin") || l.contains("中文") -> Locale.CHINESE
            l.contains("arabic") || l.contains("العربية") -> Locale("ar", "SA")
            l.contains("bengali") || l.contains("বাংলা") -> Locale("bn", "IN")
            l.contains("tamil") || l.contains("தமிழ்") -> Locale("ta", "IN")
            l.contains("telugu") || l.contains("తెలుగు") -> Locale("te", "IN")
            l.contains("marathi") || l.contains("मराठी") -> Locale("mr", "IN")
            l.contains("korean") || l.contains("한국어") -> Locale.KOREAN
            l.contains("uk") || l.contains("british") -> Locale.UK
            l.contains("indian english") -> Locale("en", "IN")
            else -> Locale.US
        }
    }

    fun stop() {
        tts?.stop()
        _isPlaying.value = false
        _currentSpokenPhrase.value = ""
        _speechProgress.value = 0f
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
        _isPlaying.value = false
        _isReady.value = false
        _speechProgress.value = 0f
    }
}
