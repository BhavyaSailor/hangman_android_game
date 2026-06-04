package com.example.hangman

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.os.VibrationEffect

import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

class GameActivity : AppCompatActivity() {

    private lateinit var tvWord: TextView
    private lateinit var tvStatus: TextView
    private lateinit var keyboard: LinearLayout
    private lateinit var hangmanView: HangmanView
    private lateinit var wordToGuess: String
    private lateinit var displayedWord: CharArray
    private val guessedLetters = mutableSetOf<Char>()
    private lateinit var currentWordEntry: WordEntry
    private lateinit var konfettiView: KonfettiView
    private lateinit var btnHint: Button
    private var hintUsed = false
    private var wrongGuesses = 0
    private val maxWrongGuesses = 6
    private lateinit var selectedWord: String
    private lateinit var selectedCategory: String
    private lateinit var selectedDifficulty: String
    private var currentLevel: Int = 1


    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, MusicManager::class.java))

    }
    override fun onPause() {
        super.onPause()
        MusicManager.pauseMusic()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tvWord = findViewById(R.id.tvWord)
        tvStatus = findViewById(R.id.tvStatus)
        keyboard = findViewById(R.id.keyboard)
        hangmanView = findViewById(R.id.hangmanView)
        btnHint = findViewById(R.id.btnHint)
        konfettiView = findViewById(R.id.konfettiView)
        currentLevel = intent.getIntExtra("level", 1)


        val mode = intent.getStringExtra("mode")

        val selectedWordEntry = if (mode == "level") {
            val levelNumber = intent.getIntExtra("level", 1)
            WordBank.getWordByLevel(levelNumber)
        } else {
            WordBank.getRandomWordEntry()
        }

// ✅ Use all details from the WordEntry
        selectedWord = selectedWordEntry.word
        selectedCategory = selectedWordEntry.category
        selectedDifficulty = selectedWordEntry.difficulty

        btnHint.setOnClickListener {
            useHint()
        }

        val backButton = findViewById<ImageButton>(R.id.btnBack)
        val settingsButton = findViewById<ImageButton>(R.id.btnSettings)
        val levelTitle = findViewById<TextView>(R.id.tvLevelTitle)
//        val musicIntent = Intent(this, MusicManager::class.java)
//        startService(musicIntent)

        // Example after user completes a level
//        val currentLevel = intent.getIntExtra("level", 1)
//        PreferenceManager.saveLevelProgress(this, currentLevel, true)
//        PreferenceManager.unlockNextLevel(this, currentLevel)


// ✅ Display current level
        if (mode == "random") {
            levelTitle.text = "Random levels"

        } else {
        val levelNumber = intent.getIntExtra("level", 1)
        levelTitle.text = "Level $levelNumber"

        }

// 🔙 Handle Back
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

        startNewGame(selectedWordEntry)
        setupKeyboard()
        resetKeyboard()
        updateStatus()

    }

    private fun startNewGame(wordEntry: WordEntry) {
        wrongGuesses = 0
        currentWordEntry = wordEntry
        wordToGuess = wordEntry.word

        displayedWord = CharArray(wordToGuess.length) { i ->
            if (wordToGuess[i] == ' ') ' ' else '_'
        }

        guessedLetters.clear()
        updateWordDisplay()

        // ✅ Show category & difficulty properly
        findViewById<TextView>(R.id.tvCategory).text =
            "Category: ${wordEntry.category}"
        findViewById<TextView>(R.id.tvdifficulty).text =
            "Difficulty: ${wordEntry.difficulty}"

        updateStatus()
    }


    private fun setupKeyboard() {
        val rows = listOf("QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM")

        keyboard.removeAllViews()

        for (rowLetters in rows) {
            val rowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
            }

            for (letter in rowLetters) {
                val button = Button(this).apply {
                    text = letter.toString()
                    textSize = 18f
                    isAllCaps = false
                    setTextColor(ContextCompat.getColor(this@GameActivity, R.color.keyText))
                    setBackgroundResource(R.drawable.key_background)

                    setOnClickListener {
                        handleGuess(letter, this)
                        isEnabled = false
                    }
                }

                val params = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                ).apply {
                    setMargins(6, 6, 6, 6)
                }
                button.layoutParams = params

                rowLayout.addView(button)
            }

            keyboard.addView(rowLayout)
        }
    }


    private fun handleGuess(letter: Char, button: Button) {
        // Prevent repeated guesses
        Log.d("DEBUG", "DisplayedWord = ${String(displayedWord)}")

        if (guessedLetters.contains(letter)) return
        guessedLetters.add(letter)

        var correct = false

        // Check if the guessed letter is in the word
        for (i in wordToGuess.indices) {
            if (wordToGuess[i].equals(letter, ignoreCase = true)) {
                displayedWord[i] = wordToGuess[i] // reveal letter
                correct = true
            }
        }

        if (correct) {
            // ✅ correct guess
            button.background.setTint(ContextCompat.getColor(this, R.color.guessGreen))
            updateWordDisplay()

            // ✅ Win only when no underscores left
            if (!displayedWord.contains('_')) {
                showGameOver(true)
            }

        } else {
            // ❌ wrong guess
            button.background.setTint(ContextCompat.getColor(this, R.color.guessRed))
            wrongGuesses++
            updateHangman()
            updateStatus()
            vibrateOnWrongGuess()

            if (wrongGuesses >= maxWrongGuesses) {
                showGameOver(false)
            }
        }
    }
    private fun vibrateOnWrongGuess() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(150)
            }
        }
    }



    private fun updateWordDisplay() {
        val display = displayedWord.joinToString(" ")
        tvWord.text = display
    }


    private fun updateHangman() {
        hangmanView.setWrongGuesses(wrongGuesses)
    }

    private fun showGameOver(won: Boolean) {
        disableKeyboard()

        // Inflate your custom dialog layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_game_result, null)

        val title = dialogView.findViewById<TextView>(R.id.tvResultTitle)
        val btnHome = dialogView.findViewById<ImageButton>(R.id.btnHome)
        val btnBack = dialogView.findViewById<ImageButton>(R.id.btnBack)
        val btnExtra = dialogView.findViewById<ImageButton>(R.id.btnExtra)

        // Set the dialog text depending on win/lose
        title.text = if (won) "You Won!" else "Game Over!"

        if (won) {
            PreferenceManager.markLevelCompleted(this, currentLevel)
            PreferenceManager.unlockNextLevel(this, currentLevel)
            konfettiView.visibility = View.VISIBLE
            konfettiView.start(
                Party(
                    speed = 5f,
                    maxSpeed = 15f,
                    damping = 0.9f,
                    spread = 360,
                    angle = 270,
                    colors = listOf(
                        0xfce18a.toInt(),
                        0xff726d.toInt(),
                        0xf4306d.toInt(),
                        0xb48def.toInt()
                    ),
                    emitter = Emitter(duration = 2, TimeUnit.SECONDS).perSecond(30),
                    position = Position.Relative(0.5, 0.25)
                )
            )
        } else {
            konfettiView.visibility = View.GONE
        }

        // Build the dialog with your layout (no TransparentDialog)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Set button actions
        btnHome.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            dialog.dismiss()
        }

        val source = intent.getStringExtra("source")

        btnBack.setOnClickListener {
            if (source == "menu") {
                // Came from Menu → go back to Menu
                startActivity(Intent(this, MenuActivity::class.java))
            } else {
                // Came from Levels → go back to Levels
                startActivity(Intent(this, LevelsActivity::class.java))
            }
            finish()
        }

        if (won) {

            btnExtra.setImageResource(R.drawable.ic_next)
            btnExtra.setOnClickListener {
                val mode = intent.getStringExtra("mode")
                if (mode == "level") {
                    val levelNumber = intent.getIntExtra("level", 1)
                    val nextLevel = levelNumber + 1

                    if (nextLevel <= WordBank.words.size) {
                        val nextIntent = Intent(this, GameActivity::class.java)
                        nextIntent.putExtra("mode", "level")
                        nextIntent.putExtra("level", nextLevel)
                        nextIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(nextIntent)
                    } else {
                        val backIntent = Intent(this, LevelsActivity::class.java)
                        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(backIntent)
                    }
                } else {
                    // 🔹 RANDOM mode → get a new random word every time
                    val nextIntent = Intent(this, GameActivity::class.java)
                    nextIntent.putExtra("mode", "random")
                    nextIntent.putExtra("source", source)
                    nextIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(nextIntent)
                }
                finish()
                dialog.dismiss()
            }
        } else {
            btnExtra.setImageResource(R.drawable.ic_retry)
            btnExtra.setOnClickListener {
                val mode = intent.getStringExtra("mode")
                val retryIntent = Intent(this, GameActivity::class.java)
                retryIntent.putExtra("mode", mode)
                retryIntent.putExtra("level", intent.getIntExtra("level", 1))
                retryIntent.putExtra("source", source)
                retryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(retryIntent)
                finish()
                dialog.dismiss()
            }
        }
        dialog.show()
    }


    private fun useHint() {
        if (hintUsed) {
            Toast.makeText(this, "Hint already used!", Toast.LENGTH_SHORT).show()
            return
        }

        // Find unrevealed letters
        val unrevealedIndices = wordToGuess.indices.filter { displayedWord[it] == '_' }
        if (unrevealedIndices.isNotEmpty()) {
            val randomIndex = unrevealedIndices.random()
            val hintLetter = wordToGuess[randomIndex]

            // Reveal that letter (if appears multiple times)
            for (i in wordToGuess.indices) {
                if (wordToGuess[i] == hintLetter) {
                    displayedWord[i] = hintLetter
                }
            }

            // Update display
            updateWordDisplay()

            // Mark hint as used and disable button
            hintUsed = true
            btnHint.isEnabled = false

            // Penalize with 1 wrong guess
            wrongGuesses++
            updateHangman()
            updateStatus()

            // ✅ First check if the word is now complete (win condition)
            if (!displayedWord.contains('_')) {
                showGameOver(true)
                return
            }

            // ❌ Only lose if after using hint all chances are gone and word still incomplete
            if (wrongGuesses >= maxWrongGuesses && displayedWord.contains('_')) {
                showGameOver(false)
                return
            }
        }
    }

    private fun updateStatus() {
        tvStatus.text = "Wrong guesses: $wrongGuesses/$maxWrongGuesses"
    }

    private fun disableKeyboard() {
        for (i in 0 until keyboard.childCount) {
            val row = keyboard.getChildAt(i) as ViewGroup
            for (j in 0 until row.childCount) {
                val button = row.getChildAt(j) as Button
                button.isEnabled = false
            }
        }
    }
    @Suppress("MissingSuperCall")
    override fun onBackPressed() {
        super.onBackPressed()
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
//    private fun unlockNextLevel(currentLevel: Int) {
//        val nextLevel = currentLevel + 1
//        PreferenceManager.unlockLevel(this, nextLevel)
//    }
    private fun resetKeyboard() {
        for (i in 0 until keyboard.childCount) {
            val row = keyboard.getChildAt(i) as ViewGroup
            for (j in 0 until row.childCount) {
                val button = row.getChildAt(j) as Button
                button.isEnabled = true
                button.setBackgroundResource(R.drawable.key_background)

                // Reset tint to neutral (theme-based)
                val neutralColor = ContextCompat.getColor(this, R.color.keyDefault)
                button.background.setTint(neutralColor)

                // Reset text color to theme text
                val textColor = ContextCompat.getColor(this, R.color.keyText)
                button.setTextColor(textColor)
            }
        }
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