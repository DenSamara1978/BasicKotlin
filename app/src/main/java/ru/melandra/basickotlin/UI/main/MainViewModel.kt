package ru.melandra.basickotlin.UI.main

import androidx.lifecycle.Observer
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.Data.NoteResult
import ru.melandra.basickotlin.Data.NotesRepository
import ru.melandra.basickotlin.UI.base.BaseViewModel
import java.util.*

class MainViewModel: BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = Observer<NoteResult> { result ->
        result ?: return@Observer
        when (result) {
            is NoteResult.Success<*> -> {
                @Suppress("UNCHECKED_CAST")
                viewStateLiveData.value = MainViewState(notes = result.data as? List<Note>)
            }

            is NoteResult.Error -> {
                viewStateLiveData.value = MainViewState(error = result.error)
            }
        }
    }

    private val repositoryNotes = NotesRepository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
        super.onCleared()
    }
}
