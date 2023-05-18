package com.books.gamewepapp.screens.game.views

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import com.books.gamewepapp.R

class DialogMessage : DialogFragment() {

    private var cannonCustomView: CannonCustomView? = null
    companion object {
        const val MESSAGE_ID = "messageid"
        const val SHOTS_FIRED = "shotsFired"
        const val TOTAL_ELAPSED_TIME = "totalElapsedTime"

        fun newInstance(messageId: Int, shotsFired: Int, totalElapsedTime: Double): DialogMessage {
            val dialogMessage = DialogMessage()
            val bdl = Bundle()
            bdl.putInt(MESSAGE_ID, messageId)
            bdl.putInt(SHOTS_FIRED, shotsFired)
            bdl.putDouble(TOTAL_ELAPSED_TIME, totalElapsedTime)
            dialogMessage.arguments = bdl
            return dialogMessage
        }
    }

    fun initializeCannonCustomView(externalCannonCustomView: CannonCustomView) {
        cannonCustomView = externalCannonCustomView
    }

    override fun onCreateDialog(bundle: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(resources.getString(arguments.getInt(MESSAGE_ID)))
        builder.setMessage(
            resources.getString(
                R.string.results_format, arguments.getInt(SHOTS_FIRED), arguments.getDouble(
                    TOTAL_ELAPSED_TIME
                )
            )
        )
        builder.setPositiveButton(
            R.string.reset_game
        ) { dialog, which ->
            cannonCustomView?.dialogIsDisplayed = false
            cannonCustomView?.newGame()
        }
        return builder.create()
    }
}