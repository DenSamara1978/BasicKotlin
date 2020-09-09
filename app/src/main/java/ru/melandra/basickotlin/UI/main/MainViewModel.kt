package ru.melandra.basickotlin.UI.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.melandra.basickotlin.Data.NotesRepository

class MainViewModel: ViewModel() {
    private val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        NotesRepository.getNotes().observeForever { notes ->
            notes?.let { notes ->
                viewStateLiveData.value = viewStateLiveData.value?.copy(notes=notes) ?: MainViewState(notes)
            }
        }
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData
}