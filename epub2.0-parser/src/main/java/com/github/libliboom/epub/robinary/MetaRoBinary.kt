package com.github.libliboom.epub.robinary

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
class MetaRoBinary(path: String) : RoBinary() {

    private val path: String = path

    private val file: RandomAccessFile = RandomAccessFile(path, "r")

    private var attributes = mutableMapOf<String, ByteArray>()

    init {
        registerMagicNumber()
        registerMimeType()
        registerMime()
        registerContainer()
        registerOpf()
    }

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
        val filelist = ZipFileUtils.findFiles(path) { it.contains(FILE_OF_CONTAINER) }

        // @TODO: compare the file name with result of xml parser

        takeIf { filelist.isNotEmpty() }.let {
            registerByteArray(
                META_INF_CONTAINER,
                filelist[0].toByteArray()
            )
        }
    }

    private fun registerOpf() {
        val filelist = ZipFileUtils.findFiles(path) { it.contains(".$OPF") }
        takeIf { filelist.isNotEmpty() }.let {
            registerByteArray(
                OPF,
                filelist[0].toByteArray()
            )
        }
    }

    fun getBytes(key: String): ByteArray {
        return attributes.get(key) ?: ByteArray(0)
    }

    private fun registerByteArray(key: String, from: Int, to: Int) {
        val len = to - from
        var bytes = ByteArray(len)
        file.seek(from.toLong())
        file.read(bytes, 0, len)
        registerByteArray(key, bytes)
    }

    private fun registerByteArray(key: String, bytes: ByteArray) {
        attributes.put(key, bytes)
    }
}