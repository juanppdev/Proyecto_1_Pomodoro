package com.mundocode.pomodoro

import android.app.Application
import com.mundocode.pomodoro.core.CrashReportingTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PomodoroApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(
            if (BuildConfig.DEBUG) {
                Timber.DebugTree()
            } else {
                CrashReportingTree()
            },
        )
    }
}
