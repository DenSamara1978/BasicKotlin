package ru.melandra.basickotlin.UI

import android.app.Application
import org.koin.android.ext.android.startKoin
import ru.melandra.basickotlin.Data.DI.appModule
import ru.melandra.basickotlin.Data.DI.mainModule
import ru.melandra.basickotlin.Data.DI.noteModule
import ru.melandra.basickotlin.Data.DI.splashModule

class OwnApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}