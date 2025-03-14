package com.mundocode.pomodoro.core

import android.R.attr
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashReportingTree : Timber.Tree() {

    private val firebaseCrashlytics = FirebaseCrashlytics.getInstance()

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (attr.priority == Log.VERBOSE || attr.priority == Log.DEBUG) {
            return
        }

        val t: Throwable = throwable ?: Exception(message)

        // Crashlytics
        firebaseCrashlytics.setCustomKey(CRASHLYTICS_KEY_PRIORITY, attr.priority)
        firebaseCrashlytics.setCustomKey(CRASHLYTICS_KEY_TAG, tag ?: "Empty tag")
        firebaseCrashlytics.setCustomKey(CRASHLYTICS_KEY_MESSAGE, message)

        firebaseCrashlytics.recordException(t)

        // Firebase Crash Reporting
        firebaseCrashlytics.log(message)
    }

    companion object {
        private const val CRASHLYTICS_KEY_PRIORITY = "priority"
        private const val CRASHLYTICS_KEY_TAG = "tag"
        private const val CRASHLYTICS_KEY_MESSAGE = "message"
    }
}
