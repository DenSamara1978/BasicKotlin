package ru.melandra.basickotlin.Data.Provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import ru.melandra.basickotlin.Data.NoAuthException
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.Data.NoteResult
import ru.melandra.basickotlin.Data.User
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreDataProvider(val store: FirebaseFirestore, val auth: FirebaseAuth) : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USER_COLLECTION = "users"
    }

    private val currentUser
        get() = auth.currentUser

    val userNotesCollection
        get() = currentUser?.let { store.collection(USER_COLLECTION).document(it.uid).collection(NOTES_COLLECTION) } ?: throw NoAuthException()

    override fun subscribeToAllNotes(): ReceiveChannel<NoteResult> = Channel<NoteResult>(Channel.CONFLATED).apply {
        var registration: ListenerRegistration? = null
        try {
            registration = userNotesCollection.addSnapshotListener { snapshot, error ->
                val value = error?.let {
                    NoteResult.Error(error)
                } ?: let {
                    snapshot?.let {
                        val notes = snapshot.documents.map { doc -> doc.toObject(Note::class.java) }
                        NoteResult.Success(notes)
                    }
                }

                value?.let { offer(it)}
            }
        } catch (error: Throwable) {
            offer(NoteResult.Error(error))
        }

        invokeOnClose { registration?.remove() }
    }

    override suspend fun getNoteById(id: String): Note = suspendCoroutine { continuation ->
        try {
            userNotesCollection.document(id).get()
                    .addOnSuccessListener { snapshot ->
                        continuation.resume(snapshot.toObject(Note::class.java)!!)
                    }.addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
        } catch (error: Throwable) {
            continuation.resumeWithException(error)
        }
    }

    override suspend fun saveNote(note: Note): Note = suspendCoroutine { continuation ->
        try {
            userNotesCollection.document(note.id).set(note)
                    .addOnSuccessListener { snapshot ->
                        continuation.resume(note)
                    }.addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
        } catch (error: Throwable) {
            continuation.resumeWithException(error)
        }
    }

    override suspend fun deleteNote(id: String): Unit = suspendCoroutine { continuation ->
        try {
            userNotesCollection.document(id).delete()
                    .addOnSuccessListener {
                        continuation.resume(Unit)
                    }.addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
        } catch (error: Throwable) {
            continuation.resumeWithException(error)
        }
    }

    override suspend fun getCurrentUser(): User? = suspendCoroutine {continuation ->
        currentUser?.let { User(it.displayName ?: "", it.email ?: "" )}.let { continuation.resume(it) }
    }

}