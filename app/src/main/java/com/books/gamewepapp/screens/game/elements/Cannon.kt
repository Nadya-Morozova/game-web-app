package com.books.gamewepapp.screens.game.elements

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import com.books.gamewepapp.screens.game.views.CannonCustomView
import kotlin.math.cos
import kotlin.math.sin

class Cannon(
    view: CannonCustomView,
    baseRadius: Int,
    barrelLength: Int, barrelWidth: Int
) {
    private val baseRadius: Int
    private val barrelLength: Int
    private val barrelEnd = Point()
    private var barrelAngle = 0.0
    var cannonball: Cannonball? = null
        private set
    private val paint = Paint()
    private val view: CannonCustomView

    init {
        this.view = view
        this.baseRadius = baseRadius
        this.barrelLength = barrelLength
        paint.strokeWidth = barrelWidth.toFloat()
        paint.color = Color.BLACK
        align(Math.PI / 2)
    }

    // aligns the Cannon's barrel to the given angle
    fun align(barrelAngle: Double) {
        this.barrelAngle = barrelAngle
        barrelEnd.x = (barrelLength * sin(barrelAngle)).toInt()
        barrelEnd.y = (-barrelLength * cos(barrelAngle)).toInt() +
                view.screenHeight / 2
    }

    fun fireCannonball() {
        val velocityX = (CannonCustomView.CANNONBALL_SPEED_PERCENT *
                view.screenHeight * sin(barrelAngle)).toInt()

        val velocityY = (CannonCustomView.CANNONBALL_SPEED_PERCENT *
                view.screenHeight * -cos(barrelAngle)).toInt()

        val radius = (view.screenHeight *
                CannonCustomView.CANNONBALL_RADIUS_PERCENT).toInt()

        cannonball = Cannonball(
            view,
            Color.BLACK,
            -radius,
            view.screenHeight / 2 - radius,
            radius,
            velocityX.toFloat(),
            velocityY.toFloat()
        )
    }

    fun draw(canvas: Canvas) {
        canvas.drawLine(
            0f, (view.screenHeight / 2).toFloat(), barrelEnd.x.toFloat(),
            barrelEnd.y.toFloat(), paint
        )

        canvas.drawCircle(
            0f, (view.screenHeight / 2).toFloat(),
            baseRadius.toFloat(), paint
        )
    }

    fun removeCannonball() {
        cannonball = null
    }
}