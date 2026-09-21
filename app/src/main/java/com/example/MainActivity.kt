package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.GenerationLoadingDialog
import com.example.ui.components.IntelligentPaywallDialog
import com.example.ui.components.QuickActionsModal
import com.example.ui.components.ReviaBottomNav
import com.example.ui.components.ReviaTopBar
import com.example.ui.screens.AddCourseScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AudioPlayerScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CourseDetailScreen
import com.example.ui.screens.CourseListScreen
import com.example.ui.screens.CreditsScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FlashcardsRunnerScreen
import com.example.ui.screens.LandingScreen
import com.example.ui.screens.PricingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProgressionScreen
import com.example.ui.screens.QuizRunnerScreen
import com.example.ui.screens.QuizSetupScreen
import com.example.ui.screens.SummaryScreen
import com.example.ui.screens.TutorScreen
import com.example.ui.theme.DarkBg
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ReviaViewModel
import com.example.ui.viewmodel.Screen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ReviaApp()
            }
        }
    }
}

@Composable
fun ReviaApp(viewModel: ReviaViewModel = viewModel()) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val courses by viewModel.courses.collectAsState()
    val summaries by viewModel.summaries.collectAsState()
    val quizResults by viewModel.quizResults.collectAsState()
    val audioLessons by viewModel.audioLessons.collectAsState()
    val creditTransactions by viewModel.creditTransactions.collectAsState()
    val tutorMessages by viewModel.tutorMessages.collectAsState()

    val selectedCourse by viewModel.selectedCourse.collectAsState()
    val activeSummary by viewModel.activeSummary.collectAsState()
    val activeQuiz by viewModel.activeQuiz.collectAsState()
    val parsedQuestions by viewModel.parsedQuestions.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
    val userSelectedAnswers by viewModel.userSelectedAnswers.collectAsState()
    val quizCompletedResult by viewModel.quizCompletedResult.collectAsState()

    val activeFlashcards by viewModel.activeFlashcards.collectAsState()
    val flashcardIndex by viewModel.flashcardIndex.collectAsState()
    val isCardFlipped by viewModel.isCardFlipped.collectAsState()

    val activeAudioLesson by viewModel.activeAudioLesson.collectAsState()

    val isGenerating by viewModel.isGenerating.collectAsState()
    val generationMessage by viewModel.generationMessage.collectAsState()
    val paywallVisible by viewModel.paywallVisible.collectAsState()
    val paywallFeature by viewModel.paywallFeature.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    var showQuickActionsModal by remember { mutableStateOf(false) }

    // Toast watcher
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // Android Hardware Back button handling
    BackHandler(enabled = currentScreen != Screen.DASHBOARD && currentScreen != Screen.LANDING) {
        when (currentScreen) {
            Screen.ADD_COURSE, Screen.COURSE_DETAIL -> viewModel.navigateTo(Screen.COURSES)
            Screen.SUMMARY_VIEW, Screen.QUIZ_SETUP, Screen.QUIZ_RUNNER, Screen.FLASHCARDS_RUNNER, Screen.AUDIO_PLAYER -> {
                viewModel.navigateTo(Screen.COURSE_DETAIL)
            }
            Screen.PRICING, Screen.CREDITS, Screen.ADMIN -> viewModel.navigateTo(Screen.DASHBOARD)
            Screen.LOGIN, Screen.REGISTER, Screen.FORGOT_PASSWORD -> viewModel.navigateTo(Screen.LANDING)
            else -> viewModel.navigateTo(Screen.DASHBOARD)
        }
    }

    val isMainTab = currentScreen in listOf(
        Screen.DASHBOARD,
        Screen.COURSES,
        Screen.TUTOR,
        Screen.PROGRESSION,
        Screen.PROFILE
    )

    val showTopBar = currentScreen !in listOf(
        Screen.LANDING,
        Screen.LOGIN,
        Screen.REGISTER,
        Screen.FORGOT_PASSWORD,
        Screen.QUIZ_RUNNER,
        Screen.FLASHCARDS_RUNNER
    )

    Box(modifier = Modifier.fillMaxSize().background(DarkBg)) {
        Scaffold(
            containerColor = DarkBg,
            topBar = {
                if (showTopBar) {
                    ReviaTopBar(
                        userProfile = userProfile,
                        onCreditsClick = { viewModel.navigateTo(Screen.CREDITS) },
                        onProgressionClick = { viewModel.navigateTo(Screen.PROGRESSION) },
                        onProfileClick = { viewModel.navigateTo(Screen.PROFILE) },
                        onLandingClick = { viewModel.navigateTo(Screen.LANDING) }
                    )
                }
            },
            bottomBar = {
                if (isMainTab) {
                    ReviaBottomNav(
                        currentScreen = currentScreen,
                        onNavigate = { viewModel.navigateTo(it) },
                        onPlusClick = { showQuickActionsModal = true }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    Screen.LANDING -> LandingScreen(
                        onStartFree = { viewModel.navigateTo(Screen.REGISTER) },
                        onLoginClick = { viewModel.navigateTo(Screen.LOGIN) },
                        onPricingClick = { viewModel.navigateTo(Screen.PRICING) }
                    )

                    Screen.LOGIN, Screen.REGISTER, Screen.FORGOT_PASSWORD -> AuthScreen(
                        currentAuthMode = currentScreen,
                        onLogin = { email, name -> viewModel.login(email, name) },
                        onRegister = { name, email -> viewModel.register(name, email) },
                        onNavigate = { viewModel.navigateTo(it) }
                    )

                    Screen.DASHBOARD -> DashboardScreen(
                        userProfile = userProfile,
                        courses = courses,
                        summaries = summaries,
                        quizResults = quizResults,
                        audioLessons = audioLessons,
                        onNavigate = { viewModel.navigateTo(it) },
                        onSelectCourse = { viewModel.selectCourse(it) },
                        onOpenSummary = { viewModel.openSummary(it) },
                        onAddCourse = { viewModel.navigateTo(Screen.ADD_COURSE) },
                        onCreateSummary = {
                            if (courses.isNotEmpty()) {
                                viewModel.selectCourse(courses.first())
                                viewModel.generateSummaryForCourse(courses.first())
                            } else {
                                viewModel.navigateTo(Screen.ADD_COURSE)
                            }
                        },
                        onCreateQuiz = {
                            if (courses.isNotEmpty()) {
                                viewModel.selectCourse(courses.first())
                                viewModel.navigateTo(Screen.QUIZ_SETUP)
                            } else {
                                viewModel.navigateTo(Screen.ADD_COURSE)
                            }
                        },
                        onCreateAudio = {
                            if (courses.isNotEmpty()) {
                                viewModel.selectCourse(courses.first())
                                viewModel.generateAudioForCourse(courses.first())
                            } else {
                                viewModel.navigateTo(Screen.ADD_COURSE)
                            }
                        },
                        onSeedStarterCourse = { viewModel.seedStarterCourse() }
                    )

                    Screen.COURSES -> CourseListScreen(
                        courses = courses,
                        onSelectCourse = {
                            viewModel.selectCourse(it)
                            viewModel.navigateTo(Screen.COURSE_DETAIL)
                        },
                        onAddCourseClick = { viewModel.navigateTo(Screen.ADD_COURSE) },
                        onDeleteCourse = {
                            viewModel.deleteCourse(it)
                        },
                        onSeedStarterCourse = { viewModel.seedStarterCourse() }
                    )

                    Screen.ADD_COURSE -> AddCourseScreen(
                        onBack = { viewModel.navigateTo(Screen.COURSES) },
                        onSubmit = { title, subject, level, desc, content ->
                            viewModel.addCourse(title, subject, level, desc, content)
                        },
                        onLoadSample = { viewModel.seedStarterCourse() }
                    )

                    Screen.COURSE_DETAIL -> {
                        val course = selectedCourse ?: courses.firstOrNull()
                        if (course != null) {
                            CourseDetailScreen(
                                course = course,
                                onBack = { viewModel.navigateTo(Screen.COURSES) },
                                onGenerateSummary = { viewModel.generateSummaryForCourse(course) },
                                onGenerateQuiz = { viewModel.navigateTo(Screen.QUIZ_SETUP) },
                                onGenerateFlashcards = { viewModel.generateFlashcardsForCourse(course) },
                                onGenerateAudio = { viewModel.generateAudioForCourse(course) },
                                onOpenTutor = { viewModel.navigateTo(Screen.TUTOR) },
                                onDeleteCourse = {
                                    viewModel.deleteCourse(it)
                                    viewModel.navigateTo(Screen.COURSES)
                                }
                            )
                        } else {
                            viewModel.navigateTo(Screen.COURSES)
                        }
                    }

                    Screen.SUMMARY_VIEW -> {
                        val summary = activeSummary
                        if (summary != null) {
                            SummaryScreen(
                                summary = summary,
                                onBack = { viewModel.navigateTo(Screen.COURSE_DETAIL) },
                                onRegenerate = {
                                    selectedCourse?.let { viewModel.generateSummaryForCourse(it) }
                                },
                                onToast = { viewModel.showToast(it) }
                            )
                        } else {
                            viewModel.navigateTo(Screen.COURSES)
                        }
                    }

                    Screen.QUIZ_SETUP -> {
                        val course = selectedCourse ?: courses.firstOrNull()
                        if (course != null) {
                            QuizSetupScreen(
                                course = course,
                                onBack = { viewModel.navigateTo(Screen.COURSE_DETAIL) },
                                onStartQuiz = { count, difficulty, type ->
                                    viewModel.startQuizGeneration(course, count, difficulty, type)
                                }
                            )
                        } else {
                            viewModel.navigateTo(Screen.COURSES)
                        }
                    }

                    Screen.QUIZ_RUNNER -> {
                        val quiz = activeQuiz
                        if (quiz != null) {
                            QuizRunnerScreen(
                                quiz = quiz,
                                questions = parsedQuestions,
                                currentIndex = currentQuestionIndex,
                                selectedAnswers = userSelectedAnswers,
                                quizResult = quizCompletedResult,
                                onSelectAnswer = { qIdx, oIdx -> viewModel.selectQuizAnswer(qIdx, oIdx) },
                                onNext = { viewModel.nextQuizQuestion() },
                                onPrevious = { viewModel.previousQuizQuestion() },
                                onExit = { viewModel.navigateTo(Screen.COURSES) },
                                onRestart = {
                                    selectedCourse?.let {
                                        viewModel.startQuizGeneration(it, quiz.questionCount, quiz.difficulty, quiz.quizType)
                                    }
                                }
                            )
                        } else {
                            viewModel.navigateTo(Screen.COURSES)
                        }
                    }

                    Screen.FLASHCARDS_RUNNER -> {
                        FlashcardsRunnerScreen(
                            cards = activeFlashcards,
                            currentIndex = flashcardIndex,
                            isFlipped = isCardFlipped,
                            onFlip = { viewModel.flipFlashcard() },
                            onMarkAnswer = { viewModel.markFlashcard(it) },
                            onBack = { viewModel.navigateTo(Screen.COURSE_DETAIL) }
                        )
                    }

                    Screen.AUDIO_PLAYER -> {
                        val audio = activeAudioLesson
                        if (audio != null) {
                            AudioPlayerScreen(
                                audioLesson = audio,
                                audioTtsManager = viewModel.audioTtsManager,
                                onBack = { viewModel.navigateTo(Screen.COURSE_DETAIL) }
                            )
                        } else {
                            viewModel.navigateTo(Screen.COURSES)
                        }
                    }

                    Screen.TUTOR -> TutorScreen(
                        courses = courses,
                        selectedCourse = selectedCourse ?: courses.firstOrNull(),
                        messages = tutorMessages,
                        onSelectCourse = { viewModel.selectCourse(it) },
                        onSendMessage = { query, modifier -> viewModel.askTutor(query, modifier) },
                        onAddCourse = { viewModel.navigateTo(Screen.ADD_COURSE) }
                    )

                    Screen.PROGRESSION -> ProgressionScreen(
                        userProfile = userProfile,
                        coursesCount = courses.size,
                        quizResults = quizResults
                    )

                    Screen.PROFILE -> ProfileScreen(
                        userProfile = userProfile,
                        onNavigate = { viewModel.navigateTo(it) },
                        onLogout = { viewModel.logout() },
                        onToggleDarkMode = { viewModel.toggleDarkMode(it) },
                        onToggleNotifications = { viewModel.toggleNotifications(it) },
                        onToast = { viewModel.showToast(it) }
                    )

                    Screen.PRICING -> PricingScreen(
                        currentPlan = userProfile?.plan ?: "FREE",
                        onBack = { viewModel.navigateTo(Screen.DASHBOARD) },
                        onSelectPlan = { name, price, provider ->
                            viewModel.subscribePlan(name, price, provider)
                        }
                    )

                    Screen.CREDITS -> CreditsScreen(
                        userProfile = userProfile,
                        transactions = creditTransactions,
                        onBack = { viewModel.navigateTo(Screen.DASHBOARD) },
                        onBuyPack = { credits, price, provider ->
                            viewModel.buyCreditsPack(credits, price, provider)
                        }
                    )

                    Screen.ADMIN -> AdminDashboardScreen(
                        coursesCount = courses.size,
                        quizzesCount = quizResults.size,
                        audioCount = audioLessons.size,
                        onBack = { viewModel.navigateTo(Screen.PROFILE) },
                        onAddCredits = { viewModel.adminAddCredits(it) },
                        onSetPlan = { viewModel.adminSetPlan(it) }
                    )
                }
            }
        }

        // Quick Actions modal (+)
        if (showQuickActionsModal) {
            QuickActionsModal(
                onDismiss = { showQuickActionsModal = false },
                onAddCourse = { viewModel.navigateTo(Screen.ADD_COURSE) },
                onCreateSummary = {
                    if (courses.isNotEmpty()) {
                        viewModel.selectCourse(courses.first())
                        viewModel.generateSummaryForCourse(courses.first())
                    } else {
                        viewModel.navigateTo(Screen.ADD_COURSE)
                    }
                },
                onCreateQuiz = {
                    if (courses.isNotEmpty()) {
                        viewModel.selectCourse(courses.first())
                        viewModel.navigateTo(Screen.QUIZ_SETUP)
                    } else {
                        viewModel.navigateTo(Screen.ADD_COURSE)
                    }
                },
                onCreateAudio = {
                    if (courses.isNotEmpty()) {
                        viewModel.selectCourse(courses.first())
                        viewModel.generateAudioForCourse(courses.first())
                    } else {
                        viewModel.navigateTo(Screen.ADD_COURSE)
                    }
                }
            )
        }

        // Paywall Dialog
        if (paywallVisible) {
            IntelligentPaywallDialog(
                featureName = paywallFeature,
                onViewPricing = {
                    viewModel.dismissPaywall()
                    viewModel.navigateTo(Screen.PRICING)
                },
                onDismiss = { viewModel.dismissPaywall() }
            )
        }

        // Loading Dialog for AI Generations
        if (isGenerating) {
            GenerationLoadingDialog(message = generationMessage)
        }
    }
}

// Preserve for Roborazzi screenshot test backward compatibility
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
