package com.naufal.androidocrlearning

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


class Box(context: Context) : View(context) {
    private val paint = Paint()

    override fun onDraw(canvas: Canvas) { // Override the onDraw() Method
        super.onDraw(canvas)

        paint.style = Paint.Style.STROKE
        paint.color = Color.GREEN
        paint.strokeWidth = 10F

        //center
        val x0 = width / 2
        val y0 = height / 2
        val dx = height / 3
        val dy = height / 3
        //draw guide box
        canvas.drawRect((x0 - dx).toFloat(), (y0 - dy).toFloat(), (x0 + dx).toFloat(), (y0 + dy).toFloat(), paint)
    }
}