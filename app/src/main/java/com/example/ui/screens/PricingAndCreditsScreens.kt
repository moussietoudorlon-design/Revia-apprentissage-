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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CreditTransaction
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PricingScreen(
    currentPlan: String,
    onBack: () -> Unit,
    onSelectPlan: (planName: String, price: Double, provider: String) -> Unit
) {
    val scrollState = rememberScrollState()
    var selectedCheckoutPlan by remember { mutableStateOf<Triple<String, Double, String>?>(null) }

    if (selectedCheckoutPlan != null) {
        CheckoutModal(
            planOrPack = selectedCheckoutPlan!!.first,
            price = selectedCheckoutPlan!!.second,
            onDismiss = { selectedCheckoutPlan = null },
            onConfirm = { provider ->
                val (name, price) = selectedCheckoutPlan!!
                selectedCheckoutPlan = null
                onSelectPlan(name, price, provider)
            }
        )
    }

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
            Text("✦ Ordres & Souscriptions Académiques ✦", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Élève ton savoir avec l'Académie REVIA",
            color = VintageParchment,
            fontFamily = PlayfairFamily,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Choisis ton rang d'étude et débloque les parchemins savants",
            color = VintageGold,
            fontFamily = LoraFamily,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Plan 1: FREE (Initiation)
        PlanCard(
            name = "Rang Initié (Gratuit)",
            price = "0 € / mois",
            period = "Accès libre aux fondations",
            badge = "Pour débuter",
            accentColor = VintageBorderGold,
            features = listOf(
                "1 à 2 traités étudiés simultanément",
                "Fiches de synthèse essentielles",
                "Questionnaires de 5 à 10 interrogations",
                "20 parchemins d'or offerts à l'entrée"
            ),
            ctaText = if (currentPlan == "FREE") "Ton rang actuel" else "Intégrer comme Initié",
            isCurrent = currentPlan == "FREE",
            onCtaClick = {
                if (currentPlan != "FREE") onSelectPlan("FREE", 0.0, "SYSTEM")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Plan 2: PRO (Magister)
        PlanCard(
            name = "Rang Magister (Pro)",
            price = "8,99 € / mois",
            period = "ou 79 € / an (-26%)",
            badge = "7 jours d'initiation offerts ⭐",
            accentColor = VintageCrimson,
            isHighlighted = true,
            features = listOf(
                "Bibliothèque et traités illimités",
                "Fiches académiques complètes & exégèses",
                "Épreuves illimitées (jusqu'à 30 questions)",
                "Conférences et leçons audio oralisées",
                "Export parchemin / PDF relié",
                "150 parchemins d'or chaque lune",
                "Assistance prioritaire du précepteur"
            ),
            ctaText = if (currentPlan == "PRO") "Ton rang actuel" else "Devenir Magister",
            isCurrent = currentPlan == "PRO",
            onCtaClick = {
                if (currentPlan != "PRO") selectedCheckoutPlan = Triple("PRO", 8.99, "STRIPE")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Plan 3: MAX (Doctor)
        PlanCard(
            name = "Rang Doctor (Max)",
            price = "17,99 € / mois",
            period = "Maîtrise Suprême",
            badge = "ORDRE SUPÉRIEUR 👑",
            accentColor = VintageGold,
            features = listOf(
                "Tout le privilège Magister inclus",
                "Oracle IA le plus perspicace et profond",
                "Génération audio instantanée par la voix",
                "Tuteur Précepteur disponible jour et nuit",
                "Démonstrations pas à pas et controverses",
                "500 parchemins d'or chaque mois"
            ),
            ctaText = if (currentPlan == "MAX") "Ton rang actuel" else "Accéder au rang Doctor",
            isCurrent = currentPlan == "MAX",
            onCtaClick = {
                if (currentPlan != "MAX") selectedCheckoutPlan = Triple("MAX", 17.99, "PAYPAL")
            }
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun CreditsScreen(
    userProfile: UserProfile?,
    transactions: List<CreditTransaction>,
    onBack: () -> Unit,
    onBuyPack: (packCredits: Int, price: Double, provider: String) -> Unit
) {
    val scrollState = rememberScrollState()
    var selectedPackToBuy by remember { mutableStateOf<Triple<Int, Double, String>?>(null) }

    if (selectedPackToBuy != null) {
        CheckoutModal(
            planOrPack = "Bourse de ${selectedPackToBuy!!.first} parchemins",
            price = selectedPackToBuy!!.second,
            onDismiss = { selectedPackToBuy = null },
            onConfirm = { provider ->
                val (credits, price) = selectedPackToBuy!!
                selectedPackToBuy = null
                onBuyPack(credits, price, provider)
            }
        )
    }

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
            Text("✦ Trésor & Parchemins d'Or ✦", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Balance Hero Card
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DarkCard,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, VintageBorderGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Solde du Sceau", color = VintageGold, fontFamily = LoraFamily, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = VintageGold, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${userProfile?.credits ?: 20}",
                        color = VintageParchment,
                        fontFamily = PlayfairFamily,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("parchemins", color = VintageGold, fontFamily = LoraFamily, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Dépenses d'étude : Fiche (-2) • Quiz (-3) • Cartes (-2) • Leçon audio (-4) • Précepteur (-1)",
                    color = TextSecondary,
                    fontFamily = LoraFamily,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Acquérir une dotation de parchemins :", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 16.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(10.dp))

        CreditPackItem(
            credits = 50,
            price = 3.99,
            desc = "Suffisant pour réviser un traité complet",
            accent = VintageBorderGold,
            onClick = { selectedPackToBuy = Triple(50, 3.99, "STRIPE") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        CreditPackItem(
            credits = 150,
            price = 8.99,
            desc = "Recommandé pour la semaine des épreuves",
            accent = VintageCrimson,
            badge = "PRISÉ",
            onClick = { selectedPackToBuy = Triple(150, 8.99, "PAYPAL") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        CreditPackItem(
            credits = 500,
            price = 19.99,
            desc = "Sérénité absolue pour tout le semestre d'études",
            accent = VintageGold,
            badge = "PRIVILÈGE",
            onClick = { selectedPackToBuy = Triple(500, 19.99, "STRIPE") }
        )

        Spacer(modifier = Modifier.height(26.dp))

        Text("Registre des transactions passées :", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 16.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(10.dp))

        if (transactions.isEmpty()) {
            Text("Aucune écriture consignée dans le registre.", color = TextMuted, fontFamily = LoraFamily, fontSize = 12.sp)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                transactions.take(8).forEach { tx ->
                    val isPositive = tx.amount >= 0
                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    val dateStr = sdf.format(Date(tx.timestamp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DarkCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(tx.description, color = VintageParchment, fontFamily = LoraFamily, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                Text(dateStr, color = TextMuted, fontFamily = LoraFamily, fontSize = 10.sp)
                            }
                            Text(
                                text = if (isPositive) "+${tx.amount}" else "${tx.amount}",
                                color = if (isPositive) VintageGold else VintageCrimson,
                                fontFamily = PlayfairFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun CheckoutModal(
    planOrPack: String,
    price: Double,
    onDismiss: () -> Unit,
    onConfirm: (provider: String) -> Unit
) {
    var selectedProvider by remember { mutableStateOf("PAYPAL") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = DarkCardElevated,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, VintageBorderGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Text("✦ Sceau de Paiement Sécurisé ✦", color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Dotation : $planOrPack", color = VintageGold, fontFamily = LoraFamily, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text("Tribut : ${String.format(Locale.FRANCE, "%.2f", price)} €", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 20.sp, fontWeight = FontWeight.Black)

                Spacer(modifier = Modifier.height(18.dp))

                Text("Choix du comptoir de règlement :", color = TextSecondary, fontFamily = LoraFamily, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(10.dp))

                // PayPal option
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (selectedProvider == "PAYPAL") VintageCrimson.copy(alpha = 0.25f) else DarkCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedProvider == "PAYPAL") VintageBorderGold else DarkBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { selectedProvider = "PAYPAL" }
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Payment, contentDescription = null, tint = VintageGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("PayPal", color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Règlement instantané ou en 4 fois", color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Stripe option (CB, Apple Pay, Google Pay)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (selectedProvider == "STRIPE") VintageCrimson.copy(alpha = 0.25f) else DarkCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedProvider == "STRIPE") VintageBorderGold else DarkBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { selectedProvider = "STRIPE" }
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CreditCard, contentDescription = null, tint = VintageGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Carte d'Or / Stripe", color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Visa, Mastercard, Apple Pay & Google Pay", color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onConfirm(selectedProvider) },
                    colors = ButtonDefaults.buttonColors(containerColor = VintageCrimson, contentColor = VintageParchment),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, VintageBorderGold),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_payment_btn")
                ) {
                    Text("Apposer le Sceau (${String.format(Locale.FRANCE, "%.2f", price)} €)", fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Renoncer", color = TextMuted, fontFamily = LoraFamily, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun PlanCard(
    name: String,
    price: String,
    period: String,
    badge: String,
    accentColor: Color,
    features: List<String>,
    ctaText: String,
    isCurrent: Boolean,
    isHighlighted: Boolean = false,
    onCtaClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = DarkCard,
        border = androidx.compose.foundation.BorderStroke(if (isHighlighted) 2.dp else 1.dp, if (isHighlighted) VintageBorderGold else VintageBorderGold.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(name, color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Black, fontSize = 18.sp)
                Text(
                    text = badge,
                    color = VintageGold,
                    fontFamily = LoraFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(VintageGold.copy(alpha = 0.15f))
                        .border(1.dp, VintageBorderGold.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(price, color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 24.sp, fontWeight = FontWeight.Black)
            Text(period, color = VintageGold.copy(alpha = 0.7f), fontFamily = LoraFamily, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(16.dp))

            features.forEach { feat ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 3.dp)) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = VintageGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(feat, color = TextSecondary, fontFamily = LoraFamily, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onCtaClick,
                colors = ButtonDefaults.buttonColors(containerColor = if (isCurrent) DarkCardElevated else if (isHighlighted) VintageCrimson else DarkCardElevated, contentColor = if (isCurrent) TextMuted else VintageParchment),
                border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold),
                shape = RoundedCornerShape(12.dp),
                enabled = !isCurrent,
                modifier = Modifier.fillMaxWidth().height(46.dp)
            ) {
                Text(ctaText, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun CreditPackItem(
    credits: Int,
    price: Double,
    desc: String,
    accent: Color,
    badge: String? = null,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DarkCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.45f)),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("$credits parchemins 📜", color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    if (badge != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            badge,
                            color = VintageGold,
                            fontFamily = LoraFamily,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(VintageGold.copy(alpha = 0.15f))
                                .border(1.dp, VintageBorderGold.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(desc, color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp)
            }

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = if (badge != null) VintageCrimson else DarkCardElevated, contentColor = VintageParchment),
                border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("${String.format(Locale.FRANCE, "%.2f", price)} €", fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}
