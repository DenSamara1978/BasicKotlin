package ru.melandra.basickotlin.Data.Provider

import androidx.lifecycle.LiveData
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.Data.NoteResult

interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
}
