package ru.melandra.basickotlin.UI.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.Data.NoteResult
import ru.melandra.basickotlin.Data.NotesRepository
import ru.melandra.basickotlin.UI.base.BaseViewModel
import java.util.*

open class MainViewModel(notesRepository: NotesRepository): BaseViewModel<List<Note>?, MainViewState>() {

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

    private val repositoryNotes = notesRepository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    @VisibleForTesting
    public override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
        super.onCleared()
    }
}
