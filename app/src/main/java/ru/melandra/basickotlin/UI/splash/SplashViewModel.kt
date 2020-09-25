package ru.melandra.basickotlin.UI.splash

import kotlinx.coroutines.launch
import ru.melandra.basickotlin.Data.NoAuthException
import ru.melandra.basickotlin.Data.NotesRepository
import ru.melandra.basickotlin.UI.base.BaseViewModel

class SplashViewModel(val notesRepository: NotesRepository): BaseViewModel<Boolean?>() {

    fun requestUser() = launch {
        notesRepository.getCurrentUser()?.let {
            setData(true)
        } ?: let {
                setError(NoAuthException())
        }
    }
}