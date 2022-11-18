package com.dewan.todoapp.util.log

import android.util.Log
import com.dewan.todoapp.model.local.AppPreferences
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class ReleaseTree(private val appPreferences: AppPreferences): Timber.Tree() {

    companion object {
        const val Priority = "priority"
        const val Tag = "tag"
    }

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return !(priority == Log.INFO || priority == Log.VERBOSE || priority == Log.DEBUG)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

        FirebaseCrashlytics.getInstance().also {
            it.setCustomKey(Priority, priority)
            tag?.let { _ -> it.setCustomKey(Tag, tag) }
            it.log(message)
            t?.let { e -> it.recordException(e) }
            appPreferences.getUserName()?.let {userId ->
                it.setUserId(userId)
            }
        }.sendUnsentReports()
    }
}