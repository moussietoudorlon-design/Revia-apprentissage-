package com.example.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Course
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
fun CourseListScreen(
    courses: List<Course>,
    onSelectCourse: (Course) -> Unit,
    onAddCourseClick: () -> Unit,
    onDeleteCourse: (Course) -> Unit,
    onSeedStarterCourse: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSubjectFilter by remember { mutableStateOf("Tous") }

    val subjects = listOf("Tous", "Histoire", "Physique-Chimie", "Philosophie", "Mathématiques", "SVT", "Français")

    val filteredCourses = courses.filter { course ->
        val matchesQuery = course.title.contains(searchQuery, ignoreCase = true) ||
                course.subject.contains(searchQuery, ignoreCase = true) ||
                course.description.contains(searchQuery, ignoreCase = true)
        val matchesSubject = selectedSubjectFilter == "Tous" || course.subject.equals(selectedSubjectFilter, ignoreCase = true)
        matchesQuery && matchesSubject
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "✦ Bibliothèque des Cours ✦",
                    color = VintageParchment,
                    fontFamily = PlayfairFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${courses.size} manuscrits & leçons archivés",
                    color = VintageGold,
                    fontFamily = LoraFamily,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = onAddCourseClick,
                colors = ButtonDefaults.buttonColors(containerColor = VintageCrimson, contentColor = VintageParchment),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .border(1.dp, VintageBorderGold, RoundedCornerShape(12.dp))
                    .testTag("course_list_add_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = VintageParchment, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Ajouter", fontFamily = LoraFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search bar (Antique parchment input)
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Rechercher un manuscrit, notion, matière...", fontFamily = LoraFamily, fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = VintageGold) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VintageGold,
                unfocusedBorderColor = VintageBorderGold.copy(alpha = 0.4f),
                focusedTextColor = VintageParchment,
                unfocusedTextColor = VintageParchment,
                focusedPlaceholderColor = TextMuted,
                unfocusedPlaceholderColor = TextMuted,
                focusedContainerColor = DarkCard,
                unfocusedContainerColor = DarkCard
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("course_search_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Subject filter chips (Antique bookplate tags)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(subjects) { subject ->
                val isSelected = selectedSubjectFilter == subject
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) VintageCrimson else DarkCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) VintageBorderGold else VintageBorderGold.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { selectedSubjectFilter = subject }
                ) {
                    Text(
                        text = subject,
                        color = if (isSelected) VintageParchment else TextSecondary,
                        fontFamily = LoraFamily,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        var courseToDelete by remember { mutableStateOf<Course?>(null) }

        if (courses.isEmpty()) {
            // Mandated Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(TechBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = TechBlue, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Tu n'as encore aucun cours.",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Ajoute ton premier cours pour commencer.",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = onAddCourseClick,
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("+ Ajouter un cours", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = onSeedStarterCourse,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TurquoiseAccent)
                        ) {
                            Text("Exemple démo", color = TurquoiseAccent, fontSize = 13.sp)
                        }
                    }
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filteredCourses) { course ->
                    CourseCardItem(
                        course = course,
                        onClick = { onSelectCourse(course) },
                        onDelete = { courseToDelete = course }
                    )
                }
            }
        }

        val courseToDel = courseToDelete
        if (courseToDel != null) {
            AlertDialog(
                onDismissRequest = { courseToDelete = null },
                containerColor = DarkCard,
                titleContentColor = TextPrimary,
                textContentColor = TextSecondary,
                title = { Text("Supprimer ce cours ?") },
                text = { Text("Êtes-vous sûr de vouloir supprimer « ${courseToDel.title} » ? Tous les résumés et quiz associés seront supprimés.") },
                confirmButton = {
                    Button(
                        onClick = {
                            onDeleteCourse(courseToDel)
                            courseToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RedError)
                    ) {
                        Text("Supprimer", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { courseToDelete = null }) {
                        Text("Annuler", color = TextSecondary)
                    }
                }
            )
        }
    }
}

@Composable
fun AddCourseScreen(
    onBack: () -> Unit,
    onSubmit: (title: String, subject: String, level: String, desc: String, content: String) -> Unit,
    onLoadSample: () -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("Physique-Chimie") }
    var level by remember { mutableStateOf("Terminale") }
    var description by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    var importedFileName by remember { mutableStateOf<String?>(null) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                var fileName = "Document importé"
                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (cursor.moveToFirst() && nameIndex != -1) {
                        fileName = cursor.getString(nameIndex)
                    }
                }
                val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                if (bytes != null) {
                    if (bytes.size > 5 * 1024 * 1024) {
                        validationError = "Fichier trop volumineux (limite 5 Mo)."
                    } else {
                        val text = String(bytes, Charsets.UTF_8)
                        content = text
                        importedFileName = fileName
                        validationError = null
                        if (title.isBlank()) {
                            title = fileName.substringBeforeLast(".")
                        }
                    }
                }
            } catch (e: Exception) {
                validationError = "Erreur de lecture du fichier : ${e.localizedMessage}"
            }
        }
    }

    val subjectOptions = listOf("Histoire", "Physique-Chimie", "Philosophie", "Mathématiques", "SVT", "Français", "Économie")
    val levelOptions = listOf("Collège", "Seconde", "Première", "Terminale", "Licence 1", "Licence 2/3", "Prépa")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = TextPrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ajouter un nouveau cours", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions row: Load sample + File Import
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { filePickerLauncher.launch("*/*") },
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, TurquoiseAccent)
            ) {
                Icon(Icons.Default.FileUpload, contentDescription = null, tint = TurquoiseAccent, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Importer un fichier (.txt, doc)", color = TurquoiseAccent, fontSize = 11.sp)
            }

            TextButton(onClick = onLoadSample) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TurquoiseAccent, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Charger exemple", color = TurquoiseAccent, fontSize = 11.sp)
            }
        }

        if (importedFileName != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = GreenSuccess.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, GreenSuccess),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GreenSuccess, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Fichier importé : $importedFileName (${content.length} caractères)",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }

        if (validationError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = RedError.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, RedError),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = RedError, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = validationError ?: "",
                        color = RedError,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Title
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                if (validationError != null) validationError = null
            },
            label = { Text("Nom du cours *") },
            placeholder = { Text("Ex: Les Lois de Newton, La Guerre Froide...") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ElectricViolet,
                unfocusedBorderColor = DarkBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedLabelColor = TurquoiseAccent,
                unfocusedLabelColor = TextMuted
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("course_title_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Subject Selector
        Text("Matière :", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(vertical = 4.dp)) {
            items(subjectOptions) { s ->
                val selected = subject == s
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (selected) ElectricViolet else DarkCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) ElectricViolet else DarkBorder),
                    modifier = Modifier.clickable { subject = s }
                ) {
                    Text(s, color = if (selected) Color.White else TextSecondary, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Level Selector
        Text("Niveau scolaire :", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(vertical = 4.dp)) {
            items(levelOptions) { l ->
                val selected = level == l
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (selected) TechBlue else DarkCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) TechBlue else DarkBorder),
                    modifier = Modifier.clickable { level = l }
                ) {
                    Text(l, color = if (selected) Color.White else TextSecondary, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Description
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description rapide (optionnelle)") },
            placeholder = { Text("Ex: Chapitre 2 du second trimestre...") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ElectricViolet,
                unfocusedBorderColor = DarkBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedLabelColor = TurquoiseAccent,
                unfocusedLabelColor = TextMuted
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Content
        OutlinedTextField(
            value = content,
            onValueChange = {
                content = it
                if (validationError != null) validationError = null
            },
            label = { Text("Contenu du cours (coller le texte complet) *") },
            placeholder = { Text("Colle ici le texte de ton cours, cours dicté, notes, polycopié...") },
            minLines = 8,
            maxLines = 14,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ElectricViolet,
                unfocusedBorderColor = DarkBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedLabelColor = TurquoiseAccent,
                unfocusedLabelColor = TextMuted
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("course_content_input")
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${content.length} caractères (min. 30)",
                color = if (content.length < 30 && content.isNotEmpty()) AmberAction else TextMuted,
                fontSize = 11.sp
            )
            if (content.length > 50000) {
                Text(
                    text = "Cours très détaillé",
                    color = TurquoiseAccent,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = {
                if (title.trim().isBlank()) {
                    validationError = "Veuillez saisir un nom pour le cours."
                } else if (content.trim().isBlank()) {
                    validationError = "Veuillez coller le texte de votre cours ou importer un fichier."
                } else if (content.trim().length < 30) {
                    validationError = "Le contenu est trop court pour une analyse IA de qualité (au moins 30 caractères)."
                } else {
                    validationError = null
                    onSubmit(title.trim(), subject, level, description.trim(), content.trim())
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("submit_analyze_course_btn")
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TurquoiseAccent)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Analyser avec IA", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

@Composable
fun CourseDetailScreen(
    course: Course,
    onBack: () -> Unit,
    onGenerateSummary: () -> Unit,
    onGenerateQuiz: () -> Unit,
    onGenerateFlashcards: () -> Unit,
    onGenerateAudio: () -> Unit,
    onOpenTutor: () -> Unit,
    onDeleteCourse: (Course) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var showSourceContent by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = TextPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text("Détail du cours", color = TextMuted, fontSize = 13.sp)
            }

            IconButton(onClick = { showDeleteConfirmDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Supprimer le cours", tint = RedError.copy(alpha = 0.8f))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(course.title, color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Black)

        Spacer(modifier = Modifier.height(6.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = course.subject,
                color = ElectricViolet,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(ElectricViolet.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            )
            Text(
                text = course.level,
                color = TurquoiseAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(TurquoiseAccent.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }

        if (course.extractedTopics.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DarkCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TurquoiseAccent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Notions extraites par REVIA IA :", color = TurquoiseAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(course.extractedTopics, color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
                }
            }
        }

        // Expandable Source Content Card
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = DarkCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showSourceContent = !showSourceContent }
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = TechBlue, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Contenu source (${course.rawContent.length} caractères)",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Icon(
                        imageVector = if (showSourceContent) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = TextMuted
                    )
                }

                AnimatedVisibility(visible = showSourceContent) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkBg,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = course.rawContent,
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Outils de révision IA disponibles :", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))

        CourseActionLauncher(
            title = "Fiche de Révision IA",
            desc = "Résumé, notions essentielles, définitions, formules et pièges",
            icon = Icons.Default.Description,
            badge = "2 crédits",
            accent = ElectricViolet,
            onClick = onGenerateSummary
        )

        Spacer(modifier = Modifier.height(10.dp))

        CourseActionLauncher(
            title = "Générateur de Quiz",
            desc = "5, 10, 20 ou 30 questions avec correction détaillée",
            icon = Icons.Default.Quiz,
            badge = "3 crédits",
            accent = TurquoiseAccent,
            onClick = onGenerateQuiz
        )

        Spacer(modifier = Modifier.height(10.dp))

        CourseActionLauncher(
            title = "Deck de Flashcards",
            desc = "Mémorisation active avec retournement et auto-évaluation",
            icon = Icons.Default.Style,
            badge = "2 crédits",
            accent = TechBlue,
            onClick = onGenerateFlashcards
        )

        Spacer(modifier = Modifier.height(10.dp))

        CourseActionLauncher(
            title = "Leçon Audio IA",
            desc = "Script oralisé et lecteur avec voix française",
            icon = Icons.Default.Headphones,
            badge = "4 crédits",
            accent = AmberAction,
            onClick = onGenerateAudio
        )

        Spacer(modifier = Modifier.height(10.dp))

        CourseActionLauncher(
            title = "Tuteur IA Pédagogique",
            desc = "Dialogue interactif ancré dans le cours",
            icon = Icons.Default.Psychology,
            badge = "1 crédit/msg",
            accent = Color(0xFF10B981),
            onClick = onOpenTutor
        )

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            containerColor = DarkCard,
            titleContentColor = TextPrimary,
            textContentColor = TextSecondary,
            title = { Text("Supprimer ce cours ?") },
            text = { Text("Êtes-vous certain de vouloir supprimer « ${course.title} » ? Cette action est irréversible.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        onDeleteCourse(course)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedError)
                ) {
                    Text("Supprimer", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Annuler", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun CourseCardItem(course: Course, onClick: () -> Unit, onDelete: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DarkCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.35f)),
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
                    .background(DarkCardElevated)
                    .border(1.dp, VintageBorderGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.School, contentDescription = null, tint = VintageGold, modifier = Modifier.size(22.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    course.title,
                    color = VintageParchment,
                    fontFamily = PlayfairFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${course.subject} • ${course.level}",
                    color = VintageGold,
                    fontFamily = LoraFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                if (course.description.isNotBlank()) {
                    Text(
                        course.description,
                        color = TextMuted,
                        fontFamily = LoraFamily,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = TextMuted, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun CourseActionLauncher(
    title: String,
    desc: String,
    icon: ImageVector,
    badge: String,
    accent: Color,
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
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accent.copy(alpha = 0.18f))
                    .border(1.dp, accent.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        title,
                        color = VintageParchment,
                        fontFamily = PlayfairFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        badge,
                        color = accent,
                        fontFamily = LoraFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(accent.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Text(
                    desc,
                    color = TextMuted,
                    fontFamily = LoraFamily,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = VintageGold, modifier = Modifier.size(16.dp))
        }
    }
}
