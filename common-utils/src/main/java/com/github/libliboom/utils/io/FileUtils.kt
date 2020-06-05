package com.github.libliboom.utils.io

import com.github.libliboom.utils.const.Resource.Companion.COMMON_UTILS_FOLDER_NAME
import com.github.libliboom.utils.const.Resource.Companion.OEBPS_FOLDER_NAME
import com.github.libliboom.utils.const.Resource.Companion.OEBPS_PATH
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

    fun exist(path: String) = File(path).exists()

    fun getResourceDir() = getCommonUtilsDir() + RES_FOLDER_NAME + File.separator

    fun getOutputDir() = getCommonUtilsDir() +
            OUTPUT_SOURCE_FOLDER_NAME + File.separator +
            OUTPUT_TEST_FOLDER_NAME + File.separator +
            OUTPUT_FOLDER_NAME + File.separator

    fun getOEBPSDir() = getOutputDir() + OEBPS_FOLDER_NAME + File.separator

    fun getFileUri(path: String) = "file://$path"

    fun removeFileUri(uri: String) = uri.split("file://")[1]

    fun getFileNameFromUri(uri: String) = uri.split("#")[0]

    fun getFileName(path: String) = path.split(".")[0]

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

    fun getExtractedOebpsPath(extractedPath: String, ePubFilePath: String): String {
        val filename = getFileNameFromUri(ePubFilePath)
        return extractedPath + convertToPath(filename) + OEBPS_PATH
    }

    private fun getCommonUtilsDir(): String {
        val dir = File(System.getProperty("user.dir")) // like root directory
        return dir.parent + File.separator + COMMON_UTILS_FOLDER_NAME + File.separator
    }

    private fun convertToPath(file: String) = if (file.last() == '/') file else "$file/"
}
