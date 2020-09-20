package ru.melandra.basickotlin.ui.note

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.Data.NoteResult
import ru.melandra.basickotlin.Data.NotesRepository
import ru.melandra.basickotlin.UI.note.NoteViewModel
import ru.melandra.basickotlin.UI.note.NoteViewState

class NoteViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<NotesRepository>(relaxed = true)
    private val noteLiveData = MutableLiveData<NoteResult>()
    private lateinit var viewModel: NoteViewModel

    private val testNote = Note("1", "title", "text")

    @Before
    fun setup() {
        clearAllMocks()
        every { mockRepository.getNoteById(testNote.id) } returns noteLiveData
        every { mockRepository.deleteNote(testNote.id) } returns noteLiveData
        viewModel = NoteViewModel(mockRepository)
    }

    @Test
    fun `should call getNoteById once`() {
        viewModel.loadNote("1" )
        verify(exactly = 1) { mockRepository.getNoteById(any()) }
    }

    @Test
    fun `loadNote should return NoteData`() {
        var result: NoteViewState.Data? = null
        val testData = NoteViewState.Data(false, testNote)
        viewModel.viewStateLiveData.observeForever {
            result = it?.data
        }
        viewModel.loadNote(testNote.id)
        noteLiveData.value = NoteResult.Success(testNote)
        assertEquals(testData, result)
    }


    @Test
    fun `loadNote should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.viewStateLiveData.observeForever {
            result = it?.error
        }
        viewModel.loadNote(testNote.id)
        noteLiveData.value = NoteResult.Error(error = testData)
        assertEquals(testData, result)
    }


    @Test
    fun `deleteNote should return NoteData with isDeleted`() {
        var result: NoteViewState.Data? = null
        val testData = NoteViewState.Data(true)
        viewModel.viewStateLiveData.observeForever {
            result = it?.data
        }
        viewModel.save(testNote)
        viewModel.deleteNote()
        noteLiveData.value = NoteResult.Success(null)
        assertEquals(testData, result)
    }


    @Test
    fun `delete should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.viewStateLiveData.observeForever {
            result = it?.error
        }
        viewModel.save(testNote)
        viewModel.deleteNote()
        noteLiveData.value = NoteResult.Error(error = testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should save changed`() {
        viewModel.save(testNote)
        viewModel.onCleared()
        verify(exactly = 1) { mockRepository.saveNote(testNote) }
    }

    @Test
    fun `should remove observer`(){
        viewModel.onCleared()
        Assert.assertFalse(noteLiveData.hasObservers())
    }
}