package com.books.gamewepapp.screens.game.elements

import android.graphics.Canvas
import android.graphics.Rect
import com.books.gamewepapp.screens.game.views.CannonCustomView

class Cannonball(
    view: CannonCustomView, color: Int,
    x: Int, y: Int, radius: Int,
    private var velocityX: Float,
    velocityY: Float
) : GameElement(view, color, x, y, 2 * radius, (2 * radius), velocityY) {

    var isOnScreen = true
        private set

    private val radius: Int
        get() = (shape.right - shape.left) / 2

    fun collidesWith(element: GameElement): Boolean =
        Rect.intersects(shape, element.shape) && velocityX > 0

    fun reverseVelocityX() {
        velocityX *= -1f
    }

    override fun update(interval: Double) {
        super.update(interval)
        shape.offset((velocityX * interval).toInt(), 0)

        if (shape.top < 0 || shape.left < 0 || shape.bottom > view.screenHeight || shape.right > view.screenWidth)
            isOnScreen = false
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(
            (shape.left + radius).toFloat(),
            (shape.top + radius).toFloat(), radius.toFloat(), paint
        )
    }
}