package ru.melandra.basickotlin.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.melandra.basickotlin.Data.Provider.FirestoreDataProvider
import ru.melandra.basickotlin.Data.Provider.RemoteDataProvider
import java.util.*

object NotesRepository {

    val remoteProvider: RemoteDataProvider = FirestoreDataProvider()

    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note:Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
}
