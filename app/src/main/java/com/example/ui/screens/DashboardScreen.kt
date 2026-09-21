package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AudioLesson
import com.example.data.model.Course
import com.example.data.model.QuizResult
import com.example.data.model.RevisionSummary
import com.example.data.model.UserProfile
import com.example.ui.theme.AmberAction
import com.example.ui.theme.DarkBg
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
fun DashboardScreen(
    userProfile: UserProfile?,
    courses: List<Course>,
    summaries: List<RevisionSummary>,
    quizResults: List<QuizResult>,
    audioLessons: List<AudioLesson>,
    onNavigate: (Screen) -> Unit,
    onSelectCourse: (Course) -> Unit,
    onOpenSummary: (RevisionSummary) -> Unit,
    onAddCourse: () -> Unit,
    onCreateSummary: () -> Unit,
    onCreateQuiz: () -> Unit,
    onCreateAudio: () -> Unit,
    onSeedStarterCourse: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Salutations, ${userProfile?.name ?: "Alex"} ✒️",
                            color = VintageParchment,
                            fontFamily = PlayfairFamily,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Quel savoir souhaitez-vous explorer aujourd'hui ?",
                            color = VintageGold,
                            fontFamily = LoraFamily,
                            fontSize = 13.sp
                        )
                    }

                    // Plan Badge (Vintage Wax Seal / Leather Cartouche)
                    val planText = when (userProfile?.plan) {
                        "PRO" -> "Édition Pro ✦"
                        "MAX" -> "Édition Maître 👑"
                        else -> "Édition Libre"
                    }
                    val planColor = when (userProfile?.plan) {
                        "PRO" -> VintageCrimson
                        "MAX" -> VintageGold
                        else -> TechBlue
                    }
                    Text(
                        text = planText,
                        color = planColor,
                        fontFamily = LoraFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkCard)
                            .border(1.2.dp, planColor.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                            .clickable { onNavigate(Screen.PRICING) }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Credits & Progression Quick Bar (Vintage Antique Counter)
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = DarkCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(DarkCardElevated)
                                    .border(1.dp, VintageGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("✦", color = VintageGold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "✦ ${userProfile?.credits ?: 20} crédits disponibles",
                                    color = VintageParchment,
                                    fontFamily = PlayfairFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Rechargeable au gré de vos études",
                                    color = TextMuted,
                                    fontFamily = LoraFamily,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        TextButton(onClick = { onNavigate(Screen.CREDITS) }) {
                            Text("Approvisionner", color = VintageGold, fontFamily = LoraFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 4 Primary Action Buttons
        item {
            Text(
                text = "✦ Atelier de Travail ✦",
                color = VintageParchment,
                fontFamily = PlayfairFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DashboardActionCard(
                    title = "+ Nouveau Cours",
                    subtitle = "Manuscrit ou fichier",
                    icon = Icons.Default.Add,
                    color = TechBlue,
                    modifier = Modifier.weight(1f),
                    testTag = "dashboard_add_course_btn",
                    onClick = onAddCourse
                )

                DashboardActionCard(
                    title = "Fiche de Révision",
                    subtitle = "Résumé & Notions",
                    icon = Icons.Default.Description,
                    color = ElectricViolet,
                    modifier = Modifier.weight(1f),
                    testTag = "dashboard_create_summary_btn",
                    onClick = onCreateSummary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DashboardActionCard(
                    title = "Créer un Quiz",
                    subtitle = "Épreuve d'évaluation",
                    icon = Icons.Default.Quiz,
                    color = TurquoiseAccent,
                    modifier = Modifier.weight(1f),
                    testTag = "dashboard_create_quiz_btn",
                    onClick = onCreateQuiz
                )

                DashboardActionCard(
                    title = "Leçon Audio",
                    subtitle = "Récit & écoute",
                    icon = Icons.Default.Headphones,
                    color = AmberAction,
                    modifier = Modifier.weight(1f),
                    testTag = "dashboard_create_audio_btn",
                    onClick = onCreateAudio
                )
            }
        }

        // Section: Mes cours récents
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✦ Mes Manuscrits & Cours ✦",
                    color = VintageParchment,
                    fontFamily = PlayfairFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                if (courses.isNotEmpty()) {
                    TextButton(onClick = { onNavigate(Screen.COURSES) }) {
                        Text("Consulter tout", color = VintageGold, fontFamily = LoraFamily, fontSize = 12.sp)
                    }
                }
            }

            if (courses.isEmpty()) {
                // Mandated Professional Empty State with Antique Academic Styling
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = DarkCard,
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, VintageBorderGold.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(DarkCardElevated)
                                .border(1.5.dp, VintageBorderGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, tint = VintageGold, modifier = Modifier.size(26.dp))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Votre cabinet d'étude est encore vierge.",
                            color = VintageParchment,
                            fontFamily = PlayfairFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ajoutez votre premier manuscrit ou cours pour débuter vos révisions.",
                            color = TextMuted,
                            fontFamily = LoraFamily,
                            fontSize = 13.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = onAddCourse,
                                colors = ButtonDefaults.buttonColors(containerColor = VintageCrimson, contentColor = VintageParchment),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .border(1.dp, VintageBorderGold, RoundedCornerShape(12.dp))
                                    .testTag("empty_state_add_course_btn")
                            ) {
                                Text("+ Ajouter un cours", fontFamily = LoraFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = onSeedStarterCourse,
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold)
                            ) {
                                Text("Manuscrit démo", color = VintageGold, fontFamily = LoraFamily, fontSize = 12.sp)
                            }
                        }
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    courses.take(3).forEach { course ->
                        CourseDashboardRow(
                            course = course,
                            onClick = {
                                onSelectCourse(course)
                                onNavigate(Screen.COURSE_DETAIL)
                            }
                        )
                    }
                }
            }
        }

        // Section: Mes dernières révisions
        item {
            Text(
                text = "✦ Derniers Travaux Réalisés ✦",
                color = VintageParchment,
                fontFamily = PlayfairFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))

            if (summaries.isEmpty() && quizResults.isEmpty() && audioLessons.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = DarkCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Aucune révision enregistrée pour le moment. Rédigez une fiche ou lancez un quiz !",
                        color = TextMuted,
                        fontFamily = LoraFamily,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    summaries.take(2).forEach { summary ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = DarkCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.35f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenSummary(summary) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = VintageGold, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Fiche : ${summary.courseTitle}", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(summary.miniRecap, color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = VintageGold, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    quizResults.take(2).forEach { result ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = DarkCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Quiz, contentDescription = null, tint = TurquoiseAccent, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Quiz : ${result.courseTitle}", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text("Score : ${result.score}/20 (${result.percentage}%)", color = TextSecondary, fontFamily = LoraFamily, fontSize = 11.sp)
                                }
                                Text(
                                    text = "${result.score}/20",
                                    color = if (result.score >= 12) GreenSuccess else AmberAction,
                                    fontFamily = LoraFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section: Ma progression
        item {
            Text(
                text = "✦ Registre d'Assiduité ✦",
                color = VintageParchment,
                fontFamily = PlayfairFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = DarkCard,
                border = androidx.compose.foundation.BorderStroke(1.2.dp, VintageBorderGold.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(Screen.PROGRESSION) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⚜", color = AmberAction, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Série d'assiduité", color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Text("⚜ ${userProfile?.streakDays ?: 5} jours consécutifs", color = AmberAction, fontFamily = LoraFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatMiniItem(title = "Temps d'étude", value = "${userProfile?.studyTimeMinutes ?: 120} min")
                        StatMiniItem(title = "Manuscrits", value = "${courses.size}")
                        StatMiniItem(title = "Épreuves", value = "${quizResults.size}")
                        StatMiniItem(title = "Maîtrisées", value = "${userProfile?.masteredConceptsCount ?: 18}")
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DashboardActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    testTag: String = "",
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DarkCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.35f)),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.18f))
                    .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(text = subtitle, color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp)
        }
    }
}

@Composable
private fun CourseDashboardRow(course: Course, onClick: () -> Unit) {
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
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkCardElevated)
                    .border(1.dp, VintageBorderGold.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.School, contentDescription = null, tint = VintageGold, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(course.title, color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${course.subject} • ${course.level}", color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp)
            }
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = VintageGold, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun StatMiniItem(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = VintageGold, fontFamily = LoraFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(title, color = TextMuted, fontFamily = LoraFamily, fontSize = 10.sp)
    }
}
