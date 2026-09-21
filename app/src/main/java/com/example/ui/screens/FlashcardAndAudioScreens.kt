package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AudioLesson
import com.example.data.model.Flashcard
import com.example.service.AudioTtsManager
import com.example.ui.components.VintageFlashcardComponent
import com.example.ui.theme.AmberAction
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardElevated
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.LoraFamily
import com.example.ui.theme.PlayfairFamily
import com.example.ui.theme.RedError
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TurquoiseAccent
import com.example.ui.theme.VintageBorderGold
import com.example.ui.theme.VintageCrimson
import com.example.ui.theme.VintageGold
import com.example.ui.theme.VintageParchment

@Composable
fun FlashcardsRunnerScreen(
    cards: List<Flashcard>,
    currentIndex: Int,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    onMarkAnswer: (isMastered: Boolean) -> Unit,
    onBack: () -> Unit
) {
    if (cards.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().background(DarkBg), contentAlignment = Alignment.Center) {
            Text("Aucune flashcard disponible", color = TextPrimary)
        }
        return
    }

    val currentCard = cards[currentIndex.coerceIn(0, cards.size - 1)]
    val progress = (currentIndex + 1).toFloat() / cards.size.toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = VintageGold)
                }
                Text(
                    text = "✦ Carte ${currentIndex + 1} / ${cards.size} ✦",
                    color = VintageParchment,
                    fontFamily = PlayfairFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Icon(Icons.Default.Style, contentDescription = null, tint = VintageGold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                color = VintageGold,
                trackColor = DarkCardElevated,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Composant interactif Flashcard vintage animé
            VintageFlashcardComponent(
                question = currentCard.question,
                answer = currentCard.answer,
                isFlipped = isFlipped,
                onFlip = onFlip,
                cardIndex = currentIndex + 1,
                totalCards = cards.size
            )
        }

        // Action Buttons: "À approfondir" & "Maîtrisé"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Button(
                onClick = { onMarkAnswer(false) },
                colors = ButtonDefaults.buttonColors(containerColor = VintageCrimson.copy(alpha = 0.25f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, VintageCrimson),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("flashcard_needs_review_btn")
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = VintageCrimson, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("À approfondir", color = VintageCrimson, fontFamily = LoraFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Button(
                onClick = { onMarkAnswer(true) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A6B2F), contentColor = VintageParchment),
                border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("flashcard_mastered_btn")
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = VintageParchment, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Maîtrisé", color = VintageParchment, fontFamily = LoraFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun AudioPlayerScreen(
    audioLesson: AudioLesson,
    audioTtsManager: AudioTtsManager,
    onBack: () -> Unit
) {
    val isPlaying by audioTtsManager.isPlaying.collectAsState()
    val progress by audioTtsManager.currentProgress.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = VintageGold)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("✦ Conférence & Audition de Cours ✦", color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Audio Player Banner (Antique gramophone / archival audio cylinder)
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DarkCard,
            border = androidx.compose.foundation.BorderStroke(1.2.dp, VintageBorderGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Soundwave / Vinyl visual
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(VintageGold.copy(alpha = 0.15f))
                        .border(2.dp, VintageBorderGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = null,
                        tint = VintageGold,
                        modifier = Modifier.size(46.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = audioLesson.title,
                    color = VintageParchment,
                    fontFamily = PlayfairFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Traité : ${audioLesson.courseTitle}",
                    color = VintageGold,
                    fontFamily = LoraFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Progress Slider
                Slider(
                    value = progress,
                    onValueChange = { audioTtsManager.seekTo(it) },
                    colors = SliderDefaults.colors(
                        thumbColor = VintageGold,
                        activeTrackColor = VintageGold,
                        inactiveTrackColor = DarkCardElevated
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val currentSec = (progress * audioLesson.durationSeconds).toInt()
                    Text(formatSeconds(currentSec), color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp)
                    Text(formatSeconds(audioLesson.durationSeconds), color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Controls: Play / Pause, +15s
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Play / Pause FAB
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(VintageCrimson)
                            .border(1.2.dp, VintageBorderGold, CircleShape)
                            .clickable { audioTtsManager.togglePlayPause() }
                            .testTag("audio_play_pause_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Lecture",
                            tint = VintageParchment,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    // +15s forward
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(DarkCardElevated)
                            .border(1.dp, VintageBorderGold.copy(alpha = 0.5f), CircleShape)
                            .clickable { audioTtsManager.skip15SecondsForward() }
                            .testTag("audio_skip_15s_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.FastForward, contentDescription = "+15s", tint = VintageGold, modifier = Modifier.size(20.dp))
                            Text("+15s", color = VintageGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Audio Pedagogical Script by Sections
        Text("Script magistral oralisé :", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 16.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(10.dp))

        AudioSectionCard(title = "I. Introduction du sujet", text = audioLesson.introduction, accent = VintageGold)
        Spacer(modifier = Modifier.height(8.dp))
        AudioSectionCard(title = "II. Thèse & Explication substantielle", text = audioLesson.explanation, accent = VintageParchment)
        Spacer(modifier = Modifier.height(8.dp))
        AudioSectionCard(title = "III. Illustrations & Applications concrètes", text = audioLesson.examples, accent = Color(0xFFC5A059))
        Spacer(modifier = Modifier.height(8.dp))
        AudioSectionCard(title = "IV. Synthèse & Enseignements clés", text = audioLesson.recap, accent = VintageCrimson)
        Spacer(modifier = Modifier.height(8.dp))
        AudioSectionCard(title = "V. Questions socratiques d'ancrage", text = audioLesson.quickQuestions, accent = Color(0xFF6B8E23))

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun AudioSectionCard(title: String, text: String, accent: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DarkCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, color = accent, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text, color = TextSecondary, fontFamily = LoraFamily, fontSize = 12.sp, lineHeight = 18.sp)
        }
    }
}

private fun formatSeconds(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format(java.util.Locale.getDefault(), "%02d:%02d", m, s)
}
