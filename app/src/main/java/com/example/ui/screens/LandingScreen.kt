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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun LandingScreen(
    onStartFree: () -> Unit,
    onLoginClick: () -> Unit,
    onPricingClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .verticalScroll(scrollState)
    ) {
        // Top Nav header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(VintageCrimson)
                        .border(1.5.dp, VintageBorderGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("R", color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "ACADÉMIE REVIA",
                    color = VintageParchment,
                    fontFamily = PlayfairFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp,
                    letterSpacing = 0.5.sp
                )
            }

            Row {
                Button(
                    onClick = onLoginClick,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkCardElevated, contentColor = VintageParchment),
                    border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Connexion", fontFamily = LoraFamily, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Hero Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Slogan pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(VintageCrimson.copy(alpha = 0.25f))
                    .border(1.dp, VintageBorderGold.copy(alpha = 0.6f), RoundedCornerShape(30.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = VintageGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "✦ Apprends avec rigueur & sagesse ✦",
                    color = VintageGold,
                    fontFamily = LoraFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Ton Scriptorium & Précepteur IA.",
                color = VintageParchment,
                fontFamily = PlayfairFamily,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Transforme tes manuscrits et cours en synthèses illustres, fiches, épreuves et conférences audio oralisées.",
                color = TextSecondary,
                fontFamily = LoraFamily,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // CTA Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onStartFree,
                    colors = ButtonDefaults.buttonColors(containerColor = VintageCrimson, contentColor = VintageParchment),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, VintageBorderGold),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("landing_start_free_btn")
                ) {
                    Text(
                        text = "Entrer à l'Académie",
                        fontFamily = PlayfairFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                OutlinedButton(
                    onClick = onPricingClick,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, VintageBorderGold),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = VintageGold),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Voir les Ordres",
                        fontFamily = PlayfairFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Demo Preview Card
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = DarkCard,
                border = androidx.compose.foundation.BorderStroke(1.2.dp, VintageBorderGold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
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
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(VintageGold)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Démonstration du Scriptorium", color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Text("📜 20 parchemins offerts", color = VintageGold, fontFamily = LoraFamily, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    DemoCardRow(title = "Fiche d'exégèse et synthèse", tag = "En 3 secondes", color = VintageGold)
                    DemoCardRow(title = "Questionnaire interactif (10 questions)", tag = "Auto-corrigé", color = VintageBorderGold)
                    DemoCardRow(title = "Cartes mémorielles d'érudit", tag = "Répétition espacée", color = VintageParchment)
                    DemoCardRow(title = "Conférence audio de traité", tag = "Voix oralisée", color = VintageGold)
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Section: Comment ça marche
        SectionTitle(title = "✦ Les Trois Degrés d'Étude ✦", subtitle = "La méthode pour maîtriser chaque discipline")

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            StepCard(
                number = "I",
                title = "Consigne ton traité",
                desc = "Colle le texte du cours, dépose tes manuscrits ou pioche dans notre recueil savant."
            )
            Spacer(modifier = Modifier.height(10.dp))
            StepCard(
                number = "II",
                title = "L'Oracle distille le savoir",
                desc = "L'Académie extrait définitions canoniques, théorèmes majeurs et pièges classiques d'épreuves."
            )
            Spacer(modifier = Modifier.height(10.dp))
            StepCard(
                number = "III",
                title = "Mémorise avec méthode",
                desc = "Exerce-toi aux épreuves interactives, écoute la leçon audio et questionne ton précepteur personnel."
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Section: Fonctionnalités
        SectionTitle(title = "✦ Attributs Savants ✦", subtitle = "Les instruments indispensables à tout érudit")

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            FeatureItem(
                icon = Icons.Default.Description,
                title = "Fiches Académiques d'Exégèse",
                desc = "Synthèses reliées avec définitions rigoureuses, exemples historiques et pièges d'examen.",
                accent = VintageGold
            )
            Spacer(modifier = Modifier.height(10.dp))
            FeatureItem(
                icon = Icons.Default.Quiz,
                title = "Épreuves & Questionnaires",
                desc = "5, 10, 20 ou 30 questions ciblées avec correction argumentée et note sur 20.",
                accent = VintageCrimson
            )
            Spacer(modifier = Modifier.height(10.dp))
            FeatureItem(
                icon = Icons.Default.Headphones,
                title = "Leçons Audio & Conférences",
                desc = "Métamorphose n'importe quel texte dense en cours oralisé captivant avec lecture vocale.",
                accent = VintageBorderGold
            )
            Spacer(modifier = Modifier.height(10.dp))
            FeatureItem(
                icon = Icons.Default.Psychology,
                title = "Précepteur Personnel IA",
                desc = "Dialogues érudits 24/7 ancrés mot pour mot dans tes traités pour lever tout doute.",
                accent = VintageGold
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Section: Pourquoi REVIA AI
        SectionTitle(title = "✦ Les Vertus de l'Académie ✦", subtitle = "Une exigence d'excellence pour tes résultats")

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            WhyCard(
                icon = Icons.Default.Speed,
                title = "Gain de temps quadruple",
                desc = "Épargne de longues heures de recopie stérile. Concentre ton esprit sur l'assimilation vivante."
            )
            Spacer(modifier = Modifier.height(10.dp))
            WhyCard(
                icon = Icons.Default.School,
                title = "Conforme aux programmes",
                desc = "Adapté aux cycles du secondaire, baccalauréat, classes préparatoires et facultés."
            )
            Spacer(modifier = Modifier.height(10.dp))
            WhyCard(
                icon = Icons.Default.Shield,
                title = "Secret & Préservation",
                desc = "Tes cours et parchemins restent strictement confidentiels et consignés en lieu sûr."
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Section: FAQ
        SectionTitle(title = "✦ Éclaircissements & Réponses (FAQ) ✦", subtitle = "Tout ce que l'Initié doit savoir")

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            FaqAccordion(
                question = "L'accès aux premiers parchemins est-il libre ?",
                answer = "Oui ! Chaque nouvel initié reçoit 20 parchemins d'or à son entrée, permettant de façonner fiches, épreuves et cartes immédiatement sans contrepartie financière."
            )
            Spacer(modifier = Modifier.height(8.dp))
            FaqAccordion(
                question = "Comment se décomptent les parchemins ?",
                answer = "Une fiche requiert 2 parchemins, un quiz 3 parchemins, un jeu de cartes 2 parchemins et une leçon audio 4 parchemins. Les ordres supérieurs bénéficient de dotations récurrentes généreuses."
            )
            Spacer(modifier = Modifier.height(8.dp))
            FaqAccordion(
                question = "Les traités sont-ils accessibles hors connexion ?",
                answer = "Absolument. Tes fiches rédigées, cartes et résultats d'épreuves sont gravés dans ta bibliothèque locale Room sur ton appareil."
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Footer
        Surface(
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ACADÉMIE REVIA",
                    color = VintageParchment,
                    fontFamily = PlayfairFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Apprends avec rigueur. Réussis avec éclat.",
                    color = VintageGold,
                    fontFamily = LoraFamily,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "© 2026 Académie REVIA. Scriptorium d'études et plateforme d'érudition.",
                    color = TextMuted,
                    fontFamily = LoraFamily,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = VintageParchment,
            fontFamily = PlayfairFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            color = VintageGold,
            fontFamily = LoraFamily,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun DemoCardRow(title: String, tag: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(DarkCardElevated)
            .border(1.dp, VintageBorderGold.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = VintageParchment, fontFamily = LoraFamily, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Text(
            tag,
            color = color,
            fontFamily = LoraFamily,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(color.copy(alpha = 0.15f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun StepCard(number: String, title: String, desc: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DarkCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.45f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Text(
                text = number,
                color = VintageGold,
                fontFamily = PlayfairFamily,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = desc, color = TextSecondary, fontFamily = LoraFamily, fontSize = 12.sp, lineHeight = 17.sp)
            }
        }
    }
}

@Composable
private fun FeatureItem(icon: ImageVector, title: String, desc: String, accent: Color) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DarkCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.45f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accent.copy(alpha = 0.2f))
                    .border(1.dp, VintageBorderGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(text = title, color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = desc, color = TextMuted, fontFamily = LoraFamily, fontSize = 12.sp, lineHeight = 16.sp)
            }
        }
    }
}

@Composable
private fun WhyCard(icon: ImageVector, title: String, desc: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DarkCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.45f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = VintageGold, modifier = Modifier.size(26.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = desc, color = TextSecondary, fontFamily = LoraFamily, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun FaqAccordion(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DarkCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.45f)),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(question, color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = VintageGold
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(answer, color = TextSecondary, fontFamily = LoraFamily, fontSize = 12.sp, lineHeight = 17.sp)
            }
        }
    }
}
