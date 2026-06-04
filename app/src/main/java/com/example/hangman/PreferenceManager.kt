package com.example.hangman

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object PreferenceManager {

    private const val PREF_NAME = "HangmanPrefs"
    private const val KEY_THEME = "theme"
    private const val KEY_VOLUME = "music_volume"
    private const val KEY_UNLOCKED_LEVEL = "unlocked_level"

    // --- Theme ---
    fun saveTheme(context: Context, mode: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_THEME, mode).apply()
    }

    fun applySavedTheme(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedMode = prefs.getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(savedMode)
    }

    // --- Volume ---
    fun saveVolume(context: Context, volume: Float) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putFloat(KEY_VOLUME, volume).apply()
    }

    fun getSavedVolume(context: Context): Float {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getFloat(KEY_VOLUME, 1.0f)
    }
    fun hasUsedHint(context: Context, level: Int): Boolean {
        val prefs = context.getSharedPreferences("HangmanPrefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("HintUsed_Level_$level", false)
    }

    fun markHintUsed(context: Context, level: Int) {
        val prefs = context.getSharedPreferences("HangmanPrefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("HintUsed_Level_$level", true).apply()
    }

    // --- Level Progress ---
    fun markLevelCompleted(context: Context, level: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("level_${level}_completed", true).apply()
    }

    fun isLevelCompleted(context: Context, level: Int): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("level_${level}_completed", false)
    }

    fun unlockNextLevel(context: Context, currentLevel: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val currentUnlocked = prefs.getInt(KEY_UNLOCKED_LEVEL, 1)
        if (currentLevel >= currentUnlocked) {
            prefs.edit().putInt(KEY_UNLOCKED_LEVEL, currentLevel + 1).apply()
        }
    }

    fun getUnlockedLevel(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_UNLOCKED_LEVEL, 1)
    }

    fun isLevelUnlocked(context: Context, level: Int): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val highestUnlocked = prefs.getInt(KEY_UNLOCKED_LEVEL, 1)
        return level <= highestUnlocked
    }
}
