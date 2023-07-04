package dev.bernasss12.dsl

import dev.bernasss12.dsl.svg.*
import dev.bernasss12.dsl.svg.defs.Css
import dev.bernasss12.dsl.svg.defs.Defs
import dev.bernasss12.dsl.svg.defs.Rule
import dev.bernasss12.dsl.svg.defs.Style

object New: ContainerTag("", "")

@TagMarker
inline fun svg(id: String = "", init: Svg.() -> Unit): Svg {
    val svg = Svg(id)
    svg.init()
    return svg
}

fun ContainerTag.conditionalG(id: String = "", condition: () -> Boolean, isTrue: G.() -> Unit = {}, isFalse: G.() -> Unit = {}) {
    if (condition.invoke()) {
        initComponent(G(id), isTrue)
    } else {
        initComponent(G(id), isFalse)
    }
}

@TagMarker
fun ContainerTag.text(id: String = "", init: Text.() -> Unit): Text = initComponent(Text(id), init)

@TagMarker
fun ContainerTag.g(id: String = "", init: G.() -> Unit): G = initComponent(G(id), init)

@TagMarker
fun ContainerTag.path(id: String = "", init: Path.() -> Unit): Path = initComponent(Path(id), init)

@TagMarker
fun ContainerTag.rect(id: String = "", init: Rect.() -> Unit): Rect = initComponent(Rect(id), init)

// Svg functions
@TagMarker
fun Svg.defs(init: Defs.() -> Unit): Defs = initComponent(Defs(), init)

// Defs functions
@TagMarker
fun Defs.style(init: Style.() -> Unit): Style = initComponent(Style(), init)
fun Defs.style(style: Style) = initComponent(style) {}

@TagMarker
fun Style.css(initialClass: String, vararg classes: String, init: Css.() -> Unit): Css = initComponent(Css(classes.toList() + initialClass), init)

@TagMarker
fun Css.rule(name: String, initialValue: String, block: Rule.() -> Unit = {}): Rule = initComponent(Rule(name, initialValue), block)

// General functions
@TagMarker
fun Tag.classes(vararg classes: String) {
    val value = attributes.getOrDefault("class", emptyList()).toMutableList()
    value.addAll(classes)
    addAttribute("class", *(value.toTypedArray()))
}