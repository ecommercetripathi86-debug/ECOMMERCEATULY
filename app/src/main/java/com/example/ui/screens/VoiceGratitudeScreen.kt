package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EngineUiState
import com.example.data.model.VoiceGratitudeResponse
import com.example.ui.components.JsonViewerDialog
import com.example.ui.components.LoadingCard
import com.example.ui.components.SafetyRejectionCard
import com.example.ui.theme.SophisticatedBackground
import com.example.ui.theme.SophisticatedGlassBorder
import com.example.ui.theme.SophisticatedGold
import com.example.ui.theme.SophisticatedGoldBorder
import com.example.ui.theme.SophisticatedGoldDark
import com.example.ui.theme.SophisticatedGoldPillBg
import com.example.ui.theme.SophisticatedOnSurface
import com.example.ui.theme.SophisticatedOnSurfaceVariant
import com.example.ui.theme.SophisticatedSurface
import com.example.ui.theme.SophisticatedSurfaceVariant
import com.example.ui.viewmodel.EduTributeViewModel

data class LanguageOption(val displayName: String, val nativeName: String, val flag: String)

val SUPPORTED_LANGUAGES = listOf(
    LanguageOption("English", "English", "🇬🇧"),
    LanguageOption("Hindi", "हिंदी (भारत)", "🇮🇳"),
    LanguageOption("Spanish", "Español", "🇪🇸"),
    LanguageOption("French", "Français", "🇫🇷"),
    LanguageOption("German", "Deutsch", "🇩🇪"),
    LanguageOption("Japanese", "日本語", "🇯🇵"),
    LanguageOption("Italian", "Italiano", "🇮🇹"),
    LanguageOption("Portuguese", "Português", "🇧🇷"),
    LanguageOption("Mandarin", "中文 (普通话)", "🇨🇳"),
    LanguageOption("Arabic", "العربية", "🇸🇦"),
    LanguageOption("Bengali", "বাংলা", "🇮🇳"),
    LanguageOption("Tamil", "தமிழ்", "🇮🇳"),
    LanguageOption("Telugu", "తెలుగు", "🇮🇳"),
    LanguageOption("Marathi", "मराठी", "🇮🇳"),
    LanguageOption("Russian", "Русский", "🇷🇺")
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun VoiceGratitudeScreen(
    viewModel: EduTributeViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val studentName by viewModel.voiceStudentName.collectAsState()
    val teacherName by viewModel.voiceTeacherName.collectAsState()
    val rawMessage by viewModel.voiceRawMessage.collectAsState()
    val targetLanguage by viewModel.voiceTargetLanguage.collectAsState()
    val occasion by viewModel.voiceOccasion.collectAsState()
    val tone by viewModel.voiceTone.collectAsState()
    val duration by viewModel.voiceDuration.collectAsState()
    val customEditedScript by viewModel.voiceCustomEditedScript.collectAsState()

    val voiceState by viewModel.voiceState.collectAsState()
    val isPlaying by viewModel.isTtsPlaying.collectAsState()
    val spokenPhrase by viewModel.ttsSpokenPhrase.collectAsState()
    val speechProgress by viewModel.ttsProgress.collectAsState()
    val speed by viewModel.ttsSpeed.collectAsState()
    val pitch by viewModel.ttsPitch.collectAsState()

    var showJsonDialog by remember { mutableStateOf(false) }
    var currentJsonPayload by remember { mutableStateOf("") }
    var isLanguageMenuExpanded by remember { mutableStateOf(false) }

    val occasions = listOf(
        "Teachers' Day Celebration",
        "Mentorship Gratitude",
        "Retirement Honor",
        "Farewell & Graduation",
        "Annual Day Tribute"
    )

    val tones = listOf(
        "Heartfelt & Warm",
        "Poetic & Elevating",
        "Respectful & Formal",
        "Enthusiastic & Inspiring"
    )

    val durations = listOf(
        "Short (~30s)",
        "Standard (~60s)",
        "Reflective (~90s)"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Studio Header Card
        Card(
            colors = CardDefaults.cardColors(containerColor = SophisticatedSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(16.dp))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(SophisticatedGold, SophisticatedGoldDark)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = null,
                        tint = Color(0xFF121212),
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "VOICE OF GRATITUDE STUDIO",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = SophisticatedGold,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Surface(
                            color = Color(0xFF1B382B),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFF34D399))
                        ) {
                            Text(
                                text = "TTS ENGINE ACTIVE",
                                color = Color(0xFF34D399),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Multilingual Spoken Tribute & Audio Playback",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = SophisticatedOnSurface,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Generate heartfelt, paced teacher tributes across 15+ languages and listen with integrated real-time Text-to-Speech synthesis.",
                        style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurfaceVariant)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Presets Carousel
        Text(
            text = "Multilingual Inspiration Presets:",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = SophisticatedGold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PresetPill(
                flag = "🇮🇳",
                title = "Hindi (गुरुवंदना)",
                onClick = {
                    viewModel.loadVoicePreset(
                        student = "Aarav Sharma",
                        teacher = "Dr. S. K. Mukherjee",
                        message = "आपने केवल रसायन विज्ञान नहीं सिखाया, बल्कि कठिन समय में धैर्य रखना सिखाया। आपका मार्गदर्शन अनमोल है।",
                        lang = "Hindi",
                        occasion = "Teachers' Day Celebration",
                        tone = "Heartfelt & Warm"
                    )
                },
                testTag = "voice_preset_hindi"
            )

            PresetPill(
                flag = "🇪🇸",
                title = "Spanish (Día del Maestro)",
                onClick = {
                    viewModel.loadVoicePreset(
                        student = "Mateo Lopez",
                        teacher = "Prof. Isabella Garcia",
                        message = "Gracias por inspirarme a perseguir la ciencia y por creer en mis sueños desde el primer día.",
                        lang = "Spanish",
                        occasion = "Teachers' Day Celebration",
                        tone = "Enthusiastic & Inspiring"
                    )
                },
                testTag = "voice_preset_spanish"
            )

            PresetPill(
                flag = "🇫🇷",
                title = "French (Hommage)",
                onClick = {
                    viewModel.loadVoicePreset(
                        student = "Claire Dubois",
                        teacher = "Mme. Catherine Dupont",
                        message = "Votre passion pour la littérature et vos encouragements constants ont transformé ma vision du monde.",
                        lang = "French",
                        occasion = "Mentorship Gratitude",
                        tone = "Poetic & Elevating"
                    )
                },
                testTag = "voice_preset_french"
            )

            PresetPill(
                flag = "🇯🇵",
                title = "Japanese (恩師への感謝)",
                onClick = {
                    viewModel.loadVoicePreset(
                        student = "Kenji Sato",
                        teacher = "Tanaka Sensei",
                        message = "どんな時も温かく見守り、学ぶ楽しさを教えてくださり心から感謝しています。",
                        lang = "Japanese",
                        occasion = "Teachers' Day Celebration",
                        tone = "Respectful & Formal"
                    )
                },
                testTag = "voice_preset_japanese"
            )

            PresetPill(
                flag = "🇩🇪",
                title = "German (Lehrkräfte)",
                onClick = {
                    viewModel.loadVoicePreset(
                        student = "Lukas Weber",
                        teacher = "Herr Dr. Schmidt",
                        message = "Vielen Dank für Ihre endlose Geduld, Ihre Inspiration und Ihre Unterstützung in all den Jahren.",
                        lang = "German",
                        occasion = "Teachers' Day Celebration",
                        tone = "Respectful & Formal"
                    )
                },
                testTag = "voice_preset_german"
            )

            PresetPill(
                flag = "🇬🇧",
                title = "English (Mentorship)",
                onClick = {
                    viewModel.loadVoicePreset(
                        student = "David Miller",
                        teacher = "Prof. Robert Thorne",
                        message = "Thank you for challenging us to think critically and for always keeping your door open for questions.",
                        lang = "English",
                        occasion = "Mentorship Gratitude",
                        tone = "Heartfelt & Warm"
                    )
                },
                testTag = "voice_preset_english"
            )

            PresetPill(
                flag = "🇮🇳",
                title = "Bengali (শিক্ষক প্রণাম)",
                onClick = {
                    viewModel.loadVoicePreset(
                        student = "Soham Banerjee",
                        teacher = "Acharya Roy",
                        message = "আপনার অনুপ্রেরণা ও স্নেহ চিরকাল আমার জীবনের চলার পথের পথপ্রদর্শক হয়ে থাকবে।",
                        lang = "Bengali",
                        occasion = "Teachers' Day Celebration",
                        tone = "Heartfelt & Warm"
                    )
                },
                testTag = "voice_preset_bengali"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Form Configuration Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SophisticatedSurface),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SPEECH SCRIPT BUILDER",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = SophisticatedGold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Speaker & Teacher Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = studentName,
                        onValueChange = { viewModel.voiceStudentName.value = it },
                        label = { Text("Speaker / Student") },
                        placeholder = { Text("e.g. Aarav Sharma") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = SophisticatedGold)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SophisticatedGold,
                            focusedLabelColor = SophisticatedGold,
                            cursorColor = SophisticatedGold,
                            focusedLeadingIconColor = SophisticatedGold,
                            unfocusedBorderColor = Color(0xFF383838)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("voice_student_name_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = teacherName,
                        onValueChange = { viewModel.voiceTeacherName.value = it },
                        label = { Text("Teacher Name") },
                        placeholder = { Text("e.g. Dr. Mukherjee") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.School, contentDescription = null, tint = SophisticatedGold)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SophisticatedGold,
                            focusedLabelColor = SophisticatedGold,
                            cursorColor = SophisticatedGold,
                            focusedLeadingIconColor = SophisticatedGold,
                            unfocusedBorderColor = Color(0xFF383838)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("voice_teacher_name_input"),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Target Language Selector Dropdown (15 languages)
                ExposedDropdownMenuBox(
                    expanded = isLanguageMenuExpanded,
                    onExpandedChange = { isLanguageMenuExpanded = it }
                ) {
                    val currentSelectedLang = SUPPORTED_LANGUAGES.find { it.displayName.equals(targetLanguage, ignoreCase = true) }
                        ?: SUPPORTED_LANGUAGES.first()

                    OutlinedTextField(
                        value = "${currentSelectedLang.flag}  ${currentSelectedLang.displayName} (${currentSelectedLang.nativeName})",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Target Language for Speech & TTS") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Translate, contentDescription = null, tint = SophisticatedGold)
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isLanguageMenuExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SophisticatedGold,
                            focusedLabelColor = SophisticatedGold,
                            unfocusedBorderColor = Color(0xFF383838)
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("voice_language_dropdown"),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = isLanguageMenuExpanded,
                        onDismissRequest = { isLanguageMenuExpanded = false },
                        modifier = Modifier.background(SophisticatedSurface)
                    ) {
                        SUPPORTED_LANGUAGES.forEach { langOpt ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(langOpt.flag, fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "${langOpt.displayName} - ${langOpt.nativeName}",
                                            color = SophisticatedOnSurface,
                                            fontWeight = if (langOpt.displayName == targetLanguage) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                },
                                onClick = {
                                    viewModel.voiceTargetLanguage.value = langOpt.displayName
                                    isLanguageMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Occasion Selector Chips
                Text(
                    text = "Occasion:",
                    style = MaterialTheme.typography.labelSmall.copy(color = SophisticatedOnSurfaceVariant)
                )
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    occasions.forEach { occ ->
                        FilterChip(
                            selected = occasion == occ,
                            onClick = { viewModel.voiceOccasion.value = occ },
                            label = { Text(occ, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SophisticatedGoldPillBg,
                                selectedLabelColor = SophisticatedGold,
                                containerColor = SophisticatedSurfaceVariant,
                                labelColor = SophisticatedOnSurface
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = occasion == occ,
                                borderColor = SophisticatedGlassBorder,
                                selectedBorderColor = SophisticatedGoldBorder
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Delivery Tone & Duration
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Speech Tone:",
                            style = MaterialTheme.typography.labelSmall.copy(color = SophisticatedOnSurfaceVariant)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        tones.forEach { t ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (tone == t) SophisticatedGoldPillBg else Color.Transparent,
                                border = BorderStroke(
                                    1.dp,
                                    if (tone == t) SophisticatedGoldBorder else SophisticatedGlassBorder
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                                    .clickable { viewModel.voiceTone.value = t }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (tone == t) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = SophisticatedGold, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text(
                                        text = t,
                                        fontSize = 11.sp,
                                        color = if (tone == t) SophisticatedGold else SophisticatedOnSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Estimated Duration:",
                            style = MaterialTheme.typography.labelSmall.copy(color = SophisticatedOnSurfaceVariant)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        durations.forEach { d ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (duration == d) SophisticatedGoldPillBg else Color.Transparent,
                                border = BorderStroke(
                                    1.dp,
                                    if (duration == d) SophisticatedGoldBorder else SophisticatedGlassBorder
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                                    .clickable { viewModel.voiceDuration.value = d }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (duration == d) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = SophisticatedGold, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text(
                                        text = d,
                                        fontSize = 11.sp,
                                        color = if (duration == d) SophisticatedGold else SophisticatedOnSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Raw Memory / Thought Box
                OutlinedTextField(
                    value = rawMessage,
                    onValueChange = { viewModel.voiceRawMessage.value = it },
                    label = { Text("Your Heartfelt Thoughts / Memory") },
                    placeholder = { Text("Write your raw thoughts in your words; the AI engine will pace, polish, and synthesize it into a speech script...") },
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SophisticatedGold,
                        focusedLabelColor = SophisticatedGold,
                        cursorColor = SophisticatedGold,
                        unfocusedBorderColor = Color(0xFF383838)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("voice_raw_message_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Generate Button
                Button(
                    onClick = { viewModel.generateVoiceGratitude() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("voice_generate_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SophisticatedGold,
                        contentColor = Color(0xFF121212)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Mic, contentDescription = null, tint = Color(0xFF121212))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Generate Spoken Tribute Script",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // State Output Handling
        when (val state = voiceState) {
            is EngineUiState.Loading -> {
                LoadingCard(message = "Pacing cadence, phonetics & acoustic markers for ${targetLanguage}...")
            }
            is EngineUiState.SafetyRejection -> {
                SafetyRejectionCard(
                    reason = state.reason,
                    politeAlternative = state.politeAlternative,
                    rawJson = state.rawJson,
                    onUseAlternative = {
                        viewModel.voiceRawMessage.value = state.politeAlternative
                        viewModel.generateVoiceGratitude()
                    }
                )
            }
            is EngineUiState.Success -> {
                currentJsonPayload = state.rawJson
                VoiceResultStudioCard(
                    response = state.data,
                    isPlaying = isPlaying,
                    spokenPhrase = spokenPhrase,
                    speechProgress = speechProgress,
                    customScript = customEditedScript,
                    onUpdateCustomScript = { viewModel.voiceCustomEditedScript.value = it },
                    speed = speed,
                    pitch = pitch,
                    onSpeedChange = { viewModel.ttsSpeed.value = it },
                    onPitchChange = { viewModel.ttsPitch.value = it },
                    onPlayTts = { scriptText ->
                        viewModel.playTts(scriptText, state.data.targetLanguage)
                    },
                    onStopTts = { viewModel.stopTts() },
                    onViewJson = { showJsonDialog = true },
                    onCopyScript = {
                        val textToCopy = customEditedScript.ifBlank { state.data.ttsScript }
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("EduTribute TTS Script", textToCopy)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "TTS Tribute Script copied!", Toast.LENGTH_SHORT).show()
                    },
                    onShare = {
                        val textToShare = customEditedScript.ifBlank { state.data.ttsScript }
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "Spoken Tribute Script for ${state.data.teacherName}")
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "🎙️ Teachers' Day Spoken Tribute for ${state.data.teacherName} (by ${state.data.studentName})\n\n$textToShare\n\nSummary: ${state.data.englishSummary}"
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Audio Script"))
                    }
                )
            }
            is EngineUiState.Error -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Error: ${state.message}",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            is EngineUiState.Idle -> {
                Surface(
                    color = SophisticatedSurface,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = SophisticatedGold,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Ready to Generate Audio Tribute",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SophisticatedGold
                                )
                            )
                            Text(
                                text = "Select a language, set occasion & tone, and click Generate to produce a heartfelt script with native speech playback.",
                                style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurfaceVariant)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showJsonDialog) {
        JsonViewerDialog(
            title = "VOICE_OF_GRATITUDE JSON Output Schema",
            jsonString = currentJsonPayload,
            onDismiss = { showJsonDialog = false }
        )
    }
}

@Composable
private fun PresetPill(
    flag: String,
    title: String,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SophisticatedSurfaceVariant,
        modifier = Modifier
            .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(flag, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = SophisticatedOnSurface
                )
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VoiceResultStudioCard(
    response: VoiceGratitudeResponse,
    isPlaying: Boolean,
    spokenPhrase: String,
    speechProgress: Float,
    customScript: String,
    onUpdateCustomScript: (String) -> Unit,
    speed: Float,
    pitch: Float,
    onSpeedChange: (Float) -> Unit,
    onPitchChange: (Float) -> Unit,
    onPlayTts: (String) -> Unit,
    onStopTts: () -> Unit,
    onViewJson: () -> Unit,
    onCopyScript: () -> Unit,
    onShare: () -> Unit
) {
    var activeScriptTab by remember { mutableIntStateOf(0) } // 0: Paced Script, 1: Live Editor & Rehearse

    val effectiveScript = customScript.ifBlank { response.ttsScript }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, SophisticatedGoldBorder, RoundedCornerShape(20.dp))
            .testTag("voice_result_container"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SophisticatedSurface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Language & Module Badges Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = SophisticatedGoldPillBg,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.border(1.dp, SophisticatedGoldBorder, RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = null,
                            tint = SophisticatedGold,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = response.targetLanguage.uppercase(),
                            color = SophisticatedGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Surface(
                    color = Color(0x33000000),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = "SYNTHESIZED SPEECH",
                        color = SophisticatedGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title & Speaker
            Text(
                text = "Tribute to ${response.teacherName}",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = SophisticatedOnSurface
                )
            )
            Text(
                text = "Dedicated & Spoken by ${response.studentName}",
                style = MaterialTheme.typography.bodyMedium.copy(color = SophisticatedOnSurfaceVariant)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Audio Player Console Box
            Surface(
                color = Color(0xFF131313),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Primary Play / Stop Action
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isPlaying) Color(0xFFE57373)
                                        else SophisticatedGold
                                    )
                                    .clickable {
                                        if (isPlaying) {
                                            onStopTts()
                                        } else {
                                            onPlayTts(effectiveScript)
                                        }
                                    }
                                    .testTag("voice_play_pause_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Stop Speech" else "Play Speech",
                                    tint = Color(0xFF121212),
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = if (isPlaying) "Speaking Tribute..." else "Play Audio Tribute",
                                    color = SophisticatedOnSurface,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = if (isPlaying) "Android TTS Voice Active" else "Tap to speak in ${response.targetLanguage}",
                                    color = SophisticatedGold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Animated Waveform Equalizer
                        AnimatedWaveformVisualizer(isPlaying = isPlaying)
                    }

                    // Live Spoken Phrase Ribbon
                    AnimatedVisibility(
                        visible = isPlaying && spokenPhrase.isNotBlank(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                color = SophisticatedGoldPillBg,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, SophisticatedGoldBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = null,
                                        tint = SophisticatedGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = spokenPhrase,
                                        color = SophisticatedGold,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Speech Progress Indicator
                    LinearProgressIndicator(
                        progress = { speechProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = SophisticatedGold,
                        trackColor = Color(0xFF262626),
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Speed Presets Pill Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Speech Rate:",
                            color = SophisticatedOnSurfaceVariant,
                            fontSize = 12.sp
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(0.75f, 1.0f, 1.25f, 1.5f).forEach { presetRate ->
                                val isSelected = Math.abs(speed - presetRate) < 0.05f
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isSelected) SophisticatedGold else Color(0xFF222222),
                                    modifier = Modifier
                                        .clickable { onSpeedChange(presetRate) }
                                ) {
                                    Text(
                                        text = "${presetRate}x",
                                        color = if (isSelected) Color(0xFF121212) else SophisticatedOnSurface,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Pitch Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Voice Pitch", color = SophisticatedOnSurfaceVariant, fontSize = 11.sp)
                        Text("${String.format("%.2f", pitch)}x", color = SophisticatedGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = pitch,
                        onValueChange = onPitchChange,
                        valueRange = 0.7f..1.4f,
                        colors = SliderDefaults.colors(
                            thumbColor = SophisticatedGold,
                            activeTrackColor = SophisticatedGold,
                            inactiveTrackColor = Color(0xFF333333)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Script View / Edit Tabs
            TabRow(
                selectedTabIndex = activeScriptTab,
                containerColor = SophisticatedSurfaceVariant,
                contentColor = SophisticatedGold,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[activeScriptTab]),
                        color = SophisticatedGold
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Tab(
                    selected = activeScriptTab == 0,
                    onClick = { activeScriptTab = 0 },
                    text = { Text("Paced Script", fontSize = 12.sp, fontWeight = if (activeScriptTab == 0) FontWeight.Bold else FontWeight.Normal) }
                )
                Tab(
                    selected = activeScriptTab == 1,
                    onClick = { activeScriptTab = 1 },
                    text = { Text("Live Editor & Rehearse", fontSize = 12.sp, fontWeight = if (activeScriptTab == 1) FontWeight.Bold else FontWeight.Normal) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (activeScriptTab == 0) {
                // Paced Script Box with Pacing Markers
                Surface(
                    color = Color(0xFF141414),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = effectiveScript,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = SophisticatedOnSurface,
                                lineHeight = 24.sp
                            )
                        )
                    }
                }

                if (response.pacingMarkers.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Acoustic Markers:",
                        style = MaterialTheme.typography.labelSmall.copy(color = SophisticatedOnSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        response.pacingMarkers.forEach { marker ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SophisticatedGoldPillBg,
                                modifier = Modifier.border(1.dp, SophisticatedGoldBorder, RoundedCornerShape(12.dp))
                            ) {
                                Text(
                                    text = marker,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        color = SophisticatedGold
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                // Live Editor & Rehearsal Tab
                Column {
                    OutlinedTextField(
                        value = customScript.ifBlank { response.ttsScript },
                        onValueChange = onUpdateCustomScript,
                        label = { Text("Edit Speech Script Live") },
                        minLines = 4,
                        maxLines = 8,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SophisticatedGold,
                            focusedLabelColor = SophisticatedGold,
                            cursorColor = SophisticatedGold,
                            unfocusedBorderColor = SophisticatedGlassBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("voice_custom_script_editor"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { onUpdateCustomScript(response.ttsScript) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SophisticatedGold),
                            border = BorderStroke(1.dp, SophisticatedGlassBorder),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reset to AI Script", fontSize = 11.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // English Translation Summary
            if (response.englishSummary.isNotBlank()) {
                Surface(
                    color = Color(0xFF181818),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SophisticatedGoldBorder, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "English Translation & Context Summary",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = SophisticatedGold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = response.englishSummary,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = SophisticatedOnSurface,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Pronunciation Guides with Mini TTS preview
            if (response.pronunciationGuides.isNotEmpty()) {
                Text(
                    text = "PHONETICS & PRONUNCIATION GUIDE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = SophisticatedGold,
                        letterSpacing = 0.5.sp
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    response.pronunciationGuides.forEach { guideItem ->
                        Surface(
                            color = Color(0xFF161616),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(8.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = guideItem.term,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = SophisticatedGold
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "→ ${guideItem.guide}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurfaceVariant)
                                    )
                                }

                                // Quick pronounce word button
                                IconButton(
                                    onClick = { onPlayTts(guideItem.term) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Speak Term",
                                        tint = SophisticatedGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Action Toolbar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onCopyScript,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("voice_copy_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SophisticatedGold,
                        contentColor = Color(0xFF121212)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy Script", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onShare,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SophisticatedGold),
                    border = BorderStroke(1.dp, SophisticatedGoldBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share")
                }

                IconButton(
                    onClick = onViewJson,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SophisticatedSurfaceVariant)
                        .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(12.dp))
                        .testTag("voice_json_btn")
                ) {
                    Icon(imageVector = Icons.Default.Code, contentDescription = "View JSON", tint = SophisticatedGold)
                }
            }
        }
    }
}

@Composable
fun AnimatedWaveformVisualizer(
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "waveform")

    val h1 by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "h1"
    )

    val h2 by transition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "h2"
    )

    val h3 by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "h3"
    )

    val h4 by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(350, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "h4"
    )

    Row(
        modifier = modifier.height(36.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val heights = if (isPlaying) {
            listOf(h1, h2, h3, h4, h2, h1)
        } else {
            listOf(0.2f, 0.3f, 0.4f, 0.3f, 0.2f, 0.1f)
        }

        heights.forEach { fraction ->
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height((32 * fraction).dp.coerceAtLeast(6.dp))
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isPlaying) SophisticatedGold else Color(0xFF444444))
            )
        }
    }
}
