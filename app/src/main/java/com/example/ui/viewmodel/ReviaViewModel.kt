package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ReviaDatabase
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
import com.example.data.repository.ReviaRepository
import com.example.service.AudioTtsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray

enum class Screen {
    LANDING,
    LOGIN,
    REGISTER,
    FORGOT_PASSWORD,
    DASHBOARD,
    COURSES,
    TUTOR,
    PROGRESSION,
    PROFILE,
    ADD_COURSE,
    COURSE_DETAIL,
    SUMMARY_VIEW,
    QUIZ_SETUP,
    QUIZ_RUNNER,
    FLASHCARDS_RUNNER,
    AUDIO_PLAYER,
    PRICING,
    CREDITS,
    ADMIN
}

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

class ReviaViewModel(application: Application) : AndroidViewModel(application) {

    private val db = ReviaDatabase.getInstance(application)
    val repository = ReviaRepository(db.reviaDao())
    val audioTtsManager = AudioTtsManager(application)

    // Current Navigation Screen
    private val _currentScreen = MutableStateFlow(Screen.DASHBOARD)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Active course context
    private val _selectedCourse = MutableStateFlow<Course?>(null)
    val selectedCourse: StateFlow<Course?> = _selectedCourse.asStateFlow()

    // Active summary
    private val _activeSummary = MutableStateFlow<RevisionSummary?>(null)
    val activeSummary: StateFlow<RevisionSummary?> = _activeSummary.asStateFlow()

    // Active quiz
    private val _activeQuiz = MutableStateFlow<Quiz?>(null)
    val activeQuiz: StateFlow<Quiz?> = _activeQuiz.asStateFlow()
    private val _parsedQuestions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val parsedQuestions: StateFlow<List<QuizQuestion>> = _parsedQuestions.asStateFlow()
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()
    private val _userSelectedAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val userSelectedAnswers: StateFlow<Map<Int, Int>> = _userSelectedAnswers.asStateFlow()
    private val _quizCompletedResult = MutableStateFlow<QuizResult?>(null)
    val quizCompletedResult: StateFlow<QuizResult?> = _quizCompletedResult.asStateFlow()

    // Active flashcards
    private val _activeFlashcards = MutableStateFlow<List<Flashcard>>(emptyList())
    val activeFlashcards: StateFlow<List<Flashcard>> = _activeFlashcards.asStateFlow()
    private val _flashcardIndex = MutableStateFlow(0)
    val flashcardIndex: StateFlow<Int> = _flashcardIndex.asStateFlow()
    private val _isCardFlipped = MutableStateFlow(false)
    val isCardFlipped: StateFlow<Boolean> = _isCardFlipped.asStateFlow()

    // Active Audio Lesson
    private val _activeAudioLesson = MutableStateFlow<AudioLesson?>(null)
    val activeAudioLesson: StateFlow<AudioLesson?> = _activeAudioLesson.asStateFlow()

    // Loading & Generation state
    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()
    private val _generationMessage = MutableStateFlow("")
    val generationMessage: StateFlow<String> = _generationMessage.asStateFlow()

    // Paywall Dialog
    private val _paywallVisible = MutableStateFlow(false)
    val paywallVisible: StateFlow<Boolean> = _paywallVisible.asStateFlow()
    private val _paywallFeature = MutableStateFlow("cette fonctionnalité")
    val paywallFeature: StateFlow<String> = _paywallFeature.asStateFlow()

    // Toast / Feedback message
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Database Flows
    val userProfile: StateFlow<UserProfile?> = repository.userProfileFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    val courses: StateFlow<List<Course>> = repository.allCoursesFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val summaries: StateFlow<List<RevisionSummary>> = repository.allSummariesFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val quizResults: StateFlow<List<QuizResult>> = repository.allQuizResultsFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val allFlashcards: StateFlow<List<Flashcard>> = repository.allFlashcardsFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val audioLessons: StateFlow<List<AudioLesson>> = repository.allAudioLessonsFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val creditTransactions: StateFlow<List<CreditTransaction>> = repository.creditTransactionsFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val payments: StateFlow<List<PaymentRecord>> = repository.paymentsFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _tutorMessages = MutableStateFlow<List<TutorMessage>>(emptyList())
    val tutorMessages: StateFlow<List<TutorMessage>> = _tutorMessages.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureProfileInitialized()
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun selectCourse(course: Course) {
        _selectedCourse.value = course
        loadTutorMessages(course.id)
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun showPaywall(feature: String) {
        _paywallFeature.value = feature
        _paywallVisible.value = true
    }

    fun dismissPaywall() {
        _paywallVisible.value = false
    }

    // Auth actions
    fun login(email: String, name: String = "Alex") {
        viewModelScope.launch {
            repository.login(name, email)
            _currentScreen.value = Screen.DASHBOARD
            showToast("Connexion réussie. Bienvenue, $name !")
        }
    }

    fun register(name: String, email: String) {
        viewModelScope.launch {
            repository.login(name, email)
            _currentScreen.value = Screen.DASHBOARD
            showToast("Compte créé avec succès ! 20 crédits offerts ⚡")
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _currentScreen.value = Screen.LANDING
            showToast("Vous êtes déconnecté.")
        }
    }

    // Course actions
    fun addCourse(title: String, subject: String, level: String, description: String, content: String) {
        if (title.isBlank() || content.isBlank()) {
            showToast("Veuillez renseigner un titre et un contenu de cours.")
            return
        }

        viewModelScope.launch {
            _isGenerating.value = true
            _generationMessage.value = "Analyse et structuration du cours par l'IA..."
            try {
                val newCourse = repository.createCourse(title, subject, level, description, content)
                _selectedCourse.value = newCourse
                _currentScreen.value = Screen.COURSE_DETAIL
                showToast("Cours « $title » enregistré avec succès !")
            } catch (e: Exception) {
                showToast("Erreur lors de l'enregistrement : ${e.localizedMessage}")
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            try {
                repository.deleteCourse(course)
                if (_selectedCourse.value?.id == course.id) {
                    _selectedCourse.value = null
                }
                showToast("Cours « ${course.title} » supprimé.")
            } catch (e: Exception) {
                showToast("Erreur lors de la suppression : ${e.localizedMessage}")
            }
        }
    }

    fun seedStarterCourse() {
        viewModelScope.launch {
            _isGenerating.value = true
            _generationMessage.value = "Chargement d'un cours d'exemple complet..."
            try {
                val course = repository.seedSampleCourse()
                _selectedCourse.value = course
                _currentScreen.value = Screen.COURSE_DETAIL
                showToast("Cours d'exemple « ${course.title} » ajouté !")
            } catch (e: Exception) {
                showToast("Erreur : ${e.localizedMessage}")
            } finally {
                _isGenerating.value = false
            }
        }
    }

    // Summary actions
    fun generateSummaryForCourse(course: Course) {
        viewModelScope.launch {
            if (_isGenerating.value) return@launch
            if (!repository.canAfford(repository.COST_SUMMARY)) {
                showPaywall("la génération de fiches IA")
                return@launch
            }

            _isGenerating.value = true
            _generationMessage.value = "Génération de la fiche de révision structurée..."
            try {
                val result = repository.generateSummary(course)
                result.onSuccess { summary ->
                    _activeSummary.value = summary
                    _currentScreen.value = Screen.SUMMARY_VIEW
                    showToast("Fiche générée avec succès (-${repository.COST_SUMMARY} crédits)")
                }.onFailure { err ->
                    showToast(err.message ?: "Échec de génération")
                }
            } catch (e: Exception) {
                showToast("Erreur inattendue : ${e.localizedMessage}")
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun openSummary(summary: RevisionSummary) {
        _activeSummary.value = summary
        _currentScreen.value = Screen.SUMMARY_VIEW
    }

    // Quiz actions
    fun startQuizGeneration(course: Course, count: Int, difficulty: String, type: String) {
        viewModelScope.launch {
            if (_isGenerating.value) return@launch
            if (!repository.canAfford(repository.COST_QUIZ)) {
                showPaywall("la génération de quiz personnalisés")
                return@launch
            }

            _isGenerating.value = true
            _generationMessage.value = "Conception du quiz ($count questions, $difficulty)..."
            try {
                val res = repository.generateQuiz(course, count, difficulty, type)
                res.onSuccess { quiz ->
                    _activeQuiz.value = quiz
                    val questions = parseQuestions(quiz.questionsJson)
                    _parsedQuestions.value = questions
                    _currentQuestionIndex.value = 0
                    _userSelectedAnswers.value = emptyMap()
                    _quizCompletedResult.value = null
                    _currentScreen.value = Screen.QUIZ_RUNNER
                    showToast("Quiz généré avec succès (-${repository.COST_QUIZ} crédits)")
                }.onFailure { err ->
                    showToast(err.message ?: "Échec de génération de quiz")
                }
            } catch (e: Exception) {
                showToast("Erreur inattendue : ${e.localizedMessage}")
            } finally {
                _isGenerating.value = false
            }
        }
    }

    private fun parseQuestions(json: String): List<QuizQuestion> {
        val list = mutableListOf<QuizQuestion>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val qText = obj.getString("question")
                val optsArray = obj.getJSONArray("options")
                val opts = mutableListOf<String>()
                for (j in 0 until optsArray.length()) {
                    opts.add(optsArray.getString(j))
                }
                val cIdx = obj.getInt("correctIndex")
                val exp = obj.optString("explanation", "Bonne réponse conforme au cours.")
                list.add(QuizQuestion(qText, opts, cIdx, exp))
            }
        } catch (_: Exception) {}
        return list
    }

    fun selectQuizAnswer(questionIndex: Int, optionIndex: Int) {
        val current = _userSelectedAnswers.value.toMutableMap()
        current[questionIndex] = optionIndex
        _userSelectedAnswers.value = current
    }

    fun nextQuizQuestion() {
        val next = _currentQuestionIndex.value + 1
        if (next < _parsedQuestions.value.size) {
            _currentQuestionIndex.value = next
        } else {
            finishQuiz()
        }
    }

    fun previousQuizQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value = _currentQuestionIndex.value - 1
        }
    }

    private fun finishQuiz() {
        val quiz = _activeQuiz.value ?: return
        val questions = _parsedQuestions.value
        val answers = _userSelectedAnswers.value
        var correct = 0
        var incorrect = 0

        questions.forEachIndexed { index, q ->
            val userChoice = answers[index]
            if (userChoice == q.correctIndex) {
                correct++
            } else {
                incorrect++
            }
        }

        val score20 = if (questions.isNotEmpty()) ((correct.toFloat() / questions.size.toFloat()) * 20).toInt() else 0

        viewModelScope.launch {
            val result = repository.submitQuizResult(
                quizId = quiz.id,
                courseId = quiz.courseId,
                courseTitle = quiz.courseTitle,
                score = score20,
                totalQuestions = questions.size,
                correctCount = correct,
                incorrectCount = incorrect,
                answersJson = answers.toString()
            )
            _quizCompletedResult.value = result
            showToast("Quiz terminé ! Note : $score20 / 20")
        }
    }

    // Flashcard actions
    fun generateFlashcardsForCourse(course: Course) {
        viewModelScope.launch {
            if (_isGenerating.value) return@launch
            if (!repository.canAfford(repository.COST_FLASHCARDS)) {
                showPaywall("la création de flashcards intelligentes")
                return@launch
            }

            _isGenerating.value = true
            _generationMessage.value = "Création du deck de flashcards mémorielles..."
            try {
                val res = repository.generateFlashcards(course)
                res.onSuccess { cards ->
                    _activeFlashcards.value = cards
                    _flashcardIndex.value = 0
                    _isCardFlipped.value = false
                    _currentScreen.value = Screen.FLASHCARDS_RUNNER
                    showToast("${cards.size} flashcards générées (-${repository.COST_FLASHCARDS} crédits)")
                }.onFailure { err ->
                    showToast(err.message ?: "Échec de création de flashcards")
                }
            } catch (e: Exception) {
                showToast("Erreur inattendue : ${e.localizedMessage}")
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun openCourseFlashcards(courseId: Long) {
        viewModelScope.launch {
            repository.getFlashcardsForCourse(courseId).collect { cards ->
                if (cards.isNotEmpty()) {
                    _activeFlashcards.value = cards
                    _flashcardIndex.value = 0
                    _isCardFlipped.value = false
                    _currentScreen.value = Screen.FLASHCARDS_RUNNER
                }
            }
        }
    }

    fun flipFlashcard() {
        _isCardFlipped.value = !_isCardFlipped.value
    }

    fun markFlashcard(isMastered: Boolean) {
        val list = _activeFlashcards.value
        val idx = _flashcardIndex.value
        if (idx in list.indices) {
            val card = list[idx]
            viewModelScope.launch {
                repository.updateFlashcardStatus(card, isMastered)
            }
        }
        _isCardFlipped.value = false
        if (idx < list.size - 1) {
            _flashcardIndex.value = idx + 1
        } else {
            showToast("Session de flashcards terminée ! Bravo pour cette révision active.")
            _currentScreen.value = Screen.COURSES
        }
    }

    // Audio Lesson actions
    fun generateAudioForCourse(course: Course) {
        viewModelScope.launch {
            if (_isGenerating.value) return@launch
            if (!repository.canAfford(repository.COST_AUDIO)) {
                showPaywall("la génération de leçons audio IA")
                return@launch
            }

            _isGenerating.value = true
            _generationMessage.value = "Écriture du script audio et synthèse vocale..."
            try {
                val res = repository.generateAudioLesson(course)
                res.onSuccess { audio ->
                    _activeAudioLesson.value = audio
                    audioTtsManager.prepareText(audio.fullScript)
                    _currentScreen.value = Screen.AUDIO_PLAYER
                    showToast("Audio prêt (-${repository.COST_AUDIO} crédits)")
                }.onFailure { err ->
                    showToast(err.message ?: "Échec de génération audio")
                }
            } catch (e: Exception) {
                showToast("Erreur inattendue : ${e.localizedMessage}")
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun openAudioLesson(audio: AudioLesson) {
        _activeAudioLesson.value = audio
        audioTtsManager.prepareText(audio.fullScript)
        _currentScreen.value = Screen.AUDIO_PLAYER
    }

    // Tutor actions
    fun loadTutorMessages(courseId: Long) {
        viewModelScope.launch {
            repository.getTutorMessagesFlow(courseId).collect {
                _tutorMessages.value = it
            }
        }
    }

    fun askTutor(question: String, modifier: String? = null) {
        val course = _selectedCourse.value
        if (course == null) {
            showToast("Veuillez sélectionner un cours pour le tuteur.")
            return
        }

        viewModelScope.launch {
            if (_isGenerating.value) return@launch
            if (!repository.canAfford(repository.COST_TUTOR)) {
                showPaywall("le tuteur pédagogique IA")
                return@launch
            }

            _isGenerating.value = true
            _generationMessage.value = "Le tuteur REVIA formule sa réponse..."
            try {
                val res = repository.askTutor(course, question, modifier)
                res.onFailure { err ->
                    showToast(err.message ?: "Échec de la réponse du tuteur")
                }
            } catch (e: Exception) {
                showToast("Erreur inattendue : ${e.localizedMessage}")
            } finally {
                _isGenerating.value = false
            }
        }
    }

    // Purchase & Subscriptions
    fun buyCreditsPack(packSize: Int, price: Double, provider: String = "PAYPAL") {
        viewModelScope.launch {
            val record = repository.purchaseCreditPack(packSize, price, provider)
            showToast("Achat réussi ! $packSize crédits ajoutés. Réf : ${record.transactionId}")
            _currentScreen.value = Screen.CREDITS
        }
    }

    fun subscribePlan(plan: String, price: Double, provider: String = "PAYPAL") {
        viewModelScope.launch {
            val record = repository.subscribeToPlan(plan, price, provider)
            showToast("Félicitations ! Votre abonnement $plan est actif. Réf : ${record.transactionId}")
            _paywallVisible.value = false
            _currentScreen.value = Screen.DASHBOARD
        }
    }

    // Profile Settings toggles
    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            val p = repository.getProfile()
            repository.updateProfile(p.copy(isDarkMode = enabled))
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            val p = repository.getProfile()
            repository.updateProfile(p.copy(notificationsEnabled = enabled))
            showToast(if (enabled) "Rappels quotidiens activés" else "Notifications désactivées")
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            val p = repository.getProfile()
            repository.updateProfile(p.copy(language = lang))
            showToast("Langue mise à jour : $lang")
        }
    }

    // Admin updates
    fun adminAddCredits(amount: Int) {
        viewModelScope.launch {
            val p = repository.getProfile()
            repository.updateProfile(p.copy(credits = p.credits + amount))
            repository.daoCreditBonus(amount)
            showToast("Admin : $amount crédits ajoutés.")
        }
    }

    fun adminSetPlan(plan: String) {
        viewModelScope.launch {
            val p = repository.getProfile()
            repository.updateProfile(p.copy(plan = plan))
            showToast("Admin : Plan changé en $plan.")
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioTtsManager.release()
    }
}

private suspend fun ReviaRepository.daoCreditBonus(amount: Int) {
    this.consumeCredits(-amount, "ADMIN_CREDIT", "Ajustement administrateur")
}
