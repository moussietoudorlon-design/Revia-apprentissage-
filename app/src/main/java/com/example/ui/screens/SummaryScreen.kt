package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RevisionSummary
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardElevated
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.LoraFamily
import com.example.ui.theme.PlayfairFamily
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TurquoiseAccent
import com.example.ui.theme.VintageBorderGold
import com.example.ui.theme.VintageCrimson
import com.example.ui.theme.VintageGold
import com.example.ui.theme.VintageParchment

@Composable
fun SummaryScreen(
    summary: RevisionSummary,
    onBack: () -> Unit,
    onRegenerate: () -> Unit,
    onToast: (String) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var isEditing by remember { mutableStateOf(false) }
    var editableSummaryText by remember { mutableStateOf(summary.summaryText) }

    // Section collapse states
    var expandResume by remember { mutableStateOf(true) }
    var expandNotions by remember { mutableStateOf(true) }
    var expandDefinitions by remember { mutableStateOf(true) }
    var expandPoints by remember { mutableStateOf(true) }
    var expandExemples by remember { mutableStateOf(true) }
    var expandPieges by remember { mutableStateOf(true) }
    var expandRecap by remember { mutableStateOf(true) }

    fun copyToClipboard() {
        val fullText = buildString {
            appendLine("=== FICHE DE RÉVISION REVIA AI : ${summary.courseTitle} ===")
            appendLine("\n1. RÉSUMÉ SYNTHÉTIQUE :\n$editableSummaryText")
            appendLine("\n2. NOTIONS ESSENTIELLES :\n${summary.keyConcepts}")
            appendLine("\n3. DÉFINITIONS À CONNAÎTRE :\n${summary.definitions}")
            appendLine("\n4. POINTS CLÉS À RETENIR :\n${summary.keyTakeaways}")
            appendLine("\n5. EXEMPLES CONCRETS :\n${summary.examples}")
            appendLine("\n6. PIÈGES FRÉQUENTS :\n${summary.commonMistakes}")
            appendLine("\n7. MINI-RÉCAPITULATIF :\n${summary.miniRecap}")
        }
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Fiche REVIA", fullText)
        clipboard.setPrimaryClip(clip)
        onToast("Fiche copiée dans le presse-papier !")
    }

    fun shareSummary() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Fiche de révision REVIA : ${summary.courseTitle}\n\n${summary.miniRecap}\n\n$editableSummaryText")
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "Partager ma fiche REVIA"))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Top row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = VintageGold)
            }
            Text(
                "✦ Fiche d'Étude & Synthèse ✦",
                color = VintageParchment,
                fontFamily = PlayfairFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onRegenerate) {
                Icon(Icons.Default.Refresh, contentDescription = "Régénérer", tint = VintageGold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            summary.courseTitle,
            color = VintageParchment,
            fontFamily = PlayfairFamily,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Actions toolbar: Modifier, Copier, Partager, Exporter
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryActionButton(
                icon = Icons.Default.Edit,
                text = if (isEditing) "Sauver" else "Annoter",
                color = VintageCrimson,
                modifier = Modifier.weight(1f),
                onClick = {
                    isEditing = !isEditing
                    if (!isEditing) onToast("Annotations enregistrées dans le grimoire.")
                }
            )

            SummaryActionButton(
                icon = Icons.Default.ContentCopy,
                text = "Copier",
                color = VintageGold,
                modifier = Modifier.weight(1f),
                onClick = { copyToClipboard() }
            )

            SummaryActionButton(
                icon = Icons.Default.Share,
                text = "Diffuser",
                color = Color(0xFF6B8E23),
                modifier = Modifier.weight(1f),
                onClick = { shareSummary() }
            )

            SummaryActionButton(
                icon = Icons.Default.PictureAsPdf,
                text = "Parchemin",
                color = Color(0xFFD4AF37),
                modifier = Modifier.weight(1f),
                onClick = {
                    copyToClipboard()
                    onToast("Fiche prête pour l'impression ou l'archivage.")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 7 Imposed Sections
        CollapsibleSection(
            number = "1",
            title = "Résumé synthétique",
            isExpanded = expandResume,
            onToggle = { expandResume = !expandResume }
        ) {
            if (isEditing) {
                OutlinedTextField(
                    value = editableSummaryText,
                    onValueChange = { editableSummaryText = it },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(editableSummaryText, color = TextSecondary, fontSize = 13.sp, lineHeight = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        CollapsibleSection(
            number = "2",
            title = "Notions essentielles",
            isExpanded = expandNotions,
            onToggle = { expandNotions = !expandNotions }
        ) {
            Text(summary.keyConcepts, color = TextSecondary, fontSize = 13.sp, lineHeight = 20.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        CollapsibleSection(
            number = "3",
            title = "Définitions à connaître",
            isExpanded = expandDefinitions,
            onToggle = { expandDefinitions = !expandDefinitions }
        ) {
            Text(summary.definitions, color = TextSecondary, fontSize = 13.sp, lineHeight = 20.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        CollapsibleSection(
            number = "4",
            title = "Points clés à retenir",
            isExpanded = expandPoints,
            onToggle = { expandPoints = !expandPoints }
        ) {
            Text(summary.keyTakeaways, color = TextSecondary, fontSize = 13.sp, lineHeight = 20.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        CollapsibleSection(
            number = "5",
            title = "Exemples concrets",
            isExpanded = expandExemples,
            onToggle = { expandExemples = !expandExemples }
        ) {
            Text(summary.examples, color = TextSecondary, fontSize = 13.sp, lineHeight = 20.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        CollapsibleSection(
            number = "6",
            title = "Pièges fréquents & erreurs d'examen",
            isExpanded = expandPieges,
            onToggle = { expandPieges = !expandPieges },
            accentColor = Color(0xFFEF4444)
        ) {
            Text(summary.commonMistakes, color = TextSecondary, fontSize = 13.sp, lineHeight = 20.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        CollapsibleSection(
            number = "7",
            title = "Mini-récapitulatif",
            isExpanded = expandRecap,
            onToggle = { expandRecap = !expandRecap },
            accentColor = TurquoiseAccent
        ) {
            Text(summary.miniRecap, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, lineHeight = 20.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SummaryActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    color: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DarkCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.4f)),
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text, color = VintageParchment, fontFamily = LoraFamily, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun CollapsibleSection(
    number: String,
    title: String,
    isExpanded: Boolean,
    accentColor: Color = VintageGold,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DarkCard,
        border = androidx.compose.foundation.BorderStroke(1.2.dp, VintageBorderGold.copy(alpha = 0.35f)),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "§ $number.",
                        color = accentColor,
                        fontFamily = PlayfairFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        color = VintageParchment,
                        fontFamily = PlayfairFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = VintageGold
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    content()
                }
            }
        }
    }
}
