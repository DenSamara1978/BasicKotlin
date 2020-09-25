package ru.melandra.basickotlin.note

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import io.mockk.*
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
import ru.melandra.basickotlin.UI.note.NoteActivity
import ru.melandra.basickotlin.UI.note.NoteViewModel
import ru.melandra.basickotlin.UI.note.NoteViewState

class NoteActivityTest {
    @get:Rule
    val activityRule = IntentsTestRule(NoteActivity::class.java, true, false)

    private val model: NoteViewModel = mockk()
    private val viewStateLiveData = MutableLiveData<NoteViewState>()

    private val testNote = Note("1", "title", "text")

    @Before
    fun setup() {
        loadKoinModules(listOf(
                module {
                    viewModel(override = true) { model }
                }))

        every { model.getViewState() } returns viewStateLiveData
        every { model.loadNote(any()) } just runs
        every { model.save(any()) } just runs
        every { model.deleteNote() } just runs

        Intent().apply {
            putExtra(NoteActivity::class.java.name + "extra.NOTE", testNote.id)
        }.let {
            activityRule.launchActivity(it)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun should_call_viewModel_loadNote() {
        verify(exactly = 1) { model.loadNote(testNote.id) }
    }

    @Test
    fun should_show_note() {
        activityRule.launchActivity(null)
        viewStateLiveData.postValue(NoteViewState(NoteViewState.Data(note = testNote)))

        onView(withId(R.id.et_title)).check(matches(withText(testNote.title)))
        onView(withId(R.id.et_body)).check(matches(withText(testNote.text)))
    }

    @Test
    fun should_call_saveNote() {
        onView(withId(R.id.et_title)).perform(typeText(testNote.title))
        verify(timeout = 1000) { model.save(any()) }
    }

    @Test
    fun should_call_deleteNote() {
        openActionBarOverflowOrOptionsMenu(activityRule.activity)
        onView(withText(R.string.note_delete)).perform(click())
        onView(withText(R.string.note_delete_ok)).perform(click())
        verify { model.deleteNote() }
    }

    @Test
    fun should_call_deleteNote_and_dismiss() {
        openActionBarOverflowOrOptionsMenu(activityRule.activity)
        onView(withText(R.string.note_delete)).perform(click())
        onView(withText(R.string.note_delete_cancel)).perform(click())
        verify(exactly = 0) { model.deleteNote() }
    }
}