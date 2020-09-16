package ru.melandra.basickotlin.UI.note

import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.UI.base.BaseViewState

class NoteViewState(noteData: Data = Data(), error: Throwable? = null) : BaseViewState<NoteViewState.Data>(noteData, error) {
    data class Data(val isDeleted: Boolean = false, val note: Note? = null)
}
