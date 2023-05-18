package com.books.gamewepapp.screens.game.elements

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.books.gamewepapp.screens.game.views.CannonCustomView

open class GameElement(
    view: CannonCustomView,
    color: Int,
    x: Int, y: Int,
    width: Int, length: Int,
    velocityY: Float
) {
    var view: CannonCustomView
    var paint = Paint()
    var shape: Rect
    private var velocityY: Float

    init {
        this.view = view
        paint.color = color
        shape = Rect(x, y, x + width, y + length)
        this.velocityY = velocityY
    }

    open fun update(interval: Double) {
        shape.offset(0, (velocityY * interval).toInt())

        if (shape.top < 0 && velocityY < 0 ||
            shape.bottom > view.screenHeight && velocityY > 0
        ) velocityY *= -1f
    }

    open fun draw(canvas: Canvas) {
        canvas.drawRect(shape, paint)
    }
}