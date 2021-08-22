package at.reichinger.cuaverification.hmm.math

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.math.ln

class ArrayExtensionTest {

    @Test
    fun testDeepCopy() {

    }

    @Test
    fun testSumValuesWithOther() {
        val arr = arrayOf(1.0, 2.0, 3.0, 4.0)
        val other = arrayOf(1.0, 2.0, 3.0, 4.0)
        val result = arr.sumValuesWithOther(other)

        assertEquals(2.0, result[0])
        assertEquals(4.0, result[1])
        assertEquals(6.0, result[2])
        assertEquals(8.0, result[3])
    }

    @Test
    fun testLn() {
        val arr = arrayOf(1.0, 2.0, 3.0, 4.0)
        val result = arr.ln()

        assertEquals(ln(1.0), result[0], 0.00001)
        assertEquals(ln(2.0), result[1], 0.00001)
        assertEquals(ln(3.0), result[2], 0.00001)
        assertEquals(ln(4.0), result[3], 0.00001)
    }

    @Test
    fun testExtendToTwoDimensions() {
        val arr = arrayOf(1.0, 2.0)
        val result = arr.extendToTwoDimensions(3)
        assertEquals(3, result.size)
        assertEquals(2, result[0].size)
        assertEquals(1.0, result[0][0])
        assertEquals(2.0, result[0][1])
        assertEquals(1.0, result[1][0])
        assertEquals(2.0, result[1][1])
        assertEquals(1.0, result[2][0])
        assertEquals(2.0, result[2][1])
    }

    @Test
    fun testPadToTwoDimensions() {
        val arr = arrayOf(1.0, 2.0)
        val result = arr.padToTwoDimensions(3, 0.0)
        assertEquals(3, result.size)
        assertEquals(2, result[0].size)
        assertEquals(1.0, result[0][0])
        assertEquals(2.0, result[0][1])
        assertEquals(0.0, result[1][0])
        assertEquals(0.0, result[1][1])
        assertEquals(0.0, result[2][0])
        assertEquals(0.0, result[2][1])
    }

    @Test
    fun testExp() {
        val arr = arrayOf(1.0, 2.0, 3.0, 4.0)
        val result = arr.exp()

        assertEquals(2.718281830, result[0], 0.000001)
        assertEquals(7.389056100, result[1], 0.000001)
        assertEquals(20.08553692, result[2], 0.000001)
        assertEquals(54.59815003, result[3], 0.000001)
    }

    @Test
    fun testLogSumExp() {
        val arr = arrayOf(1.0, 2.0, 3.0, 4.0)
        val result = arr.logSumExp()

        assertEquals(4.44018969, result, 0.000001)
    }
}