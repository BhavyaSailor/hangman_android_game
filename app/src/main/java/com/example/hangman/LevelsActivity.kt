package com.example.hangman

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat


class LevelsActivity : AppCompatActivity() {
    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, MusicManager::class.java))
    }
    override fun onResume() {
        super.onResume()
//        MusicManager.resumeMusic()
        loadLevelButtons() // refresh unlocked/locked levels every time you return

    }


    override fun onPause() {
        super.onPause()
        MusicManager.pauseMusic()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_levels)
        val backButton = findViewById<ImageButton>(R.id.btnBack)
        val settingsButton = findViewById<ImageButton>(R.id.btnSettings)
        val musicIntent = Intent(this, MusicManager::class.java)
        startService(musicIntent)
        backButton.setOnClickListener {
            val mode = intent.getStringExtra("mode")

            if (mode == "level") {
                // Go back to LevelsActivity
                val intent = Intent(this, LevelsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                // Go back to MenuActivity
                val intent = Intent(this, MenuActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

        }

// ⚙️ Handle Settings (optional)
        settingsButton.setOnClickListener {
            showSettingsDialog()
        }
loadLevelButtons()
    }


    private fun showSettingsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_settings, null)

        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.themeOptions)
        val rbLight = dialogView.findViewById<RadioButton>(R.id.rbLight)
        val rbDark = dialogView.findViewById<RadioButton>(R.id.rbDark)
        val rbSystem = dialogView.findViewById<RadioButton>(R.id.rbSystem)
        val volumeSeekBar = dialogView.findViewById<SeekBar>(R.id.volumeSeekBar)

        // --- Theme Pre-selection ---
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> rbDark.isChecked = true
            AppCompatDelegate.MODE_NIGHT_NO -> rbLight.isChecked = true
            else -> rbSystem.isChecked = true
        }

        // --- Music Volume Control ---
        val initialVolume = (MusicManager.currentVolume * 100).toInt()
        volumeSeekBar.progress = initialVolume

        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val volume = progress / 100f
                MusicManager.setVolume(volume)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // --- Show Dialog ---
        AlertDialog.Builder(this)
            .setTitle("Settings")
            .setView(dialogView)
            .setPositiveButton("Apply") { _, _ ->
                when (radioGroup.checkedRadioButtonId) {
                    R.id.rbLight -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    R.id.rbDark -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    R.id.rbSystem -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun loadLevelButtons() {
        val levelsLayout = findViewById<LinearLayout>(R.id.levelsLayout)
        levelsLayout.removeAllViews() // clear old buttons when coming back

        val buttonPerRow = 4
        var rowLayout: LinearLayout? = null
        val unlockedLevel = PreferenceManager.getUnlockedLevel(this)

        for ((index, wordEntry) in WordBank.words.withIndex()) {
            if (index % buttonPerRow == 0) {
                rowLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.bottomMargin = 12
                    layoutParams = params
                }
                levelsLayout.addView(rowLayout)
            }

            val levelNumber = wordEntry.level
            val button = Button(this).apply {
                text = levelNumber.toString()
                textSize = 18f
                background = ContextCompat.getDrawable(this@LevelsActivity, R.drawable.dialog_bg)
                setTextColor(ContextCompat.getColor(this@LevelsActivity, android.R.color.white))
                val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                params.marginEnd = 8
                layoutParams = params
            }

            val isUnlocked = PreferenceManager.isLevelUnlocked(this, levelNumber)
            val isCompleted = PreferenceManager.isLevelCompleted(this, levelNumber)

            button.isEnabled = isUnlocked
            button.alpha = if (isUnlocked) 1f else 0.5f

            if (isCompleted) {
                button.setBackgroundResource(R.drawable.dialog_bg)
            }

            button.setOnClickListener {
                if (isUnlocked) {
                    val intent = Intent(this@LevelsActivity, GameActivity::class.java)
                    intent.putExtra("source", "levels")
                    intent.putExtra("mode", "level")
                    intent.putExtra("level", levelNumber)
                    startActivity(intent)
                }
            }

            rowLayout?.addView(button)
        }
    }

}
