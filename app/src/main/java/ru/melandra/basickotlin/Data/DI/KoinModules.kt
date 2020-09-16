package ru.melandra.basickotlin.Data.DI

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.melandra.basickotlin.Data.NotesRepository
import ru.melandra.basickotlin.Data.Provider.FirestoreDataProvider
import ru.melandra.basickotlin.Data.Provider.RemoteDataProvider
import ru.melandra.basickotlin.UI.main.MainViewModel
import ru.melandra.basickotlin.UI.note.NoteViewModel
import ru.melandra.basickotlin.UI.splash.SplashViewModel

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirestoreDataProvider(get(), get())} bind RemoteDataProvider::class
    single { NotesRepository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}
