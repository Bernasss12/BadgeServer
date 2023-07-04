package dev.bernasss12.dsl

@TagMarker
abstract class Tag(val name: String, id: String) {
    internal val attributes: MutableMap<String, List<String>> = linkedMapOf()

    var id: String = ""
        set(value) {
            addAttribute("id", value)
            field = value
        }

    var x: String = ""
        set(value) {
            addAttribute("x", value)
            field = value
        }
    var y: String = ""
        set(value) {
            addAttribute("y", value)
            field = value
        }
    var width: String = ""
        set(value) {
            addAttribute("width", value)
            field = value
        }
    var height: String = ""
        set(value) {
            addAttribute("height", value)
            field = value
        }

    init {
        this.id = id
    }

    open fun <T : Tag> initComponent(tag: T, init: T.() -> Unit): T {
        tag.apply(init)
        return tag
    }

    open fun render(builder: StringBuilder, indent: Int) {
        builder.appendIndent(indent)
        builder.append("<$name")
        builder.appendAttributes()
        builder.append(" />\n")
    }

    internal fun StringBuilder.appendAttributes() {
        attributes.forEach { (attribute, value) ->
            append(" $attribute=\"${value.joinToString(" ")}\"")
        }
    }

    internal fun StringBuilder.appendIndent(indent: Int) {
        if (indent < 1) return
        append("  ".repeat(indent))
    }

    internal fun addAttribute(attribute: String, vararg values: String) {
        if (attribute == "id" && attributes.containsKey("id")) return
        if (values.any(String::isNotBlank)) attributes[attribute] = values.toList().filter(String::isNotBlank) else Unit
    }

    internal fun addAttributes(attrs: Map<String, List<String>>) {
        attrs.forEach { (key, values) ->
            addAttribute(key, *values.toTypedArray())
        }
    }

}