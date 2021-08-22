package at.reichinger.cuaverification.hmm.math

import at.reichinger.cuaverification.hmm.Utils
import kotlin.math.ln

fun Array<Double>.deepCopy(): Array<Double> {
    val copy = Array(this.size) { 0.0 }

    for (i in this.indices) {
        copy[i] = this[i]
    }

    return copy
}

fun Array<Double>.sumValuesWithOther(other: Array<Double>): Array<Double> {
    if (this.size != other.size) {
        throw Exception("Array.plus expects arrays of same length to be summed up")
    }

    val result = this.deepCopy()

    for (i in this.indices) {
        result[i] = this[i] + other[i]
    }

    return result
}

fun Array<Double>.ln(): Array<Double> {
    val result = this.deepCopy()

    for (i in this.indices) {
        result[i] = ln(this[i])
    }

    return result
}

fun Array<Double>.extendToTwoDimensions(rows: Int): Array2D<Double> {
    val result =
        Utils.buildEmptyArray2D(rows, this.size)

    for (second in result.indices) {
        for (first in result[0].indices) {
            result[second][first] = this[first]
        }
    }

    return result
}

fun Array<Double>.padToTwoDimensions(rows: Int, paddingValue: Double): Array2D<Double> {
    val result = Array(rows) { Array(this.size) { paddingValue } }
    for (i in this.indices) {
        result[0][i] = this[i]
    }

    return result
}

fun Array<Double>.exp(): Array<Double> {
    val result = this.deepCopy()

    for (first in this.indices) {
        result[first] = kotlin.math.exp(result[first])
    }

    return result
}

fun Array<Double>.logSumExp(): Double {
    val result = this.deepCopy()
    val exponentiation  = result.exp()
    val summed = exponentiation.sum()
    return ln(summed)
}
