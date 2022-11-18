package com.dewan.todoapp

import android.app.Application
import android.content.Context
import com.dewan.todoapp.model.local.AppPreferences
import com.dewan.todoapp.util.log.DebugTree
import com.dewan.todoapp.util.log.ReleaseTree
import timber.log.Timber

class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setup()
    }

    private fun setup() {
        val sharesPreferences =
            this.getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE)
        val appPreferences = AppPreferences(sharesPreferences)

        if (BuildConfig.DEBUG) Timber.plant(DebugTree(appPreferences)) else Timber.plant(ReleaseTree(appPreferences))
    }
}