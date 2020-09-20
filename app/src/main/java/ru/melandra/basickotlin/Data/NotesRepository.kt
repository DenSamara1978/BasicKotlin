package ru.melandra.basickotlin.Data

import ru.melandra.basickotlin.Data.Provider.RemoteDataProvider

class NotesRepository(val remoteProvider: RemoteDataProvider) {
    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note:Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun deleteNote(id: String) = remoteProvider.deleteNote(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()
}
