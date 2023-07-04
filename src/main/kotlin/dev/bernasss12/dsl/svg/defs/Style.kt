package dev.bernasss12.dsl.svg.defs

import dev.bernasss12.dsl.ContainerTag

class Style : ContainerTag("style", "") {
    override fun render(builder: StringBuilder, indent: Int) {
        builder.appendIndent(indent)
        builder.append("<$name")
        builder.appendAttributes()
        builder.append(">\n")
        for (tag in tags) {
            tag.render(builder, indent + 1)
            if (tag != tags.last()) builder.append("\n")
        }
        builder.appendIndent(indent)
        builder.append("</$name>\n")
    }
}