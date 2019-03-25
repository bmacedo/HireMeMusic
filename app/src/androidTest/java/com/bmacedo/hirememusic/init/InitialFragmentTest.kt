package com.bmacedo.hirememusic.init

import android.preference.PreferenceManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.bmacedo.hirememusic.AndroidTestCoroutineContext
import com.bmacedo.hirememusic.HireMeMusic
import com.bmacedo.hirememusic.MainActivity
import com.bmacedo.hirememusic.R
import com.bmacedo.hirememusic.authentication.AuthenticationRepository
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class InitialFragmentTest {

    private val app by lazy { InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as HireMeMusic }
    private val authRepository by lazy {
        AuthenticationRepository(
            PreferenceManager.getDefaultSharedPreferences(app),
            AndroidTestCoroutineContext
        )
    }

    @get:Rule
    val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java, true, false)

    @Test
    fun onViewCreated_whenLoggedIn_navigateToSearch() {
        runBlocking {
            authRepository.saveToken("test")
            activityTestRule.launchActivity(null)
            onView(withId(R.id.searchLabel)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun onViewCreated_whenLoggedOut_navigateToAuth() {
        runBlocking {
            authRepository.invalidateToken()
            activityTestRule.launchActivity(null)
            onView(withId(R.id.authWelcomeText)).check(matches(isDisplayed()))
        }
    }

}