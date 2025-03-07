package br.com.generic.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Instancia da aplicação usada pelo Hilt
 */
@HiltAndroidApp
class App: Application()