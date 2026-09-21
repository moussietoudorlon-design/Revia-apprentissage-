package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.UserProfile
import com.example.ui.theme.AmberAction
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardElevated
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.ElectricVioletLight
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.LoraFamily
import com.example.ui.theme.PlayfairFamily
import com.example.ui.theme.TechBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TurquoiseAccent
import com.example.ui.theme.VintageBorderGold
import com.example.ui.theme.VintageCrimson
import com.example.ui.theme.VintageGold
import com.example.ui.theme.VintageParchment
import com.example.ui.viewmodel.Screen

@Composable
fun ReviaTopBar(
    userProfile: UserProfile?,
    onCreditsClick: () -> Unit,
    onProgressionClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLandingClick: () -> Unit
) {
    Surface(
        color = DarkSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, VintageBorderGold.copy(alpha = 0.35f), RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Title (Vintage Signet / Academic Press Style)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onLandingClick() }
                    .padding(4.dp)
            ) {
                // Antique Wax Seal Emblem
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(VintageCrimson)
                        .border(1.5.dp, VintageBorderGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ℜ",
                        color = VintageParchment,
                        fontFamily = PlayfairFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "REVIA",
                        color = VintageParchment,
                        fontFamily = PlayfairFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Cabinet d'Étude & Savoir",
                        color = VintageGold,
                        fontFamily = LoraFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            // Status badges: Streak & Live Credits
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Streak badge (Antique brass cartouche)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkCardElevated)
                        .border(1.dp, VintageBorderGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                        .clickable { onProgressionClick() }
                        .padding(horizontal = 9.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "⚜",
                        color = AmberAction,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${userProfile?.streakDays ?: 5} j",
                        color = VintageParchment,
                        fontFamily = LoraFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Credits Badge (Antique pocket counter with gold trim)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .testTag("credits_badge")
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkCard)
                        .border(1.2.dp, VintageGold, RoundedCornerShape(16.dp))
                        .clickable { onCreditsClick() }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "✦",
                        color = VintageGold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${userProfile?.credits ?: 20}",
                        color = VintageGold,
                        fontFamily = LoraFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // User Avatar (Cameo / Signet seal)
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(DarkCardElevated)
                        .border(1.5.dp, VintageBorderGold, CircleShape)
                        .clickable { onProfileClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userProfile?.name?.take(1)?.uppercase() ?: "A",
                        color = VintageParchment,
                        fontFamily = PlayfairFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ReviaBottomNav(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    onPlusClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBar(
            containerColor = DarkSurface,
            contentColor = TextPrimary,
            tonalElevation = 6.dp,
            modifier = Modifier
                .border(1.dp, VintageBorderGold.copy(alpha = 0.35f), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        ) {
            NavigationBarItem(
                selected = currentScreen == Screen.DASHBOARD,
                onClick = { onNavigate(Screen.DASHBOARD) },
                icon = { Icon(Icons.Default.Home, contentDescription = "Accueil") },
                label = { Text("Accueil", fontFamily = LoraFamily, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VintageParchment,
                    selectedTextColor = VintageGold,
                    indicatorColor = VintageCrimson.copy(alpha = 0.45f),
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                )
            )

            NavigationBarItem(
                selected = currentScreen == Screen.COURSES,
                onClick = { onNavigate(Screen.COURSES) },
                icon = { Icon(Icons.Default.MenuBook, contentDescription = "Mes cours") },
                label = { Text("Cours", fontFamily = LoraFamily, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VintageParchment,
                    selectedTextColor = VintageGold,
                    indicatorColor = VintageCrimson.copy(alpha = 0.45f),
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                )
            )

            // Center Spacer for FAB
            Spacer(modifier = Modifier.width(48.dp))

            NavigationBarItem(
                selected = currentScreen == Screen.TUTOR,
                onClick = { onNavigate(Screen.TUTOR) },
                icon = { Icon(Icons.Default.Psychology, contentDescription = "Tuteur IA") },
                label = { Text("Tuteur", fontFamily = LoraFamily, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VintageParchment,
                    selectedTextColor = VintageGold,
                    indicatorColor = VintageCrimson.copy(alpha = 0.45f),
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                )
            )

            NavigationBarItem(
                selected = currentScreen == Screen.PROGRESSION,
                onClick = { onNavigate(Screen.PROGRESSION) },
                icon = { Icon(Icons.Default.BarChart, contentDescription = "Progression") },
                label = { Text("Progrès", fontFamily = LoraFamily, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VintageParchment,
                    selectedTextColor = VintageGold,
                    indicatorColor = VintageCrimson.copy(alpha = 0.45f),
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                )
            )
        }

        // Central floating action button [+] (Vintage Sealing Wax Medallion)
        FloatingActionButton(
            onClick = onPlusClick,
            containerColor = VintageCrimson,
            contentColor = VintageParchment,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(54.dp)
                .border(2.dp, VintageBorderGold, CircleShape)
                .testTag("central_fab_add")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Actions rapides",
                tint = VintageParchment,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun QuickActionsModal(
    onDismiss: () -> Unit,
    onAddCourse: () -> Unit,
    onCreateSummary: () -> Unit,
    onCreateQuiz: () -> Unit,
    onCreateAudio: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DarkCardElevated,
            border = androidx.compose.foundation.BorderStroke(1.2.dp, VintageBorderGold.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "✦ Atelier de Création ✦",
                            fontSize = 18.sp,
                            fontFamily = PlayfairFamily,
                            fontWeight = FontWeight.Bold,
                            color = VintageParchment
                        )
                        Text(
                            text = "Invoquez les outils d'apprentissage",
                            fontSize = 11.sp,
                            fontFamily = LoraFamily,
                            color = VintageGold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                QuickActionItem(
                    title = "Ajouter un cours",
                    subtitle = "Saisir, coller ou importer un parchemin",
                    icon = Icons.Default.Add,
                    badge = "Base",
                    accentColor = TechBlue,
                    onClick = {
                        onDismiss()
                        onAddCourse()
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                QuickActionItem(
                    title = "Fiche de révision",
                    subtitle = "Résumé, notions, définitions & pièges",
                    icon = Icons.Default.Description,
                    badge = "2 crédits",
                    accentColor = ElectricViolet,
                    onClick = {
                        onDismiss()
                        onCreateSummary()
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                QuickActionItem(
                    title = "Quiz d'évaluation",
                    subtitle = "QCM & questions à choix multiples",
                    icon = Icons.Default.Quiz,
                    badge = "3 crédits",
                    accentColor = TurquoiseAccent,
                    onClick = {
                        onDismiss()
                        onCreateQuiz()
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                QuickActionItem(
                    title = "Leçon audio dictée",
                    subtitle = "Récit oralisé & écoute immersive",
                    icon = Icons.Default.Headphones,
                    badge = "4 crédits",
                    accentColor = AmberAction,
                    onClick = {
                        onDismiss()
                        onCreateAudio()
                    }
                )
            }
        }
    }
}

@Composable
fun QuickActionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badge: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DarkCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.3f)),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.18f))
                    .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        color = VintageParchment,
                        fontFamily = PlayfairFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = badge,
                        color = accentColor,
                        fontFamily = LoraFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(accentColor.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = subtitle,
                    color = TextMuted,
                    fontFamily = LoraFamily,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun GenerationLoadingDialog(message: String) {
    Dialog(onDismissRequest = {}) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DarkCardElevated,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, VintageBorderGold),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = VintageGold,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "✦ L'Atelier REVIA s'affaire...",
                    color = VintageParchment,
                    fontFamily = PlayfairFamily,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    color = TextSecondary,
                    fontFamily = LoraFamily,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
