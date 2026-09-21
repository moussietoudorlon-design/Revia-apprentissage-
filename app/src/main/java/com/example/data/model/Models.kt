package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Alex",
    val email: String = "alex.etudiant@revia.ai",
    val plan: String = "FREE", // FREE, PRO, MAX
    val credits: Int = 20, // Free account starts with 20 credits
    val streakDays: Int = 5,
    val studyTimeMinutes: Int = 145,
    val masteredConceptsCount: Int = 18,
    val needsReviewCount: Int = 4,
    val isDarkMode: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val language: String = "Français",
    val isAdmin: Boolean = false,
    val subscriptionRenewalDate: String = "15 Octobre 2026",
    val isLoggedIn: Boolean = true
)

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subject: String, // ex: Histoire, Physique-Chimie, Philosophie, Mathématiques, SVT
    val level: String, // ex: Terminale, Première, Licence 1, Prépa
    val description: String,
    val rawContent: String,
    val extractedTopics: String = "", // JSON or comma separated
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "summaries")
data class RevisionSummary(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val courseId: Long,
    val courseTitle: String,
    val summaryText: String,
    val keyConcepts: String, // JSON list or newline separated
    val definitions: String,
    val keyTakeaways: String,
    val examples: String,
    val commonMistakes: String,
    val miniRecap: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "quizzes")
data class Quiz(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val courseId: Long,
    val courseTitle: String,
    val difficulty: String, // Facile, Moyen, Difficile
    val questionCount: Int,
    val quizType: String, // QCM, Vrai/Faux, Questions ouvertes, Mixte
    val questionsJson: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "quiz_results")
data class QuizResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val quizId: Long,
    val courseId: Long,
    val courseTitle: String,
    val score: Int, // e.g. 16
    val maxScore: Int = 20, // out of 20 as requested
    val totalQuestions: Int,
    val percentage: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val reviewRecommendation: String,
    val userAnswersJson: String = "",
    val completedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "flashcards")
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val courseId: Long,
    val courseTitle: String,
    val question: String,
    val answer: String,
    val isMastered: Boolean = false,
    val reviewCount: Int = 0,
    val lastReviewedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "audio_lessons")
data class AudioLesson(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val courseId: Long,
    val courseTitle: String,
    val title: String,
    val introduction: String,
    val explanation: String,
    val examples: String,
    val recap: String,
    val quickQuestions: String,
    val fullScript: String,
    val durationSeconds: Int = 180,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "credit_transactions")
data class CreditTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Int, // negative for usage, positive for recharge
    val description: String,
    val type: String, // "GENERATION_SUMMARY", "GENERATION_QUIZ", "GENERATION_AUDIO", "PACK_PURCHASE", "BONUS"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "payments")
data class PaymentRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val currency: String = "EUR",
    val status: String = "COMPLETED", // COMPLETED, PENDING, FAILED
    val provider: String = "PAYPAL", // PAYPAL, STRIPE
    val transactionId: String,
    val planOrPack: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "tutor_messages")
data class TutorMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val courseId: Long,
    val sender: String, // "USER" or "AI"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
