package ru.melandra.basickotlin.Data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.melandra.basickotlin.R
import java.util.*

@Parcelize
data class Note (
        val id: String,
        val title: String,
        val text: String,
        val color: Color = Color.WHITE,
        val lastChanged: Date = Date()
): Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this===other) return true
        if (javaClass != other?.javaClass) return false
        return (id ==(other as Note).id)
    }

    fun getIntColor() = when(color) {
        Color.WHITE -> R.color.white
        Color.RED -> R.color.red
        Color.GREEN -> R.color.green
        Color.BLUE -> R.color.blue
        Color.YELLOW -> R.color.yellow
    }

    enum class Color {
        WHITE,
        RED,
        GREEN,
        BLUE,
        YELLOW
    }
}
