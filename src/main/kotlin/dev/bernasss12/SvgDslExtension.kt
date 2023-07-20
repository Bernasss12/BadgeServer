package dev.bernasss12

import kotlinx.html.*
import kotlinx.html.SVG

open class SVGTag(tagName: String, initialAttributes : Map<String, String>, override val consumer : TagConsumer<*>) : HTMLTag(tagName, consumer, initialAttributes, "http://www.w3.org/2000/svg", false, false), HtmlBlockInlineTag

inline fun HTMLTag.newSvg(classes: String? = null, crossinline block: SVGTag.() -> Unit) : Unit = SVGTag("svg", attributesMapOf("class", classes), consumer).visit(block)

inline fun SVGTag.g(classes : String? = null, crossinline block : G.() -> Unit = {}) : Unit = G(attributesMapOf("class", classes), consumer).visit(block)
inline fun SVGTag.path(classes : String? = null, crossinline block : Path.() -> Unit = {}) : Unit = Path(attributesMapOf("class", classes), consumer).visit(block)

open class G(
    initialAttributes: Map<String, String>,
    override val consumer: TagConsumer<*>
) : SVGTag(
    tagName = "g",
    consumer = consumer,
    initialAttributes = initialAttributes,
), HtmlBlockInlineTag

open class Path(
    initialAttributes: Map<String, String>,
    override val consumer: TagConsumer<*>
) : HTMLTag(
    tagName = "path",
    consumer = consumer,
    initialAttributes = initialAttributes,
    inlineTag = false,
    emptyTag = false
), HtmlBlockInlineTag {
    var d: String = ""
    var fill: String = ""
}
