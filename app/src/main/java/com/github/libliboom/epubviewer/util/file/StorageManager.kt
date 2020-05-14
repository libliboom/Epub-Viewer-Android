package com.github.libliboom.epubviewer.util.file

import android.content.Context
import java.io.File

object StorageManager {

    fun getDir(context: Context): String {
        val dir = context.externalCacheDir?.path
        return takeIf { dir != null }?.let { dir + File.separator } ?: ""
    }
}