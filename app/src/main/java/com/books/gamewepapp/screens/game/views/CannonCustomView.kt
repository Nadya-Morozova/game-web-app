package com.books.gamewepapp.screens.game.views

import android.app.Activity
import android.app.DialogFragment
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.books.gamewepapp.R
import com.books.gamewepapp.screens.game.elements.Blocker
import com.books.gamewepapp.screens.game.elements.Cannon
import com.books.gamewepapp.screens.game.elements.Target
import java.util.*
import kotlin.math.atan2


class CannonCustomView(
    context: Context, attrs: AttributeSet?
) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    private var cannonThread: CannonThread? = null
    private val activity: Activity
     var dialogIsDisplayed = false
    private var cannon: Cannon? = null
    private var blocker: Blocker? = null
    private var targets: ArrayList<Target>? = null

    var screenWidth = 0
        private set

    var screenHeight = 0
        private set

    private var gameOver = false
    private var timeLeft = 0.0
    private var shotsFired = 0
    private var totalElapsedTime = 0.0
    private val textPaint: Paint
    private val backgroundPaint: Paint

    init {
        activity = context as Activity
        holder.addCallback(this)
        textPaint = Paint()
        backgroundPaint = Paint()
        backgroundPaint.color = Color.WHITE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenWidth = w
        screenHeight = h

        textPaint.textSize = (TEXT_SIZE_PERCENT * screenHeight).toInt().toFloat()
        textPaint.isAntiAlias = true
    }

    fun newGame() {
        cannon = Cannon(
            this,
            (CANNON_BASE_RADIUS_PERCENT * screenHeight).toInt(),
            (CANNON_BARREL_LENGTH_PERCENT * screenWidth).toInt(),
            (CANNON_BARREL_WIDTH_PERCENT * screenHeight).toInt()
        )
        val random = Random()
        targets = ArrayList()

        var targetX = (TARGET_FIRST_X_PERCENT * screenWidth).toInt()
        val targetY = ((0.5 - TARGET_LENGTH_PERCENT / 2) * screenHeight).toInt()

        var n = 0
        while (n < TARGET_PIECES) {
            var velocity = screenHeight * (random.nextDouble() *
                    (TARGET_MAX_SPEED_PERCENT - TARGET_MIN_SPEED_PERCENT) +
                    TARGET_MIN_SPEED_PERCENT)

            val color =
                if (n % 2 == 0) context.getColor(R.color.dark) else context.getColor(R.color.light)
            velocity *= -1.0

            targets?.add(
                Target(
                    this,
                    color,
                    HIT_REWARD,
                    targetX,
                    targetY,
                    (TARGET_WIDTH_PERCENT * screenWidth).toInt(),
                    (TARGET_LENGTH_PERCENT * screenHeight).toInt(),
                    velocity.toFloat()
                )
            )

            targetX += ((TARGET_WIDTH_PERCENT + TARGET_SPACING_PERCENT) *
                    screenWidth).toInt()
            n++
        }

        blocker = Blocker(
            this,
            Color.BLACK,
            MISS_PENALTY,
            (BLOCKER_X_PERCENT * screenWidth).toInt(),
            ((0.5 - BLOCKER_LENGTH_PERCENT / 2) * screenHeight).toInt(),
            (BLOCKER_WIDTH_PERCENT * screenWidth).toInt(),
            (BLOCKER_LENGTH_PERCENT * screenHeight).toInt(),
            (BLOCKER_SPEED_PERCENT * screenHeight).toFloat()
        )
        timeLeft = 10.0
        shotsFired = 0
        totalElapsedTime = 0.0
        if (gameOver) {
            gameOver = false
            cannonThread = CannonThread(holder)
            cannonThread!!.start()
        }
        hideSystemBars()
    }

    private fun updatePositions(elapsedTimeMS: Double) {
        val interval = elapsedTimeMS / 1000.0
        if (cannon != null) cannon?.cannonball?.update(interval)
        blocker?.update(interval)
        for (target in targets!!) target.update(interval)
        timeLeft -= interval

        if (timeLeft <= 0) {
            timeLeft = 0.0
            gameOver = true
            cannonThread!!.setRunning(false)
            showGameOverDialog(R.string.lose)
        }

        if (targets!!.isEmpty()) {
            cannonThread!!.setRunning(false)
            showGameOverDialog(R.string.win)
            gameOver = true
        }
    }

    fun alignAndFireCannonball(event: MotionEvent) {
        val touchPoint = Point(event.x.toInt(), event.y.toInt())
        val centerMinusY = (screenHeight / 2 - touchPoint.y).toDouble()
        val angle = atan2(touchPoint.x.toDouble(), centerMinusY)

        cannon?.align(angle)

        if (cannon?.cannonball == null || !cannon?.cannonball?.isOnScreen!!) {
            cannon?.fireCannonball()
            ++shotsFired
        }
    }

    private fun showGameOverDialog(messageId: Int) {
        val gameResult: DialogMessage =
            DialogMessage.newInstance(messageId, shotsFired, totalElapsedTime)

        gameResult.initializeCannonCustomView(this)

        activity.runOnUiThread {
            showSystemBars()
            dialogIsDisplayed = true
            gameResult.isCancelable = false
            gameResult.show(activity.fragmentManager, "results")
        }
    }

    fun drawGameElements(canvas: Canvas?) {
        canvas!!.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), backgroundPaint)
        canvas.drawText(
            resources.getString(R.string.time_remaining_format, timeLeft),
            50f,
            100f,
            textPaint
        )
        cannon?.draw(canvas)

        if (cannon?.cannonball != null && cannon?.cannonball?.isOnScreen == true)
            cannon?.cannonball?.draw(canvas)
        blocker?.draw(canvas)

        for (target in targets!!) target.draw(canvas)
    }

    fun testForCollisions() {
        if (cannon?.cannonball != null && cannon?.cannonball?.isOnScreen == true) {
            var n = 0
            while (n < targets!!.size) {
                if (cannon?.cannonball?.collidesWith(targets!![n]) == true) {
                    timeLeft += targets!![n].hitReward
                    cannon?.removeCannonball()
                    targets!!.removeAt(n)
                    --n
                    break
                }
                n++
            }
        } else {
            cannon?.removeCannonball()
        }

        if (cannon?.cannonball != null && cannon?.cannonball?.collidesWith(blocker!!) == true) {
            cannon?.cannonball?.reverseVelocityX()

            timeLeft -= blocker?.missPenalty ?: 0
        }
    }

    fun stopGame() {
        if (cannonThread != null) cannonThread?.setRunning(false)
    }

    override fun surfaceChanged(
        holder: SurfaceHolder, format: Int,
        width: Int, height: Int
    ) {}

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (!dialogIsDisplayed) {
            newGame()
            cannonThread = CannonThread(holder)
            cannonThread?.setRunning(true)
            cannonThread?.start()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        cannonThread?.setRunning(false)
        while (retry) {
            try {
                cannonThread?.join()
                retry = false
            } catch (e: InterruptedException) {
                Log.e(TAG, "Thread interrupted", e)
            }
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val action = e.action

        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            alignAndFireCannonball(e)
        }
        return true
    }

    private inner class CannonThread(
        private val surfaceHolder: SurfaceHolder
    ) : Thread() {
        private var threadIsRunning = true

        init {
            name = "CannonThread"
        }

        fun setRunning(running: Boolean) {
            threadIsRunning = running
        }

        override fun run() {
            var canvas: Canvas? = null
            var previousFrameTime = System.currentTimeMillis()
            while (threadIsRunning) {
                try {
                    canvas = surfaceHolder.lockCanvas(null)

                    synchronized(surfaceHolder) {
                        val currentTime = System.currentTimeMillis()
                        val elapsedTimeMS =
                            (currentTime - previousFrameTime).toDouble()
                        totalElapsedTime += elapsedTimeMS / 1000.0
                        updatePositions(elapsedTimeMS)
                        testForCollisions()
                        drawGameElements(canvas)
                        previousFrameTime = currentTime
                    }
                } finally {
                    if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }

    private fun hideSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    SYSTEM_UI_FLAG_FULLSCREEN or
                    SYSTEM_UI_FLAG_IMMERSIVE
    }

    private fun showSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    companion object {
        private const val TAG = "CannonView"

        const val MISS_PENALTY = 2
        const val HIT_REWARD = 3

        const val CANNON_BASE_RADIUS_PERCENT = 3.0 / 40
        const val CANNON_BARREL_WIDTH_PERCENT = 3.0 / 40
        const val CANNON_BARREL_LENGTH_PERCENT = 1.0 / 10

        const val CANNONBALL_RADIUS_PERCENT = 3.0 / 80
        const val CANNONBALL_SPEED_PERCENT = 3.0 / 2

        const val TARGET_WIDTH_PERCENT = 1.0 / 40
        const val TARGET_LENGTH_PERCENT = 3.0 / 20
        const val TARGET_FIRST_X_PERCENT = 3.0 / 5
        const val TARGET_SPACING_PERCENT = 1.0 / 60
        const val TARGET_PIECES = 9.0
        const val TARGET_MIN_SPEED_PERCENT = 3.0 / 4
        const val TARGET_MAX_SPEED_PERCENT = 6.0 / 4

        const val BLOCKER_WIDTH_PERCENT = 1.0 / 40
        const val BLOCKER_LENGTH_PERCENT = 1.0 / 4
        const val BLOCKER_X_PERCENT = 1.0 / 2
        const val BLOCKER_SPEED_PERCENT = 1.0

        const val TEXT_SIZE_PERCENT = 1.0 / 18
    }
}