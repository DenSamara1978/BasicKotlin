package ru.melandra.basickotlin.UI.splash

import ru.melandra.basickotlin.UI.base.BaseViewState

class SplashViewState(authenticated: Boolean? = null, error: Throwable? = null):
        BaseViewState<Boolean?>(authenticated, error)
