package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

@Dao
interface ReviaDao {
    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfileFlow(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfile(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    @Update
    suspend fun updateProfile(profile: UserProfile)

    // Courses
    @Query("SELECT * FROM courses ORDER BY createdAt DESC")
    fun getAllCoursesFlow(): Flow<List<Course>>

    @Query("SELECT * FROM courses WHERE id = :id LIMIT 1")
    suspend fun getCourseById(id: Long): Course?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course): Long

    @Delete
    suspend fun deleteCourse(course: Course)

    @Query("DELETE FROM summaries WHERE courseId = :courseId")
    suspend fun deleteSummariesForCourse(courseId: Long)

    @Query("DELETE FROM quizzes WHERE courseId = :courseId")
    suspend fun deleteQuizzesForCourse(courseId: Long)

    @Query("DELETE FROM flashcards WHERE courseId = :courseId")
    suspend fun deleteFlashcardsForCourse(courseId: Long)

    @Query("DELETE FROM audio_lessons WHERE courseId = :courseId")
    suspend fun deleteAudioForCourse(courseId: Long)

    @Query("DELETE FROM tutor_messages WHERE courseId = :courseId")
    suspend fun deleteTutorMessagesForCourse(courseId: Long)

    // Revision Summaries
    @Query("SELECT * FROM summaries WHERE courseId = :courseId ORDER BY createdAt DESC LIMIT 1")
    suspend fun getSummaryForCourse(courseId: Long): RevisionSummary?

    @Query("SELECT * FROM summaries ORDER BY createdAt DESC")
    fun getAllSummariesFlow(): Flow<List<RevisionSummary>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummary(summary: RevisionSummary): Long

    // Quizzes
    @Query("SELECT * FROM quizzes WHERE courseId = :courseId ORDER BY createdAt DESC")
    fun getQuizzesForCourse(courseId: Long): Flow<List<Quiz>>

    @Query("SELECT * FROM quizzes WHERE id = :id LIMIT 1")
    suspend fun getQuizById(id: Long): Quiz?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: Quiz): Long

    // Quiz Results
    @Query("SELECT * FROM quiz_results ORDER BY completedAt DESC")
    fun getAllQuizResultsFlow(): Flow<List<QuizResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: QuizResult): Long

    // Flashcards
    @Query("SELECT * FROM flashcards WHERE courseId = :courseId ORDER BY id ASC")
    fun getFlashcardsForCourse(courseId: Long): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards ORDER BY id ASC")
    fun getAllFlashcardsFlow(): Flow<List<Flashcard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcards(flashcards: List<Flashcard>)

    @Update
    suspend fun updateFlashcard(flashcard: Flashcard)

    // Audio Lessons
    @Query("SELECT * FROM audio_lessons WHERE courseId = :courseId ORDER BY createdAt DESC LIMIT 1")
    suspend fun getAudioForCourse(courseId: Long): AudioLesson?

    @Query("SELECT * FROM audio_lessons ORDER BY createdAt DESC")
    fun getAllAudioLessonsFlow(): Flow<List<AudioLesson>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudioLesson(audioLesson: AudioLesson): Long

    // Credits
    @Query("SELECT * FROM credit_transactions ORDER BY timestamp DESC")
    fun getCreditTransactionsFlow(): Flow<List<CreditTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreditTransaction(tx: CreditTransaction): Long

    // Payments
    @Query("SELECT * FROM payments ORDER BY createdAt DESC")
    fun getAllPaymentsFlow(): Flow<List<PaymentRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentRecord): Long

    // Tutor Messages
    @Query("SELECT * FROM tutor_messages WHERE courseId = :courseId ORDER BY timestamp ASC")
    fun getTutorMessagesFlow(courseId: Long): Flow<List<TutorMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTutorMessage(message: TutorMessage): Long
}
