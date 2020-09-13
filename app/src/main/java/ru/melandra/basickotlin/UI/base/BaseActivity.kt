package ru.melandra.basickotlin.UI.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import ru.melandra.basickotlin.Data.NoAuthException
import ru.melandra.basickotlin.R

abstract class BaseActivity<T, S: BaseViewState<T>>: AppCompatActivity() {

    companion object {
        const val RC_SIGN_IN = 4242
    }
    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let { setContentView(it) }

        viewModel.getViewState().observe(this, Observer { state ->
            state ?: return@Observer
            state.error?.let { error ->
                renderError(error)
                return@Observer
            }
            renderData(state.data)
        })
    }

    abstract fun renderData(data: T)

    protected fun renderError(error: Throwable?) {
        when(error) {
            is NoAuthException -> startLogin()
            else -> error?.message?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startLogin() {
        val providers = listOf( AuthUI.IdpConfig.GoogleBuilder().build())
        val intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.common_google_signin_btn_icon_dark)
                .setTheme(R.style.LoginTheme)
                .setAvailableProviders(providers)
                .build()

        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ((requestCode == RC_SIGN_IN) && (resultCode != Activity.RESULT_OK))
            finish()
        super.onActivityResult(requestCode, resultCode, data)
    }
}