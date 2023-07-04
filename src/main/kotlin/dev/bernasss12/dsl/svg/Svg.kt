package dev.bernasss12.dsl.svg

import dev.bernasss12.dsl.ContainerTag
import dev.bernasss12.dsl.Tag

class Svg(id: String) : ContainerTag("svg", id) {

    override fun <T : Tag> initComponent(tag: T, init: T.() -> Unit): T {
        addAttribute("xmlns", "http://www.w3.org/2000/svg")
        return super.initComponent(tag, init)
    }

    fun xml(): String {
        val builder = StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
        render(builder, 0)
        return builder.toString()
    }
}