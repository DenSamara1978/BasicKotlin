package ru.melandra.basickotlin.UI.splash

import org.koin.android.viewmodel.ext.android.viewModel
import ru.melandra.basickotlin.UI.base.BaseActivity
import ru.melandra.basickotlin.UI.main.MainActivity

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {

    override val model: SplashViewModel by viewModel()
    override val layoutRes = null

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let { startMainActivity() }
    }

    override fun onResume() {
        super.onResume()
        model.requestUser()
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }
}