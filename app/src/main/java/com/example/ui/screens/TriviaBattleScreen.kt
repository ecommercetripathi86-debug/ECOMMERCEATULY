package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EngineUiState
import com.example.data.model.TriviaBattleResponse
import com.example.data.model.TriviaQuestion
import com.example.ui.components.JsonViewerDialog
import com.example.ui.components.LoadingCard
import com.example.ui.components.SafetyRejectionCard
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriviaBattleScreen(
    viewModel: EduTributeViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val subjectArea by viewModel.triviaSubject.collectAsState()
    val gradeDepartment by viewModel.triviaGrade.collectAsState()
    val personaArchetype by viewModel.triviaPersona.collectAsState()
    val triviaState by viewModel.triviaState.collectAsState()

    val currentQIndex by viewModel.triviaCurrentQuestionIndex.collectAsState()
    val userAnswers by viewModel.triviaUserAnswers.collectAsState()
    val answersRevealed by viewModel.triviaAnswerRevealed.collectAsState()
    val isCompleted by viewModel.triviaQuizFinished.collectAsState()
    val score by viewModel.triviaScore.collectAsState()

    var showJsonDialog by remember { mutableStateOf(false) }
    var currentJsonPayload by remember { mutableStateOf("") }
    var isPersonaMenuExpanded by remember { mutableStateOf(false) }

    val personaArchetypes = listOf(
        "The Homework Detective",
        "The Pop Quiz Menace",
        "The Strict But Sweet Grammarian",
        "The Energetic Lab Wizard",
        "The Legendary Socratic Inquirer",
        "The Red Pen Maestro"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Module Introduction Card
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
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(SophisticatedGold, SophisticatedGoldDark)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsEsports,
                        contentDescription = null,
                        tint = Color(0xFF121212),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "MODULE: TRIVIA_BATTLE",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = SophisticatedGold,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "Teacher Persona & Quiz Battle",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = SophisticatedOnSurface,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Generates 3 interactive MCQs with persona commentary, explanations, scoring, and honor certificates.",
                        style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurfaceVariant)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Presets
        Text(
            text = "Battle Archetype Presets:",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = SophisticatedGold
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SophisticatedSurfaceVariant,
                modifier = Modifier
                    .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(20.dp))
                    .testTag("trivia_preset_homework")
            ) {
                Text(
                    text = "🕵️ The Homework Detective",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = SophisticatedOnSurface
                    ),
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clickable {
                            viewModel.loadTriviaPreset(
                                subject = "Mathematics & Algebra",
                                grade = "Grade 10 High School",
                                persona = "The Homework Detective"
                            )
                        }
                )
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SophisticatedSurfaceVariant,
                modifier = Modifier.border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(20.dp))
            ) {
                Text(
                    text = "⚡ Pop Quiz Menace",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = SophisticatedOnSurface
                    ),
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clickable {
                            viewModel.loadTriviaPreset(
                                subject = "Physics - Mechanics",
                                grade = "Undergraduate Physics",
                                persona = "The Pop Quiz Menace"
                            )
                        }
                )
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SophisticatedSurfaceVariant,
                modifier = Modifier.border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(20.dp))
            ) {
                Text(
                    text = "🧪 Lab Wizard",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = SophisticatedOnSurface
                    ),
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clickable {
                            viewModel.loadTriviaPreset(
                                subject = "Organic Chemistry",
                                grade = "Senior Secondary",
                                persona = "The Energetic Lab Wizard"
                            )
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Form Inputs Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SophisticatedSurface),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Subject Area
                OutlinedTextField(
                    value = subjectArea,
                    onValueChange = { viewModel.triviaSubject.value = it },
                    label = { Text("Subject Area / Topic") },
                    placeholder = { Text("e.g. World History, Calculus, Quantum Mechanics") },
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
                        .fillMaxWidth()
                        .testTag("trivia_subject_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Grade / Department
                OutlinedTextField(
                    value = gradeDepartment,
                    onValueChange = { viewModel.triviaGrade.value = it },
                    label = { Text("Grade Level / Department") },
                    placeholder = { Text("e.g. High School Grade 11, College Dept of Chemistry") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.MilitaryTech, contentDescription = null, tint = SophisticatedGold)
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
                        .fillMaxWidth()
                        .testTag("trivia_grade_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Teacher Persona Archetype Selector
                ExposedDropdownMenuBox(
                    expanded = isPersonaMenuExpanded,
                    onExpandedChange = { isPersonaMenuExpanded = it }
                ) {
                    OutlinedTextField(
                        value = personaArchetype,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Teacher Persona Archetype") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Psychology, contentDescription = null, tint = SophisticatedGold)
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPersonaMenuExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SophisticatedGold,
                            focusedLabelColor = SophisticatedGold,
                            unfocusedBorderColor = Color(0xFF383838)
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("trivia_persona_dropdown"),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = isPersonaMenuExpanded,
                        onDismissRequest = { isPersonaMenuExpanded = false },
                        modifier = Modifier.background(SophisticatedSurface)
                    ) {
                        personaArchetypes.forEach { persona ->
                            DropdownMenuItem(
                                text = { Text(persona, color = SophisticatedOnSurface) },
                                onClick = {
                                    viewModel.triviaPersona.value = persona
                                    isPersonaMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Generate Button
                Button(
                    onClick = { viewModel.generateTriviaBattle() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("trivia_generate_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SophisticatedGold,
                        contentColor = Color(0xFF121212)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.SportsEsports, contentDescription = null, tint = Color(0xFF121212))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Generate Persona Quiz Battle",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // State Output Handling
        when (val state = triviaState) {
            is EngineUiState.Loading -> {
                LoadingCard(message = "Summoning teacher persona, generating 3 questions & forging badge...")
            }
            is EngineUiState.SafetyRejection -> {
                SafetyRejectionCard(
                    reason = state.reason,
                    politeAlternative = state.politeAlternative,
                    rawJson = state.rawJson,
                    onUseAlternative = {
                        viewModel.triviaPersona.value = "The Legendary Socratic Inquirer"
                        viewModel.generateTriviaBattle()
                    }
                )
            }
            is EngineUiState.Success -> {
                currentJsonPayload = state.rawJson
                val questions = state.data.questions

                if (isCompleted) {
                    QuizCompletedCard(
                        response = state.data,
                        score = score,
                        maxScore = questions.size * 100,
                        onPlayAgain = { viewModel.resetTriviaGameState() },
                        onViewJson = { showJsonDialog = true },
                        onShare = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "I earned the '${state.data.digitalBadgeTitle}' Badge!")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "🏆 Teachers' Day Trivia Battle Result:\nI scored $score/${questions.size * 100} in ${state.data.subjectArea} under '${state.data.personaArchetype}' and unlocked the honor: '${state.data.digitalBadgeTitle}'!"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Badge"))
                        }
                    )
                } else if (questions.isNotEmpty() && currentQIndex < questions.size) {
                    val currentQuestion = questions[currentQIndex]
                    val selectedAnswer = userAnswers[currentQIndex]
                    val isRevealed = answersRevealed[currentQIndex] ?: false

                    QuizQuestionCard(
                        question = currentQuestion,
                        questionIndex = currentQIndex,
                        totalQuestions = questions.size,
                        personaName = state.data.personaArchetype,
                        badgeTitle = state.data.digitalBadgeTitle,
                        selectedOptionIndex = selectedAnswer,
                        isAnswerRevealed = isRevealed,
                        onSelectOption = { optIdx ->
                            viewModel.selectQuizOption(currentQIndex, optIdx, currentQuestion.correctOptionIndex)
                        },
                        onNext = {
                            viewModel.nextQuestion(questions.size)
                        },
                        onViewJson = { showJsonDialog = true }
                    )
                }
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
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = SophisticatedGold,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Ready for Trivia Battle",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SophisticatedGold
                                )
                            )
                            Text(
                                text = "Select your subject and teacher persona archetype to generate a custom 3-question quiz with honors.",
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
            title = "TRIVIA_BATTLE JSON Output Schema",
            jsonString = currentJsonPayload,
            onDismiss = { showJsonDialog = false }
        )
    }
}

@Composable
fun QuizQuestionCard(
    question: TriviaQuestion,
    questionIndex: Int,
    totalQuestions: Int,
    personaName: String,
    badgeTitle: String,
    selectedOptionIndex: Int?,
    isAnswerRevealed: Boolean,
    onSelectOption: (Int) -> Unit,
    onNext: () -> Unit,
    onViewJson: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SophisticatedSurface),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, SophisticatedGoldBorder, RoundedCornerShape(20.dp))
            .testTag("trivia_question_card")
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header Progress & Badge Goal
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
                    Text(
                        text = "QUESTION ${questionIndex + 1} OF $totalQuestions",
                        color = SophisticatedGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        letterSpacing = 0.5.sp
                    )
                }

                IconButton(
                    onClick = onViewJson,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(imageVector = Icons.Default.Code, contentDescription = "View JSON", tint = SophisticatedGold, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { (questionIndex + 1).toFloat() / totalQuestions.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = SophisticatedGold,
                trackColor = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Question Text
            Text(
                text = question.question,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = SophisticatedOnSurface,
                    lineHeight = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4 Option Cards
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                question.options.forEachIndexed { optIndex, optionText ->
                    val isSelected = selectedOptionIndex == optIndex
                    val isCorrect = question.correctOptionIndex == optIndex

                    val (containerColor, borderColor, textColor) = when {
                        !isAnswerRevealed -> {
                            if (isSelected) Triple(SophisticatedGoldPillBg, SophisticatedGold, SophisticatedGold)
                            else Triple(Color(0xFF161616), Color(0xFF333333), SophisticatedOnSurface)
                        }
                        isCorrect -> Triple(Color(0xFF0F261B), Color(0xFF10B981), Color(0xFF34D399))
                        isSelected && !isCorrect -> Triple(Color(0xFF2C1014), Color(0xFFF43F5E), Color(0xFFFB7185))
                        else -> Triple(Color(0xFF141414), Color(0xFF282828), SophisticatedOnSurfaceVariant)
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = containerColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
                            .clickable(enabled = !isAnswerRevealed) {
                                onSelectOption(optIndex)
                            }
                            .testTag("trivia_option_$optIndex")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(borderColor.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val letter = ('A' + optIndex).toString()
                                    Text(
                                        text = letter,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor,
                                        fontSize = 13.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = optionText,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = textColor,
                                        fontWeight = if (isSelected || (isAnswerRevealed && isCorrect)) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }

                            if (isAnswerRevealed) {
                                if (isCorrect) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Correct",
                                        tint = Color(0xFF10B981),
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Incorrect",
                                        tint = Color(0xFFF43F5E),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Post-answer Feedback Card
            AnimatedVisibility(visible = isAnswerRevealed) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    // Persona commentary
                    Surface(
                        color = Color(0xFF181818),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SophisticatedGoldBorder, RoundedCornerShape(12.dp))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = SophisticatedGold,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = personaName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = SophisticatedGold
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = question.personaComment,
                                    style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurface)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Educational explanation
                    Surface(
                        color = Color(0xFF141414),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Explanation:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SophisticatedGold
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = question.explanation,
                                style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurfaceVariant)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Next / Finish Button
                    Button(
                        onClick = onNext,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("trivia_next_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SophisticatedGold,
                            contentColor = Color(0xFF121212)
                        )
                    ) {
                        Text(
                            text = if (questionIndex < totalQuestions - 1) "Next Question →" else "View Score & Badge Certificate 🏆",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuizCompletedCard(
    response: TriviaBattleResponse,
    score: Int,
    maxScore: Int,
    onPlayAgain: () -> Unit,
    onViewJson: () -> Unit,
    onShare: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, SophisticatedGoldBorder, RoundedCornerShape(20.dp))
            .testTag("quiz_completed_certificate")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF1E1E1E), Color(0xFF121212))
                    )
                )
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(SophisticatedGold, SophisticatedGoldDark)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = Color(0xFF121212),
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "CERTIFICATE OF HONORS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold,
                        color = SophisticatedGold
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Teachers' Day Trivia Champion",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = SophisticatedOnSurface
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Score Display
                Text(
                    text = "$score / $maxScore PTS",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = SophisticatedGold
                    )
                )
                Text(
                    text = "Tested in ${response.subjectArea} (${response.gradeDepartment})",
                    style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurfaceVariant),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Awarded Digital Badge Title
                Surface(
                    color = SophisticatedGoldPillBg,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, SophisticatedGoldBorder, RoundedCornerShape(14.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "OFFICIALLY CONFERRED BADGE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SophisticatedGold,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = null,
                                tint = SophisticatedGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = response.digitalBadgeTitle,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SophisticatedOnSurface
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onShare,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SophisticatedGold,
                            contentColor = Color(0xFF121212)
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share Badge", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onPlayAgain,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SophisticatedGold),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SophisticatedGoldBorder)
                    ) {
                        Icon(imageVector = Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Play Again")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(onClick = onViewJson) {
                    Icon(imageVector = Icons.Default.Code, contentDescription = null, tint = SophisticatedGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Inspect Schema JSON", color = SophisticatedGold)
                }
            }
        }
    }
}
