package com.github.libliboom.utils.io

import com.github.libliboom.utils.const.Resource.Companion.COMMON_UTILS_FOLDER_NAME
import com.github.libliboom.utils.const.Resource.Companion.OEBPS_FOLDER_NAME
import com.github.libliboom.utils.const.Resource.Companion.OUTPUT_FOLDER_NAME
import com.github.libliboom.utils.const.Resource.Companion.OUTPUT_SOURCE_FOLDER_NAME
import com.github.libliboom.utils.const.Resource.Companion.OUTPUT_TEST_FOLDER_NAME
import com.github.libliboom.utils.const.Resource.Companion.RES_FOLDER_NAME
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * It's sort of snippet for debug.
 */
object FileUtils {

    fun exist(path: String): Boolean {
        val f = File(path)
        return f.exists()
    }

    fun getResourceDir(): String {
        return getCommonUtilsDir() + RES_FOLDER_NAME + File.separator
    }

    fun getOutputDir(): String {
        return getCommonUtilsDir() +
            OUTPUT_SOURCE_FOLDER_NAME + File.separator +
            OUTPUT_TEST_FOLDER_NAME + File.separator +
            OUTPUT_FOLDER_NAME + File.separator
    }

    fun getOEBPSDir(): String {
        return getOutputDir() + OEBPS_FOLDER_NAME + File.separator
    }

    fun getFileUri(path: String): String {
        return "file://$path"
    }

    fun getFileName(file: String): String {
        return file.split(".")[0]
    }

    fun convertToPath(file: String): String {
        return if (file.last() == '/') file else "$file/"
    }

    fun copy(istream: InputStream, ostream: OutputStream) {
        try {
            val buffer = ByteArray(8192)
            var length: Int
            while (istream.read(buffer).also { length = it } > 0) {
                ostream.write(buffer, 0, length)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getCommonUtilsDir(): String {
        val dir = File(System.getProperty("user.dir")) // like root directory
        return dir.parent + File.separator + COMMON_UTILS_FOLDER_NAME + File.separator
    }
}