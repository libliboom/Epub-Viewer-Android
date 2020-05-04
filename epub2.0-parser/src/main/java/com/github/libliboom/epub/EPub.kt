package com.github.libliboom.epub

/**
 * EPUB stands for Electronic publication
 *
 * Analyze step is
 * Validate OFC
 * Decompress epub
 * ...
 */
class EPub {

    private lateinit var rootPath: String

    fun validateFormat(): Boolean {
        return true
    }
}