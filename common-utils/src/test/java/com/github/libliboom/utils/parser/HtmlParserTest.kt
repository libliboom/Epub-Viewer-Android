package com.github.libliboom.utils.parser

import com.github.libliboom.utils.const.Resource
import com.github.libliboom.utils.io.FileUtils
import com.google.gson.Gson
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class HtmlParserTest {

    val opffile = FileUtils.getOEBPSDir() + Resource.CONTENT_OPF_FILE_NAME
    val ncxfile = FileUtils.getOEBPSDir() + Resource.TOC_NCX_FILE_NAME

    private lateinit var instance: HtmlParser
    private lateinit var filename: String

    @BeforeEach
    fun init() {
        instance = HtmlParser()
        filename = ncxfile
    }

    @Test
    fun parseMetadata() {
        val contents = instance.parseMetadata(filename)
        for (s in contents) {
            println(s)
        }
    }

    @Test
    fun parseManifest() {
        val parsedMap = instance.parseManifest(filename)
        val gson = Gson()
        for (s in parsedMap) {
            val jsonString = s.value
            val item = gson.fromJson(jsonString, Item::class.java)
            println(item)
        }
    }

    @Test
    fun parseSpine() {
        val parsedMap = instance.parseSpine(filename)
        val gson = Gson()
        for (s in parsedMap) {
            val jsonString = s.value
            val itemRef = gson.fromJson(jsonString, ItemRef::class.java)
            println(itemRef)
        }
    }

    @Test
    fun parseNavMap() {
        val parsedMap = instance.parseNavMap(filename)

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
        val jsonString = instance.parseGuide(filename)
        val gson = Gson()
        val guide = gson.fromJson(jsonString, Guide::class.java)

        println(guide.toString())
    }

    @Test
    fun displayAllElements() {
        instance.displayAllElements(filename)
    }

    @Test
    fun findSpecificTags() {
        instance.findSpecificTags(filename)
    }

    @Test
    fun extractText() {
        instance.extractText(filename)
    }

    @Test
    fun renderToText() {
        instance.renderToText(filename)
    }

    @Test
    fun encoding() {
        instance.encoding(filename)
    }

    data class Item(val href: String, val id: String, val `media-type`: String)
    data class ItemRef(val idref: String, val linear: String)
    data class Guide(val type: String, val title: String, val href: String)
    data class NavPoint(val id: String, val playOrder: String, val navlabelText: String, val contentSrc: String)
}