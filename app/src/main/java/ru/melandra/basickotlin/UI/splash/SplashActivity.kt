package ru.melandra.basickotlin.UI.splash

import androidx.lifecycle.ViewModelProviders
import ru.melandra.basickotlin.UI.base.BaseActivity
import ru.melandra.basickotlin.UI.main.MainActivity

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {

    override val viewModel: SplashViewModel by lazy {
        ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    override val layoutRes = null

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let { startMainActivity() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestUser()
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }
}