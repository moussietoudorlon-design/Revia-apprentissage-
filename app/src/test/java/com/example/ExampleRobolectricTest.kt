package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.ReviaDatabase
import com.example.data.model.Course
import com.example.data.model.RevisionSummary
import com.example.data.repository.ReviaRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var db: ReviaDatabase
    private lateinit var repository: ReviaRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ReviaDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = ReviaRepository(db.reviaDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("REVIA AI", appName)
    }

    @Test
    fun `user profile initializes with 20 default credits and free plan`() = runBlocking {
        repository.ensureProfileInitialized()
        val profile = repository.getProfile()
        assertEquals("FREE", profile.plan)
        assertEquals(20, profile.credits)
        assertTrue(profile.isLoggedIn)
    }

    @Test
    fun `credit consumption deducts credits and rejects when insufficient`() = runBlocking {
        repository.ensureProfileInitialized()
        val affordable = repository.canAfford(10)
        assertTrue(affordable)

        val consumed = repository.consumeCredits(5, "TEST", "Test consumption")
        assertTrue(consumed)
        assertEquals(15, repository.getProfile().credits)

        val unaffordable = repository.canAfford(30)
        assertFalse(unaffordable)
        val rejected = repository.consumeCredits(30, "TEST", "Should fail")
        assertFalse(rejected)
        assertEquals(15, repository.getProfile().credits)
    }

    @Test
    fun `purchasing credit pack increments credits`() = runBlocking {
        repository.ensureProfileInitialized()
        val initialCredits = repository.getProfile().credits
        repository.purchaseCreditPack(50, 3.99, "STRIPE")
        val updatedCredits = repository.getProfile().credits
        assertEquals(initialCredits + 50, updatedCredits)
    }

    @Test
    fun `subscribing to PRO plan updates plan and gives bonus credits`() = runBlocking {
        repository.ensureProfileInitialized()
        val initialCredits = repository.getProfile().credits
        repository.subscribeToPlan("PRO", 8.99, "STRIPE")
        val profile = repository.getProfile()
        assertEquals("PRO", profile.plan)
        assertEquals(initialCredits + 150, profile.credits)
    }

    @Test
    fun `deleting course cascades and deletes associated summaries`() = runBlocking {
        val dao = db.reviaDao()
        val course = Course(
            id = 42,
            title = "Physique Quantique",
            subject = "Physique",
            level = "Université",
            description = "Introduction aux quantas",
            rawContent = "Le principe d'incertitude d'Heisenberg..."
        )
        dao.insertCourse(course)
        assertNotNull(dao.getCourseById(42))

        val summary = RevisionSummary(
            courseId = 42,
            courseTitle = "Physique Quantique",
            summaryText = "Résumé du cours",
            keyConcepts = "Ondes et particules",
            definitions = "Constante de Planck",
            keyTakeaways = "Dualité onde-corpuscule",
            examples = "Expérience d'Young",
            commonMistakes = "Confusion état/valeur",
            miniRecap = "Synthèse rapide"
        )
        dao.insertSummary(summary)
        assertNotNull(dao.getSummaryForCourse(42))

        // Delete course via repository
        repository.deleteCourse(course)

        // Verify course and summary are deleted
        assertNull(dao.getCourseById(42))
        assertNull(dao.getSummaryForCourse(42))
    }

    @Test
    fun `login and logout toggle isLoggedIn state`() = runBlocking {
        repository.ensureProfileInitialized()
        repository.logout()
        assertFalse(repository.getProfile().isLoggedIn)

        repository.login("Thomas", "thomas@student.com")
        val profile = repository.getProfile()
        assertTrue(profile.isLoggedIn)
        assertEquals("Thomas", profile.name)
        assertEquals("thomas@student.com", profile.email)
    }
}
