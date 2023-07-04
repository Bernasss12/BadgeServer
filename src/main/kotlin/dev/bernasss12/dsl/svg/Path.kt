package dev.bernasss12.dsl.svg

import dev.bernasss12.dsl.Tag

@Suppress("FunctionName")
class Path(id: String) : Tag("path", id) {
    private val instructions = mutableListOf<String>()

    override fun render(builder: StringBuilder, indent: Int) {
        addAttribute("d", instructions.joinToString(" "))
        super.render(builder, indent)
    }

    /**
     * Move to (absolute)
     */
    fun M(x: Number, y: Number) {
        instructions.add("M ${x.round()}, ${y.round()}")
    }

    /**
     * Move to (relative)
     */
    fun m(dx: Number, dy: Number) {
        instructions.add("m ${dx.round()}, ${dy.round()}")
    }

    /**
     * Line to (absolute)
     */
    fun L(x: Number, y: Number) {
        instructions.add("L ${x.round()}, ${y.round()}")
    }

    /**
     * Line to (relative)
     */
    fun l(dx: Number, dy: Number) {
        instructions.add("l ${dx.round()}, ${dy.round()}")
    }

    /**
     * Horizontal line to (absolute)
     */
    fun H(x: Number) {
        instructions.add("H ${x.round()}")
    }

    /**
     * Horizontal line to (relative)
     */
    fun h(dx: Number) {
        instructions.add("h ${dx.round()}")
    }

    /**
     * Vertical line to (absolute)
     */
    fun V(y: Number) {
        instructions.add("V ${y.round()}")
    }

    /**
     * Vertical line to (relative)
     */
    fun v(dy: Number) {
        instructions.add("v ${dy.round()}")
    }

    /**
     * Cubic bezier curve (absolute)
     * @param x1 control point for start point curve.
     * @param y1 control point for start point curve.
     * @param x2 control point for finish point curve.
     * @param y2 control point for finish point curve.
     * @param x finish point.
     * @param y finish point.
     */
    fun C(x1: Number, y1: Number, x2: Number, y2: Number, x: Number, y: Number) {
        instructions.add("C ${x1.round()}, ${y1.round()}, ${x2.round()}, ${y2.round()}, ${x.round()}, ${y.round()}")
    }

    /**
     * Cubic bezier curve (relative)
     * @param dx1 control point for start point curve.
     * @param dy1 control point for start point curve.
     * @param dx2 control point for finish point curve.
     * @param dy2 control point for finish point curve.
     * @param dx finish point.
     * @param dy finish point.
     */
    fun c(dx1: Number, dy1: Number, dx2: Number, dy2: Number, dx: Number, dy: Number) {
        instructions.add("c ${dx1.round()}, ${dy1.round()}, ${dx2.round()}, ${dy2.round()}, ${dx.round()}, ${dy.round()}")
    }

    /**
     * S shaped bezier curves (absolute)
     */
    fun S(x2: Number, y2: Number, x: Number, y: Number) {
        instructions.add("S ${x2.round()}, ${y2.round()}, ${x.round()}, ${y.round()}")
    }

    /**
     * S shaped bezier curves (relative)
     */
    fun s(dx2: Number, dy2: Number, dx: Number, dy: Number) {
        instructions.add("s ${dx2.round()}, ${dy2.round()}, ${dx.round()}, ${dy.round()}")
    }

    /**
     * S Quadratic curves (absolute)
     */
    fun Q(x2: Number, y2: Number, x: Number, y: Number) {
        instructions.add("Q ${x2.round()}, ${y2.round()}, ${x.round()}, ${y.round()}")
    }

    /**
     * Quadratic curves (relative)
     */
    fun q(dx2: Number, dy2: Number, dx: Number, dy: Number) {
        instructions.add("q ${dx2.round()}, ${dy2.round()}, ${dx.round()}, ${dy.round()}")
    }

    /**
     * Close path
     */
    fun Z() {
        instructions.add("Z")
    }

    /**
     * Close path
     */
    fun z() {
        instructions.add("z")
    }

    private fun Number.round(): String = when (val number = this) {
        is Double, is Float -> {
            String.format("%.2f", number)
        }

        else -> number.toString()
    }
}