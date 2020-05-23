package com.github.libliboom.utils.parser

import com.github.libliboom.utils.const.Resource
import com.github.libliboom.utils.io.FileUtils
import com.google.gson.Gson
import net.htmlparser.jericho.CharacterReference
import net.htmlparser.jericho.Element
import net.htmlparser.jericho.EndTagType
import net.htmlparser.jericho.HTMLElementName
import net.htmlparser.jericho.MasonTagTypes
import net.htmlparser.jericho.MicrosoftConditionalCommentTagTypes
import net.htmlparser.jericho.PHPTagTypes
import net.htmlparser.jericho.Segment
import net.htmlparser.jericho.Source
import net.htmlparser.jericho.StartTag
import net.htmlparser.jericho.StartTagType
import net.htmlparser.jericho.TextExtractor
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.net.URL

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
        displayAllElements(contentsfile)
    }

    @Test
    fun findSpecificTags() {
        findSpecificTags(contentsfile)
    }

    @Test
    fun extractText() {
        extractText(ncxfile)
    }

    @Test
    fun renderToText() {
        renderToText(ncxfile)
    }

    @Test
    fun encoding() {
        encoding(ncxfile)
    }

    private fun displayAllElements(filename: String) {
        var sourceUrlString = filename
        if (sourceUrlString.indexOf(':') == -1) sourceUrlString = "file:$sourceUrlString"
        MicrosoftConditionalCommentTagTypes.register()
        PHPTagTypes.register()
        PHPTagTypes.PHP_SHORT.deregister() // remove PHP short tags for this example otherwise they override processing instructions

        MasonTagTypes.register()
        val source = Source(URL(sourceUrlString))
        val elementList: List<Element> = source.getAllElements()
        for (element in elementList) {
            println("-------------------------------------------------------------------------------")
            System.out.println(element.getDebugInfo())
            if (element.getAttributes() != null) System.out.println(
                """
                    XHTML StartTag:
                    ${element.getStartTag().tidy(true)}
                    """.trimIndent()
            )
            println("Source text with content:\n$element")
        }
        System.out.println(source.getCacheDebugInfo())
    }

    private fun findSpecificTags(filename: String) {
        var sourceUrlString = filename
        if (sourceUrlString.indexOf(':') === -1) sourceUrlString = "file:$sourceUrlString"
        MicrosoftConditionalCommentTagTypes.register()
        MasonTagTypes.register()
        var source =
            Source(URL(sourceUrlString))
        println("\n*******************************************************************************\n")

        println("XML Declarations:")
        displaySegments(source.getAllTags(StartTagType.XML_DECLARATION))

        println("XML Processing instructions:")
        displaySegments(source.getAllTags(StartTagType.XML_PROCESSING_INSTRUCTION))

        PHPTagTypes.register() // register PHPTagTypes after searching for XML processing instructions, otherwise PHP short tags override them.

        StartTagType.XML_DECLARATION.deregister() // deregister XML declarations so they are recognised as PHP short tags, consistent with the real PHP parser.

        source =
            Source(source) // have to create a new Source object after changing tag type registrations otherwise cache might contain tags found with previous configuration.

        println("##################### PHP tag types now added to register #####################\n")

        println("H2 Elements:")
        displaySegments(source.getAllElements(HTMLElementName.H2))

        println("Document Type Declarations:")
        displaySegments(source.getAllTags(StartTagType.DOCTYPE_DECLARATION))


        println("CDATA sections:")
        displaySegments(source.getAllTags(StartTagType.CDATA_SECTION))

        println("Common server tags: (eg ASP, JSP, PSP, ASP-style PHP or Mason substitution tag)")
        displaySegments(source.getAllTags(StartTagType.SERVER_COMMON))

        println("Tags starting with <%=")
        displaySegments(source.getAllStartTags("%="))

        println("Tags starting with <%=var")
        displaySegments(source.getAllStartTags("%=var"))

        println("HTML Comments:")
        displaySegments(source.getAllTags(StartTagType.COMMENT))

        println("Elements in namespace \"o\" (generated by MS-Word):")
        displaySegments(source.getAllElements("o:"))

        println("Tags starting with <![ (commonly generated by MS-Word):")
        displaySegments(source.getAllStartTags("!["))

        // Note: The end of a PHP tag can not be reliably found without the use of a PHP parser,
        // meaning any PHP tag found by this library is not guaranteed to have the correct end position.

        // Note: The end of a PHP tag can not be reliably found without the use of a PHP parser,
        // meaning any PHP tag found by this library is not guaranteed to have the correct end position.
        println("Standard PHP tags:")
        displaySegments(source.getAllTags(PHPTagTypes.PHP_STANDARD))

        println("Short PHP tags:")
        displaySegments(source.getAllTags(PHPTagTypes.PHP_SHORT))

        println("Mason Component Calls:")
        displaySegments(source.getAllTags(MasonTagTypes.MASON_COMPONENT_CALL))

        println("Mason Components Called With Content:")
        displaySegments(source.getAllElements(MasonTagTypes.MASON_COMPONENT_CALLED_WITH_CONTENT))

        println("Mason Named Blocks:")
        displaySegments(source.getAllElements(MasonTagTypes.MASON_NAMED_BLOCK))

        println("Unregistered start tags:")
        displaySegments(source.getAllTags(StartTagType.UNREGISTERED))

        println("Unregistered end tags:")
        displaySegments(source.getAllTags(EndTagType.UNREGISTERED))

        println(source.cacheDebugInfo)
    }

    fun extractText(filename: String) {
        var sourceUrlString = filename
        if (sourceUrlString.indexOf(':') == -1) sourceUrlString = "file:$sourceUrlString"
        MicrosoftConditionalCommentTagTypes.register()
        PHPTagTypes.register()
        PHPTagTypes.PHP_SHORT.deregister() // remove PHP short tags for this example otherwise they override processing instructions

        MasonTagTypes.register()
        val source =
            Source(URL(sourceUrlString))

        // Call fullSequentialParse manually as most of the source will be parsed.

        // Call fullSequentialParse manually as most of the source will be parsed.
        source.fullSequentialParse()

        println("Document title:")
        val title: String? = getTitle(source)
        println(title ?: "(none)")

        println("\nDocument description:")
        val description: String? = getMetaValue(source, "description")
        println(description ?: "(none)")

        println("\nDocument keywords:")
        val keywords: String? = getMetaValue(source, "keywords")
        println(keywords ?: "(none)")

        println("\nLinks to other documents:")
        val linkElements =
            source.getAllElements(HTMLElementName.A)
        for (linkElement in linkElements) {
            val href = linkElement.getAttributeValue("href") ?: continue
            // A element can contain other tags so need to extract the text from it:
            val label = linkElement.content.textExtractor.toString()
            println("$label <$href>")
        }

        println("\nAll text from file (exluding content inside SCRIPT and STYLE elements):\n")
        println(source.textExtractor.setIncludeAttributes(true).toString())

        println("\nSame again but this time extend the TextExtractor class to also exclude text from P elements and any elements with class=\"control\":\n")
        val textExtractor: TextExtractor = object : TextExtractor(source) {
            override fun excludeElement(startTag: StartTag): Boolean {
                return startTag.name === HTMLElementName.P || "control".equals(
                    startTag.getAttributeValue(
                        "class"
                    ), ignoreCase = true
                )
            }
        }
        println(textExtractor.setIncludeAttributes(true).toString())
    }

    private fun getTitle(source: Source): String? {
        val titleElement =
            source.getFirstElement(HTMLElementName.TITLE) ?: return null
        // TITLE element never contains other tags so just decode it collapsing whitespace:
        return CharacterReference.decodeCollapseWhiteSpace(titleElement.content)
    }

    private fun getMetaValue(
        source: Source,
        key: String
    ): String? {
        var pos = 0
        while (pos < source.length) {
            val startTag =
                source.getNextStartTag(pos, "name", key, false) ?: return null
            if (startTag.name === HTMLElementName.META) return startTag.getAttributeValue("content") // Attribute values are automatically decoded
            pos = startTag.end
        }
        return null
    }

    private fun renderToText(filename: String) {
        var sourceUrlString = filename
        if (sourceUrlString.indexOf(':') == -1) sourceUrlString = "file:$sourceUrlString"
        val source = Source(URL(sourceUrlString))
        val renderedText = source.renderer.toString()
        println("\nSimple rendering of the HTML document:\n")
        println(renderedText)
    }

    private fun encoding(filename: String) {
        var sourceUrlString = filename
        if (sourceUrlString.indexOf(':') === -1) sourceUrlString = "file:$sourceUrlString"
        println("\nSource URL:")
        System.out.println(sourceUrlString)
        val url = URL(sourceUrlString)
        val source = Source(url)
        println("\nDocument Title:")
        val titleElement =
            source.getFirstElement(HTMLElementName.TITLE)
        println(titleElement?.content?.toString() ?: "(none)")
        println("\nSource.getEncoding():")
        println(source.encoding)
        println("\nSource.getEncodingSpecificationInfo():")
        println(source.encodingSpecificationInfo)
        println("\nSource.getPreliminaryEncodingInfo():")
        println(source.preliminaryEncodingInfo)
    }

    private fun displaySegments(segments: List<Segment?>) {
        for (segment in segments) {
            println("-------------------------------------------------------------------------------")
            System.out.println(segment?.getDebugInfo())
            System.out.println(segment)
        }
        println("\n*******************************************************************************\n")
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