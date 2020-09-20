package ru.melandra.basickotlin.UI.note

import androidx.annotation.VisibleForTesting
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.Data.NoteResult
import ru.melandra.basickotlin.Data.NotesRepository
import ru.melandra.basickotlin.UI.base.BaseViewModel

open class NoteViewModel(val notesRepository: NotesRepository): BaseViewModel<NoteViewState.Data, NoteViewState>() {

    private var pendingNote: Note? = null;

    fun save(note: Note) {
        pendingNote = note
    }

    fun loadNote(noteId: String) {
        notesRepository.getNoteById(noteId).observeForever { result ->
            result ?: return@observeForever
            when (result) {
                is NoteResult.Success<*> -> {
                    viewStateLiveData.value = NoteViewState(NoteViewState.Data(note = result.data as? Note))
                }

                is NoteResult.Error -> {
                    viewStateLiveData.value = NoteViewState(error = result.error)
                }
            }
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        pendingNote?.let {
            notesRepository.saveNote(it)
        }
    }

    fun deleteNote() {
        pendingNote?.let {
            notesRepository.deleteNote(it.id).observeForever { result ->
                result ?: return@observeForever
                when (result) {
                    is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(NoteViewState.Data(isDeleted = true ))
                    is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = result.error)
                }
            }
        }
    }
}