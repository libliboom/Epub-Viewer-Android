package com.github.libliboom.utils.parser

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
import java.net.URL

/**
 * @TODO Refactoring those functions as generalization.
 */
class HtmlParser {

    fun parseMetadata(filename: String, tag: String = "metadata"): List<String> {
        var source = createSource(filename)
        val meta = source.getAllStartTags(tag)

        val contents = mutableListOf<String>()
        for (e in meta[0].childElements[0].childElements) {
            if (!e.name.startsWith("dc:")) continue
            contents.add(
                StringBuilder().append(e.name.substring(3))
                    .append(":").append(e.content.toString()).toString())
        }

        return contents
    }

    fun parseManifest(filename: String, tag: String = "manifest"): Map<Int, String> {
        var source = createSource(filename)
        val manifest = source.getAllStartTags(tag)

        val jsonMap = mutableMapOf<Int, String>()
        for ((idx, item) in manifest[0].childElements[0].childElements.withIndex()) {
            if (item.name == "!--") continue
            val sb = StringBuilder()
            for (attr in item.attributes) {
                sb.append("\"").append(attr.name)
                    .append("\":\"").append(attr.value).append("\"").append(",")
            }

            jsonMap[idx] = "{" + sb.substring(0, sb.length - 1) + "}"
        }

        return jsonMap
    }

    fun parseSpine(filename: String, tag: String = "spine"): Map<Int, String> {
        var source = createSource(filename)
        val spine = source.getAllStartTags(tag)

        val jsonMap = mutableMapOf<Int, String>()
        for ((idx, item) in spine[0].childElements[0].childElements.withIndex()) {
            if (item.name == "!--") continue
            val sb = StringBuilder()
            for (attr in item.attributes) {
                sb.append("\"").append(attr.name)
                    .append("\":\"").append(attr.value).append("\"").append(",")
            }

            jsonMap[idx] = "{" + sb.substring(0, sb.length - 1) + "}"
        }

        return jsonMap
    }

    fun parseNavMap(filename: String, tag: String = "navPoint"): Map<Int, String> {
        var source = createSource(filename)
        val jsonMap = mutableMapOf<Int, String>()
        val navPoints = source.getAllStartTags(tag)
        for ((idx, navPoint) in navPoints.withIndex()) {
            val sb = StringBuilder()
            for (attr in navPoint.attributes) {
                sb.append("\"").append(attr.name)
                    .append("\":\"").append(attr.value).append("\"").append(",")
            }
            for (c in navPoint.childElements[0].childElements) {
                val cname = c.name
                if (c.childElements.size != 0) {
                    for (cc in c.childElements) {
                        sb.append("\"").append(cname + cc.name.capitalize())
                            .append("\":\"").append(cc.content).append("\"").append(",")
                    }
                }
                for (attr in c.attributes) {
                    sb.append("\"").append(cname + attr.name.capitalize())
                        .append("\":\"").append(attr.value).append("\"").append(",")
                }
            }

            jsonMap[idx] = "{" + sb.substring(0, sb.length - 1) + "}"
        }

        return jsonMap.toMap()
    }

    fun parseGuide(filename: String, tag: String = "guide"): String {
        var source = createSource(filename)
        val guide = source.getAllStartTags(tag)

        val sb = StringBuilder()
        for ((idx, item) in guide[0].childElements[0].childElements.withIndex()) {
            if (item.name == "!--") continue
            for (attr in item.attributes) {
                sb.append("\"").append(attr.name)
                    .append("\":\"").append(attr.value).append("\"").append(",")
            }
        }

        return "{" + sb.substring(0, sb.length - 1) + "}"

    }

    fun parseCover(filename: String, tag: String = "img"): String {
        var source = createSource(filename)
        val img = source.getAllStartTags(tag)

        val sb = StringBuilder()
        for (attr in img[0].attributes) {
            sb.append("\"").append(attr.name)
                .append("\":\"").append(attr.value).append("\"").append(",")
        }

        return "{" + sb.substring(0, sb.length - 1) + "}"
    }

    fun parseHead(filename: String): String {
        return parseElementOrEmpty(filename, "<head>")
    }

    fun parseBody(filename: String): String {
        return parseElementOrEmpty(filename, "<body>")
    }

    private fun parseElementOrEmpty(filename: String, startTag: String): String {
        var sourceUrlString = filename
        if (sourceUrlString.indexOf(':') == -1) sourceUrlString = "file:$sourceUrlString"
        MicrosoftConditionalCommentTagTypes.register()
        PHPTagTypes.register()
        PHPTagTypes.PHP_SHORT.deregister()

        MasonTagTypes.register()
        val source = Source(URL(sourceUrlString))
        val elementList: List<Element> = source.getAllElements()
        for (element in elementList) {
            if (element.startTag.tidy(true) == startTag) {
                return element.toString()
            }
        }

        return "" // default value
    }

    // @TODO: analyze about this configuration
    private fun createSource(filename: String): Source {
        var sourceUrlString = filename
        if (sourceUrlString.indexOf(':') == -1) {
            sourceUrlString = "file:$sourceUrlString"
        }

        MicrosoftConditionalCommentTagTypes.register()
        PHPTagTypes.register()
        PHPTagTypes.PHP_SHORT.deregister()
        MasonTagTypes.register()
        return Source(URL(sourceUrlString))
    }

    fun displayAllElements(filename: String) {
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

    fun findSpecificTags(filename: String) {
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

    private fun displaySegments(segments: List<Segment?>) {
        for (segment in segments) {
            println("-------------------------------------------------------------------------------")
            System.out.println(segment?.getDebugInfo())
            System.out.println(segment)
        }
        println("\n*******************************************************************************\n")
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

    fun renderToText(filename: String) {
        var sourceUrlString = filename
        if (sourceUrlString.indexOf(':') == -1) sourceUrlString = "file:$sourceUrlString"
        val source = Source(URL(sourceUrlString))
        val renderedText = source.renderer.toString()
        println("\nSimple rendering of the HTML document:\n")
        println(renderedText)
    }

    fun encoding(filename: String) {
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
}