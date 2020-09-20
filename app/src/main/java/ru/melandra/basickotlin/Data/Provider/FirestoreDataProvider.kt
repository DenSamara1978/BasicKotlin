package ru.melandra.basickotlin.Data.Provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ru.melandra.basickotlin.Data.NoAuthException
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.Data.NoteResult
import ru.melandra.basickotlin.Data.User

class FirestoreDataProvider(val store: FirebaseFirestore, val auth: FirebaseAuth) : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USER_COLLECTION = "users"
    }

    private val currentUser
        get() = auth.currentUser

    val userNotesCollection
        get() = currentUser?.let { store.collection(USER_COLLECTION).document(it.uid).collection(NOTES_COLLECTION) } ?: throw NoAuthException()

    override fun subscribeToAllNotes(): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            userNotesCollection.addSnapshotListener { snapshot, error ->
                error?.let {
                    value = NoteResult.Error(error)
                } ?: let {
                    snapshot?.let {
                        val notes = snapshot.documents.map { doc -> doc.toObject(Note::class.java) }
                        value = NoteResult.Success(notes)
                    }
                }
            }
        } catch (error: Throwable) {
            value = NoteResult.Error(error)
        }
    }

    override fun getNoteById(id: String): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            userNotesCollection.document(id).get()
                    .addOnSuccessListener { snapshot ->
                        value = NoteResult.Success(snapshot.toObject(Note::class.java))
                    }.addOnFailureListener {
                        value = NoteResult.Error(it)
                    }
        } catch (error: Throwable) {
            value = NoteResult.Error(error)
        }
    }

    override fun saveNote(note: Note): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            userNotesCollection.document(note.id).set(note)
                    .addOnSuccessListener { snapshot ->
                        value = NoteResult.Success(note)
                    }.addOnFailureListener {
                        value = NoteResult.Error(it)
                    }
        } catch (error: Throwable) {
            value = NoteResult.Error(error)
        }
    }

    override fun deleteNote(id: String): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            userNotesCollection.document(id).delete()
                    .addOnSuccessListener {
                        value = NoteResult.Success(null)
                    }.addOnFailureListener {
                        value = NoteResult.Error(it)
                    }
        } catch (error: Throwable) {
            value = NoteResult.Error(error)
        }
    }

    override fun getCurrentUser(): LiveData<User?> = MutableLiveData<User?>().apply {
        value = currentUser?.let { User(it.displayName ?: "", it.email ?: "")}
    }

}