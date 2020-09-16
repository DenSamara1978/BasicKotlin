package ru.melandra.basickotlin.UI.note

import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.UI.base.BaseViewState

class NoteViewState(val note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)
