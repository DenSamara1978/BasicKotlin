package ru.melandra.basickotlin.UI.note

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.launch
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.Data.NotesRepository
import ru.melandra.basickotlin.UI.base.BaseViewModel

open class NoteViewModel(val notesRepository: NotesRepository): BaseViewModel<NoteViewState.Data>() {

    private val pendingNote: Note?
        get() = getViewState().poll()?.note

    fun save(note: Note) {
        setData(NoteViewState.Data(note = note))
    }

    fun loadNote(noteId: String) = launch {
        try {
            notesRepository.getNoteById(noteId).let {
                setData(NoteViewState.Data(note = it))
            }
        } catch (error: Throwable) {
            setError(error)
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        launch {
            pendingNote?.let { notesRepository.saveNote(it) }
        }
    }

    fun deleteNote() = launch {
        try {
            pendingNote?.let { notesRepository.deleteNote(it.id)}
            setData(NoteViewState.Data(isDeleted = true))
        } catch (error: Throwable) {
            setError(error)
        }
    }
}