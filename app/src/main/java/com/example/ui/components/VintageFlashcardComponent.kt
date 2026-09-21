package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardElevated
import com.example.ui.theme.LoraFamily
import com.example.ui.theme.PlayfairFamily
import com.example.ui.theme.TextMuted
import com.example.ui.theme.VintageBorderGold
import com.example.ui.theme.VintageCrimson
import com.example.ui.theme.VintageGold
import com.example.ui.theme.VintageParchment

/**
 * Composant d'interface interactif pour le mode Flashcard avec basculement fluide 3D
 * dans l'esthétique Vintage Dark Academia (parchemin noble, liserés dorés et sceau d'érudit).
 */
@Composable
fun VintageFlashcardComponent(
    question: String,
    answer: String,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier,
    cardIndex: Int? = null,
    totalCards: Int? = null,
    category: String? = null
) {
    // Animation de rotation fluide 3D (0° -> 180°)
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 480,
            easing = FastOutSlowInEasing
        ),
        label = "vintage_card_rotation"
    )

    // Effet subtil d'élévation dynamique et de mise à l'échelle au cours du retournement
    val isNearMidpoint = rotation in 45f..135f
    val scale = if (isNearMidpoint) 0.96f else 1.0f

    // Pulsation discrète pour le voyant d'interaction
    val infiniteTransition = rememberInfiniteTransition(label = "vintage_glow")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.55f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(380.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 16f * density
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (isFlipped) 12.dp else 8.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = VintageGold.copy(alpha = 0.35f)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isFlipped) {
                        listOf(Color(0xFF221A15), Color(0xFF171310))
                    } else {
                        listOf(Color(0xFF1E1914), Color(0xFF14110E))
                    }
                )
            )
            .border(
                BorderStroke(
                    1.6.dp,
                    Brush.verticalGradient(
                        colors = listOf(
                            VintageGold,
                            VintageBorderGold,
                            VintageGold.copy(alpha = 0.6f)
                        )
                    )
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = VintageGold.copy(alpha = 0.2f)),
                onClick = onFlip
            )
            .testTag("flashcard_item"),
        contentAlignment = Alignment.Center
    ) {
        // Cadre intérieur ornemental rétro (double filet à l'ancienne)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .border(
                    BorderStroke(0.8.dp, VintageBorderGold.copy(alpha = 0.45f)),
                    shape = RoundedCornerShape(16.dp)
                )
        )

        // Fleurons décoratifs aux 4 angles
        OrnamentalCorner(Modifier.align(Alignment.TopStart).padding(14.dp))
        OrnamentalCorner(Modifier.align(Alignment.TopEnd).padding(14.dp))
        OrnamentalCorner(Modifier.align(Alignment.BottomStart).padding(14.dp))
        OrnamentalCorner(Modifier.align(Alignment.BottomEnd).padding(14.dp))

        if (rotation <= 90f) {
            // ==========================================
            // FACE I : LA QUESTION / ÉNIGME
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // En-tête de la carte
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(VintageCrimson)
                                .border(1.dp, VintageBorderGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = VintageParchment,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (cardIndex != null && totalCards != null) {
                                "ÉNIGME $cardIndex / $totalCards"
                            } else {
                                "PREMIER DEGRÉ • QUESTION"
                            },
                            color = VintageGold,
                            fontFamily = PlayfairFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.2.sp
                        )
                    }

                    if (category != null) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = DarkCardElevated,
                            border = BorderStroke(0.8.dp, VintageBorderGold.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = category,
                                color = VintageParchment,
                                fontFamily = LoraFamily,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                // Cœur : Texte de la question
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = question,
                            color = VintageParchment,
                            fontFamily = PlayfairFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 27.sp
                        )
                    }
                }

                // Bas de carte : Indicateur de basculement interactif
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkCardElevated.copy(alpha = 0.7f))
                        .border(1.dp, VintageBorderGold.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cached,
                        contentDescription = null,
                        tint = VintageGold.copy(alpha = pulseAlpha),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Toucher pour retourner le parchemin",
                        color = VintageGold.copy(alpha = pulseAlpha),
                        fontFamily = LoraFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            // ==========================================
            // FACE II : LA RÉPONSE / LE SAVOIR ACQUIS
            // (Pivoté à 180° pour un affichage parfait)
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f }
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // En-tête de la réponse avec sceau de savoir
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2E4A1E))
                                .border(1.dp, VintageGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = VintageGold,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SAVOIR RÉVÉLÉ • RÉPONSE",
                            color = Color(0xFF8FBC8F),
                            fontFamily = PlayfairFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.2.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = VintageGold,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Cœur : Texte de la réponse
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = answer,
                            color = VintageParchment,
                            fontFamily = LoraFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 25.sp
                        )
                    }
                }

                // Bas de carte : Bouton / Invite de retour vers la question
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkCardElevated.copy(alpha = 0.7f))
                        .border(1.dp, VintageBorderGold.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cached,
                        contentDescription = null,
                        tint = VintageGold.copy(alpha = 0.8f),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Toucher pour revoir l'énigme",
                        color = VintageGold.copy(alpha = 0.85f),
                        fontFamily = LoraFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

/**
 * Petit ornement rétro pour marquer les angles d'un parchemin ou cartouche de cours.
 */
@Composable
private fun OrnamentalCorner(modifier: Modifier = Modifier) {
    Text(
        text = "✦",
        color = VintageBorderGold.copy(alpha = 0.6f),
        fontSize = 11.sp,
        modifier = modifier
    )
}
