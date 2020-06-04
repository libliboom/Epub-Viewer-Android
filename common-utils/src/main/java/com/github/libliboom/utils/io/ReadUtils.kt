package com.github.libliboom.utils.io

import java.io.IOException
import java.io.RandomAccessFile

object ReadUtils {

    fun readBytes(file: RandomAccessFile, from: Int, to: Int): ByteArray {
        return try {
            var bytes = ByteArray(to - from)
            file.read(bytes, from, to)
            bytes
        } catch (e: IOException) {
            ByteArray(0)
        }
    }
}
