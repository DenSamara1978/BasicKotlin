package ru.melandra.basickotlin.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

object NotesRepository {

    private val notesLiveData = MutableLiveData<List<Note>>()

    private var notes = mutableListOf(
            Note(UUID.randomUUID().toString(),"First note", "Text of the first note", Note.Color.WHITE),
            Note(UUID.randomUUID().toString(),"Second note", "Text of the second note", Note.Color.RED),
            Note(UUID.randomUUID().toString(),"Third note", "Text of the third note", Note.Color.GREEN),
            Note(UUID.randomUUID().toString(),"Fourth note", "Text of the fourth note", Note.Color.BLUE)
    )

    init {
        notesLiveData.value = notes
    }

    fun getNotes(): LiveData<List<Note>> {
        return notesLiveData
    }

    fun saveNote(note:Note){
        addOrReplace(note)
        notesLiveData.value = notes
    }

    private fun addOrReplace(note: Note){
        notes.forEachIndexed { index, thisNote ->
            if (thisNote == note) {
                notes[index] = note;
                return;
            }
        }
        notes.add(note)
    }

}
