package ru.melandra.basickotlin.Data

object NotesRepository {

    var notes: List<Note> = listOf(
            Note("First note", "Text of the first note", 0xFFFF0000.toInt()),
            Note("Second note", "Text of the second note", 0xFF00FF00.toInt()),
            Note("Third note", "Text of the third note", 0xFF0000FF.toInt()),
            Note("Fourth note", "Text of the fourth note", 0xFFFFFF00.toInt())
    )
        get() = field
        set(value) {}
}
