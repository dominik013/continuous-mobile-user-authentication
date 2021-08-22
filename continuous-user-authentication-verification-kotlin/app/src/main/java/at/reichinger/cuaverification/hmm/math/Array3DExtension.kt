package at.reichinger.cuaverification.hmm.math

import at.reichinger.cuaverification.hmm.Utils
import kotlin.math.pow

typealias Array3D<T> = Array<Array<Array<T>>>
var arr: Array3D<Double> = Utils.buildEmptyArray3D(2, 2, 2)

fun Array3D<Double>.deepCopy(): Array3D<Double> {
    val copy = Utils.buildEmptyArray3D(
        this.size,
        this[0].size,
        this[0][0].size
    )

    for (i in this.indices) {
        for (j in this[0].indices) {
            for (k in this[0][0].indices) {
                copy[i][j][k] = this[i][j][k]
            }
        }
    }

    return copy
}

// Array3D - Array2D (X - means)
fun Array3D<Double>.subtractValuesWithOther(other: Array2D<Double>): Array3D<Double> {
    // Todo: For now just works with (A * 1 * N) - (2 * N) objects
    if (this[0].size != 1) {
        throw Exception("Wrong dimension for 3D-Array ${this[0].size} should be 1!")
    }

    if (this[0][0].size != other[0].size) {
        throw Exception("Wrong dimensions ${this[0][0].size} != ${other[0].size} for last dimensions of both arrays")
    }

    val result = Array(this.size) { Array(other.size) { Array(other[0].size) { 0.0 } } }

    for (third in result.indices) {
        val temp = this[third][0].extendToTwoDimensions(other.size)
        for (second in result[0].indices) {
            for (first in result[0][0].indices) {
                result[third][second][first] = temp[second][first] - other[second][first]
            }
        }
    }

    return result
}

fun Array3D<Double>.powValues(x: Double): Array3D<Double> {
    val result = this.deepCopy()

    for (third in result.indices) {
        for (second in result[0].indices) {
            for (first in result[0][0].indices) {
                result[third][second][first] = result[third][second][first].pow(x)
            }
        }
    }

    return result
}

fun Array3D<Double>.divideValuesWithOther(other: Array2D<Double>): Array3D<Double> {
    // Todo: For now just works with (A * 2 * N) / (2 * N) objects

    if (this[0].size != other.size || this[0][0].size != other[0].size) {
        throw Exception("Wrong dimension for the arrays! Should have same dimension!")
    }

    val result = this.deepCopy()

    for (third in result.indices) {
        for (second in result[0].indices) {
            for (first in result[0][0].indices) {
                result[third][second][first] = result[third][second][first] / other[second][first]
            }
        }
    }

    return result
}

fun Array3D<Double>.sumOverAxis(axis: Int): Array2D<Double> {
    // Axis may be 0, 1, 2
    // Todo: Support for other axis
    if (axis != 2) {
        throw Exception("Summing over axis ($axis) is not supported yet!")
    }

    val result = Array(this.size) { Array(this[0].size) { 0.0 } }

    for (third in this.indices) {
        for (second in this[0].indices) {
            for (first in this[0][0].indices) {
                result[third][second] = result[third][second] + this[third][second][first]
            }
        }
    }

    return result
}

fun Array3D<Double>.plusValue(value: Double): Array3D<Double> {
    val result = this.deepCopy()

    for (third in result.indices) {
        for (second in result[0].indices) {
            for (first in result[0][0].indices) {
                result[third][second][first] = this[third][second][first] + value
            }
        }
    }

    return result
}

fun Array3D<Double>.timesValue(value: Double): Array3D<Double> {
    val result = this.deepCopy()

    for (third in result.indices) {
        for (second in result[0].indices) {
            for (first in result[0][0].indices) {
                result[third][second][first] = this[third][second][first] * value
            }
        }
    }

    return result
}

fun Array3D<Double>.plus2D(other: Array2D<Double>): Array3D<Double> {
    // Todo: For now just works with (A * 2 * N) / (2 * N) objects

    if (this[0].size != other.size || this[0][0].size != other[0].size) {
        throw Exception("Wrong dimension for the arrays! Should have same dimension!")
    }

    val result = this.deepCopy()

    for (third in result.indices) {
        for (second in result[0].indices) {
            for (first in result[0][0].indices) {
                result[third][second][first] = result[third][second][first] + other[second][first]
            }
        }
    }

    return result
}
