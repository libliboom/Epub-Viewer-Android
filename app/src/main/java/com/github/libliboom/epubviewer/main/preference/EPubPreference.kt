package com.github.libliboom.epubviewer.main.preference

import android.content.Context
import androidx.preference.PreferenceManager

object EPubPreference {

    private const val PREF_PAGE_NUMBER = "page_number"

    private const val PREF_PICTURE_URL = "picture_url"

    private const val PREF_CRAWL_RUNNING = "crawl_running"

    fun getPageNumber(context: Context?): Int {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getInt(PREF_PAGE_NUMBER, 1)
    }

    fun setPageNumber(context: Context?, pageNum: Int) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putInt(PREF_PAGE_NUMBER, pageNum)
            .apply()
    }

    fun getPictureUrl(context: Context?): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(PREF_PICTURE_URL, "")
    }

    fun setPictureUrl(context: Context?, url: String?) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_PICTURE_URL, url)
            .apply()
    }

    fun isCrawlRunning(context: Context?): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(PREF_CRAWL_RUNNING, false)
    }

    fun setCrawlRunning(context: Context?, state: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(PREF_CRAWL_RUNNING, state)
            .apply()
    }
}
