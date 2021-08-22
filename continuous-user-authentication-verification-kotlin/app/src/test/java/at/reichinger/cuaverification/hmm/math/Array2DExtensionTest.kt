package at.reichinger.cuaverification.hmm.math

import at.reichinger.cuaverification.hmm.Utils
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals


class Array2DExtensionTest {

    @Test
    fun testDeepCopy() {

    }

    @Test
    fun testPlusValueDouble() {
        val arr2D = Utils.buildEmptyArray2D(2, 2)
        val value = 4.0

        val result = arr2D.plusValue(value)

        assertEquals(4.0, result[0][0])
        assertEquals(4.0, result[0][1])
        assertEquals(4.0, result[1][0])
        assertEquals(4.0, result[1][1])
    }

    @Test
    fun testTimesValueDouble() {
        val arr = Array(2) { Array(2) { 2.0 } }
        val result = arr.timesValue(2.0)

        assertEquals(4.0, result[0][0])
        assertEquals(4.0, result[0][1])
        assertEquals(4.0, result[1][0])
        assertEquals(4.0, result[1][1])
    }


    @Test
    fun testPlusArray() {
        val arr2D = Utils.buildEmptyArray2D(2, 2)
        val arr = arrayOf(2.0, 4.0)

        val result = arr2D.plusArray(arr)

        assertEquals(2.0, result[0][0])
        assertEquals(4.0, result[0][1])
        assertEquals(2.0, result[1][0])
        assertEquals(4.0, result[1][1])
    }

    @Test
    fun testToThirdDimension() {
        val arr = Utils.buildEmptyArray2D(4, 2)
        var k = 0.0
        for (i in arr.indices) {
            for (j in arr[0].indices) {
                arr[i][j] = k
                k += 1.0
            }
        }

        val arr3D = arr.toThirdDimension()
        assertEquals(arr.size, arr3D.size)
        assertEquals(1, arr3D[0].size)
        assertEquals(arr[0].size, arr3D[0][0].size)
    }

    @Test
    fun testMaximumValue() {
        val arr = Array(2) { Array(2) { 2.0 } }
        val result = arr.maximumValue(3.0)

        assertEquals(3.0, result[0][0])
        assertEquals(3.0, result[0][1])
        assertEquals(3.0, result[1][0])
        assertEquals(3.0, result[1][1])
    }

    @Test
    fun testExp() {
        val arr = Utils.arrangeArray2D(2, 2)
        val result = arr.exp()

        assertEquals(1.000000000, result[0][0], 0.000001)
        assertEquals(2.718281830, result[0][1], 0.000001)
        assertEquals(7.389056100, result[1][0], 0.000001)
        assertEquals(20.08553692, result[1][1], 0.000001)
    }

    @Test
    fun testLogSumExpHorizontal() {
        val arr = Utils.arrangeArray2D(2, 2)
        val result = arr.logSumExpHorizontal()

        assertEquals(1.31326169, result[0], 0.000001)
        assertEquals(3.31326169, result[1], 0.000001)
    }
}