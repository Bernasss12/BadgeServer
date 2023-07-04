package dev.bernasss12.dsl.svg

import dev.bernasss12.dsl.Tag

class Text(id: String = "") : Tag("text.svg", id) {
    var contents = ""
    var split = false

    override fun render(builder: StringBuilder, indent: Int) {
        builder.appendIndent(indent)
        builder.append("<$name")
        builder.appendAttributes()
        builder.append(">")
        if (!split) {
            builder.append(contents)
        } else {
            contents.forEach { char ->
                builder.append("<tspan>$char</tspan>")
            }
        }
        builder.append("</$name>\n")
    }
}