package com.github.libliboom.utils.io

import java.io.IOException
import java.io.RandomAccessFile

object ReadUtils {

    fun readBytes(file: RandomAccessFile, from: Int, to: Int): ByteArray {
        try {
            var bytes = ByteArray(to - from)
            file.read(bytes, from, to)
            return bytes
        } catch (e: IOException) {
            return ByteArray(0)
        }
    }
}