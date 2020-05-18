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
class MetaRoBinary(filePath: String) : RoBinary() {

    private val path: String = filePath

    private val file: RandomAccessFile = RandomAccessFile(filePath, "r")

    private var attributes = mutableMapOf<String, ByteArray>()

    init {
        registerMagicNumber()
        registerMimeType()
        registerMime()
        registerContainer()
        registerOpf()
    }

    fun getBytes(key: String): ByteArray {
        return attributes.get(key) ?: ByteArray(0)
    }

    private fun registerMagicNumber() {
        registerByteArray(MAGIC_NUMBER, 0, 2)
        takeIf { getBytes(MAGIC_NUMBER) == null }?.let { throw Exception("ERROR: INVALID MAGIC_NUMBER") }
    }

    private fun registerMimeType() {
        registerByteArray(MIME_TYPE, 30, 38)
        takeIf { getBytes(MIME_TYPE) == null }?.let { throw Exception("ERROR: INVALID MIME_TYPE") }
    }

    private fun registerMime() {
        registerByteArray(MIME, 38, 58)
        takeIf { getBytes(MIME) == null }?.let { throw Exception("ERROR: INVALID MIME") }
    }

    private fun registerContainer() {
        val filelist = ZipFileUtils.findFiles(path) { it.contains(FILE_OF_CONTAINER) }
        registerByteArray(META_INF_CONTAINER, filelist[0].toByteArray())
        takeIf { getBytes(META_INF_CONTAINER) == null }?.let { throw Exception("ERROR: INVALID META_INF_CONTAINER") }
    }

    private fun registerOpf() {
        val filelist = ZipFileUtils.findFiles(path) { it.contains(".$OPF") }
        registerByteArray(OPF, filelist[0].toByteArray())
        takeIf { getBytes(OPF) == null }?.let { throw Exception("ERROR: INVALID OPF") }
    }

    private fun registerByteArray(key: String, from: Int, to: Int) {
        val len = to - from
        var bytes = ByteArray(len)
        file.seek(from.toLong())
        file.read(bytes, 0, len)
        registerByteArray(key, bytes)
    }

    private fun registerByteArray(key: String, bytes: ByteArray) {
        attributes[key] = bytes
    }
}