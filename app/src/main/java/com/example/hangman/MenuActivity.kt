package com.example.hangman

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate

class MenuActivity : AppCompatActivity() {

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, MusicManager::class.java))
    }
//    override fun onResume() {
//        super.onResume()
//        MusicManager.startMusic(this)
//    }

    override fun onPause() {
        super.onPause()
        MusicManager.pauseMusic()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val btnRandom = findViewById<Button>(R.id.btnRandom)
        val btnLevels = findViewById<Button>(R.id.btnLevels)
        val btnSettings = findViewById<Button>(R.id.btnSettings)
        val musicIntent = Intent(this, MusicManager::class.java)
        startService(musicIntent)

        val btnHowToPlay = findViewById<Button>(R.id.btnHowToPlay)
       btnHowToPlay.setOnClickListener{
           showHowToPlayDialog()
       }



        btnRandom.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("source", "menu")
            intent.putExtra("mode", "random")
            startActivity(intent)
        }

        btnLevels.setOnClickListener {
            startActivity(Intent(this, LevelsActivity::class.java))
        }

        btnSettings.setOnClickListener {
            showSettingsDialog()
        }

    }

    private fun showHowToPlayDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_how_to_play, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.show()

        // Match Settings dialog width
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.85).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val tvContent = dialogView.findViewById<TextView>(R.id.tvinst)
        val btnClose = dialogView.findViewById<Button>(R.id.btnClosehtp)

        tvContent.text = """
🎯 Objective:
Guess the hidden word before you run out of allowed wrong guesses. 
Each incorrect letter — or using a hint — counts as a wrong attempt and brings you closer to losing the round.

🕹️ How to Play:
1. A hidden word appears as blank spaces (e.g., _ _ _ _ _).  
2. Tap any letter from A–Z to make your guess.  
3. Correct letters will fill their respective positions in the word.  
4. Each wrong guess increases your wrong guess count by one.  
5. If your wrong guesses reach the limit (6 chances), the game ends.  
6. If you reveal the entire word correctly, you win the level.

💡 Hints System:
• Tap the "Hint" button to reveal one random correct letter in the word.  
• Using a hint adds +1 to your wrong guess count, just like a wrong letter.  
• Use hints wisely — they can help, but come at a cost.

⚙️ Game Progress:
• Each level features a new word to guess.  
• The wrong guess limit is fixed at 6 chances.  
• Completing a level unlocks the next one.  
• Try to win with fewer hints and fewer mistakes for a higher score.

🎵 Additional Features:
• You can pause or reset the game at any time.  
• Music and sound settings can be adjusted in the Settings menu.  
• Supports both Light and Dark themes for a comfortable experience.

🧠 Get Ready:
Challenge your logic and vocabulary skills in the ultimate word-guessing experience — Mind Bender: Hangman Edition.
""".trimIndent()


        btnClose.setOnClickListener { dialog.dismiss() }
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

}
