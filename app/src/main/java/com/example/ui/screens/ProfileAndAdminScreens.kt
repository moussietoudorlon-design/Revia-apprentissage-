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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun ProfileScreen(
    userProfile: UserProfile?,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit,
    onToggleDarkMode: (Boolean) -> Unit,
    onToggleNotifications: (Boolean) -> Unit,
    onToast: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("✦ Cabinet d'Étude & Registre ✦", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Gère ton profil académique et tes parchemins", color = VintageGold, fontFamily = LoraFamily, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(18.dp))

        // User Avatar & Identity Card (Antique scholar seal)
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = DarkCard,
            border = androidx.compose.foundation.BorderStroke(1.2.dp, VintageBorderGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(VintageCrimson)
                        .border(1.5.dp, VintageBorderGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userProfile?.name?.take(1)?.uppercase() ?: "A",
                        color = VintageParchment,
                        fontFamily = PlayfairFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(userProfile?.name ?: "Alex", color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(userProfile?.email ?: "alex.etudiant@revia.ai", color = TextMuted, fontFamily = LoraFamily, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ordre Académique : ${userProfile?.plan ?: "FREE"}",
                        color = VintageGold,
                        fontFamily = LoraFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Subscription & Credits Section
        Text("Privilèges & Parchemins d'Étude :", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Souscription actuelle", color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text("Échéance : ${userProfile?.subscriptionRenewalDate ?: "15 Octobre 2026"}", color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp)
                    }
                    Button(
                        onClick = { onNavigate(Screen.PRICING) },
                        colors = ButtonDefaults.buttonColors(containerColor = VintageCrimson, contentColor = VintageParchment),
                        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Changer", fontFamily = LoraFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("📜 ${userProfile?.credits ?: 20} parchemins disponibles", color = VintageGold, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Recharge sans engagement", color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp)
                    }
                    Button(
                        onClick = { onNavigate(Screen.CREDITS) },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkCardElevated, contentColor = VintageGold),
                        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Acquérir", fontFamily = LoraFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Preferences Section
        Text("Dispositions & Rituel d'Étude :", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DarkMode, contentDescription = null, tint = VintageGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Nocturne & Velours Sombre", color = VintageParchment, fontFamily = LoraFamily, fontSize = 13.sp)
                    }
                    Switch(
                        checked = userProfile?.isDarkMode ?: true,
                        onCheckedChange = { onToggleDarkMode(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = VintageGold, checkedTrackColor = VintageCrimson)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = VintageGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Rappels quotidiens de méditation", color = VintageParchment, fontFamily = LoraFamily, fontSize = 13.sp)
                    }
                    Switch(
                        checked = userProfile?.notificationsEnabled ?: true,
                        onCheckedChange = { onToggleNotifications(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = VintageGold, checkedTrackColor = VintageCrimson)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Admin Zone Link
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = DarkCardElevated,
            border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .clickable { onNavigate(Screen.ADMIN) }
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = VintageGold, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Registre du Grand Maître (Administration)", color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Supervision de la doctrine, érudits & parchemins", color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Security & Danger Zone
        Text("Sécurité du Sceau :", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onLogout,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, VintageCrimson),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null, tint = VintageCrimson, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Quitter le Cabinet (Déconnexion)", color = VintageCrimson, fontFamily = LoraFamily, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AdminDashboardScreen(
    coursesCount: Int,
    quizzesCount: Int,
    audioCount: Int,
    onBack: () -> Unit,
    onAddCredits: (Int) -> Unit,
    onSetPlan: (String) -> Unit
) {
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
            Text("✦ Registre du Grand Maître ✦", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text("Supervision globale de l'Académie REVIA", color = VintageGold, fontFamily = LoraFamily, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // SaaS Business KPI Grid
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AdminKpiCard(title = "Érudits Inscrits", value = "1 428", icon = Icons.Default.Person, color = VintageGold, modifier = Modifier.weight(1f))
            AdminKpiCard(title = "Actifs ce jour", value = "684", icon = Icons.Default.Bolt, color = Color(0xFF6B8E23), modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AdminKpiCard(title = "Tribut Mensuel", value = "4 280 €", icon = Icons.Default.CreditCard, color = VintageGold, modifier = Modifier.weight(1f))
            AdminKpiCard(title = "Membres de l'Ordre", value = "340", icon = Icons.Default.Star, color = VintageCrimson, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Activity Stats
        Text("Archives & Livres Générés :", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AdminStatRow(label = "Traités consignés dans l'Arche", value = "$coursesCount")
                AdminStatRow(label = "Épreuves et interrogations", value = "$quizzesCount")
                AdminStatRow(label = "Conférences audio gravées", value = "$audioCount")
                AdminStatRow(label = "Parchemins alloués au total", value = "18 940 sceaux")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Admin Management Actions
        Text("Attribution & Dotation :", color = VintageParchment, fontFamily = PlayfairFamily, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Octroyer des parchemins manuellement :", color = TextSecondary, fontFamily = LoraFamily, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { onAddCredits(50) },
                        colors = ButtonDefaults.buttonColors(containerColor = VintageGold, contentColor = DarkBg),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("+50 parchemins", fontFamily = LoraFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { onAddCredits(150) },
                        colors = ButtonDefaults.buttonColors(containerColor = VintageCrimson, contentColor = VintageParchment),
                        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("+150 parchemins", fontFamily = LoraFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("Élever au rang supérieur :", color = TextSecondary, fontFamily = LoraFamily, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { onSetPlan("FREE") }, shape = RoundedCornerShape(8.dp), border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.4f))) {
                        Text("INITIÉ", fontFamily = LoraFamily, fontSize = 11.sp, color = VintageParchment)
                    }
                    Button(onClick = { onSetPlan("PRO") }, colors = ButtonDefaults.buttonColors(containerColor = VintageCrimson, contentColor = VintageParchment), border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold), shape = RoundedCornerShape(8.dp)) {
                        Text("MAGISTER", fontFamily = PlayfairFamily, fontSize = 11.sp)
                    }
                    Button(onClick = { onSetPlan("MAX") }, colors = ButtonDefaults.buttonColors(containerColor = VintageGold, contentColor = DarkBg), shape = RoundedCornerShape(8.dp)) {
                        Text("DOCTOR", fontFamily = PlayfairFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun AdminKpiCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DarkCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.35f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Black, fontSize = 20.sp)
            Text(title, color = TextMuted, fontFamily = LoraFamily, fontSize = 11.sp)
        }
    }
}

@Composable
private fun AdminStatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextSecondary, fontFamily = LoraFamily, fontSize = 12.sp)
        Text(value, color = VintageGold, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}
