package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.model.EduModule
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
fun EngineTesterScreen(
    viewModel: EduTributeViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedModule by viewModel.apiTesterSelectedModule.collectAsState()
    val isExecuting by viewModel.apiTesterIsExecuting.collectAsState()
    val rawJson by viewModel.apiTesterRawJson.collectAsState()

    val testModules = listOf(
        EduModule.AI_GURU,
        EduModule.VOICE_OF_GRATITUDE,
        EduModule.TRIVIA_BATTLE
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
                        imageVector = Icons.Default.Code,
                        contentDescription = null,
                        tint = Color(0xFF121212),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "API ENGINE TESTER & JSON INSPECTOR",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = SophisticatedGold,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "Clean JSON Schema Validation",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = SophisticatedOnSurface,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Inspect and test real-time JSON response schemas across all 3 EduTribute modules.",
                        style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurfaceVariant)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Module Selector
        Text(
            text = "Select Module to Execute:",
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
            testModules.forEach { mod ->
                FilterChip(
                    selected = selectedModule == mod,
                    onClick = { viewModel.apiTesterSelectedModule.value = mod },
                    label = { Text(mod.title) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SophisticatedGold,
                        selectedLabelColor = Color(0xFF121212),
                        containerColor = SophisticatedSurfaceVariant,
                        labelColor = SophisticatedOnSurface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedModule == mod,
                        borderColor = if (selectedModule == mod) SophisticatedGold else SophisticatedGlassBorder
                    ),
                    modifier = Modifier.testTag("engine_chip_${mod.name.lowercase()}")
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Specification & Active Inputs Summary
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
                    text = "Active Module Specifications:",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = SophisticatedOnSurface
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                when (selectedModule) {
                    EduModule.AI_GURU -> {
                        SpecRow("Module ID", "AI_GURU")
                        SpecRow("Inputs", "Teacher Name, Subject, Key Traits, Language, Tone")
                        SpecRow("Outputs", "Personalized Metaphor Message, 4-Line Rhyming Stanza, 3 Badges")
                    }
                    EduModule.VOICE_OF_GRATITUDE -> {
                        SpecRow("Module ID", "VOICE_OF_GRATITUDE")
                        SpecRow("Inputs", "Student Name, Teacher Name, Raw Message, Target Language")
                        SpecRow("Outputs", "TTS-Optimized Pacing Script, Pronunciation Guides, 1-Sentence Summary")
                    }
                    EduModule.TRIVIA_BATTLE -> {
                        SpecRow("Module ID", "TRIVIA_BATTLE")
                        SpecRow("Inputs", "Subject Area, Grade/Department, Teacher Persona Archetype")
                        SpecRow("Outputs", "3 MCQs (4 options, correct index, explanation, comment), Digital Badge Title")
                    }
                    else -> {}
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Safety Rules Card
                Surface(
                    color = SophisticatedGoldPillBg,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SophisticatedGoldBorder, RoundedCornerShape(10.dp))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = SophisticatedGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Safety Filter: Dignity, warmth, and respect enforced on all requests.",
                            style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedGold, fontWeight = FontWeight.Medium)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Execute Button
                Button(
                    onClick = { viewModel.executeApiTester() },
                    enabled = !isExecuting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("engine_execute_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SophisticatedGold,
                        contentColor = Color(0xFF121212)
                    )
                ) {
                    if (isExecuting) {
                        CircularProgressIndicator(color = Color(0xFF121212), modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Executing Engine...", color = Color(0xFF121212), fontWeight = FontWeight.Bold)
                    } else {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF121212))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Run Engine & Inspect JSON Output", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // JSON Response Viewer
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SophisticatedSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Code, contentDescription = null, tint = SophisticatedGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Engine JSON Response",
                            color = SophisticatedOnSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    if (rawJson.isNotBlank()) {
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("EduTribute JSON", rawJson)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "JSON copied!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = SophisticatedGold, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    color = Color(0xFF101010),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(8.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                            .horizontalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = if (rawJson.isNotBlank()) rawJson else "// Tap 'Run Engine' above to execute and inspect clean JSON payload...",
                            color = if (rawJson.isNotBlank()) SophisticatedGold else SophisticatedOnSurfaceVariant,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = SophisticatedGold),
            modifier = Modifier.width(90.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurface)
        )
    }
}
