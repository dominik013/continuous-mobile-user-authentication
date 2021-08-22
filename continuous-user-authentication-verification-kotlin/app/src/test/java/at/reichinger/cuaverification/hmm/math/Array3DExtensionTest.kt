package at.reichinger.cuaverification.hmm.math

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class Array3DExtensionTest {

    @Test
    fun testDeepCopy() {

    }

    @Test
    fun testSubtractValuesWithOther() {
        val arr = Array(3) { Array(1) { Array(2) { 4.0 } } }
        val other = Array(2) { Array(2) { 2.0 } }
        val result = arr.subtractValuesWithOther(other)

        assertEquals(2.0, result[0][0][0])
        assertEquals(2.0, result[0][0][1])
        assertEquals(2.0, result[0][1][0])
        assertEquals(2.0, result[0][1][1])

        assertEquals(2.0, result[1][0][0])
        assertEquals(2.0, result[1][0][1])
        assertEquals(2.0, result[1][1][0])
        assertEquals(2.0, result[1][1][1])

        assertEquals(2.0, result[2][0][0])
        assertEquals(2.0, result[2][0][1])
        assertEquals(2.0, result[2][1][0])
        assertEquals(2.0, result[2][1][1])
    }

    @Test
    fun testPowValues() {
        val arr = Array(1) { Array(1) { Array(2) { 2.0 } } }
        val result = arr.powValues(2.0)
        assertEquals(4.0, result[0][0][0])
        assertEquals(4.0, result[0][0][1])
    }

    @Test
    fun testDivideValuesWithOther() {
        val arr = Array(3) { Array(2) { Array(2) { 8.0 } } }
        val other = Array(2) { arrayOf(2.0, 4.0) }
        val result = arr.divideValuesWithOther(other)

        assertEquals(4.0, result[0][0][0])
        assertEquals(2.0, result[0][0][1])
        assertEquals(4.0, result[0][1][0])
        assertEquals(2.0, result[0][1][1])

        assertEquals(4.0, result[1][0][0])
        assertEquals(2.0, result[1][0][1])
        assertEquals(4.0, result[1][1][0])
        assertEquals(2.0, result[1][1][1])

        assertEquals(4.0, result[2][0][0])
        assertEquals(2.0, result[2][0][1])
        assertEquals(4.0, result[2][1][0])
        assertEquals(2.0, result[2][1][1])
    }

    @Test
    fun testSumOverAxis() {
        val arr = Array(2) { Array(2) { Array(2) { 2.0 } } }
        val result = arr.sumOverAxis(2)

        assertEquals(4.0, result[0][0])
        assertEquals(4.0, result[0][1])
        assertEquals(4.0, result[1][0])
        assertEquals(4.0, result[1][1])
    }

    @Test
    fun testPlusValueDouble() {
        val arr = Array(2) { Array(2) { Array(2) { 2.0 } } }
        val result = arr.plusValue(3.0)

        assertEquals(5.0, result[0][0][0])
        assertEquals(5.0, result[0][0][1])
        assertEquals(5.0, result[0][1][0])
        assertEquals(5.0, result[0][1][1])

        assertEquals(5.0, result[1][0][0])
        assertEquals(5.0, result[1][0][1])
        assertEquals(5.0, result[1][1][0])
        assertEquals(5.0, result[1][1][1])
    }

    @Test
    fun testTimesValueDouble() {
        val arr = Array(2) { Array(2) { Array(2) { 2.0 } } }
        val result = arr.timesValue(3.0)

        assertEquals(6.0, result[0][0][0])
        assertEquals(6.0, result[0][0][1])
        assertEquals(6.0, result[0][1][0])
        assertEquals(6.0, result[0][1][1])

        assertEquals(6.0, result[1][0][0])
        assertEquals(6.0, result[1][0][1])
        assertEquals(6.0, result[1][1][0])
        assertEquals(6.0, result[1][1][1])
    }

    @Test
    fun testPlus2D() {
        val arr = Array(3) { Array(2) { Array(2) { 8.0 } } }
        val other = Array(2) { arrayOf(2.0, 4.0) }
        val result = arr.plus2D(other)

        assertEquals(10.0, result[0][0][0])
        assertEquals(12.0, result[0][0][1])
        assertEquals(10.0, result[0][1][0])
        assertEquals(12.0, result[0][1][1])

        assertEquals(10.0, result[1][0][0])
        assertEquals(12.0, result[1][0][1])
        assertEquals(10.0, result[1][1][0])
        assertEquals(12.0, result[1][1][1])

        assertEquals(10.0, result[2][0][0])
        assertEquals(12.0, result[2][0][1])
        assertEquals(10.0, result[2][1][0])
        assertEquals(12.0, result[2][1][1])
    }
}