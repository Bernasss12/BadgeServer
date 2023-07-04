package dev.bernasss12.dsl.svg

import dev.bernasss12.dsl.ContainerTag
import dev.bernasss12.dsl.Tag

class G(id: String) : ContainerTag("g", id) {
    override fun <T : Tag> initComponent(tag: T, init: T.() -> Unit): T {
        super.initComponent(tag, init)
        tag.addAttributes(attributes.filter { it.key != id })
        return tag
    }
}