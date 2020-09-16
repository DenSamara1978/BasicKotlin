package ru.melandra.basickotlin.UI.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.R
import ru.melandra.basickotlin.UI.base.BaseActivity
import ru.melandra.basickotlin.UI.base.BaseViewModel
import ru.melandra.basickotlin.UI.note.NoteActivity

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    override val viewModel: BaseViewModel<List<Note>?, MainViewState> by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override val layoutRes = R.layout.activity_main

    lateinit var adapter: NotesRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_notes.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesRecyclerViewAdapter { note -> NoteActivity.start(this, note.id)}
        rv_notes.adapter = adapter

        fab.setOnClickListener { NoteActivity.start(this) }
    }

    override fun renderData(data: List<Note>?) {
        data?.let { adapter.notes = it }
    }
}