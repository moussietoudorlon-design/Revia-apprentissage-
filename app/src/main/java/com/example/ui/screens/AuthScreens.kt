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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.viewmodel.Screen

@Composable
fun AuthScreen(
    currentAuthMode: Screen, // LOGIN, REGISTER, FORGOT_PASSWORD
    onLogin: (String, String) -> Unit,
    onRegister: (String, String) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Seal
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(VintageCrimson)
                .border(2.dp, VintageBorderGold, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("R", color = VintageParchment, fontFamily = PlayfairFamily, fontWeight = FontWeight.Black, fontSize = 34.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "ACADÉMIE REVIA",
            color = VintageParchment,
            fontFamily = PlayfairFamily,
            fontWeight = FontWeight.Black,
            fontSize = 24.sp,
            letterSpacing = 1.sp
        )

        Text(
            text = when (currentAuthMode) {
                Screen.LOGIN -> "Ravis de te revoir ! Accède à tes traités d'étude."
                Screen.REGISTER -> "Inscris ton nom au registre et reçois 20 parchemins d'or."
                Screen.FORGOT_PASSWORD -> "Restauration du sceau d'accès"
                else -> ""
            },
            color = VintageGold,
            fontFamily = LoraFamily,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        // Auth Form Card
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DarkCard,
            border = androidx.compose.foundation.BorderStroke(1.2.dp, VintageBorderGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Name field (Register only)
                if (currentAuthMode == Screen.REGISTER) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nom ou Titre d'Érudit", fontFamily = LoraFamily) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = VintageGold) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VintageBorderGold,
                            unfocusedBorderColor = VintageBorderGold.copy(alpha = 0.5f),
                            focusedTextColor = VintageParchment,
                            unfocusedTextColor = VintageParchment,
                            focusedLabelColor = VintageGold,
                            unfocusedLabelColor = TextMuted
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_name_input")
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Courrier Académique (Email)", fontFamily = LoraFamily) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = VintageGold) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VintageBorderGold,
                        unfocusedBorderColor = VintageBorderGold.copy(alpha = 0.5f),
                        focusedTextColor = VintageParchment,
                        unfocusedTextColor = VintageParchment,
                        focusedLabelColor = VintageGold,
                        unfocusedLabelColor = TextMuted
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_email_input")
                )

                if (currentAuthMode != Screen.FORGOT_PASSWORD) {
                    Spacer(modifier = Modifier.height(14.dp))

                    // Password field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Sceau Secret (Mot de passe)", fontFamily = LoraFamily) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = VintageGold) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = VintageGold
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VintageBorderGold,
                            unfocusedBorderColor = VintageBorderGold.copy(alpha = 0.5f),
                            focusedTextColor = VintageParchment,
                            unfocusedTextColor = VintageParchment,
                            focusedLabelColor = VintageGold,
                            unfocusedLabelColor = TextMuted
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_password_input")
                    )
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage!!, color = Color(0xFFEF4444), fontFamily = LoraFamily, fontSize = 12.sp)
                }

                if (currentAuthMode == Screen.LOGIN) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { onNavigate(Screen.FORGOT_PASSWORD) }) {
                            Text("Sceau secret oublié ?", color = VintageGold, fontFamily = LoraFamily, fontSize = 12.sp)
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(18.dp))
                }

                // Main Submit Button
                Button(
                    onClick = {
                        if (email.isBlank()) {
                            errorMessage = "Veuillez entrer une adresse email valide."
                            return@Button
                        }
                        if (currentAuthMode != Screen.FORGOT_PASSWORD && password.length < 4) {
                            errorMessage = "Le mot de passe doit comporter au moins 4 caractères."
                            return@Button
                        }
                        errorMessage = null
                        when (currentAuthMode) {
                            Screen.LOGIN -> onLogin(email, "Alex")
                            Screen.REGISTER -> onRegister(name.ifBlank { "Alex" }, email)
                            Screen.FORGOT_PASSWORD -> {
                                errorMessage = "Lien de réinitialisation envoyé à $email."
                            }
                            else -> {}
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VintageCrimson, contentColor = VintageParchment),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, VintageBorderGold),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("auth_submit_btn")
                ) {
                    Text(
                        text = when (currentAuthMode) {
                            Screen.LOGIN -> "Entrer dans l'Académie"
                            Screen.REGISTER -> "Inscrire mon nom au Registre"
                            Screen.FORGOT_PASSWORD -> "Envoyer le parchemin de secours"
                            else -> "Valider"
                        },
                        fontFamily = PlayfairFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Google Sign In Option
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarkCardElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, VintageBorderGold.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            onLogin("google.user@revia.ai", "Alex")
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Continuer avec Google",
                            color = VintageParchment,
                            fontFamily = LoraFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Switch Mode Links
        Row(verticalAlignment = Alignment.CenterVertically) {
            when (currentAuthMode) {
                Screen.LOGIN -> {
                    Text("Pas encore inscrit au Registre ?", color = TextMuted, fontFamily = LoraFamily, fontSize = 13.sp)
                    TextButton(onClick = { onNavigate(Screen.REGISTER) }) {
                        Text("S'inscrire", color = VintageGold, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
                Screen.REGISTER -> {
                    Text("Déjà initié ?", color = TextMuted, fontFamily = LoraFamily, fontSize = 13.sp)
                    TextButton(onClick = { onNavigate(Screen.LOGIN) }) {
                        Text("Se connecter", color = VintageGold, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
                Screen.FORGOT_PASSWORD -> {
                    TextButton(onClick = { onNavigate(Screen.LOGIN) }) {
                        Text("Retour à la porte", color = VintageGold, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
                else -> {}
            }
        }
    }
}
