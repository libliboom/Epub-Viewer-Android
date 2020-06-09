package com.github.libliboom.epub.io.robinary

import com.github.libliboom.epub.common.Constant.FILE_OF_CONTAINER
import com.github.libliboom.epub.common.Constant.MAGIC_NUMBER
import com.github.libliboom.epub.common.Constant.META_INF_CONTAINER
import com.github.libliboom.epub.common.Constant.MIME
import com.github.libliboom.epub.common.Constant.MIME_TYPE
import com.github.libliboom.epub.common.Constant.OPF
import com.github.libliboom.utils.io.ZipFileUtils
import com.github.libliboom.utils.io.robinary.RoBinary
import java.io.RandomAccessFile

/**
 * It's based on RandomAccessFile.
 * Include the following list
 * - register meta information with key-value
 * - without unregister meta information
 */
class MetaRoBinary(private val filePath: String) : RoBinary() {

    private val file: RandomAccessFile = RandomAccessFile(filePath, "r")

    private var attributes = mutableMapOf<String, ByteArray>()

    init {
        registerMagicNumber()
        registerMimeType()
        registerMime()
        registerContainer()
        registerOpf()
    }

    fun getBytes(key: String) = attributes[key] ?: ByteArray(0)

    private fun registerMagicNumber() {
        registerByteArray(MAGIC_NUMBER, 0, 2)
    }

    private fun registerMimeType() {
        registerByteArray(MIME_TYPE, 30, 38)
    }

    private fun registerMime() {
        registerByteArray(MIME, 38, 58)
    }

    private fun registerContainer() {
        val filelist = ZipFileUtils.findFiles(filePath) { it.contains(FILE_OF_CONTAINER) }
        registerByteArray(META_INF_CONTAINER, filelist[0].toByteArray())
    }

    private fun registerOpf() {
        val filelist = ZipFileUtils.findFiles(filePath) { it.contains(".$OPF") }
        registerByteArray(OPF, filelist[0].toByteArray())
    }

    // REVIEW: 2020/06/05 Exception
    private fun registerByteArray(key: String, from: Int, to: Int) {
        val len = to - from
        val bytes = ByteArray(len)
        file.seek(from.toLong())
        file.read(bytes, 0, len)
        registerByteArray(key, bytes)
    }

    private fun registerByteArray(key: String, bytes: ByteArray) {
        attributes[key] = bytes
    }
}
