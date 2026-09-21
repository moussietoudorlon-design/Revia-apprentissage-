package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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

@Database(
    entities = [
        UserProfile::class,
        Course::class,
        RevisionSummary::class,
        Quiz::class,
        QuizResult::class,
        Flashcard::class,
        AudioLesson::class,
        CreditTransaction::class,
        PaymentRecord::class,
        TutorMessage::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ReviaDatabase : RoomDatabase() {
    abstract fun reviaDao(): ReviaDao

    companion object {
        @Volatile
        private var INSTANCE: ReviaDatabase? = null

        fun getInstance(context: Context): ReviaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReviaDatabase::class.java,
                    "revia_ai_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
