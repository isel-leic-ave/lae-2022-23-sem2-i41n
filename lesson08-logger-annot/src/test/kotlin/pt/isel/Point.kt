package pt.isel

import kotlin.math.sqrt

class Point(@get:NonLog val x: Int, @get:NonLog val y: Int) {
    @get:AltName(label = "MODULE") val module get() = sqrt(x.toDouble()*x + y*y)
}