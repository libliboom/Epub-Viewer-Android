package com.github.libliboom.utils.parser

import org.dom4j.io.SAXReader
import org.xml.sax.helpers.DefaultHandler
import javax.xml.parsers.SAXParserFactory

class XmlParser {

    fun parse(path: String, handler: DefaultHandler) {
        val parser = SAXParserFactory.newInstance().newSAXParser()
        parser.parse(path, handler)
    }

    /**
     * read all element and attribute
     * @TODO implement it for validation
     */
    fun read(path: String) {
        val reader = SAXReader()
        val doc = reader.read(path)
        val root = doc.rootElement

        for (e in root.elementIterator()) {
            println(e.name)
            for (c in e.elementIterator()) {
                println(c.name)
                for (a in c.attributeIterator()) {
                    println("${a.name}: ${a.data}")
                }
            }
        }
    }
}