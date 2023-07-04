package dev.bernasss12.dsl.svg.defs

import dev.bernasss12.dsl.Tag

class Rule(val rule: String, initialValue: String) : Tag("", "") {
    private val values: MutableList<String> = mutableListOf(initialValue)

    override fun render(builder: StringBuilder, indent: Int) {
        if(rule.isBlank() || values.isEmpty() || values.joinToString().isBlank()) return
        builder.appendIndent(indent)
        builder.append("$rule: ${values.joinToString(", ")};\n")
    }

    operator fun String.unaryPlus() = values.add(this)
}