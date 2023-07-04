import dev.bernasss12.dsl.ContainerTag
import dev.bernasss12.dsl.svg.Path

fun ContainerTag.roundedBackgroundPath(id: String, width: Number, height: Number, init: Path.() -> Unit) = initComponent(Path(id)) {
    apply(init)
    val convertedWidth = width.toDouble()
    val convertedHeight = height.toDouble()
    val halfHeight = convertedHeight / 2
    val straightWidth = convertedWidth - halfHeight
    val origin = 0
    m(halfHeight, origin)
    h(straightWidth)
    v(convertedHeight)
    h(-straightWidth)
    Q(0, height, 0, halfHeight)
    Q(0, 0, halfHeight, 0)
    Z()
}

fun ContainerTag.flatBackgroundPath(id: String, width: Number, height: Number, init: Path.() -> Unit) = initComponent(Path(id)) {
    apply(init)
    m(0, 0)
    h(width)
    v(height)
    h(-width.toDouble())
    z()
}
