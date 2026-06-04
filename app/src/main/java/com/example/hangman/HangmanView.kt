//package com.example.hangman
//
//import android.content.Context
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.Paint
//import android.util.AttributeSet
//import android.view.View
//
//class HangmanView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
//    private var wrongGuesses = 0
//    private val paint = Paint().apply {
//        color = Color.BLACK
//        strokeWidth = 8f
//        style = Paint.Style.STROKE
//        isAntiAlias = true
//    }
//
//    fun setWrongGuesses(count: Int) {
//        wrongGuesses = count
//        invalidate() // redraw
//    }
//    private fun updateHangman() {
//        val hangmanView = findViewById<HangmanView>(R.id.hangmanView)
//        hangmanView.setWrongGuesses(wrongGuesses)
//    }
//    fun reset() {
//        wrongGuesses = 0
//        invalidate() // redraws the canvas clean
//    }
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        val w = width.toFloat()
//        val h = height.toFloat()
//
//        // Gallows
//        canvas.drawLine(w * 0.2f, h * 0.9f, w * 0.8f, h * 0.9f, paint) // base
//        canvas.drawLine(w * 0.3f, h * 0.9f, w * 0.3f, h * 0.1f, paint) // pole
//        canvas.drawLine(w * 0.3f, h * 0.1f, w * 0.6f, h * 0.1f, paint) // top beam
//        canvas.drawLine(w * 0.6f, h * 0.1f, w * 0.6f, h * 0.2f, paint) // rope
//
//        // Stick figure (progressive)
//        if (wrongGuesses > 0) {
//            // Head
//            canvas.drawCircle(w * 0.6f, h * 0.28f, w * 0.08f, paint)
//        }
//        if (wrongGuesses > 1) {
//            // Body
//            canvas.drawLine(w * 0.6f, h * 0.36f, w * 0.6f, h * 0.6f, paint)
//        }
//        if (wrongGuesses > 2) {
//            // Left arm
//            canvas.drawLine(w * 0.6f, h * 0.4f, w * 0.5f, h * 0.5f, paint)
//        }
//        if (wrongGuesses > 3) {
//            // Right arm
//            canvas.drawLine(w * 0.6f, h * 0.4f, w * 0.7f, h * 0.5f, paint)
//        }
//        if (wrongGuesses > 4) {
//            // Left leg
//            canvas.drawLine(w * 0.6f, h * 0.6f, w * 0.5f, h * 0.75f, paint)
//        }
//        if (wrongGuesses > 5) {
//            // Right leg
//            canvas.drawLine(w * 0.6f, h * 0.6f, w * 0.7f, h * 0.75f, paint)
//        }
//    }
//}

package com.example.hangman

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class HangmanView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var wrongGuesses = 0
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 8f
        isAntiAlias = true
    }

    init {
        updatePaintColor()
    }

    private fun updatePaintColor() {
        val color = ContextCompat.getColor(context, R.color.hangmanLineColor)
        paint.color = color
    }

    fun setWrongGuesses(count: Int) {
        wrongGuesses = count
        invalidate()
    }

    fun reset() {
        wrongGuesses = 0
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // ✅ Dynamically get color depending on current theme
        val currentTextColor = ContextCompat.getColor(context, R.color.hangmanLineColor)
        paint.color = currentTextColor

        val w = width.toFloat()
        val h = height.toFloat()

        // Gallows
        canvas.drawLine(w * 0.2f, h * 0.9f, w * 0.8f, h * 0.9f, paint) // base
        canvas.drawLine(w * 0.3f, h * 0.9f, w * 0.3f, h * 0.1f, paint) // pole
        canvas.drawLine(w * 0.3f, h * 0.1f, w * 0.6f, h * 0.1f, paint) // top beam
        canvas.drawLine(w * 0.6f, h * 0.1f, w * 0.6f, h * 0.2f, paint) // rope

        // Stick figure
        if (wrongGuesses > 0)
            canvas.drawCircle(w * 0.6f, h * 0.28f, w * 0.08f, paint) // head
        if (wrongGuesses > 1)
            canvas.drawLine(w * 0.6f, h * 0.36f, w * 0.6f, h * 0.6f, paint) // body
        if (wrongGuesses > 2)
            canvas.drawLine(w * 0.6f, h * 0.4f, w * 0.5f, h * 0.5f, paint) // left arm
        if (wrongGuesses > 3)
            canvas.drawLine(w * 0.6f, h * 0.4f, w * 0.7f, h * 0.5f, paint) // right arm
        if (wrongGuesses > 4)
            canvas.drawLine(w * 0.6f, h * 0.6f, w * 0.5f, h * 0.75f, paint) // left leg
        if (wrongGuesses > 5)
            canvas.drawLine(w * 0.6f, h * 0.6f, w * 0.7f, h * 0.75f, paint) // right leg
    }
}
