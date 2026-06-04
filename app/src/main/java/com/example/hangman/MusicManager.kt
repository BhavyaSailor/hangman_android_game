package com.example.hangman

import android.content.Context
import android.media.MediaPlayer

object MusicManager {
    private var mediaPlayer: MediaPlayer? = null
    var currentVolume: Float = 0.4f
        private set

    fun startMusic(context: Context, musicResId: Int) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, musicResId)
            mediaPlayer?.isLooping = true
            mediaPlayer?.setVolume(currentVolume, currentVolume)
        }
        if (!(mediaPlayer?.isPlaying ?: false)) {
            mediaPlayer?.start()
        }
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
    }
    fun resumeMusic() {
        mediaPlayer?.start()
    }

    fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun setVolume(volume: Float) {
        currentVolume = volume
        mediaPlayer?.setVolume(volume, volume)
    }
}
