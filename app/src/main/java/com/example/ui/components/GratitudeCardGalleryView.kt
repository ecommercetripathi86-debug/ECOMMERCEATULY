package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.SavedGratitudeCard
import com.example.ui.theme.CrimsonGradientBottom
import com.example.ui.theme.CrimsonGradientTop
import com.example.ui.theme.EmeraldGradientBottom
import com.example.ui.theme.EmeraldGradientTop
import com.example.ui.theme.IndigoGradientBottom
import com.example.ui.theme.IndigoGradientTop
import com.example.ui.theme.ParchmentGoldBottom
import com.example.ui.theme.ParchmentGoldTop
import com.example.ui.theme.SlateGradientBottom
import com.example.ui.theme.SlateGradientTop
import com.example.ui.theme.SophisticatedGlassBorder
import com.example.ui.theme.SophisticatedGold
import com.example.ui.theme.SophisticatedGoldBorder
import com.example.ui.theme.SophisticatedGoldDark
import com.example.ui.theme.SophisticatedGoldPillBg
import com.example.ui.theme.SophisticatedOnSurface
import com.example.ui.theme.SophisticatedOnSurfaceVariant
import com.example.ui.theme.SophisticatedSurface
import com.example.ui.theme.SophisticatedSurfaceVariant
import com.example.ui.theme.VioletNebulaBottom
import com.example.ui.theme.VioletNebulaTop
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val templateLabels = listOf(
    "Luxe Gold Certificate",
    "Modern Luminary Glass",
    "Classic Scroll Parchment",
    "Editorial Minimalist",
    "Cosmic Nebula"
)

private val themeGradients = listOf(
    listOf(SlateGradientTop, SlateGradientBottom),
    listOf(ParchmentGoldTop, ParchmentGoldBottom),
    listOf(EmeraldGradientTop, EmeraldGradientBottom),
    listOf(IndigoGradientTop, IndigoGradientBottom),
    listOf(CrimsonGradientTop, CrimsonGradientBottom),
    listOf(VioletNebulaTop, VioletNebulaBottom)
)

@Composable
fun GratitudeCardGalleryView(
    savedCards: List<SavedGratitudeCard>,
    onOpenInStudio: (SavedGratitudeCard) -> Unit,
    onDeleteCard: (Long) -> Unit,
    onClearAll: () -> Unit,
    onCreateNewCard: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var showClearAllConfirm by remember { mutableStateOf(false) }
    var cardToDelete by remember { mutableStateOf<SavedGratitudeCard?>(null) }

    val filteredCards = remember(savedCards, searchQuery) {
        if (searchQuery.isBlank()) savedCards
        else {
            val query = searchQuery.trim().lowercase(Locale.getDefault())
            savedCards.filter { card ->
                card.teacherName.lowercase(Locale.getDefault()).contains(query) ||
                        card.subject.lowercase(Locale.getDefault()).contains(query) ||
                        card.studentSignature.lowercase(Locale.getDefault()).contains(query) ||
                        card.personalizedMessage.lowercase(Locale.getDefault()).contains(query)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("gallery_view_container"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Gallery Header Bar
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
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SophisticatedGoldPillBg)
                                .border(1.dp, SophisticatedGoldBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = SophisticatedGold,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Card Gallery",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = SophisticatedGold
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = SophisticatedGoldPillBg,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.border(0.5.dp, SophisticatedGoldBorder, RoundedCornerShape(12.dp))
                                ) {
                                    Text(
                                        text = "${savedCards.size} Saved",
                                        color = SophisticatedGold,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Local Room database archive of personalized gratitude cards.",
                                style = MaterialTheme.typography.bodySmall.copy(color = SophisticatedOnSurfaceVariant)
                            )
                        }
                    }

                    if (savedCards.isNotEmpty()) {
                        IconButton(
                            onClick = { showClearAllConfirm = true },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0x22EF4444))
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Clear All Cards",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Box
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by teacher, subject, or student name...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = SophisticatedGold)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search", tint = SophisticatedOnSurfaceVariant)
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SophisticatedGold,
                        focusedLabelColor = SophisticatedGold,
                        cursorColor = SophisticatedGold,
                        focusedLeadingIconColor = SophisticatedGold,
                        unfocusedBorderColor = Color(0xFF383838),
                        unfocusedContainerColor = SophisticatedSurfaceVariant,
                        focusedContainerColor = SophisticatedSurfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("gallery_search_input")
                )
            }
        }

        // Empty State or List of Cards
        if (filteredCards.isEmpty()) {
            Surface(
                color = SophisticatedSurface,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(16.dp))
                    .padding(vertical = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(SophisticatedGoldPillBg)
                            .border(1.dp, SophisticatedGoldBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = SophisticatedGold,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = if (searchQuery.isNotBlank()) "No Matching Cards Found" else "No Saved Cards Yet",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SophisticatedGold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (searchQuery.isNotBlank())
                            "No cards matched \"$searchQuery\". Try a different search keyword or clear the filter."
                        else
                            "Generate an appreciation card in the Card Studio and tap \"Save to Gallery\" to store it locally for Teachers' Day.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = SophisticatedOnSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onCreateNewCard,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SophisticatedGold,
                            contentColor = Color(0xFF121212)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("gallery_create_first_btn")
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF121212))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Create Appreciation Card", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            filteredCards.forEach { card ->
                GalleryCardItem(
                    card = card,
                    onOpenInStudio = { onOpenInStudio(card) },
                    onDelete = { cardToDelete = card },
                    onCopy = {
                        val fullTribute = buildFormattedGalleryTribute(card)
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("EduTribute Card", fullTribute)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Appreciation card copied to clipboard!", Toast.LENGTH_SHORT).show()
                    },
                    onShare = {
                        val fullTribute = buildFormattedGalleryTribute(card)
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "Teachers' Day Card for ${card.teacherName}")
                            putExtra(Intent.EXTRA_TEXT, fullTribute)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Appreciation Card"))
                    }
                )
            }
        }
    }

    // Delete Confirmation Dialog
    if (cardToDelete != null) {
        val target = cardToDelete!!
        AlertDialog(
            onDismissRequest = { cardToDelete = null },
            containerColor = SophisticatedSurface,
            title = {
                Text(
                    text = "Delete Appreciation Card?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SophisticatedGold)
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to remove the card for ${target.teacherName} from your local gallery?",
                    color = SophisticatedOnSurface
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteCard(target.id)
                        cardToDelete = null
                        Toast.makeText(context, "Card deleted from gallery", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444), contentColor = Color.White)
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { cardToDelete = null }) {
                    Text("Cancel", color = SophisticatedOnSurfaceVariant)
                }
            }
        )
    }

    // Clear All Confirmation Dialog
    if (showClearAllConfirm) {
        AlertDialog(
            onDismissRequest = { showClearAllConfirm = false },
            containerColor = SophisticatedSurface,
            title = {
                Text(
                    text = "Clear Entire Gallery?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SophisticatedGold)
                )
            },
            text = {
                Text(
                    text = "This will permanently delete all ${savedCards.size} saved appreciation cards from local storage.",
                    color = SophisticatedOnSurface
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearAll()
                        showClearAllConfirm = false
                        Toast.makeText(context, "All cards cleared from gallery", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444), contentColor = Color.White)
                ) {
                    Text("Clear All", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearAllConfirm = false }) {
                    Text("Cancel", color = SophisticatedOnSurfaceVariant)
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GalleryCardItem(
    card: SavedGratitudeCard,
    onOpenInStudio: () -> Unit,
    onDelete: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    var isExpandedNote by remember { mutableStateOf(false) }
    val formattedDate = remember(card.createdAt) {
        SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(card.createdAt))
    }
    val gradientColors = themeGradients.getOrElse(card.themeIndex) { themeGradients[0] }
    val templateLabel = templateLabels.getOrElse(card.templateIndex) { templateLabels[0] }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SophisticatedSurface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(20.dp))
            .testTag("gallery_card_item_${card.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Theme Top Banner Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(gradientColors))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0x33000000),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.border(0.5.dp, SophisticatedGoldBorder, RoundedCornerShape(8.dp))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = SophisticatedGold,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = templateLabel,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SophisticatedGold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Surface(
                            color = Color(0x22000000),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = card.tone,
                                fontSize = 10.sp,
                                color = SophisticatedOnSurface,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Text(
                        text = formattedDate,
                        fontSize = 11.sp,
                        color = SophisticatedGold.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Main Content Area
            Column(modifier = Modifier.padding(16.dp)) {
                // Teacher Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = card.teacherName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = SophisticatedGold,
                                fontFamily = FontFamily.Serif
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = card.subject,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = SophisticatedOnSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0x15FFFFFF))
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Card",
                            tint = Color(0xFFE57373),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Personalized Message Quote
                Surface(
                    color = SophisticatedSurfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, SophisticatedGlassBorder, RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = null,
                            tint = SophisticatedGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = card.personalizedMessage,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = SophisticatedOnSurface,
                                fontStyle = FontStyle.Italic,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }

                // 4-Line Poem Preview
                val stanzas = card.getStanzaLines()
                if (stanzas.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = Color(0x1A000000),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(0.5.dp, SophisticatedGoldBorder.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "HONORARY STANZA",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SophisticatedGold,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            stanzas.forEach { line ->
                                Text(
                                    text = line,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = SophisticatedOnSurface,
                                        fontFamily = FontFamily.Serif,
                                        fontStyle = FontStyle.Italic,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    }
                }

                // Digital Badges
                val badges = card.getBadges()
                if (badges.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        badges.forEach { badge ->
                            Surface(
                                color = SophisticatedGoldPillBg,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.border(0.5.dp, SophisticatedGoldBorder, RoundedCornerShape(16.dp))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = SophisticatedGold,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = badge,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = SophisticatedGold
                                    )
                                }
                            }
                        }
                    }
                }

                // Expandable Student Reverse Note
                AnimatedVisibility(
                    visible = isExpandedNote,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = Color(0x33000000),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, SophisticatedGoldBorder, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "REVERSE DEDICATION NOTE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = SophisticatedGold,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = card.backNote.ifBlank { "Thank you for inspiring our curiosity and always believing in us!" },
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = SophisticatedOnSurface,
                                        fontStyle = FontStyle.Italic
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "- ${card.studentSignature} (${card.studentRole})",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = SophisticatedGold,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = SophisticatedGlassBorder)
                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left action: Open in Studio
                    Button(
                        onClick = onOpenInStudio,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SophisticatedGold,
                            contentColor = Color(0xFF121212)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Launch, contentDescription = null, tint = Color(0xFF121212), modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Open in Studio", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Flip note toggle
                        OutlinedButton(
                            onClick = { isExpandedNote = !isExpandedNote },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SophisticatedGold),
                            border = BorderStroke(1.dp, SophisticatedGlassBorder),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(imageVector = Icons.Default.FlipCameraAndroid, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isExpandedNote) "Hide Note" else "Note", fontSize = 11.sp)
                        }

                        // Copy
                        IconButton(
                            onClick = onCopy,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(SophisticatedSurfaceVariant)
                                .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(10.dp))
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Tribute", tint = SophisticatedGold, modifier = Modifier.size(16.dp))
                        }

                        // Share
                        IconButton(
                            onClick = onShare,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(SophisticatedSurfaceVariant)
                                .border(1.dp, SophisticatedGlassBorder, RoundedCornerShape(10.dp))
                                .testTag("gallery_card_share_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "Share Tribute", tint = SophisticatedGold, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

private fun buildFormattedGalleryTribute(card: SavedGratitudeCard): String {
    val builder = StringBuilder()
    builder.append("🎓 TEACHERS' DAY APPRECIATION TRIBUTE 🎓\n\n")
    builder.append("Presented To: ${card.teacherName}\n")
    builder.append("Subject: ${card.subject}\n\n")
    builder.append("Personal Message:\n\"${card.personalizedMessage}\"\n\n")

    val stanzas = card.getStanzaLines()
    if (stanzas.isNotEmpty()) {
        builder.append("Honorary Stanza:\n")
        stanzas.forEach { builder.append("  $it\n") }
        builder.append("\n")
    }

    val badges = card.getBadges()
    if (badges.isNotEmpty()) {
        builder.append("Distinction Badges: ${badges.joinToString(" • ")}\n\n")
    }

    if (card.backNote.isNotBlank()) {
        builder.append("Student Note: \"${card.backNote}\"\n\n")
    }

    builder.append("Presented with gratitude by: ${card.studentSignature}")
    if (card.studentRole.isNotBlank()) {
        builder.append(" (${card.studentRole})")
    }
    builder.append("\nCreated with EduTribute AI Studio")
    return builder.toString()
}
