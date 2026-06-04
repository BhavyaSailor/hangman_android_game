package com.example.hangman

data class GameState(
    val word: String,
    var guessedLetters: MutableSet<Char> = mutableSetOf(),
    var wrongGuesses: Int = 0,
    val maxWrong: Int = 6
) {
    fun getMaskedWord(): String {
        return word.map { c ->
            if (guessedLetters.contains(c.lowercaseChar())) c else '_'
        }.joinToString(" ")
    }

    fun guess(letter: Char): GuessResult {
        val l = letter.lowercaseChar()
        if (guessedLetters.contains(l)) return GuessResult.AlreadyGuessed
        guessedLetters.add(l)

        return if (word.lowercase().contains(l)) {
            if (isWon()) GuessResult.Win else GuessResult.Correct
        } else {
            wrongGuesses++
            if (wrongGuesses >= maxWrong) GuessResult.Lose else GuessResult.Incorrect
        }
    }

    fun isWon(): Boolean = word.lowercase().all { it == ' ' || guessedLetters.contains(it) }
    fun isLost(): Boolean = wrongGuesses >= maxWrong
}

sealed class GuessResult {
    object Correct : GuessResult()
    object Incorrect : GuessResult()
    object AlreadyGuessed : GuessResult()
    object Win : GuessResult()
    object Lose : GuessResult()
}
