package ru.melandra.basickotlin.UI.main

import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.UI.base.BaseViewState

class MainViewState (val notes: List<Note>? = null, error: Throwable? = null): BaseViewState<List<Note>?>(notes, error)
