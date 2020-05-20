package com.github.libliboom.utils.io.robinary

import com.github.libliboom.utils.Constant
import com.github.libliboom.utils.const.Resource
import com.github.libliboom.utils.io.FileUtils
import com.github.libliboom.utils.io.ZipFileUtils
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files

internal class PageRoBinaryTest {

    @Test
    fun getAllPage() {
        val expect = 770 // based on size 1000
        val actual = pagination.pageCount

        assertEquals(expect, actual)
    }

    @Test
    fun getPageOfChapter() {
        contentsPaths.stream()
            .map { s -> pagination.getPageOfChapter(s) }
            .forEach { p -> println(p) }
    }

    @Test
    fun getChapterWithNth() {
        val p = pagination.getChapterWithNth(573)
        println(p)
    }

    companion object {
        lateinit var filelist: MutableList<String>

        private lateinit var ePubFile: String
        private lateinit var destFilePath: String
        private lateinit var contentsPaths: List<String>
        private lateinit var pagination: PageRoBinary

        @JvmStatic
        @BeforeAll
        fun setup() {
            ePubFile = FileUtils.getResourceDir() + Resource.EPUB_FILE_NAME_01
            destFilePath = FileUtils.getOutputDir()
            ZipFileUtils.extract(ePubFile, destFilePath)

            val oepbsPath = FileUtils.getOEBPSDir()
            val contentsPaths = mutableListOf<String>()
            File(oepbsPath).walk().forEach {
                if (it.path.contains(".htm.html")) {
                    contentsPaths.add(it.path)
                }
            }
            this.contentsPaths = contentsPaths
            pagination = PageRoBinary(contentsPaths)
        }

        @JvmStatic
        @AfterAll
        fun cleanup() {
            val keepFileList = Constant.getKeepFilelist()
            val outputPath = FileUtils.getOutputDir()
            val filePaths = mutableListOf<String>()
            File(outputPath).walk().forEach {
                filePaths.add(it.path)
            }

            val arr = filePaths.stream()
                .filter { p ->
                    keepFileList.stream()
                        .noneMatch { f -> p.contains(f) }
                }
                .toArray { size -> arrayOfNulls<String>(size) }

            for (f in arr) {
                if (File(f).isDirectory) continue
                Files.delete(File(f).toPath())
            }
        }
    }
}