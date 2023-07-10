package dev.bernasss12.templater

import java.io.File

class Templater(file: File) {

    private val contents = file.readText()

    /**
     * The expected format is as follows:
     *  - "{!(text.svg)}" will be replaced with the given value.
     *  - "text.svg" can either be a key or a key -> default value pair.
     *  - "text.svg" without default value will be considered "required"
     *  - key -> default value are separated by "==".
     *  - default value will only be used if there is no matching key in [valuesMap]
     *  - if there is no matching key in [valuesMap] and no default value for a key this method will complain.
     */
    public fun replacePlaceholders(valuesMap: Map<String, Any>): String {
        val regex = "\\{!(.+?)}".toRegex(RegexOption.DOT_MATCHES_ALL)
        val results = regex.findAll(contents).map(::Replace)

        val used = mutableSetOf<String>()
        val missing = mutableSetOf<String>()

        var replaced = contents
        results.forEach { match ->
            if (valuesMap.containsKey(match.key)) {
                replaced = replaced.replace(match.replace, valuesMap[match.key].toString())
                used.add(match.key)
            } else if (match.hasDefault) {
                replaced = replaced.replace(match.replace, match.default)
                missing.add(match.key)
            } else {
                println("Match ${match.key} did not have value or default.")
                missing.add(match.key)
            }
        }

        val unused = valuesMap.map { it.key }.toMutableSet().also { it.removeAll(used) }
        println("Unused: $unused")

        return replaced
    }

    fun replacePlaceholders(vararg values: Pair<String, Any>) = replacePlaceholders(valuesMap = values.toMap())

    fun replaceWithSingleText(text: String, width: Int, template: Template) =
        replacePlaceholders(
            "text" to text,
            "text-x" to (width + template.padding) / 2.0 + template.minWidth,
            "text-y" to template.textHeightOffset,
            "text-rect-width" to width + template.padding + template.minWidth,
            "text-rect-height" to template.height,
            "svg-width" to width + template.padding + template.minWidth,
            "svg-height" to template.height,
        )

    fun replaceText(text: String) {

    }

    class Replace(match: MatchResult) {
        val replace = match.value
        val content = match.groupValues[1]
        val key: String
        val default: String

        val hasDefault: Boolean
            get() = key != default

        init {
            content.split("==").let {
                key = it.first()
                default = it.last()
            }
        }
    }

}