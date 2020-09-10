package ru.melandra.basickotlin.UI.note

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_note.*
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.Data.NotesRepository.saveNote
import ru.melandra.basickotlin.R
import java.text.SimpleDateFormat
import java.util.*
import ru.melandra.basickotlin.UI.note.NoteViewModel as NoteViewModel

class NoteActivity : AppCompatActivity() {

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
        private val DATE_FORMAT = "dd.MM.yyyy HH:mm"

        fun start(context: Context, note: Note? = null) = Intent(context, NoteActivity::class.java).run {
            note?.let { putExtra(EXTRA_NOTE, note)}
            context.startActivity(this)
        }
    }

    private var note: Note? = null
    lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        note = intent.getParcelableExtra(EXTRA_NOTE)
        viewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)

        initViews()
    }

    private fun initViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = note?.let { note ->
            ""
        } ?: let {
            getString(R.string.new_note_title)
        }

        et_title.removeTextChangedListener(textChangeListener)
        et_body.removeTextChangedListener(textChangeListener)

        note?.let {note ->
            et_title.setText(note.title)
            et_body.setText(note.text)
            toolbar_note.setBackgroundColor(note.getColor())
        }

        et_title.addTextChangedListener(textChangeListener)
        et_body.addTextChangedListener(textChangeListener)
    }

    private fun saveNote (){
        if ((et_title.text == null) || (et_title.text!!.isEmpty())) return

        val title = et_title.text.toString()
        val text = et_body.text.toString()

        note = note?.copy(
                title = title,
                text = text,
                lastChanged = Date()
        ) ?: Note(UUID.randomUUID().toString(), title, text)

        note?.let { viewModel.save(it) }
    }

    val textChangeListener = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            saveNote()
        }

    }
}