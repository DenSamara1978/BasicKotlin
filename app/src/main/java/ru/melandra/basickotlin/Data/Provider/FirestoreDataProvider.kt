package ru.melandra.basickotlin.Data.Provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.Data.NoteResult

class FirestoreDataProvider : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
    }

    private val store = FirebaseFirestore.getInstance()
    private val notesReference = store.collection(NOTES_COLLECTION)

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.addSnapshotListener { value, error ->
            error?.let {
                result.value = NoteResult.Error(error)
            } ?: let {
                value?.let {
                    val notes = value.documents.map { doc -> doc.toObject(Note::class.java) }
                    result.value = NoteResult.Success(notes)
                }
            }
        }
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(id).get()
                .addOnSuccessListener { snapshot ->
                    result.value = NoteResult.Success(snapshot.toObject(Note::class.java))
                }.addOnFailureListener {
                    result.value = NoteResult.Error(it)
                }
        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(note.id).set(note)
                .addOnSuccessListener { snapshot ->
                    result.value = NoteResult.Success(note)
                }.addOnFailureListener {
                    result.value = NoteResult.Error(it)
                }
        return result
    }

}