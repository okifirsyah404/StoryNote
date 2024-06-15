package com.okifirsyah.storynote

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.okifirsyah.data.utils.BaseUrlProvider
import com.okifirsyah.data.utils.EspressoIdlingResource
import com.okifirsyah.storynote.presentation.view.dashboard.DashboardFragment
import com.okifirsyah.storynote.presentation.view.setting.SettingFragment
import com.okifirsyah.storynote.presentation.view.sign_in.SignInFragment
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SignInSignOutTest {

    private lateinit var mockWebServer: MockWebServer

    private lateinit var instrumentationContext: Context
    private val email = "nana12@example.com"
    private val password = "nana12@example.com"

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
        mockWebServer.start(8080)
        BaseUrlProvider.baseUrl = "http://localhost:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loadSignInScreen() {
        Thread.sleep(4000L)
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val titleScenario =
            launchFragmentInContainer<SignInFragment>(themeResId = R.style.Theme_StoryNote)

        titleScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.main_navigation)
            navController.setCurrentDestination(R.id.signInFragment)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.ed_login_email)).perform(ViewActions.typeText(email))
        onView(withId(R.id.ed_login_password)).perform(ViewActions.typeText(password))
        onView(withId(R.id.btn_login)).perform(ViewActions.click())

        val mockResponse = MockResponse().setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("response.json"))
        mockWebServer.enqueue(mockResponse)

        ViewActions.closeSoftKeyboard()
    }

    @Test
    fun loadDashboardScreen() {
        Thread.sleep(4000L)
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val titleScenario =
            launchFragmentInContainer<DashboardFragment>(themeResId = R.style.Theme_StoryNote)

        titleScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.main_navigation)
            navController.setCurrentDestination(R.id.dashboardFragment)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
        onView(withId(R.id.btn_setting))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun loadSettingScreen() {
        Thread.sleep(4000L)
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val titleScenario =
            launchFragmentInContainer<SettingFragment>(themeResId = R.style.Theme_StoryNote)

        titleScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.main_navigation)
            navController.setCurrentDestination(R.id.settingFragment)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.btn_sign_out))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.btn_sign_out)).perform(ViewActions.click())
        onView(withId(android.R.id.button2)).perform(ViewActions.click())
    }

}