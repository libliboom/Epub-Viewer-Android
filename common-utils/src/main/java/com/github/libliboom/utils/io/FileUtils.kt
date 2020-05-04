package com.github.libliboom.utils.io

import com.github.libliboom.utils.const.Resource.Companion.COMMON_UTILS_FOLDER_NAME
import com.github.libliboom.utils.const.Resource.Companion.OEBPS_FOLDER_NAME
import com.github.libliboom.utils.const.Resource.Companion.OUTPUT_FOLDER_NAME
import com.github.libliboom.utils.const.Resource.Companion.RES_FOLDER_NAME
import java.io.File

object FileUtils {

    fun exist(path: String): Boolean {
        val f = File(path)
        return f.exists()
    }

    fun getResourceDir(): String {
        return getCommonUtilsDir() + File.separator + RES_FOLDER_NAME + File.separator
    }

    fun getOutputDir(): String {
        return getCommonUtilsDir() + File.separator + OUTPUT_FOLDER_NAME + File.separator
    }

    fun getOEBPSDir(): String {
        return getOutputDir() + OEBPS_FOLDER_NAME + File.separator
    }

    private fun getCommonUtilsDir(): String {
        val dir = File(System.getProperty("user.dir"))
        return dir.parent + File.separator + COMMON_UTILS_FOLDER_NAME + File.separator
    }
}