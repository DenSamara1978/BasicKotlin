package ru.melandra.basickotlin.UI.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.melandra.basickotlin.R

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    val adapter: NotesRecyclerViewAdapter = NotesRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        rv_notes.layoutManager = GridLayoutManager(this, 2)
        rv_notes.adapter = adapter

        viewModel.viewState().observe(this, Observer { state ->
            state?.let { state ->
                adapter.notes = state.notes
            }
        })
    }
}