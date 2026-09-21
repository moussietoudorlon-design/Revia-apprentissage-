package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Course
import com.example.data.model.Quiz
import com.example.data.model.QuizResult
import com.example.ui.theme.AmberAction
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardElevated
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.LoraFamily
import com.example.ui.theme.PlayfairFamily
import com.example.ui.theme.RedError
import com.example.ui.theme.TechBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TurquoiseAccent
import com.example.ui.theme.VintageBorderGold
import com.example.ui.theme.VintageCrimson
import com.example.ui.theme.VintageGold
import com.example.ui.theme.VintageParchment
import com.example.ui.viewmodel.QuizQuestion

@Composable
fun QuizSetupScreen(
    course: Course,
    onBack: () -> Unit,
    onStartQuiz: (count: Int, difficulty: String, type: String) -> Unit
) {
    var questionCount by remember { mutableStateOf(10) }
    var difficulty by remember { mutableStateOf("Moyen") }
    var quizType by remember { mutableStateOf("QCM") }

    val counts = listOf(5, 10, 20, 30)
    val difficulties = listOf("Facile", "Moyen", "Difficile")
    val types = listOf("QCM", "Vrai-Faux", "Questions ouvertes", "Mixte")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = VintageGold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("✦ Paramétrer l'Épreuve Académique ✦", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text("Manuscrit sélectionné :", color = TextMuted, fontFamily = LoraFamily, fontSize = 12.sp)
            Text(course.title, color = VintageGold, fontFamily = PlayfairFamily, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Question Count
            Text("Nombre de questions :", color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                counts.forEach { c ->
                    val isSelected = questionCount == c
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) VintageCrimson else DarkCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) VintageBorderGold else VintageBorderGold.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { questionCount = c }
                    ) {
                        Text(
                            text = "$c",
                            color = if (isSelected) VintageParchment else TextSecondary,
                            fontFamily = LoraFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Difficulty
            Text("Degré d'exigence académique :", color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                difficulties.forEach { d ->
                    val isSelected = difficulty == d
                    val activeColor = when (d) {
                        "Facile" -> Color(0xFF6B8E23)
                        "Moyen" -> VintageGold
                        else -> VintageCrimson
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) activeColor else DarkCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) VintageBorderGold else VintageBorderGold.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { difficulty = d }
                    ) {
                        Text(
                            text = d,
                            color = if (isSelected) VintageParchment else TextSecondary,
                            fontFamily = LoraFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Quiz Type
            Text("Format de l'interrogation :", color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                types.forEach { t ->
                    val isSelected = quizType == t
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) VintageGold.copy(alpha = 0.15f) else DarkCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) VintageBorderGold else VintageBorderGold.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { quizType = t }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, if (isSelected) VintageGold else TextMuted, CircleShape)
                                    .background(if (isSelected) VintageGold else Color.Transparent)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = t,
                                color = if (isSelected) VintageGold else TextPrimary,
                                fontFamily = LoraFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Generate Quiz CTA
        Button(
            onClick = { onStartQuiz(questionCount, difficulty, quizType) },
            colors = ButtonDefaults.buttonColors(containerColor = VintageCrimson, contentColor = VintageParchment),
            border = androidx.compose.foundation.BorderStroke(1.2.dp, VintageBorderGold),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("launch_quiz_btn")
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = VintageGold)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Initier l'Épreuve ($questionCount Q • 3 parchemins)", fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

@Composable
fun QuizRunnerScreen(
    quiz: Quiz,
    questions: List<QuizQuestion>,
    currentIndex: Int,
    selectedAnswers: Map<Int, Int>,
    quizResult: QuizResult?,
    onSelectAnswer: (questionIndex: Int, optionIndex: Int) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onExit: () -> Unit,
    onRestart: () -> Unit
) {
    if (quizResult != null) {
        QuizResultsView(result = quizResult, questions = questions, selectedAnswers = selectedAnswers, onExit = onExit, onRestart = onRestart)
        return
    }

    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().background(DarkBg), contentAlignment = Alignment.Center) {
            Text("Chargement du quiz...", color = TextPrimary)
        }
        return
    }

    val currentQ = questions[currentIndex.coerceIn(0, questions.size - 1)]
    val progress = (currentIndex + 1).toFloat() / questions.size.toFloat()
    val userSelected = selectedAnswers[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Header & Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onExit) {
                    Icon(Icons.Default.Close, contentDescription = "Quitter", tint = VintageGold)
                }
                Text(
                    text = "✦ Question ${currentIndex + 1} / ${questions.size} ✦",
                    color = VintageParchment,
                    fontFamily = PlayfairFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = quiz.difficulty,
                    color = VintageGold,
                    fontFamily = LoraFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                color = VintageGold,
                trackColor = DarkCardElevated,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Question Card (Parchment question board)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = DarkCard,
                border = androidx.compose.foundation.BorderStroke(1.2.dp, VintageBorderGold),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = currentQ.question,
                    color = VintageParchment,
                    fontFamily = PlayfairFamily,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Options list
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                currentQ.options.forEachIndexed { optIndex, optionText ->
                    val isSelected = userSelected == optIndex
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) VintageCrimson.copy(alpha = 0.25f) else DarkCard,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) VintageBorderGold else VintageBorderGold.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectAnswer(currentIndex, optIndex) }
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) VintageCrimson else DarkCardElevated)
                                    .border(1.dp, if (isSelected) VintageBorderGold else VintageBorderGold.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                val label = ('A' + optIndex).toString()
                                Text(
                                    text = label,
                                    color = if (isSelected) VintageParchment else TextSecondary,
                                    fontFamily = PlayfairFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = optionText,
                                color = if (isSelected) VintageParchment else TextPrimary,
                                fontFamily = LoraFamily,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // Navigation row (Précédent / Suivant)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onPrevious,
                enabled = currentIndex > 0,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.4f))
            ) {
                Text("Précédent", fontFamily = LoraFamily, color = if (currentIndex > 0) VintageParchment else TextMuted)
            }

            Button(
                onClick = onNext,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentIndex == questions.size - 1) Color(0xFF4A6B2F) else VintageCrimson,
                    contentColor = VintageParchment
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("quiz_next_submit_btn")
            ) {
                Text(
                    text = if (currentIndex == questions.size - 1) "Conclure l'Épreuve" else "Suivant",
                    fontFamily = PlayfairFamily,
                    fontWeight = FontWeight.Bold,
                    color = VintageParchment
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = VintageParchment, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun QuizResultsView(
    result: QuizResult,
    questions: List<QuizQuestion>,
    selectedAnswers: Map<Int, Int>,
    onExit: () -> Unit,
    onRestart: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("✦ Bilan Académique de l'Épreuve ✦", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Traité : ${result.courseTitle}", color = VintageGold, fontFamily = LoraFamily, fontSize = 13.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Score Card (Antique Diploma Seal)
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = DarkCard,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, VintageBorderGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${result.score} / 20",
                    color = VintageGold,
                    fontFamily = PlayfairFamily,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${result.percentage}% d'exactitude doctrinale",
                    color = VintageParchment,
                    fontFamily = LoraFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("✦ ${result.correctCount}", color = Color(0xFF6B8E23), fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text("Justes", color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("✦ ${result.incorrectCount}", color = VintageCrimson, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text("À revoir", color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Total", color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text("${result.totalQuestions} Q", color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pedagogical recommendation
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarkCardElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = VintageGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(result.reviewRecommendation, color = VintageParchment, fontFamily = LoraFamily, fontSize = 12.sp, lineHeight = 17.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Examen critique par question :", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 15.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(10.dp))

        // Detailed answers review
        questions.forEachIndexed { qIdx, question ->
            val userChoice = selectedAnswers[qIdx]
            val isCorrect = userChoice == question.correctIndex

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DarkCard,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isCorrect) Color(0xFF6B8E23).copy(alpha = 0.6f) else VintageCrimson.copy(alpha = 0.6f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                            contentDescription = null,
                            tint = if (isCorrect) Color(0xFF6B8E23) else VintageCrimson,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = question.question,
                            color = VintageParchment,
                            fontFamily = PlayfairFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val correctText = question.options.getOrNull(question.correctIndex) ?: ""
                    val userText = userChoice?.let { question.options.getOrNull(it) } ?: "Non répondu"

                    Text("Votre réponse : $userText", color = if (isCorrect) Color(0xFF6B8E23) else VintageCrimson, fontFamily = LoraFamily, fontSize = 12.sp)
                    if (!isCorrect) {
                        Text("Doctrine conforme : $correctText", color = Color(0xFF6B8E23), fontFamily = LoraFamily, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text("✦ Commentaire du maître : ${question.explanation}", color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp, lineHeight = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onRestart,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.5f)),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = VintageGold, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Recommencer", fontFamily = LoraFamily, color = VintageParchment)
            }

            Button(
                onClick = onExit,
                colors = ButtonDefaults.buttonColors(containerColor = VintageCrimson, contentColor = VintageParchment),
                border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("Retour aux traités", fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold)
            }
        }
    }
}
