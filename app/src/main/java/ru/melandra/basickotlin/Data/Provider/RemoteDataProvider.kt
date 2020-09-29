package ru.melandra.basickotlin.Data.Provider

import kotlinx.coroutines.channels.ReceiveChannel
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.Data.NoteResult
import ru.melandra.basickotlin.Data.User

interface RemoteDataProvider {
    fun subscribeToAllNotes(): ReceiveChannel<NoteResult>
    suspend fun getNoteById(id: String): Note
    suspend fun saveNote(note: Note): Note
    suspend fun deleteNote(id: String)
    suspend fun getCurrentUser(): User?
}
