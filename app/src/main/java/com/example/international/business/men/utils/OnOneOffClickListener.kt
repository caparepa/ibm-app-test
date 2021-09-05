package com.example.international.business.men.utils

import android.os.SystemClock
import android.view.View

abstract class OnOneOffClickListener : View.OnClickListener {
    private var mLastClickTime: Long = 0
    abstract fun onSingleClick(v: View?)

    override fun onClick(v: View?) {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - mLastClickTime
        mLastClickTime = currentClickTime
        if (elapsedTime <= MIN_CLICK_INTERVAL) return
        if (!isViewClicked) {
            isViewClicked = true
            startTimer()
        } else {
            return
        }
        onSingleClick(v)
    }

    /**
     * This method delays simultaneous touch events of multiple views.
     */
    private fun startTimer() {
        delayAction(MIN_CLICK_INTERVAL) {
            isViewClicked = false
        }
    }

    companion object {
        private const val MIN_CLICK_INTERVAL: Long = 600
        var isViewClicked = false
    }
}