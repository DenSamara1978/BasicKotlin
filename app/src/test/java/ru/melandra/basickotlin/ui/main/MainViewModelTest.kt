package ru.melandra.basickotlin.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.channels.Channel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.Data.NoteResult
import ru.melandra.basickotlin.Data.NotesRepository
import ru.melandra.basickotlin.UI.main.MainViewModel

class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<NotesRepository>()
    private val receiveChannelNoteResult = Channel<NoteResult>()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        clearAllMocks()
        every { mockRepository.getNotes() } returns receiveChannelNoteResult
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `should call getNotes once`() {
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `should return notes`(){
        var result: List<Note>? = null
        val testData = listOf(Note("1"), Note("2"))
        viewModel.viewStateLiveData.observeForever{
            result = it?.data
        }
        notesLiveData.value = NoteResult.Success(testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should return error`(){
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.viewStateLiveData.observeForever{
            result = it?.error
        }
        notesLiveData.value = NoteResult.Error(error = testData)
        assertEquals(testData, result)
    }
}