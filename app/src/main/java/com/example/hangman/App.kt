package com.example.hangman

import android.app.Application
import android.app.Activity
import android.os.Bundle

class App : Application(), Application.ActivityLifecycleCallbacks {
    private var activityCount = 0

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        PreferenceManager.applySavedTheme(this)
    }

    override fun onActivityResumed(activity: Activity) {
        activityCount++
        if (activityCount == 1) {
            // App moved to foreground
            MusicManager.startMusic(activity.applicationContext, R.raw.bg_music)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        activityCount--
        if (activityCount == 0) {
            // App moved to background
            MusicManager.pauseMusic()
        }
    }

    // Unused but required methods
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
