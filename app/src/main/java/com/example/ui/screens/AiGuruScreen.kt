package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AiGuruResponse
import com.example.data.model.EngineUiState
import com.example.ui.components.BadgeChip
import com.example.ui.components.GratitudeCardGalleryView
import com.example.ui.components.JsonViewerDialog
import com.example.ui.components.LoadingCard
import com.example.ui.components.SafetyRejectionCard
import com.example.ui.theme.CrimsonGradientBottom
import com.example.ui.theme.CrimsonGradientTop
import com.example.ui.theme.EmeraldGradientBottom
import com.example.ui.theme.EmeraldGradientTop
import com.example.ui.theme.IndigoGradientBottom
import com.example.ui.theme.IndigoGradientTop
import com.example.ui.theme.ParchmentGoldBottom
import com.example.ui.theme.ParchmentGoldTop
import com.example.ui.theme.SapphireGradientBottom
import com.example.ui.theme.SapphireGradientTop
import com.example.ui.theme.SlateGradientBottom
import com.example.ui.theme.SlateGradientTop
import com.example.ui.theme.SophisticatedBackground
import com.example.ui.theme.SophisticatedGlassBorder
import com.example.ui.theme.SophisticatedGold
import com.example.ui.theme.SophisticatedGoldBorder
import com.example.ui.theme.SophisticatedGoldDark
import com.example.ui.theme.SophisticatedGoldLight
import com.example.ui.theme.SophisticatedGoldPillBg
import com.example.ui.theme.SophisticatedOnSurface
import com.example.ui.theme.SophisticatedOnSurfaceVariant
import com.example.ui.theme.SophisticatedSurface
import com.example.ui.theme.SophisticatedSurfaceVariant
import com.example.ui.theme.VioletNebulaBottom
import com.example.ui.theme.VioletNebulaTop
import com.example.ui.viewmodel.EduTributeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AiGuruScreen(
    viewModel: EduTributeViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val teacherName by viewModel.guruTeacherName.collectAsState()
    val subject by viewModel.guruSubject.collectAsState()
    val keyTraits by viewModel.guruKeyTraits.collectAsState()
    val language by viewModel.guruLanguage.collectAsState()
    val tone by viewModel.guruTone.collectAsState()
    val cardThemeIndex by viewModel.guruCardTheme.collectAsState()
    val selectedTemplate by viewModel.guruSelectedTemplate.collectAsState()
    val studentSignature by viewModel.guruStudentSignature.collectAsState()
    val studentRole by viewModel.guruStudentRole.collectAsState()
    val selectedAccent by viewModel.guruSelectedAccent.collectAsState()
    val backNote by viewModel.guruBackNote.collectAsState()
    val isCardFlipped by viewModel.guruIsCardFlipped.collectAsState()
    val guruState by viewModel.guruState.collectAsState()
    val activeBuilderTab by viewModel.guruActiveTab.collectAsState()
    val savedCards by viewModel.savedCards.collectAsState()

    var showJsonDialog by remember { mutableStateOf(false) }
    var currentJsonPayload by remember { mutableStateOf("") }
    var isLanguageMenuExpanded by remember { mutableStateOf(false) }
    var showEditCardDialog by remember { mutableStateOf(false) }
    var showAddBadgeDialog by remember { mutableStateOf(false) }
    var newBadgeText by remember { mutableStateOf("") }

    val tones = listOf("Inspiring", "Poetic", "Humorous", "Formal", "Heartfelt", "Nostalgic")
    val languages = listOf("English", "Hindi", "Spanish", "French", "German", "Japanese")

    // Template metadata
    val templateNames = listOf(
        TemplateOption("Luxe Gold Certificate", Icons.Default.WorkspacePremium, "Formal seal, ornate borders & certificate aesthetic"),
        TemplateOption("Modern Luminary Glass", Icons.Default.AutoAwesome, "Frosted glassmorphism, neon gold accent & sleek typography"),
        TemplateOption("Classic Scroll Parchment", Icons.Default.HistoryEdu, "Antique scroll styling, drop caps & poetic elegance"),
        TemplateOption("Editorial Minimalist", Icons.Default.Tune, "High-contrast clean layout with refined rules & modern typography"),
        TemplateOption("Cosmic Nebula", Icons.Default.Star, "Deep celestial gradient, constellation badges & starry accents")
    )

    // Accents metadata
    val accentIcons = listOf(
        AccentOption("Laurel Wreath", Icons.Default.WorkspacePremium),
        AccentOption("Graduation Cap", Icons.Default.School),
        AccentOption("Quill & Scroll", Icons.Default.HistoryEdu),
        AccentOption("Star of Honor", Icons.Default.Star),
        AccentOption("Wisdom Book", Icons.Default.AutoStories),
        AccentOption("Academic Medal", Icons.Default.MilitaryTech)
    )

    // Themes metadata
    val themes = listOf(
        ThemeOption("Dark Obsidian", Color(0xFF1E1E1E), Brush.linearGradient(listOf(SlateGradientTop, SlateGradientBottom)), SophisticatedGoldBorder),
        ThemeOption("Champagne Luxe", Color(0xFF42341A), Brush.linearGradient(listOf(ParchmentGoldTop, ParchmentGoldBottom)), SophisticatedGoldBorder),
        ThemeOption("Emerald Academy", Color(0xFF152E22), Brush.linearGradient(listOf(EmeraldGradientTop, EmeraldGradientBottom)), Color(0x6610B981)),
        ThemeOption("Cosmic Navy", Color(0xFF191D33), Brush.linearGradient(listOf(IndigoGradientTop, IndigoGradientBottom)), Color(0x666366F1)),
        ThemeOption("Crimson Regal", Color(0xFF2E151B), Brush.linearGradient(listOf(CrimsonGradientTop, CrimsonGradientBottom)), Color(0x66E57373)),
        ThemeOption("Violet Nebula", Color(0xFF231738), Brush.linearGradient(listOf(VioletNebulaTop, VioletNebulaBottom)), Color(0x66A855F7))
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Module Header Banner
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
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFF121212),
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "MODULE: AI_GURU",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = SophisticatedGold,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = SophisticatedGoldPillBg,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.border(0.5.dp, SophisticatedGoldBorder, RoundedCornerShape(6.dp))
                        ) {
                            Text(
                                text = "INTERACTIVE BUILDER",
                                color = SophisticatedGold,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Appreciation Card & Poem Generator",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = SophisticatedOnSurface,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Select designer templates, personalize subject metaphors, and generate bespoke cards using Gemini.",
                        style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurfaceVariant)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Luxury Step Workflow Indicator & Tabs (Step 1: Style, Step 2: Prompts, Step 3: Studio, plus Gallery)
        Surface(
            color = SophisticatedSurface,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                // Stepper Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Step 1: Template & Style (Tab 3)
                    val step1Active = activeBuilderTab == 3
                    val step1Completed = activeBuilderTab == 2 || activeBuilderTab == 0
                    val step1Scale by animateFloatAsState(
                        targetValue = if (step1Active) 1.05f else 1.0f,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "step1Scale"
                    )
                    Row(
                        modifier = Modifier
                            .clickable { viewModel.guruActiveTab.value = 3 }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = when {
                                step1Active -> SophisticatedGold
                                step1Completed -> SophisticatedGoldPillBg
                                else -> SophisticatedSurfaceVariant
                            },
                            border = BorderStroke(
                                1.dp,
                                if (step1Active || step1Completed) SophisticatedGold else SophisticatedGlassBorder
                            ),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (step1Completed && !step1Active) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = SophisticatedGold, modifier = Modifier.size(14.dp))
                                } else {
                                    Text(
                                        text = "1",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (step1Active) Color(0xFF121212) else SophisticatedOnSurfaceVariant
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Template",
                            fontSize = 12.sp,
                            fontWeight = if (step1Active) FontWeight.Bold else FontWeight.Normal,
                            color = if (step1Active) SophisticatedGold else SophisticatedOnSurfaceVariant
                        )
                    }

                    // Divider Line 1->2
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .padding(horizontal = 6.dp)
                            .background(
                                if (activeBuilderTab == 2 || activeBuilderTab == 0) SophisticatedGold else SophisticatedGlassBorder,
                                shape = RoundedCornerShape(1.dp)
                            )
                    )

                    // Step 2: Message Prompts (Tab 2)
                    val step2Active = activeBuilderTab == 2
                    val step2Completed = activeBuilderTab == 0
                    Row(
                        modifier = Modifier
                            .clickable { viewModel.guruActiveTab.value = 2 }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = when {
                                step2Active -> SophisticatedGold
                                step2Completed -> SophisticatedGoldPillBg
                                else -> SophisticatedSurfaceVariant
                            },
                            border = BorderStroke(
                                1.dp,
                                if (step2Active || step2Completed) SophisticatedGold else SophisticatedGlassBorder
                            ),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (step2Completed && !step2Active) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = SophisticatedGold, modifier = Modifier.size(14.dp))
                                } else {
                                    Text(
                                        text = "2",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (step2Active) Color(0xFF121212) else SophisticatedOnSurfaceVariant
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Prompts",
                            fontSize = 12.sp,
                            fontWeight = if (step2Active) FontWeight.Bold else FontWeight.Normal,
                            color = if (step2Active) SophisticatedGold else SophisticatedOnSurfaceVariant
                        )
                    }

                    // Divider Line 2->3
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .padding(horizontal = 6.dp)
                            .background(
                                if (activeBuilderTab == 0) SophisticatedGold else SophisticatedGlassBorder,
                                shape = RoundedCornerShape(1.dp)
                            )
                    )

                    // Step 3: Card Studio (Tab 0)
                    val step3Active = activeBuilderTab == 0
                    Row(
                        modifier = Modifier
                            .clickable { viewModel.guruActiveTab.value = 0 }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (step3Active) SophisticatedGold else SophisticatedSurfaceVariant,
                            border = BorderStroke(1.dp, if (step3Active) SophisticatedGold else SophisticatedGlassBorder),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "3",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (step3Active) Color(0xFF121212) else SophisticatedOnSurfaceVariant
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Studio",
                            fontSize = 12.sp,
                            fontWeight = if (step3Active) FontWeight.Bold else FontWeight.Normal,
                            color = if (step3Active) SophisticatedGold else SophisticatedOnSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Gallery Tab (Tab 1)
                    val galleryActive = activeBuilderTab == 1
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (galleryActive) SophisticatedGoldPillBg else Color.Transparent,
                        border = BorderStroke(1.dp, if (galleryActive) SophisticatedGold else SophisticatedGlassBorder),
                        modifier = Modifier.clickable { viewModel.guruActiveTab.value = 1 }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "Gallery",
                                tint = if (galleryActive) SophisticatedGold else SophisticatedOnSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Gallery (${savedCards.size})",
                                fontSize = 11.sp,
                                fontWeight = if (galleryActive) FontWeight.Bold else FontWeight.Normal,
                                color = if (galleryActive) SophisticatedGold else SophisticatedOnSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Preset Templates Carousel (Always accessible)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "✨ Instant Template Presets:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = SophisticatedGold
                )
            )
            Text(
                text = "Tap to load",
                style = MaterialTheme.typography.labelSmall.copy(color = SophisticatedOnSurfaceVariant)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PresetTemplateChip(
                icon = "🔭",
                title = "Physics Wonder",
                subtitle = "Vance (Cosmic)",
                isSelected = teacherName.contains("Vance")
            ) {
                viewModel.loadGuruPreset(
                    name = "Prof. Eleanor Vance",
                    subject = "Physics & Astronomy",
                    traits = "Passionate about astrophysics, explains relativity through cosmic stories, endlessly patient",
                    tone = "Inspiring",
                    templateIndex = 4,
                    themeIndex = 3,
                    accentIndex = 3,
                    signature = "Aarav Sharma",
                    role = "Astronomy Club Lead"
                )
            }
            PresetTemplateChip(
                icon = "📐",
                title = "Calculus Logic",
                subtitle = "Cooper (Luxe)",
                isSelected = teacherName.contains("Cooper")
            ) {
                viewModel.loadGuruPreset(
                    name = "Dr. Alan Cooper",
                    subject = "Mathematics & Calculus",
                    traits = "Makes integration intuitive, shows mathematical beauty in nature, cheerful mentor",
                    tone = "Poetic",
                    templateIndex = 0,
                    themeIndex = 1,
                    accentIndex = 0,
                    signature = "Meera Krishnan",
                    role = "Batch of 2026"
                )
            }
            PresetTemplateChip(
                icon = "🧪",
                title = "Chemistry Spark",
                subtitle = "Verma (Emerald)",
                isSelected = teacherName.contains("Verma")
            ) {
                viewModel.loadGuruPreset(
                    name = "Mrs. Sunita Verma",
                    subject = "Chemistry & Lab Sciences",
                    traits = "Catalyst for scientific curiosity, memorable chemical reaction demos, warm mentor",
                    tone = "Humorous",
                    templateIndex = 1,
                    themeIndex = 2,
                    accentIndex = 1,
                    signature = "Rohan Mehta",
                    role = "Chemistry Olympiad Team"
                )
            }
            PresetTemplateChip(
                icon = "📚",
                title = "Literature & Arts",
                subtitle = "Miller (Parchment)",
                isSelected = teacherName.contains("Miller")
            ) {
                viewModel.loadGuruPreset(
                    name = "Prof. Beatrice Miller",
                    subject = "English Literature & Poetry",
                    traits = "Brings Shakespeare and world poetry to life, teaches students to find their voice, thoughtful guide",
                    tone = "Poetic",
                    templateIndex = 2,
                    themeIndex = 1,
                    accentIndex = 2,
                    signature = "Sophia Chen",
                    role = "Editorial Board"
                )
            }
            PresetTemplateChip(
                icon = "🏛️",
                title = "School Principal",
                subtitle = "Reed (Regal)",
                isSelected = teacherName.contains("Reed")
            ) {
                viewModel.loadGuruPreset(
                    name = "Dr. Marcus Reed",
                    subject = "School Leadership & Ethics",
                    traits = "Visionary pillar of our institution, encourages student agency, leads with empathy",
                    tone = "Formal",
                    templateIndex = 0,
                    themeIndex = 4,
                    accentIndex = 0,
                    signature = "Student Council",
                    role = "Class Representatives"
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ANIMATED TRANSITION CONTAINER FOR BUILDER STEPS
        AnimatedContent(
            targetState = activeBuilderTab,
            transitionSpec = {
                val stepOrder = mapOf(3 to 1, 2 to 2, 0 to 3, 1 to 4)
                val initialStep = stepOrder[initialState] ?: 1
                val targetStep = stepOrder[targetState] ?: 1
                if (targetStep > initialStep) {
                    (slideInHorizontally(
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioNoBouncy),
                        initialOffsetX = { fullWidth -> (fullWidth * 0.4f).toInt() }
                    ) + fadeIn(tween(250))).togetherWith(
                        slideOutHorizontally(
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioNoBouncy),
                            targetOffsetX = { fullWidth -> (-fullWidth * 0.4f).toInt() }
                        ) + fadeOut(tween(200))
                    )
                } else {
                    (slideInHorizontally(
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioNoBouncy),
                        initialOffsetX = { fullWidth -> (-fullWidth * 0.4f).toInt() }
                    ) + fadeIn(tween(250))).togetherWith(
                        slideOutHorizontally(
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioNoBouncy),
                            targetOffsetX = { fullWidth -> (fullWidth * 0.4f).toInt() }
                        ) + fadeOut(tween(200))
                    )
                }
            },
            label = "builderStepAnimation"
        ) { targetTab ->
            when (targetTab) {
                // STEP 1 / TAB 3: TEMPLATES & STYLING (Aesthetics First)
                3 -> {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SophisticatedSurface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Live Miniature Preview Banner
                            val currentPreviewTheme = themes.getOrElse(cardThemeIndex) { themes[0] }
                            val currentPreviewAccent = accentIcons.getOrElse(selectedAccent) { accentIcons[0] }
                            val currentPreviewTemplate = templateNames.getOrElse(selectedTemplate) { templateNames[0] }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SophisticatedSurfaceVariant,
                                border = BorderStroke(1.dp, SophisticatedGoldBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(currentPreviewTheme.brush)
                                        .padding(14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = currentPreviewAccent.icon,
                                                    contentDescription = null,
                                                    tint = SophisticatedGold,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "LIVE TEMPLATE PREVIEW",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = SophisticatedGold,
                                                    letterSpacing = 1.sp
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = currentPreviewTemplate.title,
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = SophisticatedOnSurface
                                                )
                                            )
                                            Text(
                                                text = "Theme: ${currentPreviewTheme.name} • Crest: ${currentPreviewAccent.name}",
                                                style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurfaceVariant)
                                            )
                                        }
                                        Surface(
                                            shape = CircleShape,
                                            color = SophisticatedGoldPillBg,
                                            border = BorderStroke(1.dp, SophisticatedGoldBorder),
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = currentPreviewTemplate.icon,
                                                    contentDescription = null,
                                                    tint = SophisticatedGold,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "1. Select Card Template Layout",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SophisticatedGold
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            templateNames.forEachIndexed { index, template ->
                                val isSelected = selectedTemplate == index
                                val cardScale by animateFloatAsState(
                                    targetValue = if (isSelected) 1.01f else 1.0f,
                                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                                    label = "templateScale_$index"
                                )
                                val cardBgColor by animateColorAsState(
                                    targetValue = if (isSelected) SophisticatedGoldPillBg else SophisticatedSurfaceVariant,
                                    animationSpec = tween(200),
                                    label = "templateBg_$index"
                                )
                                val cardBorderColor by animateColorAsState(
                                    targetValue = if (isSelected) SophisticatedGold else SophisticatedGlassBorder,
                                    animationSpec = tween(200),
                                    label = "templateBorder_$index"
                                )

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = cardBgColor,
                                    border = BorderStroke(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = cardBorderColor
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { viewModel.guruSelectedTemplate.value = index }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(CircleShape)
                                                .background(if (isSelected) SophisticatedGold else Color(0x33FFFFFF)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = template.icon,
                                                contentDescription = null,
                                                tint = if (isSelected) Color(0xFF121212) else SophisticatedGold,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = template.title,
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected) SophisticatedGold else SophisticatedOnSurface
                                                )
                                            )
                                            Text(
                                                text = template.description,
                                                style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurfaceVariant)
                                            )
                                        }
                                        AnimatedVisibility(
                                            visible = isSelected,
                                            enter = scaleIn(tween(200)) + fadeIn(tween(200)),
                                            exit = scaleOut(tween(150)) + fadeOut(tween(150))
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = SophisticatedGold,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = SophisticatedGlassBorder)
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "2. Color Palette & Luxury Gradients",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SophisticatedGold
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                themes.forEachIndexed { idx, thm ->
                                    val isSelected = cardThemeIndex == idx
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.clickable { viewModel.guruCardTheme.value = idx }
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .clip(CircleShape)
                                                .background(thm.dotColor)
                                                .border(
                                                    width = if (isSelected) 3.dp else 1.dp,
                                                    color = if (isSelected) SophisticatedGold else Color.Gray.copy(alpha = 0.4f),
                                                    shape = CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            androidx.compose.animation.AnimatedVisibility(
                                                visible = isSelected,
                                                enter = scaleIn() + fadeIn(),
                                                exit = scaleOut() + fadeOut()
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = SophisticatedGold,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = thm.name,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = if (isSelected) SophisticatedGold else SophisticatedOnSurfaceVariant,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = SophisticatedGlassBorder)
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "3. Crest & Emblem Ornament",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SophisticatedGold
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                accentIcons.forEachIndexed { idx, acc ->
                                    val isSelected = selectedAccent == idx
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { viewModel.guruSelectedAccent.value = idx },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = acc.icon,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp),
                                                tint = if (isSelected) Color(0xFF121212) else SophisticatedGold
                                            )
                                        },
                                        label = { Text(acc.name) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = SophisticatedGold,
                                            selectedLabelColor = Color(0xFF121212),
                                            containerColor = SophisticatedSurfaceVariant,
                                            labelColor = SophisticatedOnSurface
                                        ),
                                        border = FilterChipDefaults.filterChipBorder(
                                            enabled = true,
                                            selected = isSelected,
                                            borderColor = if (isSelected) SophisticatedGold else SophisticatedGlassBorder
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Step 1 Navigation Buttons
                            Button(
                                onClick = { viewModel.guruActiveTab.value = 2 },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SophisticatedGold,
                                    contentColor = Color(0xFF121212)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Proceed to Message Prompts", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFF121212))
                            }
                        }
                    }
                }

                // STEP 2 / TAB 2: TEACHER & STUDENT DETAILS / PROMPTS
                2 -> {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SophisticatedSurface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "1. Teacher Information",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SophisticatedGold
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            // Teacher Name
                            OutlinedTextField(
                                value = teacherName,
                                onValueChange = { viewModel.guruTeacherName.value = it },
                                label = { Text("Teacher Name & Title") },
                                placeholder = { Text("e.g. Prof. Eleanor Vance / Dr. Sarah Jenkins") },
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
                                    .fillMaxWidth()
                                    .testTag("guru_teacher_name_input"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Subject / Department
                            OutlinedTextField(
                                value = subject,
                                onValueChange = { viewModel.guruSubject.value = it },
                                label = { Text("Subject / Department") },
                                placeholder = { Text("e.g. Physics & Astronomy, Calculus, Literature, Chemistry") },
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
                                    .testTag("guru_subject_input"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Key Traits
                            OutlinedTextField(
                                value = keyTraits,
                                onValueChange = { viewModel.guruKeyTraits.value = it },
                                label = { Text("Key Traits, Memories & Special Analogies") },
                                placeholder = { Text("e.g. Uses space analogies, stays after class to help, patient, inspiring") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Outlined.Psychology, contentDescription = null, tint = SophisticatedGold)
                                },
                                minLines = 2,
                                maxLines = 4,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SophisticatedGold,
                                    focusedLabelColor = SophisticatedGold,
                                    cursorColor = SophisticatedGold,
                                    focusedLeadingIconColor = SophisticatedGold,
                                    unfocusedBorderColor = Color(0xFF383838)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("guru_traits_input"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Quick Trait Suggestions Chips
                            Text(
                                text = "✨ Tap to append inspiration traits:",
                                style = MaterialTheme.typography.labelSmall.copy(color = SophisticatedGold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val traitSuggestions = listOf(
                                    "✦ Explains with vivid stories",
                                    "✦ Endlessly patient & encouraging",
                                    "✦ Inspires curiosity",
                                    "✦ Makes hard concepts intuitive",
                                    "✦ Lifelong mentor"
                                )
                                traitSuggestions.forEach { traitChip ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = SophisticatedSurfaceVariant,
                                        border = BorderStroke(1.dp, SophisticatedGlassBorder),
                                        modifier = Modifier.clickable {
                                            val cleanTrait = traitChip.removePrefix("✦ ")
                                            val current = viewModel.guruKeyTraits.value
                                            viewModel.guruKeyTraits.value = if (current.isBlank()) cleanTrait else "$current, $cleanTrait"
                                        }
                                    ) {
                                        Text(
                                            text = traitChip,
                                            fontSize = 11.sp,
                                            color = SophisticatedOnSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = SophisticatedGlassBorder)
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "2. Student Signature & Note",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SophisticatedGold
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = studentSignature,
                                    onValueChange = { viewModel.guruStudentSignature.value = it },
                                    label = { Text("Student Name") },
                                    placeholder = { Text("e.g. Aarav Sharma") },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = SophisticatedGold,
                                        focusedLabelColor = SophisticatedGold,
                                        cursorColor = SophisticatedGold,
                                        unfocusedBorderColor = Color(0xFF383838)
                                    ),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                OutlinedTextField(
                                    value = studentRole,
                                    onValueChange = { viewModel.guruStudentRole.value = it },
                                    label = { Text("Role / Class") },
                                    placeholder = { Text("e.g. Class of 2026") },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = SophisticatedGold,
                                        focusedLabelColor = SophisticatedGold,
                                        cursorColor = SophisticatedGold,
                                        unfocusedBorderColor = Color(0xFF383838)
                                    ),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = backNote,
                                onValueChange = { viewModel.guruBackNote.value = it },
                                label = { Text("Card Back Handwritten Message") },
                                placeholder = { Text("A personal note from the heart...") },
                                minLines = 2,
                                maxLines = 3,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SophisticatedGold,
                                    focusedLabelColor = SophisticatedGold,
                                    cursorColor = SophisticatedGold,
                                    unfocusedBorderColor = Color(0xFF383838)
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = SophisticatedGlassBorder)
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "3. Tone & Language",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SophisticatedGold
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Tone chips
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                tones.forEach { toneOption ->
                                    FilterChip(
                                        selected = tone == toneOption,
                                        onClick = { viewModel.guruTone.value = toneOption },
                                        label = { Text(toneOption) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = SophisticatedGold,
                                            selectedLabelColor = Color(0xFF121212),
                                            containerColor = SophisticatedSurfaceVariant,
                                            labelColor = SophisticatedOnSurface
                                        ),
                                        border = FilterChipDefaults.filterChipBorder(
                                            enabled = true,
                                            selected = tone == toneOption,
                                            borderColor = if (tone == toneOption) SophisticatedGold else SophisticatedGlassBorder
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Language dropdown
                            ExposedDropdownMenuBox(
                                expanded = isLanguageMenuExpanded,
                                onExpandedChange = { isLanguageMenuExpanded = it }
                            ) {
                                OutlinedTextField(
                                    value = language,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Language") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isLanguageMenuExpanded) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = SophisticatedGold,
                                        focusedLabelColor = SophisticatedGold,
                                        unfocusedBorderColor = Color(0xFF383838)
                                    ),
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = isLanguageMenuExpanded,
                                    onDismissRequest = { isLanguageMenuExpanded = false },
                                    modifier = Modifier.background(SophisticatedSurface)
                                ) {
                                    languages.forEach { lang ->
                                        DropdownMenuItem(
                                            text = { Text(lang, color = SophisticatedOnSurface) },
                                            onClick = {
                                                viewModel.guruLanguage.value = lang
                                                isLanguageMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Step 2 Navigation Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.guruActiveTab.value = 3 },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = SophisticatedGold
                                    ),
                                    border = BorderStroke(1.dp, SophisticatedGoldBorder),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Templates", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }

                                Button(
                                    onClick = {
                                        viewModel.guruActiveTab.value = 0
                                        viewModel.generateAiGuru()
                                    },
                                    modifier = Modifier
                                        .weight(2f)
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SophisticatedGold,
                                        contentColor = Color(0xFF121212)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF121212), modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Generate with Gemini", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }

                // STEP 3 / TAB 0: LIVE PREVIEW & STUDIO
                0 -> {
                    Column {
                        // Studio Header Navigation Bar to jump back to edit
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { viewModel.guruActiveTab.value = 3 },
                                colors = ButtonDefaults.textButtonColors(contentColor = SophisticatedGold)
                            ) {
                                Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Change Template", fontSize = 12.sp)
                            }

                            TextButton(
                                onClick = { viewModel.guruActiveTab.value = 2 },
                                colors = ButtonDefaults.textButtonColors(contentColor = SophisticatedGold)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Edit Prompts", fontSize = 12.sp)
                            }
                        }

                        // Animated State Transition for Card Canvas
                        AnimatedContent(
                            targetState = guruState,
                            transitionSpec = {
                                (fadeIn(tween(300)) + scaleIn(spring(stiffness = Spring.StiffnessMediumLow), initialScale = 0.96f))
                                    .togetherWith(fadeOut(tween(200)))
                            },
                            label = "guruStateTransition"
                        ) { state ->
                            when (state) {
                                is EngineUiState.Loading -> {
                                    LoadingCard(message = "Gemini AI is crafting subject metaphors, poetic rhymes & digital badges...")
                                }
                                is EngineUiState.SafetyRejection -> {
                                    SafetyRejectionCard(
                                        reason = state.reason,
                                        politeAlternative = state.politeAlternative,
                                        rawJson = state.rawJson,
                                        onUseAlternative = {
                                            viewModel.guruKeyTraits.value = "Dedicated educator who inspires students with knowledge, guidance, and patience."
                                            viewModel.generateAiGuru()
                                        }
                                    )
                                }
                                is EngineUiState.Success -> {
                                    currentJsonPayload = state.rawJson
                                    InteractiveGuruCardPreview(
                                        response = state.data,
                                        selectedTemplate = selectedTemplate,
                                        theme = themes.getOrElse(cardThemeIndex) { themes[0] },
                                        accent = accentIcons.getOrElse(selectedAccent) { accentIcons[0] },
                                        studentSignature = studentSignature,
                                        studentRole = studentRole,
                                        backNote = backNote,
                                        isFlipped = isCardFlipped,
                                        isPlayingTts = viewModel.isTtsPlaying.collectAsState().value,
                                        onTogglePlayCardTts = {
                                            if (viewModel.isTtsPlaying.value) {
                                                viewModel.stopTts()
                                            } else {
                                                viewModel.playCardTts(
                                                    teacherName = state.data.teacherName,
                                                    message = state.data.personalizedMessage,
                                                    poemStanza = state.data.rhymingStanza,
                                                    language = "English"
                                                )
                                            }
                                        },
                                        onFlipCard = { viewModel.toggleCardFlip() },
                                        onEditContent = { showEditCardDialog = true },
                                        onAddBadge = { showAddBadgeDialog = true },
                                        onRemoveBadge = { badge -> viewModel.removeGuruBadge(badge) },
                                        onSaveCard = {
                                            viewModel.saveCurrentCard()
                                            Toast.makeText(context, "Appreciation card saved to Gallery!", Toast.LENGTH_SHORT).show()
                                        },
                                        onCopyCard = {
                                            val tributeFull = buildFormattedTribute(state.data, studentSignature, studentRole, backNote)
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("EduTribute Card", tributeFull)
                                            clipboard.setPrimaryClip(clip)
                                            Toast.makeText(context, "Appreciation Card copied to clipboard!", Toast.LENGTH_SHORT).show()
                                        },
                                        onShareCard = {
                                            val tributeFull = buildFormattedTribute(state.data, studentSignature, studentRole, backNote)
                                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                type = "text/plain"
                                                putExtra(Intent.EXTRA_SUBJECT, "Teachers' Day Appreciation Card for ${state.data.teacherName}")
                                                putExtra(Intent.EXTRA_TEXT, tributeFull)
                                            }
                                            context.startActivity(Intent.createChooser(shareIntent, "Share Appreciation Card"))
                                        },
                                        onViewJson = { showJsonDialog = true }
                                    )
                                }
                                is EngineUiState.Error -> {
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                text = "Generation Error: ${state.message}",
                                                color = MaterialTheme.colorScheme.onErrorContainer
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Button(
                                                onClick = { viewModel.generateAiGuru() },
                                                colors = ButtonDefaults.buttonColors(containerColor = SophisticatedGold, contentColor = Color(0xFF121212))
                                            ) {
                                                Text("Retry Generation")
                                            }
                                        }
                                    }
                                }
                                is EngineUiState.Idle -> {
                                    // Interactive Canvas Empty State with Instant Generate Call-to-Action
                                    Surface(
                                        color = SophisticatedSurface,
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(16.dp))
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(20.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(56.dp)
                                                    .clip(CircleShape)
                                                    .background(SophisticatedGoldPillBg)
                                                    .border(1.dp, SophisticatedGoldBorder, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.AutoAwesome,
                                                    contentDescription = null,
                                                    tint = SophisticatedGold,
                                                    modifier = Modifier.size(30.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(12.dp))
                                            Text(
                                                text = "Create Bespoke Appreciation Card",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = SophisticatedGold
                                                )
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Honor ${teacherName.ifBlank { "your teacher" }} with tailored metaphors, rhyming verses, and digital badges.",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = SophisticatedOnSurfaceVariant,
                                                    textAlign = TextAlign.Center
                                                )
                                            )
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Button(
                                                onClick = { viewModel.generateAiGuru() },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(48.dp)
                                                    .testTag("guru_generate_button"),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = SophisticatedGold,
                                                    contentColor = Color(0xFF121212)
                                                ),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF121212))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Generate Appreciation Card with Gemini", fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // TAB 1: GALLERY VIEW (Saved appreciation cards)
                1 -> {
                    GratitudeCardGalleryView(
                        savedCards = savedCards,
                        onOpenInStudio = { card ->
                            viewModel.loadSavedCard(card)
                            Toast.makeText(context, "Loaded card for ${card.teacherName} in Card Studio", Toast.LENGTH_SHORT).show()
                        },
                        onDeleteCard = { cardId ->
                            viewModel.deleteSavedCard(cardId)
                        },
                        onClearAll = {
                            viewModel.clearAllSavedCards()
                        },
                        onCreateNewCard = {
                            viewModel.guruActiveTab.value = 3
                        }
                    )
                }
            }
        }
    }

    // Edit Card Dialog (allows in-place tweaks of AI output)
    if (showEditCardDialog && guruState is EngineUiState.Success) {
        val currentData = (guruState as EngineUiState.Success<AiGuruResponse>).data
        var editMessage by remember { mutableStateOf(currentData.personalizedMessage) }
        var editLine1 by remember { mutableStateOf(currentData.rhymingStanza.getOrElse(0) { "" }) }
        var editLine2 by remember { mutableStateOf(currentData.rhymingStanza.getOrElse(1) { "" }) }
        var editLine3 by remember { mutableStateOf(currentData.rhymingStanza.getOrElse(2) { "" }) }
        var editLine4 by remember { mutableStateOf(currentData.rhymingStanza.getOrElse(3) { "" }) }

        AlertDialog(
            onDismissRequest = { showEditCardDialog = false },
            containerColor = SophisticatedSurface,
            title = {
                Text(
                    text = "Edit Appreciation Content",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SophisticatedGold)
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Customize the generated text before sharing or exporting:",
                        style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurfaceVariant)
                    )

                    OutlinedTextField(
                        value = editMessage,
                        onValueChange = { editMessage = it },
                        label = { Text("Personalized Tribute Message") },
                        minLines = 3,
                        maxLines = 6,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SophisticatedGold,
                            focusedLabelColor = SophisticatedGold,
                            cursorColor = SophisticatedGold,
                            unfocusedBorderColor = Color(0xFF383838)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "4-Line Rhyming Stanza:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = SophisticatedGold)
                    )

                    OutlinedTextField(
                        value = editLine1,
                        onValueChange = { editLine1 = it },
                        label = { Text("Poem Line 1") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editLine2,
                        onValueChange = { editLine2 = it },
                        label = { Text("Poem Line 2") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editLine3,
                        onValueChange = { editLine3 = it },
                        label = { Text("Poem Line 3") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editLine4,
                        onValueChange = { editLine4 = it },
                        label = { Text("Poem Line 4") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateGuruMessage(editMessage)
                        viewModel.updateGuruStanza(listOf(editLine1, editLine2, editLine3, editLine4))
                        showEditCardDialog = false
                        Toast.makeText(context, "Card content updated!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SophisticatedGold, contentColor = Color(0xFF121212))
                ) {
                    Text("Save Changes", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditCardDialog = false }) {
                    Text("Cancel", color = SophisticatedOnSurfaceVariant)
                }
            }
        )
    }

    // Add Custom Badge Dialog
    if (showAddBadgeDialog) {
        AlertDialog(
            onDismissRequest = { showAddBadgeDialog = false },
            containerColor = SophisticatedSurface,
            title = {
                Text(
                    text = "Add Custom Digital Badge",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SophisticatedGold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Add a personalized honor or badge title (e.g., 'Master of Patience', 'Quantum Mentor'):",
                        style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurfaceVariant)
                    )
                    OutlinedTextField(
                        value = newBadgeText,
                        onValueChange = { newBadgeText = it },
                        placeholder = { Text("e.g. Master of Galactic Analogies") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SophisticatedGold,
                            focusedLabelColor = SophisticatedGold,
                            cursorColor = SophisticatedGold,
                            unfocusedBorderColor = Color(0xFF383838)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newBadgeText.isNotBlank()) {
                            viewModel.addGuruBadge(newBadgeText.trim())
                            newBadgeText = ""
                            showAddBadgeDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SophisticatedGold, contentColor = Color(0xFF121212))
                ) {
                    Text("Add Badge", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddBadgeDialog = false }) {
                    Text("Cancel", color = SophisticatedOnSurfaceVariant)
                }
            }
        )
    }

    if (showJsonDialog) {
        JsonViewerDialog(
            title = "AI_GURU JSON Output Schema",
            jsonString = currentJsonPayload,
            onDismiss = { showJsonDialog = false }
        )
    }
}

// -------------------------------------------------------------------------------------------------
// Interactive Live Card Canvas (Front & Back Views with 5 Distinct Templates)
// -------------------------------------------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InteractiveGuruCardPreview(
    response: AiGuruResponse,
    selectedTemplate: Int,
    theme: ThemeOption,
    accent: AccentOption,
    studentSignature: String,
    studentRole: String,
    backNote: String,
    isFlipped: Boolean,
    isPlayingTts: Boolean = false,
    onTogglePlayCardTts: () -> Unit = {},
    onFlipCard: () -> Unit,
    onEditContent: () -> Unit,
    onAddBadge: () -> Unit,
    onRemoveBadge: (String) -> Unit,
    onSaveCard: () -> Unit,
    onCopyCard: () -> Unit,
    onShareCard: () -> Unit,
    onViewJson: () -> Unit
) {
    val currentDate = remember { SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("guru_result_container")
    ) {
        // Card Canvas Control Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.WorkspacePremium, contentDescription = null, tint = SophisticatedGold, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isFlipped) "Card Canvas (Back Note)" else "Card Canvas (Front Cover)",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = SophisticatedGold)
                )
            }

            // Flip Button, Listen TTS Button, Share Button & Edit Button
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isPlayingTts) Color(0xFFE57373) else SophisticatedGoldPillBg,
                    border = BorderStroke(1.dp, if (isPlayingTts) Color(0xFFE57373) else SophisticatedGoldBorder),
                    modifier = Modifier
                        .clickable { onTogglePlayCardTts() }
                        .testTag("guru_listen_tts_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isPlayingTts) Icons.Default.Stop else Icons.Default.VolumeUp,
                            contentDescription = "Listen Aloud",
                            tint = if (isPlayingTts) Color(0xFF121212) else SophisticatedGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isPlayingTts) "Stop" else "Listen",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPlayingTts) Color(0xFF121212) else SophisticatedGold
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SophisticatedGoldPillBg,
                    border = BorderStroke(1.dp, SophisticatedGoldBorder),
                    modifier = Modifier
                        .clickable { onShareCard() }
                        .testTag("guru_top_share_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Appreciation Card",
                            tint = SophisticatedGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Share",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SophisticatedGold
                        )
                    }
                }

                OutlinedButton(
                    onClick = onFlipCard,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SophisticatedGold),
                    border = BorderStroke(1.dp, SophisticatedGoldBorder),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(imageVector = Icons.Default.FlipCameraAndroid, contentDescription = "Flip Card", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isFlipped) "View Front" else "Flip Note", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onEditContent,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SophisticatedGold),
                    border = BorderStroke(1.dp, SophisticatedGlassBorder),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Text", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Card Container with Flip/Transition Animation
        AnimatedContent(
            targetState = isFlipped,
            transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(250)) },
            label = "CardFlipAnimation"
        ) { flipped ->
            if (flipped) {
                // BACK SIDE OF THE CARD
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    shadowElevation = 10.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, theme.border, RoundedCornerShape(24.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(theme.brush)
                            .padding(24.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Back Seal
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(SophisticatedGoldPillBg)
                                    .border(1.5.dp, SophisticatedGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = accent.icon,
                                    contentDescription = null,
                                    tint = SophisticatedGold,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "PERSONAL STUDENT DEDICATION",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SophisticatedGold,
                                    letterSpacing = 1.5.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Surface(
                                color = Color(0x33000000),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(14.dp))
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Icon(imageVector = Icons.Default.FormatQuote, contentDescription = null, tint = SophisticatedGold, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = backNote.ifBlank { "Thank you for being an exceptional mentor whose lessons continue to shape our lives." },
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontStyle = FontStyle.Italic,
                                            color = SophisticatedOnSurface,
                                            lineHeight = 24.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Student Signature Block
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Column {
                                    Text(
                                        text = "Date Issued:",
                                        style = MaterialTheme.typography.labelSmall.copy(color = SophisticatedOnSurfaceVariant)
                                    )
                                    Text(
                                        text = currentDate,
                                        style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedGold, fontWeight = FontWeight.SemiBold)
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Presented By:",
                                        style = MaterialTheme.typography.labelSmall.copy(color = SophisticatedOnSurfaceVariant)
                                    )
                                    Text(
                                        text = studentSignature.ifBlank { "Grateful Student" },
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = SophisticatedGold,
                                            fontStyle = FontStyle.Italic
                                        )
                                    )
                                    if (studentRole.isNotBlank()) {
                                        Text(
                                            text = studentRole,
                                            style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurfaceVariant)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // FRONT SIDE OF THE CARD (Renders chosen Template)
                when (selectedTemplate) {
                    1 -> ModernLuminaryCardLayout(response, theme, accent, studentSignature, onAddBadge, onRemoveBadge)
                    2 -> ClassicScrollCardLayout(response, theme, accent, studentSignature, onAddBadge, onRemoveBadge)
                    3 -> EditorialMinimalistCardLayout(response, theme, accent, studentSignature, onAddBadge, onRemoveBadge)
                    4 -> CosmicNebulaCardLayout(response, theme, accent, studentSignature, onAddBadge, onRemoveBadge)
                    else -> LuxeGoldCertificateLayout(response, theme, accent, studentSignature, onAddBadge, onRemoveBadge)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Toolbar: Primary Share Card & Save to Gallery, Secondary Copy & JSON
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onShareCard,
                modifier = Modifier
                    .weight(1.1f)
                    .height(48.dp)
                    .testTag("guru_share_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SophisticatedGold,
                    contentColor = Color(0xFF121212)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = Color(0xFF121212), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share Card", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onSaveCard,
                modifier = Modifier
                    .weight(0.9f)
                    .height(48.dp)
                    .testTag("guru_save_gallery_button"),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SophisticatedGold),
                border = BorderStroke(1.dp, SophisticatedGoldBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.BookmarkAdd, contentDescription = null, tint = SophisticatedGold, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Save Card", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onCopyCard,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .testTag("guru_copy_button"),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SophisticatedGold),
                border = BorderStroke(1.dp, SophisticatedGlassBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Copy Tribute")
            }

            IconButton(
                onClick = onViewJson,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SophisticatedSurfaceVariant)
                    .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(12.dp))
                    .testTag("guru_json_btn")
            ) {
                Icon(imageVector = Icons.Default.Code, contentDescription = "View JSON", tint = SophisticatedGold)
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// TEMPLATE 0: LUXE GOLD CERTIFICATE
// -------------------------------------------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LuxeGoldCertificateLayout(
    response: AiGuruResponse,
    theme: ThemeOption,
    accent: AccentOption,
    studentSignature: String,
    onAddBadge: () -> Unit,
    onRemoveBadge: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, SophisticatedGoldBorder, RoundedCornerShape(24.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(theme.brush)
                .padding(22.dp)
        ) {
            Column {
                // Top Seal Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = SophisticatedGoldPillBg,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.border(1.dp, SophisticatedGoldBorder, RoundedCornerShape(8.dp))
                    ) {
                        Text(
                            text = "HONORARY DISTINCTION 2026",
                            color = SophisticatedGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            letterSpacing = 1.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SophisticatedGoldPillBg)
                            .border(1.dp, SophisticatedGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = accent.icon, contentDescription = null, tint = SophisticatedGold, modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Title
                Text(
                    text = response.teacherName,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF5E6C8)
                    )
                )
                Text(
                    text = "Distinguished Educator • Department of ${response.subject}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = SophisticatedGold,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Personalized Subject Metaphor
                Surface(
                    color = Color(0x33000000),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Subject Tribute",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = SophisticatedGold,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = response.personalizedMessage,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = SophisticatedOnSurface,
                                lineHeight = 22.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4-Line Rhyming Stanza
                if (response.rhymingStanza.isNotEmpty()) {
                    Surface(
                        color = Color(0x44000000),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SophisticatedGoldBorder, RoundedCornerShape(12.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = null,
                                tint = SophisticatedGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            response.rhymingStanza.forEach { line ->
                                Text(
                                    text = line,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontStyle = FontStyle.Italic,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFFF5E6C8),
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Badges with Interactive Add & Remove
                BadgesSection(
                    badges = response.badgeNames,
                    onAddBadge = onAddBadge,
                    onRemoveBadge = onRemoveBadge
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// TEMPLATE 1: MODERN LUMINARY GLASS
// -------------------------------------------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ModernLuminaryCardLayout(
    response: AiGuruResponse,
    theme: ThemeOption,
    accent: AccentOption,
    studentSignature: String,
    onAddBadge: () -> Unit,
    onRemoveBadge: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, theme.border, RoundedCornerShape(20.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(theme.brush)
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(SophisticatedGold)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "LUMINARY SPOTLIGHT",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SophisticatedGold,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                    }

                    Surface(
                        color = Color(0x33FFFFFF),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = response.tone.uppercase(),
                            color = SophisticatedGoldLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = response.teacherName,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = SophisticatedOnSurface,
                        fontSize = 24.sp
                    )
                )
                Text(
                    text = "Faculty of ${response.subject}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = SophisticatedGold,
                        fontWeight = FontWeight.Medium
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Message
                Text(
                    text = response.personalizedMessage,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = SophisticatedOnSurface,
                        lineHeight = 22.sp
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Poem
                if (response.rhymingStanza.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0x22FFFFFF), RoundedCornerShape(12.dp))
                            .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            response.rhymingStanza.forEach { line ->
                                Text(
                                    text = "“ $line ”",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontStyle = FontStyle.Italic,
                                        color = SophisticatedGoldLight
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                BadgesSection(
                    badges = response.badgeNames,
                    onAddBadge = onAddBadge,
                    onRemoveBadge = onRemoveBadge
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// TEMPLATE 2: CLASSIC SCROLL & PARCHMENT
// -------------------------------------------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ClassicScrollCardLayout(
    response: AiGuruResponse,
    theme: ThemeOption,
    accent: AccentOption,
    studentSignature: String,
    onAddBadge: () -> Unit,
    onRemoveBadge: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFF8C7545), RoundedCornerShape(18.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(ParchmentGoldTop, ParchmentGoldBottom)))
                .padding(22.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.HistoryEdu,
                    contentDescription = null,
                    tint = SophisticatedGold,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "TO OUR REVERED MENTOR",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = SophisticatedGold,
                        letterSpacing = 2.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = response.teacherName,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF5E6C8),
                        textAlign = TextAlign.Center
                    )
                )
                Text(
                    text = "Department of ${response.subject}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = SophisticatedGold,
                        fontStyle = FontStyle.Italic
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Color(0xFF8C7545), thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = response.personalizedMessage,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFFE2D4B7),
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (response.rhymingStanza.isNotEmpty()) {
                    Surface(
                        color = Color(0x33000000),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0x44D1B06B), RoundedCornerShape(10.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            response.rhymingStanza.forEach { line ->
                                Text(
                                    text = line,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontStyle = FontStyle.Italic,
                                        color = Color(0xFFF5E6C8),
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                BadgesSection(
                    badges = response.badgeNames,
                    onAddBadge = onAddBadge,
                    onRemoveBadge = onRemoveBadge
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// TEMPLATE 3: EDITORIAL MINIMALIST
// -------------------------------------------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EditorialMinimalistCardLayout(
    response: AiGuruResponse,
    theme: ThemeOption,
    accent: AccentOption,
    studentSignature: String,
    onAddBadge: () -> Unit,
    onRemoveBadge: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF383838), RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF141414))
                .padding(22.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "TEACHERS' DAY // 2026",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF888888),
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = response.language.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SophisticatedGold,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = response.teacherName,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = response.subject,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = SophisticatedGold,
                        fontWeight = FontWeight.Normal
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(60.dp)
                            .background(SophisticatedGold)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = response.personalizedMessage,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFFCCCCCC),
                            lineHeight = 22.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (response.rhymingStanza.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        response.rhymingStanza.forEach { line ->
                            Text(
                                text = line,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontStyle = FontStyle.Italic,
                                    color = Color(0xFFE0E0E0)
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                BadgesSection(
                    badges = response.badgeNames,
                    onAddBadge = onAddBadge,
                    onRemoveBadge = onRemoveBadge
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// TEMPLATE 4: COSMIC NEBULA
// -------------------------------------------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CosmicNebulaCardLayout(
    response: AiGuruResponse,
    theme: ThemeOption,
    accent: AccentOption,
    studentSignature: String,
    onAddBadge: () -> Unit,
    onRemoveBadge: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, Color(0x66818CF8), RoundedCornerShape(22.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(IndigoGradientTop, VioletNebulaBottom)))
                .padding(22.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "STELLAR LUMINARY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFFBBF24),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                        )
                    }

                    Surface(
                        color = Color(0x336366F1),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.border(0.5.dp, Color(0x66818CF8), RoundedCornerShape(8.dp))
                    ) {
                        Text(
                            text = "COSMIC TRIBUTE",
                            color = Color(0xFFC7D2FE),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = response.teacherName,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEEF2FF)
                    )
                )
                Text(
                    text = "Beacon of Wisdom • ${response.subject}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = Color(0xFF93C5FD),
                        fontWeight = FontWeight.Medium
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    color = Color(0x33000000),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0x33818CF8), RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = response.personalizedMessage,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFFE0E7FF),
                            lineHeight = 22.sp
                        ),
                        modifier = Modifier.padding(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (response.rhymingStanza.isNotEmpty()) {
                    Surface(
                        color = Color(0x440F172A),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0x44FBBF24), RoundedCornerShape(12.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            response.rhymingStanza.forEach { line ->
                                Text(
                                    text = "✦ $line ✦",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontStyle = FontStyle.Italic,
                                        color = Color(0xFFFDE68A),
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                BadgesSection(
                    badges = response.badgeNames,
                    onAddBadge = onAddBadge,
                    onRemoveBadge = onRemoveBadge
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Interactive Digital Badges Section with Add/Remove
// -------------------------------------------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BadgesSection(
    badges: List<String>,
    onAddBadge: () -> Unit,
    onRemoveBadge: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "HONORARY DIGITAL BADGES",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = SophisticatedGold,
                    letterSpacing = 1.sp
                )
            )

            // Add Custom Badge Mini Button
            Surface(
                color = SophisticatedGoldPillBg,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .border(0.5.dp, SophisticatedGoldBorder, RoundedCornerShape(12.dp))
                    .clickable { onAddBadge() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Badge", tint = SophisticatedGold, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Add Badge", color = SophisticatedGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            badges.forEach { badge ->
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SophisticatedGoldPillBg,
                    modifier = Modifier.border(1.dp, SophisticatedGoldBorder, RoundedCornerShape(20.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = null,
                            tint = SophisticatedGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = badge,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = SophisticatedGold,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = SophisticatedGold.copy(alpha = 0.6f),
                            modifier = Modifier
                                .size(14.dp)
                                .clickable { onRemoveBadge(badge) }
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Helper Preset Template Chip Component
// -------------------------------------------------------------------------------------------------
@Composable
private fun PresetTemplateChip(
    icon: String,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) SophisticatedGoldPillBg else SophisticatedSurfaceVariant,
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) SophisticatedGold else SophisticatedGlassBorder
        ),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) SophisticatedGold else SophisticatedOnSurface
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = SophisticatedOnSurfaceVariant,
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Helper Models & Formatter
// -------------------------------------------------------------------------------------------------
data class TemplateOption(val title: String, val icon: ImageVector, val description: String)
data class AccentOption(val name: String, val icon: ImageVector)
data class ThemeOption(val name: String, val dotColor: Color, val brush: Brush, val border: Color)

private fun buildFormattedTribute(
    response: AiGuruResponse,
    studentSignature: String,
    studentRole: String,
    backNote: String
): String = buildString {
    appendLine("══════════════════════════════════════════════")
    appendLine("🎓 TEACHERS' DAY APPRECIATION TRIBUTE 2026")
    appendLine("══════════════════════════════════════════════")
    appendLine("Honoring: ${response.teacherName}")
    appendLine("Department: ${response.subject}")
    appendLine("Tone: ${response.tone} | Language: ${response.language}")
    appendLine()
    appendLine("✨ PERSONALIZED TRIBUTE:")
    appendLine(response.personalizedMessage)
    appendLine()
    if (response.rhymingStanza.isNotEmpty()) {
        appendLine("📜 RHYMING STANZA:")
        response.rhymingStanza.forEach { appendLine("  \"$it\"") }
        appendLine()
    }
    if (response.badgeNames.isNotEmpty()) {
        appendLine("🎖️ DIGITAL HONORS: " + response.badgeNames.joinToString(" • "))
        appendLine()
    }
    if (backNote.isNotBlank()) {
        appendLine("💌 PERSONAL STUDENT DEDICATION:")
        appendLine("  \"$backNote\"")
        appendLine()
    }
    appendLine("— Presented with gratitude by ${studentSignature.ifBlank { "A Grateful Student" }}${if (studentRole.isNotBlank()) " ($studentRole)" else ""}")
    appendLine("══════════════════════════════════════════════")
}
