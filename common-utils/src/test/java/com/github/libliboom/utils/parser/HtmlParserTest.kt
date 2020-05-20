package com.github.libliboom.utils.parser

import com.github.libliboom.utils.const.Resource
import com.github.libliboom.utils.io.FileUtils
import com.google.gson.Gson
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

internal class HtmlParserTest {

    @Test
    fun parseMetadata() {
        val contents = instance.parseMetadata(opffile)
        for (s in contents) {
            println(s)
        }
    }

    @Test
    fun parseManifest() {
        val parsedMap = instance.parseManifest(opffile)
        val gson = Gson()
        for (s in parsedMap) {
            val jsonString = s.value
            val item = gson.fromJson(jsonString, Item::class.java)
            println(item)
        }
    }

    @Test
    fun parseSpine() {
        val parsedMap = instance.parseSpine(opffile)
        val gson = Gson()
        for (s in parsedMap) {
            val jsonString = s.value
            val itemRef = gson.fromJson(jsonString, ItemRef::class.java)
            println(itemRef)
        }
    }

    @Test
    fun parseNavMap() {
        val parsedMap = instance.parseNavMap(ncxfile)

        val gson = Gson()
        for (s in parsedMap) {
            println("${s.key}: ${s.value}")
            val jsonString = s.value
            val navPoint = gson.fromJson(jsonString, NavPoint::class.java)
            println(navPoint)
        }
    }

    @Test
    fun parseGuide() {
        val jsonString = instance.parseGuide(opffile)
        val gson = Gson()
        val guide = gson.fromJson(jsonString, Guide::class.java)

        println(guide.toString())
    }

    @Test
    fun parseCover() {
        val jsonString = instance.parseGuide(opffile)
        val gson = Gson()
        val guide = gson.fromJson(jsonString, Guide::class.java)
        val hrefPath = FileUtils.getOEBPSDir() + guide.href
        val resultString = instance.parseCover(hrefPath)
        val cover = gson.fromJson(resultString, Cover::class.java)
        println(cover.toString())
    }

    @Test
    fun parseElement() {
        val header = instance.parseHead(contentsfile)
        println(header)
        val body = instance.parseBody(contentsfile)
        println("$body(${body.length})")
    }

    @Test
    fun displayAllElements() {
        instance.displayAllElements(contentsfile)
    }

    @Test
    fun findSpecificTags() {
        instance.findSpecificTags(contentsfile)
    }

    @Test
    fun extractText() {
        instance.extractText(ncxfile)
    }

    @Test
    fun renderToText() {
        instance.renderToText(ncxfile)
    }

    @Test
    fun encoding() {
        instance.encoding(ncxfile)
    }

    data class Item(val href: String, val id: String, val `media-type`: String)
    data class ItemRef(val idref: String, val linear: String)
    data class Guide(val type: String, val title: String, val href: String)
    data class Cover(val src: String, val alt: String)
    data class NavPoint(val id: String, val playOrder: String, val navlabelText: String, val contentSrc: String)

    companion object {
        private val opffile = FileUtils.getOEBPSDir() + Resource.CONTENT_OPF_FILE_NAME
        private val ncxfile = FileUtils.getOEBPSDir() + Resource.TOC_NCX_FILE_NAME
        private val contentsfile = FileUtils.getOEBPSDir() + Resource.CONTENTS_SAMPLE_FILE_NAME_45

        private lateinit var instance: HtmlParser

        @JvmStatic
        @BeforeAll
        fun setup() {
            instance = HtmlParser()
        }
    }
}