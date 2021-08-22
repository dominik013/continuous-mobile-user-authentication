package at.reichinger.cuaverification.hmm.math

import at.reichinger.cuaverification.hmm.Utils
import java.lang.Exception
import kotlin.math.ln
import kotlin.math.max

typealias Array2D<T> = Array<Array<T>>

fun Array2D<Double>.deepCopy(): Array2D<Double> {
    val copy = Utils.buildEmptyArray2D(
        this.size,
        this[0].size
    )

    for (i in this.indices) {
        for (j in this[0].indices) {
            copy[i][j] = this[i][j]
        }
    }

    return copy
}

fun Array2D<Double>.string(): String {
    val builder = StringBuilder()
    for (i in this.indices) {
        builder.append("|\t")
        for (j in this[0].indices) {
            builder.append(this[i][j])
            builder.append("\t")
        }
        builder.append("|\n")
    }

    return builder.toString()
}

fun Array2D<Double>.ln(): Array2D<Double> {
    if (this.isEmpty()) return this

    val result = this.deepCopy()

    for (i in this.indices) {
        for (j in this[0].indices) {
            result[i][j] = ln(this[i][j])
        }
    }

    return result
}

fun Array2D<Double>.sumOverAxis(horizontal: Boolean): Array<Double> {
    if (this.isEmpty()) {
        throw Exception("Did not expect an empty 2D-array when summing up")
    }

    val size = if (horizontal) this.size else this[0].size
    val result = Array(size) { 0.0 }

    if (horizontal) {
        for (i in this.indices) {
            for (j in this[0].indices) {
                result[i] = result[i] + this[i][j]
            }
        }
    } else {
        for(i in this[0].indices) {
            for(j in this.indices) {
                result[i] = result[i] + this[j][i]
            }
        }
    }

    return result
}

fun Array2D<Double>.plusValue(value: Double): Array2D<Double> {
    if (this.isEmpty()) return this

    val result = this.deepCopy()

    for (i in this.indices) {
        for (j in this[0].indices) {
            result[i][j] += value
        }
    }

    return result
}

fun Array2D<Double>.plusArray(other: Array<Double>): Array2D<Double> {
    if (this.isEmpty()) return this

    if (this[0].size != other.size) {
        throw Exception("Dimensions are not matching!")
    }

    val result = this.deepCopy()

    for (i in this.indices) {
        for (j in this[0].indices) {
            result[i][j] += other[j]
        }
    }

    return result
}

fun Array2D<Double>.timesValue(value: Double): Array2D<Double> {
    val result = this.deepCopy()

    for (second in result.indices) {
        for (first in result[0].indices) {
            result[second][first] = this[second][first] * value
        }
    }

    return result
}

fun Array2D<Double>.toThirdDimension(): Array3D<Double> {
    val result = Utils.buildEmptyArray3D(
        this.size,
        1,
        this[0].size
    )

    for (i in result.indices) {
        result[i][0] = this[i]
    }

    return result
}

fun Array2D<Double>.maximumValue(value: Double): Array2D<Double> {
    val result = this.deepCopy()

    for (second in result.indices) {
        for (first in result[0].indices) {
            result[second][first] = max(this[second][first], value)
        }
    }

    return result
}

fun Array2D<Double>.exp(): Array2D<Double> {
    val result = this.deepCopy()

    for (second in this.indices) {
        for (first in this[0].indices) {
            result[second][first] = kotlin.math.exp(result[second][first])
        }
    }

    return result
}

fun Array2D<Double>.logSumExpHorizontal(): Array<Double> {
    val result = this.deepCopy()
    val exponentiation  = result.exp()
    val summed = exponentiation.sumOverAxis(true)
    return summed.ln()
}