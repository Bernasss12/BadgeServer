package dev.bernasss12.dsl.svg.defs

import dev.bernasss12.dsl.Tag
import dev.bernasss12.dsl.classes
import dev.bernasss12.dsl.rule

class Css(private val classes: List<String>) : Tag("", "") {

    private val rules: MutableList<Rule> = mutableListOf()

    override fun render(builder: StringBuilder, indent: Int) {
        builder.appendIndent(indent)
        builder.append(".${classes.joinToString(", .")} {\n")
        rules.forEach { rule ->
            rule.render(builder, indent + 1)
        }
        builder.appendIndent(indent)
        builder.append("}\n")
    }

    override fun <T : Tag> initComponent(tag: T, init: T.() -> Unit): T {
        tag.apply(init)
        val rule = tag as? Rule
        rule?.let {
            rules.add(tag)
        }
        return tag
    }
}