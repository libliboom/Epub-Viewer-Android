package com.github.libliboom.epubviewer.util.event

import android.os.Handler

class ClickUtils {
    private var isLoadedOnce = false
    private val timerTask = Runnable { isLoadedOnce = false }
    private val scrollLoadHandler = Handler()

    fun isLoadedOnce(): Boolean {
        if (isLoadedOnce) return true
        isLoadedOnce = true
        scrollLoadHandler.postDelayed(timerTask, ONE_SECOND)
        return false
    }

    companion object {
        const val ONE_SECOND: Long = 1000
    }
}