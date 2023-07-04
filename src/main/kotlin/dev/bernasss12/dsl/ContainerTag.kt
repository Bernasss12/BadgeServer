package dev.bernasss12.dsl

@TagMarker
abstract class ContainerTag(name: String, id: String) : Tag(name, id) {

    internal val tags: MutableList<Tag> = mutableListOf()

    override fun render(builder: StringBuilder, indent: Int) {
        builder.appendIndent(indent)
        builder.append("<$name")
        builder.appendAttributes()
        builder.append(">\n")
        for (tag in tags) {
            tag.render(builder, indent + 1)
        }
        builder.appendIndent(indent)
        builder.append("</$name>\n")
    }

    override fun <T : Tag> initComponent(tag: T, init: T.() -> Unit): T {
        super.initComponent(tag, init)
        tags.add(tag)
        return tag
    }
}