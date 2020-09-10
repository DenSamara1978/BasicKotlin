package ru.melandra.basickotlin.UI.note

import androidx.lifecycle.ViewModel
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.Data.NotesRepository

class NoteViewModel: ViewModel() {

    private var pendingNote: Note? = null;

    fun save(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let {
            NotesRepository.saveNote(it)
        }
    }
}