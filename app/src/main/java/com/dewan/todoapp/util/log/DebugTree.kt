package com.dewan.todoapp.util.log

import com.dewan.todoapp.model.local.AppPreferences
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class DebugTree(private val appPreferences: AppPreferences): Timber.DebugTree() {
    companion object {
        const val Priority = "priority"
        const val Tag = "tag"
    }

    override fun createStackElementTag(element: StackTraceElement): String? =
        "(${element.fileName}:${element.lineNumber}) ${element.methodName}"



}