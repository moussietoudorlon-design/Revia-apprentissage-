package com.example.data.repository

import com.example.data.ai.GeminiClient
import com.example.data.local.ReviaDao
import com.example.data.model.AudioLesson
import com.example.data.model.Course
import com.example.data.model.CreditTransaction
import com.example.data.model.Flashcard
import com.example.data.model.PaymentRecord
import com.example.data.model.Quiz
import com.example.data.model.QuizResult
import com.example.data.model.RevisionSummary
import com.example.data.model.TutorMessage
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class ReviaRepository(private val dao: ReviaDao) {

    // Cost in credits
    val COST_SUMMARY = 2
    val COST_QUIZ = 3
    val COST_FLASHCARDS = 2
    val COST_AUDIO = 4
    val COST_TUTOR = 1

    suspend fun ensureProfileInitialized() {
        val existing = dao.getUserProfile()
        if (existing == null) {
            dao.insertOrUpdateProfile(
                UserProfile(
                    id = 1,
                    name = "Alex",
                    email = "alex.etudiant@revia.ai",
                    plan = "FREE",
                    credits = 20,
                    streakDays = 5,
                    studyTimeMinutes = 120,
                    isLoggedIn = true
                )
            )
            dao.insertCreditTransaction(
                CreditTransaction(
                    amount = 20,
                    description = "Crédits de bienvenue REVIA AI",
                    type = "BONUS"
                )
            )
        }
    }

    val userProfileFlow: Flow<UserProfile?> = dao.getUserProfileFlow()

    suspend fun getProfile(): UserProfile {
        ensureProfileInitialized()
        return dao.getUserProfile() ?: UserProfile()
    }

    suspend fun updateProfile(profile: UserProfile) {
        dao.insertOrUpdateProfile(profile)
    }

    suspend fun login(name: String, email: String) {
        val current = getProfile()
        dao.insertOrUpdateProfile(
            current.copy(
                name = name.ifBlank { "Alex" },
                email = email.ifBlank { "alex@revia.ai" },
                isLoggedIn = true
            )
        )
    }

    suspend fun logout() {
        val current = getProfile()
        dao.insertOrUpdateProfile(current.copy(isLoggedIn = false))
    }

    // Credits management
    suspend fun canAfford(creditsNeeded: Int): Boolean {
        val profile = getProfile()
        if (profile.plan == "MAX") return true // MAX plan has unlimited / high priority
        return profile.credits >= creditsNeeded
    }

    suspend fun consumeCredits(amount: Int, type: String, description: String): Boolean {
        val profile = getProfile()
        if (profile.plan == "MAX") {
            dao.insertCreditTransaction(
                CreditTransaction(
                    amount = 0,
                    description = "$description (Inclus dans Plan MAX)",
                    type = type
                )
            )
            return true
        }

        if (profile.credits < amount) {
            return false
        }

        val newCredits = profile.credits - amount
        dao.insertOrUpdateProfile(profile.copy(credits = newCredits))
        dao.insertCreditTransaction(
            CreditTransaction(
                amount = -amount,
                description = description,
                type = type
            )
        )
        return true
    }

    suspend fun purchaseCreditPack(packCredits: Int, price: Double, provider: String): PaymentRecord {
        val profile = getProfile()
        val txId = "REV-" + UUID.randomUUID().toString().take(8).uppercase()
        val payment = PaymentRecord(
            amount = price,
            currency = "EUR",
            status = "COMPLETED",
            provider = provider,
            transactionId = txId,
            planOrPack = "Pack $packCredits crédits"
        )
        dao.insertPayment(payment)
        dao.insertOrUpdateProfile(profile.copy(credits = profile.credits + packCredits))
        dao.insertCreditTransaction(
            CreditTransaction(
                amount = packCredits,
                description = "Achat Pack $packCredits crédits ($price €)",
                type = "PACK_PURCHASE"
            )
        )
        return payment
    }

    suspend fun subscribeToPlan(planName: String, price: Double, provider: String): PaymentRecord {
        val profile = getProfile()
        val txId = "SUB-" + UUID.randomUUID().toString().take(8).uppercase()
        val payment = PaymentRecord(
            amount = price,
            currency = "EUR",
            status = "COMPLETED",
            provider = provider,
            transactionId = txId,
            planOrPack = "Abonnement $planName"
        )
        dao.insertPayment(payment)
        val bonusCredits = if (planName == "PRO") 150 else 500
        dao.insertOrUpdateProfile(
            profile.copy(
                plan = planName,
                credits = profile.credits + bonusCredits,
                subscriptionRenewalDate = "15 Octobre 2026"
            )
        )
        dao.insertCreditTransaction(
            CreditTransaction(
                amount = bonusCredits,
                description = "Activation Plan $planName (Bonus $bonusCredits crédits)",
                type = "BONUS"
            )
        )
        return payment
    }

    val creditTransactionsFlow: Flow<List<CreditTransaction>> = dao.getCreditTransactionsFlow()
    val paymentsFlow: Flow<List<PaymentRecord>> = dao.getAllPaymentsFlow()

    // Courses
    val allCoursesFlow: Flow<List<Course>> = dao.getAllCoursesFlow()

    suspend fun getCourseById(id: Long): Course? = dao.getCourseById(id)

    suspend fun createCourse(
        title: String,
        subject: String,
        level: String,
        description: String,
        rawContent: String
    ): Course {
        val topics = GeminiClient.extractCourseInsights(rawContent).joinToString(" | ")
        val course = Course(
            title = title,
            subject = subject,
            level = level,
            description = description,
            rawContent = rawContent,
            extractedTopics = topics
        )
        val id = dao.insertCourse(course)
        return course.copy(id = id)
    }

    suspend fun deleteCourse(course: Course) {
        dao.deleteSummariesForCourse(course.id)
        dao.deleteQuizzesForCourse(course.id)
        dao.deleteFlashcardsForCourse(course.id)
        dao.deleteAudioForCourse(course.id)
        dao.deleteTutorMessagesForCourse(course.id)
        dao.deleteCourse(course)
    }

    // Revision Summary
    suspend fun getSummaryForCourse(courseId: Long): RevisionSummary? = dao.getSummaryForCourse(courseId)
    val allSummariesFlow: Flow<List<RevisionSummary>> = dao.getAllSummariesFlow()

    suspend fun generateSummary(course: Course): Result<RevisionSummary> {
        if (!canAfford(COST_SUMMARY)) {
            return Result.failure(Exception("Crédits insuffisants"))
        }

        consumeCredits(COST_SUMMARY, "GENERATION_SUMMARY", "Génération fiche de révision : ${course.title}")
        val summaryData = GeminiClient.generateRevisionSummary(course.title, course.rawContent)

        val summary = RevisionSummary(
            courseId = course.id,
            courseTitle = course.title,
            summaryText = summaryData.summaryText,
            keyConcepts = summaryData.keyConcepts,
            definitions = summaryData.definitions,
            keyTakeaways = summaryData.keyTakeaways,
            examples = summaryData.examples,
            commonMistakes = summaryData.commonMistakes,
            miniRecap = summaryData.miniRecap
        )
        val id = dao.insertSummary(summary)
        return Result.success(summary.copy(id = id))
    }

    // Quiz Generation & Execution
    fun getQuizzesForCourse(courseId: Long): Flow<List<Quiz>> = dao.getQuizzesForCourse(courseId)
    val allQuizResultsFlow: Flow<List<QuizResult>> = dao.getAllQuizResultsFlow()

    suspend fun generateQuiz(
        course: Course,
        questionCount: Int = 10,
        difficulty: String = "Moyen",
        quizType: String = "QCM"
    ): Result<Quiz> {
        if (!canAfford(COST_QUIZ)) {
            return Result.failure(Exception("Crédits insuffisants"))
        }

        consumeCredits(COST_QUIZ, "GENERATION_QUIZ", "Génération quiz ($questionCount questions, $difficulty) : ${course.title}")

        val questionsJson = buildQuizQuestionsJson(course.title, course.rawContent, questionCount, difficulty, quizType)
        val quiz = Quiz(
            courseId = course.id,
            courseTitle = course.title,
            difficulty = difficulty,
            questionCount = questionCount,
            quizType = quizType,
            questionsJson = questionsJson
        )
        val id = dao.insertQuiz(quiz)
        return Result.success(quiz.copy(id = id))
    }

    private suspend fun buildQuizQuestionsJson(
        courseTitle: String,
        content: String,
        count: Int,
        difficulty: String,
        type: String
    ): String {
        val prompt = """
            Génère exactement $count questions de quiz sur le cours '$courseTitle' (Niveau/Difficulté: $difficulty, Type: $type).
            Réponds UNIQUEMENT sous forme d'un tableau JSON d'objets avec les champs suivants :
            [
              {
                "question": "Texte de la question ?",
                "options": ["Choix A", "Choix B", "Choix C", "Choix D"],
                "correctIndex": 0,
                "explanation": "Explication pédagogique claire de la bonne réponse."
              }
            ]
            Contenu du cours :
            $content
        """.trimIndent()

        val apiRes = GeminiClient.callGeminiRaw(prompt)
        if (apiRes.isLiveApi && apiRes.text.isNotBlank()) {
            try {
                val clean = apiRes.text.substringAfter("[").substringBeforeLast("]")
                val completeJson = "[$clean]"
                JSONArray(completeJson) // validate
                return completeJson
            } catch (_: Exception) {}
        }

        // Heuristic Quiz Generator fallback
        val array = JSONArray()
        val sampleQuestions = listOf(
            Triple(
                "Quelle est la notion centrale abordée dans le cours « $courseTitle » ?",
                listOf("Le principe fondateur et ses lois", "Une théorie annexe réfutée", "Un détail accessoire", "Aucune de ces réponses"),
                0
            ),
            Triple(
                "Comment s'applique la méthode démontrée dans ce chapitre ?",
                listOf("Par approximations successives", "Par une démarche logique et vérifiable", "Uniquement de façon empirique", "Par mémorisation brute"),
                1
            ),
            Triple(
                "Quelle est la principale définition à retenir concernant $courseTitle ?",
                listOf("Une règle universelle vérifiée", "Une exception historique isolée", "Une hypothèse non confirmée", "Une simple opinion"),
                0
            ),
            Triple(
                "Quel piège fréquent doit-on éviter lors de l'examen ?",
                listOf("Confondre les termes proches", "Rédiger trop clairement", "Donner trop d'exemples précis", "Citer le cours"),
                0
            ),
            Triple(
                "Dans quel cas concret utilise-t-on les notions de ce cours ?",
                listOf("Dans l'analyse et la résolution de problèmes types", "Uniquement dans le supérieur", "Jamais en conditions réelles", "Seulement à l'oral"),
                0
            )
        )

        val total = count.coerceIn(5, 30)
        for (i in 0 until total) {
            val base = sampleQuestions[i % sampleQuestions.size]
            val obj = JSONObject()
            obj.put("question", "${i + 1}. ${base.first}")
            val opts = JSONArray()
            base.second.forEach { opts.put(it) }
            obj.put("options", opts)
            obj.put("correctIndex", base.third)
            obj.put("explanation", "Justification REVIA : Cette réponse s'appuie directement sur les définitions et propriétés établies dans le cours « $courseTitle ».")
            array.put(obj)
        }
        return array.toString()
    }

    suspend fun submitQuizResult(
        quizId: Long,
        courseId: Long,
        courseTitle: String,
        score: Int,
        totalQuestions: Int,
        correctCount: Int,
        incorrectCount: Int,
        answersJson: String
    ): QuizResult {
        val percentage = ((correctCount.toFloat() / totalQuestions.toFloat()) * 100).toInt()
        val recommendation = when {
            percentage >= 80 -> "Excellente maîtrise ! Revois simplement les détails pour viser le 20/20."
            percentage >= 50 -> "Bonne compréhension générale. Révise les définitions et retente un quiz ciblé."
            else -> "Plusieurs notions essentielles demandent à être consolidées. Relis la fiche de synthèse REVIA."
        }

        val result = QuizResult(
            quizId = quizId,
            courseId = courseId,
            courseTitle = courseTitle,
            score = score,
            maxScore = 20,
            totalQuestions = totalQuestions,
            percentage = percentage,
            correctCount = correctCount,
            incorrectCount = incorrectCount,
            reviewRecommendation = recommendation,
            userAnswersJson = answersJson
        )
        val id = dao.insertQuizResult(result)

        // update user streak and mastered concepts
        val profile = getProfile()
        dao.insertOrUpdateProfile(
            profile.copy(
                masteredConceptsCount = profile.masteredConceptsCount + correctCount,
                studyTimeMinutes = profile.studyTimeMinutes + (totalQuestions * 2)
            )
        )

        return result.copy(id = id)
    }

    // Flashcards
    fun getFlashcardsForCourse(courseId: Long): Flow<List<Flashcard>> = dao.getFlashcardsForCourse(courseId)
    val allFlashcardsFlow: Flow<List<Flashcard>> = dao.getAllFlashcardsFlow()

    suspend fun generateFlashcards(course: Course): Result<List<Flashcard>> {
        if (!canAfford(COST_FLASHCARDS)) {
            return Result.failure(Exception("Crédits insuffisants"))
        }

        consumeCredits(COST_FLASHCARDS, "GENERATION_FLASHCARDS", "Génération flashcards : ${course.title}")

        val prompt = """
            Génère 6 flashcards pédagogiques (Question / Réponse) sur le cours '${course.title}'.
            Format strict par carte :
            Q: [Question claire]
            R: [Réponse concise et exacte]
            ---
            Contenu :
            ${course.rawContent}
        """.trimIndent()

        val apiRes = GeminiClient.callGeminiRaw(prompt)
        val cards = mutableListOf<Flashcard>()

        if (apiRes.isLiveApi && apiRes.text.contains("Q:")) {
            val blocks = apiRes.text.split("---")
            for (block in blocks) {
                val q = block.lines().find { it.trim().startsWith("Q:") }?.substringAfter("Q:")?.trim()
                val r = block.lines().find { it.trim().startsWith("R:") }?.substringAfter("R:")?.trim()
                if (!q.isNullOrBlank() && !r.isNullOrBlank()) {
                    cards.add(Flashcard(courseId = course.id, courseTitle = course.title, question = q, answer = r))
                }
            }
        }

        if (cards.isEmpty()) {
            // Heuristic flashcards
            cards.addAll(
                listOf(
                    Flashcard(
                        courseId = course.id,
                        courseTitle = course.title,
                        question = "Quelle est la définition première de « ${course.title} » ?",
                        answer = "C'est l'ensemble des règles, concepts et théorèmes structurant ce domaine d'étude."
                    ),
                    Flashcard(
                        courseId = course.id,
                        courseTitle = course.title,
                        question = "Quel est le principe fondamental à toujours expliciter ?",
                        answer = "Il faut expliciter la loi directrice ainsi que ses conditions d'application."
                    ),
                    Flashcard(
                        courseId = course.id,
                        courseTitle = course.title,
                        question = "Quelle est la distinction clé à ne pas confondre ?",
                        answer = "La distinction entre la théorie générale et les cas particuliers d'application."
                    ),
                    Flashcard(
                        courseId = course.id,
                        courseTitle = course.title,
                        question = "Quelle formule ou méthode permet la résolution type ?",
                        answer = "La méthode en 3 étapes : analyse des données, formulation de la loi, déduction."
                    ),
                    Flashcard(
                        courseId = course.id,
                        courseTitle = course.title,
                        question = "Quel exemple concret illustre le mieux cette notion ?",
                        answer = "L'application directe aux cas pratiques vus en travaux dirigés ou examens."
                    ),
                    Flashcard(
                        courseId = course.id,
                        courseTitle = course.title,
                        question = "Que faire en priorité devant un énoncé sur ce sujet ?",
                        answer = "Identifier les mots-clés, poser les définitions et isoler les variables."
                    )
                )
            )
        }

        dao.insertFlashcards(cards)
        return Result.success(cards)
    }

    suspend fun updateFlashcardStatus(card: Flashcard, isMastered: Boolean) {
        val updated = card.copy(
            isMastered = isMastered,
            reviewCount = card.reviewCount + 1,
            lastReviewedAt = System.currentTimeMillis()
        )
        dao.updateFlashcard(updated)

        // update user metrics
        val profile = getProfile()
        if (isMastered && !card.isMastered) {
            dao.insertOrUpdateProfile(profile.copy(masteredConceptsCount = profile.masteredConceptsCount + 1))
        }
    }

    // Audio Lesson
    suspend fun getAudioForCourse(courseId: Long): AudioLesson? = dao.getAudioForCourse(courseId)
    val allAudioLessonsFlow: Flow<List<AudioLesson>> = dao.getAllAudioLessonsFlow()

    suspend fun generateAudioLesson(course: Course): Result<AudioLesson> {
        if (!canAfford(COST_AUDIO)) {
            return Result.failure(Exception("Crédits insuffisants"))
        }

        consumeCredits(COST_AUDIO, "GENERATION_AUDIO", "Génération leçon audio : ${course.title}")

        val prompt = """
            Transforme ce cours en un script audio pédagogique captivant et oralisé en français pour un élève qui révise.
            Format strict avec balises :
            [INTRODUCTION]
            Bonjour et bienvenue sur REVIA AI. Aujourd'hui nous révisons...
            [EXPLICATION]
            Commençons par le coeur du sujet...
            [EXEMPLES]
            Prenons un exemple parlant...
            [RECAPITULATIF]
            Pour résumer l'essentiel...
            [QUESTIONS_RAPIDES]
            Une question pour tester ta mémoire...
            
            Cours : ${course.title}
            ${course.rawContent}
        """.trimIndent()

        val apiRes = GeminiClient.callGeminiRaw(prompt)
        val text = apiRes.text

        fun extractPart(tag: String, next: List<String>): String {
            val start = text.indexOf(tag)
            if (start == -1) return ""
            val cStart = start + tag.length
            var end = text.length
            for (n in next) {
                val idx = text.indexOf(n, cStart)
                if (idx != -1 && idx < end) end = idx
            }
            return text.substring(cStart, end).trim()
        }

        val intro = extractPart("[INTRODUCTION]", listOf("[EXPLICATION]"))
            .ifBlank { "Bienvenue sur REVIA AI pour votre session audio express sur : ${course.title}." }
        val explication = extractPart("[EXPLICATION]", listOf("[EXEMPLES]"))
            .ifBlank { "Ce cours structure les notions fondamentales. Retenez les définitions clés et le schéma global de compréhension." }
        val exemples = extractPart("[EXEMPLES]", listOf("[RECAPITULATIF]"))
            .ifBlank { "Pour illustrer cela, imaginez une situation où ce principe intervient directement pour résoudre une énigme concrète." }
        val recap = extractPart("[RECAPITULATIF]", listOf("[QUESTIONS_RAPIDES]"))
            .ifBlank { "En récapitulatif : premier point, la définition. Deuxième point, les lois d'application. Troisième point, la méthode." }
        val questions = extractPart("[QUESTIONS_RAPIDES]", emptyList())
            .ifBlank { "Question flash : es-tu capable de formuler la règle principale sans hésiter ?" }

        val fullScript = "$intro\n\n$explication\n\n$exemples\n\n$recap\n\n$questions"
        val duration = (fullScript.split(" ").size / 2.5).toInt().coerceAtLeast(60)

        val audio = AudioLesson(
            courseId = course.id,
            courseTitle = course.title,
            title = "Audio Express : ${course.title}",
            introduction = intro,
            explanation = explication,
            examples = exemples,
            recap = recap,
            quickQuestions = questions,
            fullScript = fullScript,
            durationSeconds = duration
        )
        val id = dao.insertAudioLesson(audio)
        return Result.success(audio.copy(id = id))
    }

    // AI Tutor
    fun getTutorMessagesFlow(courseId: Long): Flow<List<TutorMessage>> = dao.getTutorMessagesFlow(courseId)

    suspend fun askTutor(course: Course, userQuestion: String, modifier: String? = null): Result<TutorMessage> {
        if (!canAfford(COST_TUTOR)) {
            return Result.failure(Exception("Crédits insuffisants"))
        }

        consumeCredits(COST_TUTOR, "TUTOR_QUESTION", "Question tuteur IA : ${course.title}")

        // Save user message
        dao.insertTutorMessage(
            TutorMessage(courseId = course.id, sender = "USER", text = userQuestion)
        )

        val promptInstruction = when (modifier) {
            "SIMPLIFY" -> "Explique avec des mots très simples et une métaphore accessible à un élève qui débute."
            "EXAMPLE" -> "Donne un exemple concret du quotidien pour rendre la notion tangible."
            "QUIZ_ME" -> "Pose une mini-question pertinente à l'élève pour vérifier sa compréhension immédiate."
            "SUMMARY" -> "Résume en 3 phrases choc les éléments clés de réponse."
            else -> "Réponds avec bienveillance, clarté et rigueur pédagogique, prioritairement basé sur le contenu du cours."
        }

        val prompt = """
            Tu es le tuteur pédagogique personnel REVIA AI. Tu aides un élève sur son cours intitulé « ${course.title} ».
            Directive particulière : $promptInstruction
            
            Contenu du cours de l'élève :
            ${course.rawContent}
            
            Question de l'élève :
            $userQuestion
            
            Réponds en français directement à l'élève. Ajoute à la fin une petite phrase stimulante ou une question de vérification.
        """.trimIndent()

        val res = GeminiClient.callGeminiRaw(prompt)
        val replyText = if (res.isLiveApi && res.text.isNotBlank()) {
            res.text
        } else {
            when (modifier) {
                "SIMPLIFY" -> "Pour faire très simple concernant « ${course.title} » : c'est comme un jeu avec des règles bien précises. Si tu comprends la règle de base, tout le reste en découle naturellement !"
                "EXAMPLE" -> "Voici un exemple parlant : pense à un problème réel où tu dois appliquer ce théorème pas à pas. En décomposant les variables, la réponse apparaît clairement."
                "QUIZ_ME" -> "Mini-test pour toi : peux-tu m'expliquer en une phrase la notion clé sans regarder ton cours ? J'attends ta réponse !"
                "SUMMARY" -> "Synthèse express : 1. La définition de base. 2. Son application principale. 3. Le piège classique à éviter le jour de l'épreuve."
                else -> "Sur « ${course.title} », le point essentiel est de bien relier la règle générale aux cas concrets. As-tu une formule précise en tête ou veux-tu qu'on approfondisse un exemple ?"
            }
        }

        val aiMessage = TutorMessage(
            courseId = course.id,
            sender = "AI",
            text = replyText
        )
        dao.insertTutorMessage(aiMessage)
        return Result.success(aiMessage)
    }

    // Seed a starter course if requested
    suspend fun seedSampleCourse(): Course {
        return createCourse(
            title = "Les Lois du Mouvement de Newton",
            subject = "Physique-Chimie",
            level = "Terminale",
            description = "Étude approfondie de la dynamique newtonienne, principe d'inertie, relation fondamentale et action-réaction.",
            rawContent = """
                Chapitre : Les Lois de Newton en Mécanique Classique.
                
                1. Première loi de Newton (Principe d'inertie) :
                Dans un référentiel galiléen, le centre d'inertie G d'un système est persistant dans son état de repos ou de mouvement rectiligne uniforme si et seulement si la somme vectorielle des forces extérieures qui s'exercent sur lui est nulle (somme(F_ext) = 0 => vecteur(v) = constante).
                
                2. Deuxième loi de Newton (Principe fondamental de la dynamique) :
                Dans un référentiel galiléen, la somme des forces extérieures appliquées à un point matériel de masse m constante est égale au produit de sa masse par le vecteur accélération : somme(F_ext) = m * vecteur(a) = d(vecteur(p))/dt.
                Cette loi permet de déterminer la trajectoire par intégration des équations différentielles.
                
                3. Troisième loi de Newton (Principe des actions réciproques) :
                Lorsque deux corps A et B interagissent, la force exercée par A sur B est directement opposée à la force exercée par B sur A : vecteur(F_A/B) = - vecteur(F_B/A).
                Ce principe s'applique que les corps soient en contact ou à distance, immobiles ou en mouvement.
                
                Notions clés et pièges :
                - Toujours préciser le système étudié et le référentiel galiléen avant toute application.
                - Ne pas confondre accélération et vitesse.
                - Ne pas oublier de projeter sur les axes orthonormés (Ox, Oy, Oz).
            """.trimIndent()
        )
    }
}
