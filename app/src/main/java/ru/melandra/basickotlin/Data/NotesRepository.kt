package ru.melandra.basickotlin.Data

import ru.melandra.basickotlin.Data.Provider.RemoteDataProvider

class NotesRepository(val remoteProvider: RemoteDataProvider) {
    fun getNotes() = remoteProvider.subscribeToAllNotes()
    suspend fun saveNote(note:Note) = remoteProvider.saveNote(note)
    suspend fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    suspend fun deleteNote(id: String) = remoteProvider.deleteNote(id)
    suspend fun getCurrentUser() = remoteProvider.getCurrentUser()
}
