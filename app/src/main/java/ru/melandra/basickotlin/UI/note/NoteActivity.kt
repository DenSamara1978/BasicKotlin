package ru.melandra.basickotlin.UI.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_note.*
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.R
import ru.melandra.basickotlin.UI.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*
import ru.melandra.basickotlin.UI.note.NoteViewModel as NoteViewModel

class NoteActivity: BaseActivity<Note?, NoteViewState>() {

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
        private val DATE_FORMAT = "dd.MM.yyyy HH:mm"

        fun start(context: Context, noteId: String? = null) = Intent(context, NoteActivity::class.java).run {
            noteId?.let { putExtra(EXTRA_NOTE, noteId)}
            context.startActivity(this)
        }
    }

    private var note: Note? = null
    override val viewModel: NoteViewModel by lazy {
        ViewModelProviders.of(this).get(NoteViewModel::class.java)
    }
    override val layoutRes = R.layout.activity_note

    override fun renderData(data: Note?) {
        this.note = data
        supportActionBar?.title = note?.let { note ->
            SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(note.lastChanged)
        } ?: let {
            getString(R.string.new_note_title)
        }
        initViews()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        noteId?.let { id ->
            viewModel.loadNote(id)
        } ?: let {
            supportActionBar?.title = getString(R.string.new_note_title)
        }

        initViews()
    }

    private fun initViews() {
        et_title.removeTextChangedListener(textChangeListener)
        et_body.removeTextChangedListener(textChangeListener)

        note?.let {note ->
            et_title.setText(note.title)
            et_body.setText(note.text)
            toolbar_note.setBackgroundColor(note.getIntColor())
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> { onBackPressed(); true}
            else -> return super.onOptionsItemSelected(item)
        }
    }
}