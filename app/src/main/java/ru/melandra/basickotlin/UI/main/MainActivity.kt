package ru.melandra.basickotlin.UI.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.R
import ru.melandra.basickotlin.UI.base.BaseActivity
import ru.melandra.basickotlin.UI.base.BaseViewModel
import ru.melandra.basickotlin.UI.note.NoteActivity
import ru.melandra.basickotlin.UI.splash.SplashActivity

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override val model: MainViewModel by viewModel()

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return MenuInflater(this).inflate(R.menu.main, menu).let { true }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.logout -> showLogoutDialog().let { true }
        else -> false
    }

    fun showLogoutDialog() {
        supportFragmentManager.findFragmentByTag(LogoutDialog.TAG) ?: LogoutDialog.createInstance {
            onLogout()
        }.show(supportFragmentManager, LogoutDialog.TAG)
    }

    fun onLogout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    startActivity(Intent(this, SplashActivity::class.java))
                    finish()
                }
    }
}