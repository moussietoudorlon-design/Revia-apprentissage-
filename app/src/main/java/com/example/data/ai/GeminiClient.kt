package com.example.data.ai

import android.util.Log
import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

data class GenerationResult(
    val text: String,
    val isLiveApi: Boolean,
    val apiStatusMessage: String
)

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    private val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    fun isApiKeyConfigured(): Boolean {
        val key = BuildConfig.GEMINI_API_KEY
        return key.isNotBlank() && key != "MY_GEMINI_API_KEY"
    }

    suspend fun callGeminiRaw(prompt: String): GenerationResult = withContext(Dispatchers.IO) {
        val key = BuildConfig.GEMINI_API_KEY
        if (!isApiKeyConfigured()) {
            return@withContext GenerationResult(
                text = "",
                isLiveApi = false,
                apiStatusMessage = "Clé API Gemini non configurée (mode heuristique local activé)"
            )
        }

        try {
            val req = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(GeminiPart(text = prompt))
                    )
                ),
                generationConfig = GeminiGenConfig(temperature = 0.7f)
            )
            val res = service.generateContent(key, req)
            val candidateText = res.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (!candidateText.isNullOrBlank()) {
                GenerationResult(candidateText, isLiveApi = true, apiStatusMessage = "Généré via Gemini 3.5 Flash")
            } else {
                val errorMsg = res.error?.message ?: "Réponse vide de l'API"
                GenerationResult("", isLiveApi = false, apiStatusMessage = "Erreur API : $errorMsg")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Gemini API call failed", e)
            GenerationResult("", isLiveApi = false, apiStatusMessage = "Erreur réseau : ${e.localizedMessage}")
        }
    }

    // High-Level Helper: Course Summary
    suspend fun generateRevisionSummary(courseTitle: String, content: String): SummaryData = withContext(Dispatchers.Default) {
        val prompt = """
            Tu es le moteur pédagogique REVIA AI. Analyse ce cours intitulé "$courseTitle" et génère une fiche de révision complète en français avec la structure exacte suivante :
            ---RESUME---
            (Résumé clair et percutant en 2-3 paragraphes)
            ---NOTIONS---
            - Notion 1 : description
            - Notion 2 : description
            ---DEFINITIONS---
            - Terme 1 : définition précise
            - Terme 2 : définition précise
            ---POINTS_A_RETENIR---
            - Point clé 1
            - Point clé 2
            ---EXEMPLES---
            - Exemple 1
            ---ERREURS_FREQUENTES---
            - Erreur 1 à éviter
            ---MINI_RECAP---
            (Mini récapitulatif en 2 phrases pour mémoriser rapidement)

            Voici le cours :
            $content
        """.trimIndent()

        val apiResult = callGeminiRaw(prompt)
        if (apiResult.isLiveApi && apiResult.text.isNotBlank()) {
            parseSummarySections(apiResult.text)
        } else {
            // Heuristic pedagogical fallback
            buildFallbackSummary(courseTitle, content)
        }
    }

    private fun parseSummarySections(text: String): SummaryData {
        fun extractSection(tag: String, nextTags: List<String>): String {
            val startIndex = text.indexOf(tag)
            if (startIndex == -1) return ""
            val contentStart = startIndex + tag.length
            var endIndex = text.length
            for (next in nextTags) {
                val idx = text.indexOf(next, contentStart)
                if (idx != -1 && idx < endIndex) {
                    endIndex = idx
                }
            }
            return text.substring(contentStart, endIndex).trim()
        }

        val resume = extractSection("---RESUME---", listOf("---NOTIONS---"))
        val notions = extractSection("---NOTIONS---", listOf("---DEFINITIONS---"))
        val definitions = extractSection("---DEFINITIONS---", listOf("---POINTS_A_RETENIR---"))
        val points = extractSection("---POINTS_A_RETENIR---", listOf("---EXEMPLES---"))
        val exemples = extractSection("---EXEMPLES---", listOf("---ERREURS_FREQUENTES---"))
        val erreurs = extractSection("---ERREURS_FREQUENTES---", listOf("---MINI_RECAP---"))
        val recap = extractSection("---MINI_RECAP---", emptyList())

        return SummaryData(
            summaryText = if (resume.isNotBlank()) resume else text.take(300),
            keyConcepts = notions.ifBlank { "• Structure fondamentale\n• Concepts directeurs\n• Méthodologie" },
            definitions = definitions.ifBlank { "• Notions clés associées au sujet" },
            keyTakeaways = points.ifBlank { "• Retenir les mécanismes clés\n• Savoir mobiliser les définitions" },
            examples = exemples.ifBlank { "• Cas d'application concrète" },
            commonMistakes = erreurs.ifBlank { "• Confusion des notions similaires\n• Oubli des étapes démonstratives" },
            miniRecap = recap.ifBlank { "Fiche synthétique prête pour la mémorisation active." }
        )
    }

    private fun buildFallbackSummary(courseTitle: String, content: String): SummaryData {
        val lines = content.lines().filter { it.isNotBlank() }
        val preview = lines.take(4).joinToString(" ")
        return SummaryData(
            summaryText = "Synthèse REVIA de « $courseTitle » : Ce cours aborde les fondements essentiels et les articulations majeures du programme. $preview",
            keyConcepts = "• Concept fondamental de $courseTitle\n• Propriétés et principes directeurs\n• Applications directes et théoriques",
            definitions = "• Terme clé 1 : Éléments constitutifs et règles appliquées\n• Vocabulaire technique propre à la matière",
            keyTakeaways = "• Mémoriser la chronologie / l'enchaînement logique\n• Être capable d'illustrer chaque point d'un exemple précis",
            examples = "• Exemple type d'examen : mise en situation réelle et résolution pas à pas",
            commonMistakes = "• Piège fréquent : ne pas justifier avec les notions du cours",
            miniRecap = "En résumé : maîtriser la définition centrale et ses 3 déclinaisons opérationnelles."
        )
    }

    // High-Level Helper: Course Extraction
    suspend fun extractCourseInsights(content: String): List<String> {
        val prompt = "Extrais de ce cours 5 notions ou formules essentielles sous forme de liste à puces (une par ligne) : \n$content"
        val res = callGeminiRaw(prompt)
        return if (res.isLiveApi && res.text.isNotBlank()) {
            res.text.lines().map { it.replace(Regex("^[-*•0-9.]+\\s*"), "").trim() }.filter { it.isNotBlank() }
        } else {
            listOf("Notion clé principale", "Définitions fondamentales", "Formules & Démonstrations", "Applications concrètes", "Méthodes de révision")
        }
    }
}

data class SummaryData(
    val summaryText: String,
    val keyConcepts: String,
    val definitions: String,
    val keyTakeaways: String,
    val examples: String,
    val commonMistakes: String,
    val miniRecap: String
)
