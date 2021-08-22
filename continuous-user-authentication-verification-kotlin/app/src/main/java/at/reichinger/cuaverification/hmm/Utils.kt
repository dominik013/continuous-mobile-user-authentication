package at.reichinger.cuaverification.hmm

import at.reichinger.cuaverification.hmm.math.Array2D
import at.reichinger.cuaverification.hmm.math.Array3D
import java.lang.Exception

object Utils {

    // Todo: Make this function more generic
    fun checkArrayValid(array: Array<Array<Double>>) {
        for (i in array) {
            for (j in i) {
                if (j.isInfinite().or(j.isNaN())) {
                    throw Exception("Input array is not valid since $j is not a valid input.")
                }
            }
        }
    }

    // Array Building

    fun arrangeArray2D(rows: Int, cols: Int): Array2D<Double> {
        val result =
            buildEmptyArray2D(rows, cols)
        var count = 0.0
        for (i in result.indices) {
            for (j in result[0].indices) {
                result[i][j] = count
                count += 1.0
            }
        }

        return result
    }

    fun buildEmptyArray2D(rows: Int, cols: Int, value: Double = 0.0): Array2D<Double> {
        return Array(rows) { Array(cols) { value } }
    }

    fun buildEmptyArray3D(depth: Int, rows: Int, cols: Int, value: Double = 0.0): Array3D<Double> {
        return Array(depth) { Array(rows) { Array(cols) { value } } }
    }
}