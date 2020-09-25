package ru.melandra.basickotlin.UI.note

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_note.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.R
import ru.melandra.basickotlin.UI.base.BaseActivity
import java.util.*

class NoteActivity: BaseActivity<NoteViewState.Data>() {

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
        private val DATE_FORMAT = "dd.MM.yyyy HH:mm"

        fun start(context: Context, noteId: String? = null) = Intent(context, NoteActivity::class.java).run {
            noteId?.let { putExtra(EXTRA_NOTE, noteId)}
            context.startActivity(this)
        }
    }

    private var note: Note? = null
    override val model: NoteViewModel by viewModel()
    override val layoutRes = R.layout.activity_note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        noteId?.let { id -> model.loadNote(id) }
        initViews()
    }

    private fun initViews() {
        et_title.removeTextChangedListener(textChangeListener)
        et_body.removeTextChangedListener(textChangeListener)

        note?.let {note ->
            et_title.setText(note.title)
            et_body.setText(note.text)
            toolbar_note.setBackgroundColor(note.getIntColor())
        } ?: let {
            supportActionBar?.title = getString(R.string.new_note_title)
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

        note?.let { model.save(it) }
    }

    val textChangeListener = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            saveNote()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?) =
        menuInflater.inflate(R.menu.note, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> onBackPressed().let { true }
            R.id.delete_itm -> deleteNote().let { true }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun renderData(data: NoteViewState.Data) {
        if (!data.isDeleted) {
            this.note = data.note
            initViews()
        } else
            finish()

    }

    fun deleteNote() {
        AlertDialog.Builder(this)
                .setMessage(getString(R.string.note_delete_message))
                .setPositiveButton(R.string.note_delete_ok) { dialog, which -> model.deleteNote() }
                .setNegativeButton(R.string.note_delete_cancel) { dialog, which -> dialog.dismiss() }
                .show()
    }
}
