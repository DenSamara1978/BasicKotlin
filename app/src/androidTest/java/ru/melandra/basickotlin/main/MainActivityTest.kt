package ru.melandra.basickotlin.main

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.R
import ru.melandra.basickotlin.UI.main.MainActivity
import ru.melandra.basickotlin.UI.main.MainViewModel
import ru.melandra.basickotlin.UI.main.MainViewState
import ru.melandra.basickotlin.UI.main.NotesRecyclerViewAdapter

class MainActivityTest {
    @get:Rule
    val activityRule = IntentsTestRule(MainActivity::class.java, true, false)

    private val model: MainViewModel = mockk()
    private val viewStateLiveData = MutableLiveData<MainViewState>()

    private val testNotes = listOf(
            Note("1", "title1", "text1"),
            Note("2", "title2", "text2"),
            Note("3", "title3", "text3")
    )

    @Before
    fun setup(){
        loadKoinModules(
                listOf(module {
                    viewModel(override = true) { model }
                })
        )

        every { model.getViewState() } returns viewStateLiveData
        every { model.onCleared() } just runs
        activityRule.launchActivity(null)
        viewStateLiveData.postValue(MainViewState(notes = testNotes))
    }

    @After
    fun tearDown(){
        stopKoin()
    }

    @Test
    fun check_data_is_displayed() {
        onView(withId(R.id.rv_notes)).perform(scrollToPosition<NotesRecyclerViewAdapter.ViewHolder>(1))
        onView(withText(testNotes[0].text)).check(matches(isDisplayed()))
    }

}