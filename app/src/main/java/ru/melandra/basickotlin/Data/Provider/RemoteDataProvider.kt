package ru.melandra.basickotlin.Data.Provider

import androidx.lifecycle.LiveData
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.Data.NoteResult
import ru.melandra.basickotlin.Data.User

interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
    fun deleteNote(id: String): LiveData<NoteResult>
    fun getCurrentUser(): LiveData<User?>
}
