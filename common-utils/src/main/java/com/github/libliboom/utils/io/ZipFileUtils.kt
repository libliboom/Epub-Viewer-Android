package com.github.libliboom.utils.io

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipFile
import org.apache.commons.compress.utils.IOUtils
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Enumeration

object ZipFileUtils {

    fun findFiles(zipFilePath: String, condition: (String) -> (Boolean)): List<String> {
        var filelist = mutableListOf<String>()
        ZipFile(zipFilePath).entries.run {
            while (hasMoreElements()) {
                val entry = nextElement()
                if (condition(entry.name)) {
                    filelist.add(entry.name)
                }
            }
        }

        return filelist
    }

    /**
     * Extract specific file by entry name.
     */
    fun extract(zipFilePath: String, dest: String, name: String) {
        ZipFile(zipFilePath).run outer@{
            entries.run {
                while (hasMoreElements()) {
                    val entry = nextElement()
                    if (entry.name == name) {
                        copy(this@outer, entry, dest)
                    }
                }
            }
        }
    }

    fun extract(zipFilePath: String, dest: String) {
        ZipFile(zipFilePath).run outer@{
            entries.run {
                while (hasMoreElements()) {
                    copy(this@outer, nextElement(), dest)
                }
            }
        }
    }

    private fun Enumeration<ZipArchiveEntry>.copy(
        zipFile: ZipFile,
        entry: ZipArchiveEntry,
        dest: String
    ) {
        entry.run {
            try {
                val outputfile = File(dest, name)
                createFile(outputfile)
                val istream = zipFile.getInputStream(this)
                val bostream = BufferedOutputStream(FileOutputStream(outputfile))
                IOUtils.copy(istream, bostream)
                bostream.close()
                istream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun createFile(outputfile: File) {
        takeUnless { outputfile.parentFile.exists() }
            .let { outputfile.parentFile.mkdirs() }
        outputfile.createNewFile()
    }
}
