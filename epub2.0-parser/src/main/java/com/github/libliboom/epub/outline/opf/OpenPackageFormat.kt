package com.github.libliboom.epub.outline.opf

import com.github.libliboom.epub.common.Constant.OPF
import com.github.libliboom.epub.io.robinary.MetaRoBinary
import com.github.libliboom.utils.const.Resource
import com.github.libliboom.utils.const.Resource.Companion.TOC_NCX_FILE_NAME
import com.github.libliboom.utils.parser.HtmlParser
import com.google.gson.Gson
import java.io.File

class OpenPackageFormat(meta: MetaRoBinary, decompressedPath: String) {

    private val opfPath = decompressedPath + meta.getBytes(OPF).toString(Charsets.UTF_8)
    private val tocNcxPath = decompressedPath + Resource.OEBPS_FOLDER_NAME + File.separator + TOC_NCX_FILE_NAME

    private val htmlParser = HtmlParser()
    private val gson = Gson()

    private var metadata = listOf<String>()
    private var items = mutableListOf<Item>()
    private var itemRefs = mutableListOf<ItemRef>()
    private lateinit var ncx: NavigationControlXml
    private lateinit var guide: Guide

    init {
        initOpf()
    }

    private fun initOpf() {
        initMetadata()
        initManifest()
        initSpine()
        initGuide()
        initNcx()
    }

    private fun initMetadata() {
        metadata = htmlParser.parseMetadata(opfPath)
    }

    private fun initManifest() {
        for (s in htmlParser.parseManifest(opfPath)) {
            items.add(gson.fromJson(s.value, Item::class.java))
        }
    }

    private fun initSpine() {
        for (s in htmlParser.parseSpine(opfPath)) {
            itemRefs.add(gson.fromJson(s.value, ItemRef::class.java))
        }
    }

    private fun initGuide() {
        guide = gson.fromJson(htmlParser.parseGuide(opfPath), Guide::class.java)
    }

    private fun initNcx() {
        ncx = NavigationControlXml(tocNcxPath)
    }

    data class Item(val href: String, val id: String, val `media-type`: String)
    data class ItemRef(val idref: String, val linear: String)
    data class Guide(val type: String, val title: String, val href: String)
}