package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EduModule
import com.example.ui.theme.SophisticatedBackground
import com.example.ui.theme.SophisticatedGlassBorder
import com.example.ui.theme.SophisticatedGold
import com.example.ui.theme.SophisticatedGoldBorder
import com.example.ui.theme.SophisticatedGoldDark
import com.example.ui.theme.SophisticatedGoldPillBg
import com.example.ui.theme.SophisticatedOnSurface
import com.example.ui.theme.SophisticatedOnSurfaceVariant
import com.example.ui.theme.SophisticatedSurface

@Composable
fun EduTributeHeader(
    currentModule: EduModule,
    onSelectModule: (EduModule) -> Unit
) {
    Surface(
        color = Color(0xFF181818),
        tonalElevation = 6.dp,
        modifier = Modifier.border(
            width = 1.dp,
            color = SophisticatedGlassBorder,
            shape = RoundedCornerShape(0.dp)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            // App Branding Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(SophisticatedGold, SophisticatedGoldDark)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "EduTribute Icon",
                            tint = Color(0xFF121212),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "EduTribute",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = SophisticatedOnSurface,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Text(
                            text = "Teachers' Day AI Engine",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SophisticatedGold,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }

                // AI Badge Pill (Sophisticated Gold Pill)
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SophisticatedGoldPillBg,
                    modifier = Modifier.border(1.dp, SophisticatedGoldBorder, RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = SophisticatedGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Gemini 3.5",
                            color = SophisticatedGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Navigation Tabs
            val modules = EduModule.values()
            val selectedIndex = modules.indexOf(currentModule)

            ScrollableTabRow(
                selectedTabIndex = selectedIndex,
                containerColor = Color.Transparent,
                contentColor = SophisticatedGold,
                edgePadding = 12.dp,
                indicator = { tabPositions ->
                    if (selectedIndex in tabPositions.indices) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                            color = SophisticatedGold,
                            height = 2.5.dp
                        )
                    }
                },
                divider = {}
            ) {
                modules.forEach { module ->
                    val selected = module == currentModule
                    val icon = when (module) {
                        EduModule.AI_GURU -> Icons.Default.MenuBook
                        EduModule.VOICE_OF_GRATITUDE -> Icons.Default.Mic
                        EduModule.TRIVIA_BATTLE -> Icons.Default.Psychology
                        EduModule.API_ENGINE -> Icons.Default.Code
                    }
                    Tab(
                        selected = selected,
                        onClick = { onSelectModule(module) },
                        text = {
                            Text(
                                text = module.title,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = if (selected) SophisticatedGold else SophisticatedOnSurfaceVariant,
                                fontSize = 13.sp
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = module.title,
                                tint = if (selected) SophisticatedGold else Color(0xFF6E6E6E),
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        modifier = Modifier.testTag("tab_${module.name.lowercase()}")
                    )
                }
            }
        }
    }
}

@Composable
fun BadgeChip(
    title: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.MilitaryTech
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SophisticatedGoldPillBg,
        shadowElevation = 0.dp,
        modifier = modifier.border(1.dp, SophisticatedGoldBorder, RoundedCornerShape(20.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SophisticatedGold,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = SophisticatedGold
                )
            )
        }
    }
}

@Composable
fun SafetyRejectionCard(
    reason: String,
    politeAlternative: String,
    rawJson: String,
    onUseAlternative: (() -> Unit)? = null
) {
    var showJson = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, Color(0x66E57373), RoundedCornerShape(16.dp))
            .testTag("safety_rejection_card"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF221618)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.WarningAmber,
                    contentDescription = "Safety Alert",
                    tint = Color(0xFFE57373),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Dignity & Respect Safety Guidance",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFCA5A5)
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = reason,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFF87171))
            )

            if (politeAlternative.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = Color(0xFF181818),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(8.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Suggested Dignified Alternative:",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SophisticatedGold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = politeAlternative,
                            style = MaterialTheme.typography.bodyMedium.copy(color = SophisticatedOnSurface)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { showJson.value = true },
                    colors = ButtonDefaults.textButtonColors(contentColor = SophisticatedGold)
                ) {
                    Icon(imageVector = Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("View Schema JSON")
                }

                if (onUseAlternative != null && politeAlternative.isNotBlank()) {
                    Button(
                        onClick = onUseAlternative,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SophisticatedGold,
                            contentColor = Color(0xFF121212)
                        )
                    ) {
                        Text("Apply Alternative")
                    }
                }
            }
        }
    }

    if (showJson.value) {
        JsonViewerDialog(title = "Safety Rejection Payload", jsonString = rawJson) {
            showJson.value = false
        }
    }
}

@Composable
fun LoadingCard(message: String = "Generating with Teachers' Day AI Engine...") {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(16.dp))
            .testTag("loading_indicator"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SophisticatedSurface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = SophisticatedGold,
                strokeWidth = 3.5.dp,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = SophisticatedGold
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Crafting subject metaphors, rhyming rhythm & heartfelt badges...",
                style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurfaceVariant),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun JsonViewerDialog(
    title: String,
    jsonString: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SophisticatedSurface,
        titleContentColor = SophisticatedGold,
        textContentColor = SophisticatedOnSurface,
        modifier = Modifier.border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(24.dp)),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Code, contentDescription = null, tint = SophisticatedGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close", tint = SophisticatedOnSurfaceVariant)
                }
            }
        },
        text = {
            Surface(
                color = SophisticatedBackground,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .verticalScroll(rememberScrollState())
                        .horizontalScroll(rememberScrollState())
                ) {
                    Text(
                        text = jsonString.ifBlank { "{ \"message\": \"No JSON data yet.\" }" },
                        color = SophisticatedGold,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("EduTribute JSON", jsonString)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "JSON copied to clipboard!", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SophisticatedGold,
                    contentColor = Color(0xFF121212)
                )
            ) {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Copy JSON")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SophisticatedOnSurfaceVariant)
            ) {
                Text("Close")
            }
        }
    )
}
