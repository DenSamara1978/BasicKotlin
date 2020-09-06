package ru.melandra.basickotlin.UI.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_item.view.*
import ru.melandra.basickotlin.Data.Note
import ru.melandra.basickotlin.R

class NotesRecyclerViewAdapter: RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value;
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(notes[position])

    override fun getItemCount() = notes.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(note: Note) = with(itemView) {
            with(note) {
                tv_title.text = title
                tv_text.text = text
                setBackgroundColor(color)
            }
        }
    }
}