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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Course
import com.example.data.model.QuizResult
import com.example.data.model.TutorMessage
import com.example.data.model.UserProfile
import com.example.ui.theme.AmberAction
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardElevated
import com.example.ui.theme.ElectricViolet
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

@Composable
fun TutorScreen(
    courses: List<Course>,
    selectedCourse: Course?,
    messages: List<TutorMessage>,
    onSelectCourse: (Course) -> Unit,
    onSendMessage: (question: String, modifier: String?) -> Unit,
    onAddCourse: () -> Unit
) {
    var inputQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(16.dp)
    ) {
        // Header
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
                        .background(DarkCardElevated)
                        .border(1.2.dp, VintageBorderGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Psychology, contentDescription = null, tint = VintageGold, modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        "✦ Mentor Académique IA ✦",
                        color = VintageParchment,
                        fontFamily = PlayfairFamily,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Dialogue socratique & explications approfondies",
                        color = VintageGold,
                        fontFamily = LoraFamily,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Course Selector Chips
        if (courses.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DarkCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Ajoute un manuscrit pour interroger le mentor IA", color = TextMuted, fontFamily = LoraFamily, fontSize = 12.sp)
                    Button(
                        onClick = onAddCourse,
                        colors = ButtonDefaults.buttonColors(containerColor = VintageCrimson, contentColor = VintageParchment),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.border(1.dp, VintageBorderGold, RoundedCornerShape(8.dp))
                    ) {
                        Text("+ Ajouter", fontFamily = LoraFamily, fontSize = 11.sp)
                    }
                }
            }
        } else {
            Text("Sélectionne le manuscrit à étudier :", color = VintageGold, fontFamily = LoraFamily, fontSize = 11.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(vertical = 4.dp)) {
                items(courses) { course ->
                    val isSelected = selectedCourse?.id == course.id
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) VintageCrimson else DarkCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) VintageBorderGold else VintageBorderGold.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onSelectCourse(course) }
                    ) {
                        Text(
                            text = course.title,
                            color = if (isSelected) VintageParchment else TextSecondary,
                            fontFamily = LoraFamily,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick action prompt chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            item {
                TutorActionChip("Explique plus simplement") {
                    onSendMessage("Explique-moi ce cours plus simplement avec une métaphore.", "SIMPLIFY")
                }
            }
            item {
                TutorActionChip("Donne un exemple") {
                    onSendMessage("Donne-moi un exemple concret d'application de cette notion.", "EXAMPLE")
                }
            }
            item {
                TutorActionChip("Interroge-moi") {
                    onSendMessage("Pose-moi une question sur le cours pour tester ma compréhension.", "QUIZ_ME")
                }
            }
            item {
                TutorActionChip("Fais la synthèse") {
                    onSendMessage("Fais-moi un résumé express en 3 points clés.", "SUMMARY")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Chat messages
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TurquoiseAccent, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Pose ta première question !", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Le tuteur REVIA t'explique pas à pas et t'aide à comprendre.",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            } else {
                items(messages) { msg ->
                    ChatBubble(message = msg)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Input Bar (Antique parchment writing dock)
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = DarkCard,
            border = androidx.compose.foundation.BorderStroke(1.2.dp, VintageBorderGold.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputQuery,
                    onValueChange = { inputQuery = it },
                    placeholder = { Text("Interrogez le mentor socratique...", fontFamily = LoraFamily, fontSize = 12.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = VintageParchment,
                        unfocusedTextColor = VintageParchment,
                        focusedPlaceholderColor = TextMuted,
                        unfocusedPlaceholderColor = TextMuted
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("tutor_input_field")
                )

                IconButton(
                    onClick = {
                        if (inputQuery.isNotBlank()) {
                            val text = inputQuery
                            inputQuery = ""
                            onSendMessage(text, null)
                        }
                    },
                    modifier = Modifier.testTag("tutor_send_btn")
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Envoyer", tint = VintageGold)
                }
            }
        }
    }
}

@Composable
private fun TutorActionChip(text: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DarkCardElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.4f)),
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            color = VintageGold,
            fontFamily = LoraFamily,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun ChatBubble(message: TutorMessage) {
    val isUser = message.sender == "USER"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isUser) 14.dp else 2.dp,
                bottomEnd = if (isUser) 2.dp else 14.dp
            ),
            color = if (isUser) VintageCrimson else DarkCardElevated,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isUser) VintageBorderGold.copy(alpha = 0.6f) else VintageBorderGold.copy(alpha = 0.35f)
            ),
            modifier = Modifier.widthIn(max = 295.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = if (isUser) "Étudiant" else "Mentor Socratique IA",
                    color = if (isUser) VintageParchment.copy(alpha = 0.85f) else VintageGold,
                    fontFamily = PlayfairFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message.text,
                    color = VintageParchment,
                    fontFamily = LoraFamily,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }
        }
    }
}

@Composable
fun ProgressionScreen(
    userProfile: UserProfile?,
    coursesCount: Int,
    quizResults: List<QuizResult>
) {
    val scrollState = rememberScrollState()

    val avgScore = if (quizResults.isNotEmpty()) {
        quizResults.map { it.score }.average().toInt()
    } else 14

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            "✦ Registre de Savoir & Progression ✦",
            color = VintageParchment,
            fontFamily = PlayfairFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Chronique des accomplissements et maîtrise des traités",
            color = VintageGold,
            fontFamily = LoraFamily,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Hero Streak Card (Antique parchment achievement banner)
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkCard,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, VintageBorderGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(VintageGold.copy(alpha = 0.15f))
                        .border(1.dp, VintageBorderGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = VintageGold, modifier = Modifier.size(30.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "✦ ${userProfile?.streakDays ?: 5} jours d'études consécutifs",
                        color = VintageGold,
                        fontFamily = PlayfairFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    Text(
                        text = "Votre rigueur intellectuelle porte ses fruits. Les concepts s'ancrent durablement dans votre mémoire.",
                        color = TextSecondary,
                        fontFamily = LoraFamily,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Key Metrics Grid
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard(title = "Traités étudiés", value = "$coursesCount", icon = Icons.Default.School, color = VintageGold, modifier = Modifier.weight(1f))
            MetricCard(title = "Épreuves réussies", value = "${quizResults.size}", icon = Icons.Default.CheckCircle, color = Color(0xFF6B8E23), modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard(title = "Moyenne académique", value = "$avgScore / 20", icon = Icons.Default.AutoAwesome, color = VintageCrimson, modifier = Modifier.weight(1f))
            MetricCard(title = "Temps de lecture", value = "${userProfile?.studyTimeMinutes ?: 120} min", icon = Icons.Default.Timer, color = Color(0xFFC5A059), modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Concepts Mastery Status
        Text(
            "Maîtrise des doctrines & notions :",
            color = VintageParchment,
            fontFamily = PlayfairFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = DarkCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Notions assimilées avec brio", color = Color(0xFF6B8E23), fontFamily = LoraFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("${userProfile?.masteredConceptsCount ?: 18} notions", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { 0.82f },
                    color = Color(0xFF6B8E23),
                    trackColor = DarkCardElevated,
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Notions nécessitant approfondissement", color = VintageGold, fontFamily = LoraFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("${userProfile?.needsReviewCount ?: 4} notions", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { 0.18f },
                    color = VintageGold,
                    trackColor = DarkCardElevated,
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DarkCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.35f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.15f))
                    .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(title, color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp)
        }
    }
}
