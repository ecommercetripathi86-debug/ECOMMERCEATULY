package com.example.data.engine

import com.example.BuildConfig
import com.example.data.api.GeminiApiClient
import com.example.data.api.GeminiContent
import com.example.data.api.GeminiGenerateRequest
import com.example.data.api.GeminiGenerationConfig
import com.example.data.api.GeminiPart
import com.example.data.model.AiGuruRequest
import com.example.data.model.AiGuruResponse
import com.example.data.model.PronunciationGuideItem
import com.example.data.model.SafetyRejectionResponse
import com.example.data.model.TriviaBattleRequest
import com.example.data.model.TriviaBattleResponse
import com.example.data.model.TriviaQuestion
import com.example.data.model.VoiceGratitudeRequest
import com.example.data.model.VoiceGratitudeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

sealed class EngineOutput<out T> {
    data class Success<T>(val data: T, val rawJson: String, val isAi: Boolean = true) : EngineOutput<T>()
    data class SafetyRejection(val reason: String, val politeAlternative: String, val rawJson: String) : EngineOutput<Nothing>()
    data class Failure(val errorMessage: String) : EngineOutput<Nothing>()
}

class EduTributeEngine {

    private val moshi = GeminiApiClient.moshi
    private val aiGuruAdapter = moshi.adapter(AiGuruResponse::class.java)
    private val voiceAdapter = moshi.adapter(VoiceGratitudeResponse::class.java)
    private val triviaAdapter = moshi.adapter(TriviaBattleResponse::class.java)
    private val safetyAdapter = moshi.adapter(SafetyRejectionResponse::class.java)

    private val systemInstruction = """
        You are the specialized AI Engine for 'EduTribute: Teachers' Day Special Web Platform'.
        Your purpose is to celebrate and honor educators, mentors, professors, school principals, and vice principals with heartfelt admiration, authentic intellectual warmth, and creative precision.
        
        CRITICAL SAFETY & DIGNITY RULES:
        1. Maintain the highest level of dignity, warmth, and respect for educators, principals, and vice principals.
        2. Reject any disrespectful, derogatory, bullying, mocking, or inappropriate prompts gracefully. If the input is disrespectful or trolling, output ONLY a JSON object with:
           {
             "status": "REJECTED_DISRESPECTFUL_CONTENT",
             "reason": "Input lacks the required dignity and respect for educators.",
             "politeAlternative": "<A respectful, constructive alternative message or tribute honoring the teacher's dedication>"
           }
        3. ALWAYS output clean, strictly valid JSON matching the requested module schema without markdown backticks or commentary.
    """.trimIndent()

    // ---------------------------------------------------------------------------------------------
    // MODULE 1: AI_GURU
    // ---------------------------------------------------------------------------------------------
    suspend fun processAiGuru(request: AiGuruRequest): EngineOutput<AiGuruResponse> = withContext(Dispatchers.IO) {
        if (isDisrespectful(request.teacherName, request.keyTraits)) {
            val polite = "Honoring ${request.teacherName.ifBlank { "our esteemed teacher" }} with immense gratitude for wisdom and patience."
            val rej = SafetyRejectionResponse(
                status = "REJECTED_DISRESPECTFUL_CONTENT",
                reason = "Input contained language that did not meet the dignity standards for educators.",
                politeAlternative = polite
            )
            return@withContext EngineOutput.SafetyRejection(rej.reason, rej.politeAlternative, safetyAdapter.toJson(rej))
        }

        val prompt = """
            Generate an appreciation card payload for MODULE: AI_GURU.
            Inputs:
            - Teacher Name: "${request.teacherName}"
            - Subject/Department: "${request.subject}"
            - Key Traits: "${request.keyTraits}"
            - Language: "${request.language}"
            - Tone: "${request.tone}"

            Requirements:
            1. Output MUST be valid JSON with this exact schema:
            {
              "module": "AI_GURU",
              "teacherName": "${request.teacherName}",
              "subject": "${request.subject}",
              "tone": "${request.tone}",
              "language": "${request.language}",
              "personalizedMessage": "A heartfelt, personalized appreciation message incorporating vivid subject-specific metaphors (e.g., calculus/limits/integrals for Math, Newton's laws/thermodynamics for Physics, chemical bonds/catalysts for Chemistry, syntax/algorithms for CS, Shakespearean sonnets for Literature, historical catalysts for History, leadership beacons for Principals).",
              "rhymingStanza": [
                "Line 1 (rhyming)",
                "Line 2 (rhyming)",
                "Line 3 (rhyming)",
                "Line 4 (rhyming)"
              ],
              "badgeNames": [
                "Badge 1 Name",
                "Badge 2 Name",
                "Badge 3 Name"
              ]
            }
        """.trimIndent()

        val apiKey = getApiKey()
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            val fallback = createFallbackAiGuru(request)
            return@withContext EngineOutput.Success(fallback, aiGuruAdapter.indent("  ").toJson(fallback), isAi = false)
        }

        try {
            val rawResponse = callGemini(prompt)
            val cleanJson = cleanJsonString(rawResponse)
            
            if (cleanJson.contains("REJECTED_DISRESPECTFUL_CONTENT")) {
                val rej = safetyAdapter.fromJson(cleanJson) ?: SafetyRejectionResponse(politeAlternative = "Honoring our educators with respect.")
                return@withContext EngineOutput.SafetyRejection(rej.reason, rej.politeAlternative, cleanJson)
            }

            val parsed = aiGuruAdapter.fromJson(cleanJson)
            if (parsed != null && parsed.personalizedMessage.isNotBlank()) {
                EngineOutput.Success(parsed, cleanJson, isAi = true)
            } else {
                val fallback = createFallbackAiGuru(request)
                EngineOutput.Success(fallback, aiGuruAdapter.indent("  ").toJson(fallback), isAi = false)
            }
        } catch (e: Exception) {
            val fallback = createFallbackAiGuru(request)
            EngineOutput.Success(fallback, aiGuruAdapter.indent("  ").toJson(fallback), isAi = false)
        }
    }

    // ---------------------------------------------------------------------------------------------
    // MODULE 2: VOICE_OF_GRATITUDE
    // ---------------------------------------------------------------------------------------------
    suspend fun processVoiceGratitude(request: VoiceGratitudeRequest): EngineOutput<VoiceGratitudeResponse> = withContext(Dispatchers.IO) {
        if (isDisrespectful(request.studentName, request.rawMessage)) {
            val polite = "Dear ${request.teacherName}, thank you for your patience, wisdom, and selfless guidance in my academic journey."
            val rej = SafetyRejectionResponse(
                status = "REJECTED_DISRESPECTFUL_CONTENT",
                reason = "Input contained inappropriate phrasing for a teacher tribute.",
                politeAlternative = polite
            )
            return@withContext EngineOutput.SafetyRejection(rej.reason, rej.politeAlternative, safetyAdapter.toJson(rej))
        }

        val prompt = """
            Generate an emotionally resonant tribute script for MODULE: VOICE_OF_GRATITUDE.
            Inputs:
            - Student Name: "${request.studentName}"
            - Teacher Name: "${request.teacherName}"
            - Raw Message: "${request.rawMessage}"
            - Target Language: "${request.targetLanguage}"

            Requirements:
            1. Optimize the script for Text-to-Speech (TTS) reading with clear pacing cues and pauses in square brackets like [pause 0.5s], [warm tone], [inspirational emphasis].
            2. Provide 2-3 pronunciation guides for key words or proper titles.
            3. Provide a 1-sentence English translation summary if the target language is not English, or a concise executive summary if English.
            4. Output MUST be valid JSON with this exact schema:
            {
              "module": "VOICE_OF_GRATITUDE",
              "studentName": "${request.studentName}",
              "teacherName": "${request.teacherName}",
              "targetLanguage": "${request.targetLanguage}",
              "ttsScript": "Full polished speech script in ${request.targetLanguage} formatted with pacing brackets...",
              "pronunciationGuides": [
                { "term": "...", "guide": "Phonetic / pronunciation tip" }
              ],
              "englishSummary": "1-sentence English summary...",
              "pacingMarkers": ["[pause 0.5s]", "[warm tone]", "[gentle emphasis]"]
            }
        """.trimIndent()

        val apiKey = getApiKey()
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            val fallback = createFallbackVoiceGratitude(request)
            return@withContext EngineOutput.Success(fallback, voiceAdapter.indent("  ").toJson(fallback), isAi = false)
        }

        try {
            val rawResponse = callGemini(prompt)
            val cleanJson = cleanJsonString(rawResponse)
            
            if (cleanJson.contains("REJECTED_DISRESPECTFUL_CONTENT")) {
                val rej = safetyAdapter.fromJson(cleanJson) ?: SafetyRejectionResponse(politeAlternative = "Thank you for being an inspiring teacher.")
                return@withContext EngineOutput.SafetyRejection(rej.reason, rej.politeAlternative, cleanJson)
            }

            val parsed = voiceAdapter.fromJson(cleanJson)
            if (parsed != null && parsed.ttsScript.isNotBlank()) {
                EngineOutput.Success(parsed, cleanJson, isAi = true)
            } else {
                val fallback = createFallbackVoiceGratitude(request)
                EngineOutput.Success(fallback, voiceAdapter.indent("  ").toJson(fallback), isAi = false)
            }
        } catch (e: Exception) {
            val fallback = createFallbackVoiceGratitude(request)
            EngineOutput.Success(fallback, voiceAdapter.indent("  ").toJson(fallback), isAi = false)
        }
    }

    // ---------------------------------------------------------------------------------------------
    // MODULE 3: TRIVIA_BATTLE
    // ---------------------------------------------------------------------------------------------
    suspend fun processTriviaBattle(request: TriviaBattleRequest): EngineOutput<TriviaBattleResponse> = withContext(Dispatchers.IO) {
        if (isDisrespectful(request.personaArchetype, request.subjectArea)) {
            val polite = "The Wisdom Sage - Master of Intellectual Guidance"
            val rej = SafetyRejectionResponse(
                status = "REJECTED_DISRESPECTFUL_CONTENT",
                reason = "Persona description was inappropriate for an educator tribute game.",
                politeAlternative = polite
            )
            return@withContext EngineOutput.SafetyRejection(rej.reason, rej.politeAlternative, safetyAdapter.toJson(rej))
        }

        val prompt = """
            Generate an engaging Teacher Persona & Quiz payload for MODULE: TRIVIA_BATTLE.
            Inputs:
            - Subject Area: "${request.subjectArea}"
            - Grade/Department: "${request.gradeDepartment}"
            - Teacher Persona Archetype: "${request.personaArchetype}"

            Requirements:
            1. Output 3 respectful, intellectually stimulating, and fun multiple-choice trivia questions tailored to this subject area and persona archetype.
            2. Each question has 4 distinct options, a correctOptionIndex (0, 1, 2, or 3), an insightful educational explanation, and a witty in-character personaComment.
            3. Provide an honorable digital badge title (e.g., 'Master of the Quantum Matrix', 'Honorary Calculus Connoisseur', 'Grand Scribe of History').
            4. Output MUST be valid JSON with this exact schema:
            {
              "module": "TRIVIA_BATTLE",
              "subjectArea": "${request.subjectArea}",
              "gradeDepartment": "${request.gradeDepartment}",
              "personaArchetype": "${request.personaArchetype}",
              "digitalBadgeTitle": "...",
              "questions": [
                {
                  "id": 1,
                  "question": "...",
                  "options": ["A", "B", "C", "D"],
                  "correctOptionIndex": 0,
                  "explanation": "...",
                  "personaComment": "..."
                },
                {
                  "id": 2,
                  "question": "...",
                  "options": ["A", "B", "C", "D"],
                  "correctOptionIndex": 1,
                  "explanation": "...",
                  "personaComment": "..."
                },
                {
                  "id": 3,
                  "question": "...",
                  "options": ["A", "B", "C", "D"],
                  "correctOptionIndex": 2,
                  "explanation": "...",
                  "personaComment": "..."
                }
              ]
            }
        """.trimIndent()

        val apiKey = getApiKey()
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            val fallback = createFallbackTriviaBattle(request)
            return@withContext EngineOutput.Success(fallback, triviaAdapter.indent("  ").toJson(fallback), isAi = false)
        }

        try {
            val rawResponse = callGemini(prompt)
            val cleanJson = cleanJsonString(rawResponse)
            
            if (cleanJson.contains("REJECTED_DISRESPECTFUL_CONTENT")) {
                val rej = safetyAdapter.fromJson(cleanJson) ?: SafetyRejectionResponse(politeAlternative = "The Wisdom Sage Persona")
                return@withContext EngineOutput.SafetyRejection(rej.reason, rej.politeAlternative, cleanJson)
            }

            val parsed = triviaAdapter.fromJson(cleanJson)
            if (parsed != null && parsed.questions.isNotEmpty()) {
                EngineOutput.Success(parsed, cleanJson, isAi = true)
            } else {
                val fallback = createFallbackTriviaBattle(request)
                EngineOutput.Success(fallback, triviaAdapter.indent("  ").toJson(fallback), isAi = false)
            }
        } catch (e: Exception) {
            val fallback = createFallbackTriviaBattle(request)
            EngineOutput.Success(fallback, triviaAdapter.indent("  ").toJson(fallback), isAi = false)
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Helper & Fallback Generators
    // ---------------------------------------------------------------------------------------------
    private fun getApiKey(): String = try {
        BuildConfig.GEMINI_API_KEY
    } catch (_: Exception) {
        ""
    }

    private suspend fun callGemini(prompt: String): String {
        val request = GeminiGenerateRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(GeminiPart(text = prompt)),
                    role = "user"
                )
            ),
            generationConfig = GeminiGenerationConfig(
                temperature = 0.7f,
                responseMimeType = "application/json"
            ),
            systemInstruction = GeminiContent(
                parts = listOf(GeminiPart(text = systemInstruction))
            )
        )
        val response = GeminiApiClient.service.generateContent(getApiKey(), request)
        return response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: throw IllegalStateException("Empty response from Gemini API")
    }

    private fun cleanJsonString(raw: String): String {
        var str = raw.trim()
        if (str.startsWith("```json")) {
            str = str.removePrefix("```json")
        } else if (str.startsWith("```")) {
            str = str.removePrefix("```")
        }
        if (str.endsWith("```")) {
            str = str.removeSuffix("```")
        }
        return str.trim()
    }

    private fun isDisrespectful(vararg texts: String): Boolean {
        val forbidden = listOf(
            "hate", "stupid", "idiot", "dumb", "useless", "worst teacher", "lazy",
            "ugly", "kill", "harass", "abuse", "scam", "fraud"
        )
        return texts.any { text ->
            val lower = text.lowercase(Locale.ROOT)
            forbidden.any { badWord -> lower.contains(badWord) }
        }
    }

    fun createFallbackAiGuru(request: AiGuruRequest): AiGuruResponse {
        val teacher = request.teacherName.ifBlank { "Esteemed Teacher" }
        val subject = request.subject.ifBlank { "General Education" }
        val traits = request.keyTraits.ifBlank { "Inspiring, dedicated, and patient" }

        val (metaphorMsg, poem, badges) = when {
            subject.contains("Math", ignoreCase = true) -> Triple(
                "Dear $teacher, in a world full of complex variables and perplexing problems, you have always been the constant that provides clarity. Just as calculus finds harmony in change and limits conquer the infinite, your guidance has integrated our potential and multiplied our curiosity. Your $traits illuminate every theorem of life.",
                listOf(
                    "You charted slopes where doubts once grew,",
                    "And made the toughest proofs ring true,",
                    "With limits bridged and sine curves clear,",
                    "You taught us hope throughout the year."
                ),
                listOf("Master of the Integral", "Infinite Patience Laureate", "Theorem Pioneer")
            )
            subject.contains("Physic", ignoreCase = true) -> Triple(
                "Dear $teacher, like Newton's laws governing momentum, your boundless energy creates an equal and profound inspiration in every student. You taught us that light refracts through challenges to reveal a vibrant spectrum of discovery. Thank you for anchoring us with your $traits.",
                listOf(
                    "With optics bright and vectors bold,",
                    "You turned dry laws to living gold,",
                    "No gravity can hold us down,",
                    "With knowledge as our brightest crown."
                ),
                listOf("Quantum Catalyst", "Beacon of Absolute Zero Doubt", "Relativity Champion")
            )
            subject.contains("Chem", ignoreCase = true) -> Triple(
                "Dear $teacher, in the laboratory of learning, you have been the ultimate catalyst—lowering our activation energy for curiosity and forging unbreakable covalent bonds of wisdom. Your $traits turn every volatile challenge into an enduring solution.",
                listOf(
                    "You sparked the flame of keen desire,",
                    "And set our questioning minds on fire,",
                    "In every bond and formula spun,",
                    "You proved that chemistry is fun."
                ),
                listOf("Master Catalyst", "Noble Element of Wisdom", "Alchemist of Potential")
            )
            subject.contains("Computer", ignoreCase = true) || subject.contains("Code", ignoreCase = true) -> Triple(
                "Dear $teacher, you helped us debug our self-doubts and compile a future of infinite possibilities. Your $traits taught us that while algorithms require logic, true mentorship requires heart, empathy, and graceful error-handling.",
                listOf(
                    "Through syntax bugs and loops that spun,",
                    "You taught till every test had won,",
                    "No stack overflow could break the line,",
                    "Your algorithm of care was fine."
                ),
                listOf("Grand Debugger", "Kernel Architect", "Syntax & Soul Maestro")
            )
            subject.contains("Literature", ignoreCase = true) || subject.contains("English", ignoreCase = true) || subject.contains("Language", ignoreCase = true) -> Triple(
                "Dear $teacher, with Shakespearean depth and poetic grace, you unlocked the transformative power of language for us. Through the prose of life, your $traits have written chapters of courage, eloquence, and boundless empathy in our hearts.",
                listOf(
                    "You opened worlds on printed pages,",
                    "With timeless wit from ancient sages,",
                    "Through every stanza, verse, and rhyme,",
                    "Your mentorship outlasts all time."
                ),
                listOf("Keeper of the Muse", "Literary Luminary", "Master of Metaphors")
            )
            subject.contains("Principal", ignoreCase = true) || subject.contains("Leadership", ignoreCase = true) -> Triple(
                "Dear $teacher, as the compass and cornerstone of our institution, your visionary leadership steers our entire academic community toward excellence. Your $traits provide a safe harbor for innovation, character, and lifelong learning.",
                listOf(
                    "With steady hand and noble grace,",
                    "You made our school a sacred place,",
                    "A guiding light for young and old,",
                    "Whose legacy is spun in gold."
                ),
                listOf("Pillar of Excellence", "Visionary Helmsman", "Guardian of Culture")
            )
            else -> Triple(
                "Dear $teacher, in the great curriculum of life, your guidance has been our master textbook and greatest inspiration. Through your $traits, you have shaped not just our understanding of $subject, but our character and confidence for the world ahead.",
                listOf(
                    "You planted seeds in fertile ground,",
                    "Where wisdom and great hope are found,",
                    "With selfless care both day and night,",
                    "You led our steps into the light."
                ),
                listOf("Torchbearer of Wisdom", "Inspirational Luminary", "Mentor of the Mind")
            )
        }

        return AiGuruResponse(
            module = "AI_GURU",
            teacherName = teacher,
            subject = subject,
            tone = request.tone,
            language = request.language,
            personalizedMessage = metaphorMsg,
            rhymingStanza = poem,
            badgeNames = badges
        )
    }

    fun createFallbackVoiceGratitude(request: VoiceGratitudeRequest): VoiceGratitudeResponse {
        val student = request.studentName.ifBlank { "Grateful Student" }
        val teacher = request.teacherName.ifBlank { "Dear Teacher" }
        val lang = request.targetLanguage.ifBlank { "English" }
        val rawMsg = request.rawMessage.ifBlank { "Thank you for always believing in me and showing me the right path." }

        val (script, guides, summary) = when {
            lang.contains("Hindi", ignoreCase = true) || lang.contains("हिंदी") -> Triple(
                "[warm tone] आदरणीय $teacher जी, [pause 0.6s] मैं, $student, आज शिक्षक दिवस के पावन अवसर पर आपके चरणों में सादर प्रणाम अर्पित करता हूँ। [inspirational pause 0.8s] $rawMsg [pause 0.5s] आपने केवल पुस्तकें नहीं पढ़ाईं, बल्कि जीवन को सकारात्मक दृष्टि से जीना सिखाया। [gentle emphasis] आपकी कृपा और प्रेरणा के लिए हृदय से कोटि-कोटि धन्यवाद।",
                listOf(
                    PronunciationGuideItem("आदरणीय (Aadarniya)", "Aah-dar-nee-ya (Respected/Honorable)"),
                    PronunciationGuideItem("प्रणाम (Pranaam)", "Pruh-naam (Respectful greeting/bow)"),
                    PronunciationGuideItem("शिक्षक (Shikshak)", "Shik-shuk (Teacher/Educator)")
                ),
                "A heartfelt Hindi speech expressing deep reverence and gratitude from $student to $teacher for transforming life through patience and knowledge."
            )
            lang.contains("Spanish", ignoreCase = true) || lang.contains("Español") -> Triple(
                "[warm tone] Querido(a) profesor(a) $teacher, [pause 0.6s] habla $student. En este Día del Maestro, quiero expresar mi más sincero agradecimiento. [inspirational pause 0.8s] $rawMsg [pause 0.5s] Gracias por encender en nosotros la llama del conocimiento y la curiosidad. [gentle emphasis] ¡Su dedicación deja una huella imborrable en nuestras vidas!",
                listOf(
                    PronunciationGuideItem("Maestro", "Mah-ES-troh (Teacher/Master)"),
                    PronunciationGuideItem("Agradecimiento", "Ah-grah-deh-see-mee-EN-toh (Gratitude)")
                ),
                "An affectionate Spanish tribute from $student honoring $teacher's dedication and lasting intellectual legacy."
            )
            lang.contains("French", ignoreCase = true) || lang.contains("Français") -> Triple(
                "[warm tone] Cher(e) Professeur $teacher, [pause 0.6s] c'est $student qui vous adresse ces quelques mots chaleureux. [inspirational pause 0.8s] $rawMsg [pause 0.5s] Votre passion pour l'enseignement et votre bienveillance ont éclairé notre chemin. [gentle emphasis] Merci du fond du cœur pour votre dévouement exceptionnel.",
                listOf(
                    PronunciationGuideItem("Bienveillance", "Byen-vay-yahns (Kindness/Benevolence)"),
                    PronunciationGuideItem("Dévouement", "Day-voo-mahn (Dedication)")
                ),
                "An elegant French tribute honoring $teacher's wisdom and benevolent teaching from $student."
            )
            lang.contains("German", ignoreCase = true) || lang.contains("Deutsch") -> Triple(
                "[warm tone] Sehr geehrte(r) Lehrer(in) $teacher, [pause 0.6s] hier spricht $student. Zum Ehrentag der Lehrkräfte möchte ich mich von ganzem Herzen bedanken. [inspirational pause 0.8s] $rawMsg [pause 0.5s] Ihre Geduld, Hingabe und Begeisterung für das Lernen haben uns nachhaltig geprägt. [gentle emphasis] Vielen Dank für Ihre unschätzbare Unterstützung.",
                listOf(
                    PronunciationGuideItem("Sehr geehrte(r)", "Zair geh-AIR-teh (Highly respected)"),
                    PronunciationGuideItem("Dankbarkeit", "DAHNK-bar-kite (Gratitude)")
                ),
                "A heartfelt German speech honoring $teacher's mentorship and inspiring educational guidance."
            )
            lang.contains("Japanese", ignoreCase = true) || lang.contains("日本語") -> Triple(
                "[warm tone] $teacher 先生、[pause 0.6s] $student です。日頃の温かいご指導に心より感謝申し上げます。[inspirational pause 0.8s] $rawMsg [pause 0.5s] 先生が教えてくださった学びへの情熱と優しさは、私の人生の道標です。[gentle emphasis] 本当にありがとうございました。",
                listOf(
                    PronunciationGuideItem("先生 (Sensei)", "Sen-say (Teacher/Mentor)"),
                    PronunciationGuideItem("感謝 (Kansha)", "Kahn-shah (Deep gratitude)")
                ),
                "A polite and deeply respectful Japanese tribute from $student expressing gratitude to $teacher for guiding light and encouragement."
            )
            lang.contains("Italian", ignoreCase = true) || lang.contains("Italiano") -> Triple(
                "[warm tone] Gentile Professore $teacher, [pause 0.6s] sono $student. In questo giorno speciale, desidero ringraziarla per tutto ciò che mi ha insegnato. [inspirational pause 0.8s] $rawMsg [pause 0.5s] La sua passione e la sua pazienza rimarranno sempre una guida preziosa per me. [gentle emphasis] Grazie di cuore!",
                listOf(
                    PronunciationGuideItem("Ringraziamento", "Reen-grah-tsyah-MEN-toh (Thank you/Gratitude)"),
                    PronunciationGuideItem("Maestro", "Mah-ES-tro (Teacher)")
                ),
                "A passionate Italian tribute from $student thanking $teacher for unwavering patience and inspiring knowledge."
            )
            lang.contains("Portuguese", ignoreCase = true) || lang.contains("Português") -> Triple(
                "[warm tone] Querido(a) professor(a) $teacher, [pause 0.6s] aqui é o(a) $student. No Dia dos Professores, quero expressar toda a minha gratidão. [inspirational pause 0.8s] $rawMsg [pause 0.5s] O seu apoio constante e sua dedicação nos inspiram a alcançar grandes conquistas. [gentle emphasis] Muito obrigado(a) por tudo!",
                listOf(
                    PronunciationGuideItem("Gratidão", "Grah-chee-DOW (Deep gratitude)"),
                    PronunciationGuideItem("Professor", "Pro-feh-SOR (Teacher)")
                ),
                "A warm Portuguese tribute from $student honoring $teacher's dedication and guidance."
            )
            lang.contains("Mandarin", ignoreCase = true) || lang.contains("Chinese") || lang.contains("中文") -> Triple(
                "[warm tone] 亲爱的 $teacher 老师，[pause 0.6s] 我是 $student。在教师节之际，我怀着无比崇敬的心情向您致以诚挚的感谢。[inspirational pause 0.8s] $rawMsg [pause 0.5s] 感谢您如明灯般照亮我们的求学之路，春风化雨，润物无声。[gentle emphasis] 祝您教师节快乐，桃李满天下！",
                listOf(
                    PronunciationGuideItem("老师 (Lǎoshī)", "Low-shrr (Teacher)"),
                    PronunciationGuideItem("感谢 (Gǎnxiè)", "Gahn-shyeh (Thank you)")
                ),
                "A poetic Chinese tribute celebrating $teacher's wisdom, nurturing spirit, and profound impact on $student."
            )
            lang.contains("Arabic", ignoreCase = true) || lang.contains("العربية") -> Triple(
                "[warm tone] أستاذي الفاضل $teacher، [pause 0.6s] يتحدث $student معبرًا عن أسمى آيات الشكر والتقدير. [inspirational pause 0.8s] $rawMsg [pause 0.5s] شكرًا لك على زرع بذور العلم والمعرفة في قلوبنا وعقولنا. [gentle emphasis] دمت منارة تضيء طريق النجاح دائمًا.",
                listOf(
                    PronunciationGuideItem("أستاذي (Ustādhi)", "Oos-taa-zee (My Teacher)"),
                    PronunciationGuideItem("شكراً (Shukran)", "Shook-ran (Thank you)")
                ),
                "A noble Arabic tribute expressing reverence and gratitude to $teacher for illuminating the path of knowledge."
            )
            lang.contains("Bengali", ignoreCase = true) || lang.contains("বাংলা") -> Triple(
                "[warm tone] শ্রদ্ধেয় $teacher মহাশয়, [pause 0.6s] আমি $student, শিক্ষক দিবসের এই শুভক্ষণে আপনার চরণে বিনম্র শ্রদ্ধা জানাই। [inspirational pause 0.8s] $rawMsg [pause 0.5s] আপনার মূল্যবান শিক্ষা ও স্নেহ আমার জীবনের শ্রেষ্ঠ পাথেয়। [gentle emphasis] আপনাকে জানাই আন্তরিক কৃতজ্ঞতা ও প্রণাম।",
                listOf(
                    PronunciationGuideItem("শ্রদ্ধেয় (Sraddheyo)", "Shrod-dhe-yo (Respected)"),
                    PronunciationGuideItem("প্রণাম (Pronam)", "Pro-naam (Respectful salutation)")
                ),
                "A soulful Bengali tribute paying reverence to $teacher for noble guidance and lifelong lessons."
            )
            lang.contains("Tamil", ignoreCase = true) || lang.contains("தமிழ்") -> Triple(
                "[warm tone] வணக்கத்திற்குரிய $teacher ஆசிரியர் அவர்களே, [pause 0.6s] நான் $student பேசுகிறேன். ஆசிரியர் தினத்தில் எனது மனமார்ந்த நன்றிகளைத் தெரிவித்துக் கொள்கிறேன். [inspirational pause 0.8s] $rawMsg [pause 0.5s] உங்கள் அன்பும் வழிகாட்டலும் என்றும் எனது வாழ்க்கைக்கு ஒளி தரும். [gentle emphasis] தங்களுக்கு என் சிரம் தாழ்ந்த வணக்கங்கள்.",
                listOf(
                    PronunciationGuideItem("ஆசிரியர் (Aasiriyar)", "Aa-si-ri-yar (Teacher)"),
                    PronunciationGuideItem("வணக்கம் (Vanakkam)", "Vah-nah-kahm (Respectful greeting)")
                ),
                "A respectful Tamil tribute expressing deep devotion and thanks to $teacher on Teachers' Day."
            )
            lang.contains("Telugu", ignoreCase = true) || lang.contains("తెలుగు") -> Triple(
                "[warm tone] పూజ్యనీయులైన $teacher గురువుగారికి, [pause 0.6s] నేను $student. ఉపాధ్యాయ దినోత్సవం సందర్భంగా మీకు హృదయపూర్వక కృతజ్ఞతలు తెలుపుకుంటున్నాను. [inspirational pause 0.8s] $rawMsg [pause 0.5s] మీ మార్గదర్శకత్వం నా భవిష్యత్తుకు వెలుగునిచ్చింది. [gentle emphasis] మీకు నా పాదాభివందనాలు.",
                listOf(
                    PronunciationGuideItem("గురువుగారు (Guruvugaaru)", "Goo-roo-voo-gaa-roo (Respected Teacher)"),
                    PronunciationGuideItem("కృతజ్ఞతలు (Krutajnatalu)", "Kroo-tuh-jnyuh-tuh-loo (Gratitude)")
                ),
                "A reverent Telugu tribute expressing profound gratitude to $teacher for enlightening guidance."
            )
            lang.contains("Marathi", ignoreCase = true) || lang.contains("मराठी") -> Triple(
                "[warm tone] आदरणीय $teacher सर/मॅडम, [pause 0.6s] मी $student, शिक्षक दिनानिमित्त आपल्या चरणी कोटी कोटी प्रणाम व्यक्त करतो. [inspirational pause 0.8s] $rawMsg [pause 0.5s] आपण दिलेल्या ज्ञानामुळे आणि संस्कारांमुळे आमचे जीवन समृद्ध झाले आहे. [gentle emphasis] आपल्या मार्गदर्शनाबद्दल मनःपूर्वक धन्यवाद!",
                listOf(
                    PronunciationGuideItem("आदरणीय (Aadarniya)", "Aa-dar-nee-ya (Respected)"),
                    PronunciationGuideItem("प्रणाम (Pranam)", "Pruh-naam (Salutation)")
                ),
                "A respectful Marathi tribute honoring $teacher for wisdom, patience, and character building."
            )
            lang.contains("Russian", ignoreCase = true) || lang.contains("Русский") -> Triple(
                "[warm tone] Дорогой(ая) учитель $teacher, [pause 0.6s] говорит $student. В этот праздничный день хочу выразить вам самую искреннюю благодарность. [inspirational pause 0.8s] $rawMsg [pause 0.5s] Ваша мудрость, терпение и вера в нас открыли путь к новым знаниям. [gentle emphasis] Спасибо вам от всего сердца!",
                listOf(
                    PronunciationGuideItem("Учитель (Uchitel)", "Oo-chee-tyel (Teacher)"),
                    PronunciationGuideItem("Благодарность (Blagodarnost)", "Blah-guh-DAR-nust (Gratitude)")
                ),
                "A warm and sincere Russian tribute from $student expressing deep appreciation for $teacher's dedication."
            )
            else -> Triple(
                "[warm tone] Dear $teacher, [pause 0.6s] this is $student speaking with profound gratitude on Teachers' Day. [inspirational pause 0.8s] $rawMsg [pause 0.5s] Beyond textbooks and exams, you taught us resilience, intellectual courage, and curiosity. [gentle emphasis] Thank you for being our constant beacon of light and mentorship.",
                listOf(
                    PronunciationGuideItem("Gratitude", "GRA-ti-tyood (Heartfelt thankfulness)"),
                    PronunciationGuideItem("Mentorship", "MEN-tor-ship (Guiding wisdom)")
                ),
                "A sincere tribute from $student expressing deep gratitude to $teacher for academic excellence and personal mentorship."
            )
        }

        return VoiceGratitudeResponse(
            module = "VOICE_OF_GRATITUDE",
            studentName = student,
            teacherName = teacher,
            targetLanguage = lang,
            ttsScript = script,
            pronunciationGuides = guides,
            englishSummary = summary,
            pacingMarkers = listOf("[pause 0.6s]", "[inspirational pause 0.8s]", "[warm tone]", "[gentle emphasis]")
        )
    }

    fun createFallbackTriviaBattle(request: TriviaBattleRequest): TriviaBattleResponse {
        val subject = request.subjectArea.ifBlank { "Science & Mathematics" }
        val grade = request.gradeDepartment.ifBlank { "High School" }
        val persona = request.personaArchetype.ifBlank { "The Homework Detective" }

        val (badge, qList) = when {
            subject.contains("Math", ignoreCase = true) -> Pair(
                "Grand Sovereign of Quadratic Realms",
                listOf(
                    TriviaQuestion(
                        id = 1,
                        question = "Which ancient mathematical sequence describes the spiral pattern found in pinecones, sunflowers, and galaxy arms?",
                        options = listOf("The Fibonacci Sequence", "The Pascal Matrix", "The Fourier Transform", "The Euler Identity"),
                        correctOptionIndex = 0,
                        explanation = "The Fibonacci sequence (0, 1, 1, 2, 3, 5, 8, 13...) generates the Golden Ratio (phi ≈ 1.618), ubiquitous in natural geometry.",
                        personaComment = "$persona says: 'No calculators needed here—nature had this solved billions of years before your exam!'"
                    ),
                    TriviaQuestion(
                        id = 2,
                        question = "What is the only even prime number in all of mathematics?",
                        options = listOf("0", "2", "4", "6"),
                        correctOptionIndex = 1,
                        explanation = "2 is the only even prime number because all other even numbers are divisible by 2.",
                        personaComment = "$persona says: 'A classic test trap! Always look out for the number two!'"
                    ),
                    TriviaQuestion(
                        id = 3,
                        question = "Who is credited with co-discovering calculus alongside Isaac Newton in the late 17th century?",
                        options = listOf("Gottfried Wilhelm Leibniz", "René Descartes", "Carl Friedrich Gauss", "Blaise Pascal"),
                        correctOptionIndex = 0,
                        explanation = "Leibniz developed calculus independently and gave us the integral (∫) and derivative (dx/dt) notations we use today.",
                        personaComment = "$persona says: 'Remember to show your work—and give Leibniz credit for the notation!'"
                    )
                )
            )
            subject.contains("Physic", ignoreCase = true) || subject.contains("Scien", ignoreCase = true) -> Pair(
                "Quantum Maestro of the Cosmos",
                listOf(
                    TriviaQuestion(
                        id = 1,
                        question = "What fundamental physical constant denotes the speed of light in a vacuum?",
                        options = listOf("c ≈ 3 x 10^8 m/s", "G ≈ 6.67 x 10^-11 N m^2/kg^2", "h ≈ 6.626 x 10^-34 J s", "k_B ≈ 1.38 x 10^-23 J/K"),
                        correctOptionIndex = 0,
                        explanation = "The speed of light in vacuum is approximately 299,792,458 m/s, represented by the constant 'c'.",
                        personaComment = "$persona smiles: 'Nothing moves faster than light, except perhaps a student packing up when the bell rings!'"
                    ),
                    TriviaQuestion(
                        id = 2,
                        question = "Which phenomenon describes the change in observed frequency of a wave when the source and observer are moving relative to each other?",
                        options = listOf("The Doppler Effect", "Photoelectric Effect", "Compton Scattering", "Bernoulli Principle"),
                        correctOptionIndex = 0,
                        explanation = "The Doppler effect causes siren pitches to shift as an ambulance approaches and moves away.",
                        personaComment = "$persona notes: 'Just like the pitch of my voice when I ask where the homework is!'"
                    ),
                    TriviaQuestion(
                        id = 3,
                        question = "What property of matter causes an object to resist changes in its state of motion?",
                        options = listOf("Inertia", "Enthalpy", "Entropy", "Capacitance"),
                        correctOptionIndex = 0,
                        explanation = "Inertia is Newton's First Law in action: an object remains at rest or constant velocity unless acted on by an external force.",
                        personaComment = "$persona quips: 'Monday morning inertia is real, but physics conquers all!'"
                    )
                )
            )
            subject.contains("Hist", ignoreCase = true) || subject.contains("Social", ignoreCase = true) -> Pair(
                "Chronicler of Millennia",
                listOf(
                    TriviaQuestion(
                        id = 1,
                        question = "Which ancient wonder was the tallest man-made structure in the world for over 3,800 years?",
                        options = listOf("The Great Pyramid of Giza", "The Colossus of Rhodes", "The Lighthouse of Alexandria", "The Hanging Gardens of Babylon"),
                        correctOptionIndex = 0,
                        explanation = "Built around 2560 BC for Pharaoh Khufu, the Great Pyramid held the height record until Lincoln Cathedral in 1311 AD.",
                        personaComment = "$persona observes: 'Historical foundations stand the test of time, just like foundational study habits!'"
                    ),
                    TriviaQuestion(
                        id = 2,
                        question = "Which invention in 1440 revolutionized the spread of knowledge across the European continent?",
                        options = listOf("Gutenberg's Movable Type Printing Press", "The Magnetic Compass", "The Mechanical Pendulum Clock", "The Astrolabe"),
                        correctOptionIndex = 0,
                        explanation = "Johannes Gutenberg's printing press democratized literacy, books, and scientific discourse.",
                        personaComment = "$persona remarks: 'The greatest knowledge multiplier in human history—cherish every book you read!'"
                    ),
                    TriviaQuestion(
                        id = 3,
                        question = "In India, Teachers' Day on September 5th commemorates the birthday of which philosopher and former President?",
                        options = listOf("Dr. Sarvepalli Radhakrishnan", "Dr. A.P.J. Abdul Kalam", "Dr. B.R. Ambedkar", "Rabindranath Tagore"),
                        correctOptionIndex = 0,
                        explanation = "Dr. S. Radhakrishnan requested that his birthday be observed as Teachers' Day to honor all educators nationwide.",
                        personaComment = "$persona beams: 'A true statesman who believed teachers are the architects of the nation!'"
                    )
                )
            )
            else -> Pair(
                "Champion of Intellectual Discovery",
                listOf(
                    TriviaQuestion(
                        id = 1,
                        question = "What is the primary organelle responsible for producing cellular energy (ATP)?",
                        options = listOf("Mitochondria", "Ribosome", "Endoplasmic Reticulum", "Golgi Apparatus"),
                        correctOptionIndex = 0,
                        explanation = "Mitochondria are famously termed the 'powerhouses of the cell' for aerobic cellular respiration.",
                        personaComment = "$persona smiles: 'The single most famous biology meme of all time, and scientifically accurate!'"
                    ),
                    TriviaQuestion(
                        id = 2,
                        question = "Who wrote the famous quote 'Education is the most powerful weapon which you can use to change the world'?",
                        options = listOf("Nelson Mandela", "Albert Einstein", "Mahatma Gandhi", "Marie Curie"),
                        correctOptionIndex = 0,
                        explanation = "Nelson Mandela emphasized education as the cornerstone of human freedom and global transformation.",
                        personaComment = "$persona nods: 'A timeless quote that defines the sacred calling of every teacher.'",
                    ),
                    TriviaQuestion(
                        id = 3,
                        question = "Which atmospheric layer protects life on Earth by absorbing ultraviolet (UV) radiation from the Sun?",
                        options = listOf("The Stratosphere (Ozone Layer)", "The Troposphere", "The Mesosphere", "The Thermosphere"),
                        correctOptionIndex = 0,
                        explanation = "The ozone layer within the stratosphere absorbs between 97% and 99% of the Sun's medium-frequency UV light.",
                        personaComment = "$persona observes: 'Just as the ozone protects Earth, a teacher's guidance shields students from doubt!'"
                    )
                )
            )
        }

        return TriviaBattleResponse(
            module = "TRIVIA_BATTLE",
            subjectArea = subject,
            gradeDepartment = grade,
            personaArchetype = persona,
            digitalBadgeTitle = badge,
            questions = qList
        )
    }

    fun formatGuruOutputJson(response: AiGuruResponse): String {
        return aiGuruAdapter.indent("  ").toJson(response)
    }
}
