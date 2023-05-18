package com.books.gamewepapp.screens.game.elements

import com.books.gamewepapp.screens.game.views.CannonCustomView

class Target(
    view: CannonCustomView, color: Int,
    val hitReward: Int,
    x: Int, y: Int,
    width: Int, length: Int,
    velocityY: Float
) : GameElement(view, color, x, y, width, length, velocityY)