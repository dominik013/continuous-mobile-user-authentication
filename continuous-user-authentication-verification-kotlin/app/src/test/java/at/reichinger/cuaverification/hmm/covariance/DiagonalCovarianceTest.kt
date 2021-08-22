package at.reichinger.cuaverification.hmm.covariance

import at.reichinger.cuaverification.hmm.Utils
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class DiagonalCovarianceTest {

    @Test
    fun testLogMultivariateNormalDensity() {
        val data = Utils.buildEmptyArray2D(5, 2, value = 10.0)
        val means = Utils.buildEmptyArray2D(2, 2, value = 4.0)
        val covars =  Utils.buildEmptyArray2D(2, 2, value = 4.0)

        val diagonalCovariance =
            DiagonalCovariance()
        val density = diagonalCovariance.logMultivariateNormalDensity(data, means, covars)

        assertEquals(-12.2241714, density[0][0], 0.0000001)
        assertEquals(-12.2241714, density[0][1], 0.0000001)
        assertEquals(-12.2241714, density[1][0], 0.0000001)
        assertEquals(-12.2241714, density[1][1], 0.0000001)
        assertEquals(-12.2241714, density[2][0], 0.0000001)
        assertEquals(-12.2241714, density[2][1], 0.0000001)
        assertEquals(-12.2241714, density[3][0], 0.0000001)
        assertEquals(-12.2241714, density[3][1], 0.0000001)
        assertEquals(-12.2241714, density[4][0], 0.0000001)
        assertEquals(-12.2241714, density[4][1], 0.0000001)
    }

    @Test
    fun testLogMultivariateNormalDensityArrangedInput() {
        val data = Utils.arrangeArray2D(5, 2)
        val means = Utils.buildEmptyArray2D(2, 2, value = 4.0)
        val covars =  Utils.buildEmptyArray2D(2, 2, value = 4.0)

        val diagonalCovariance =
            DiagonalCovariance()
        val density = diagonalCovariance.logMultivariateNormalDensity(data, means, covars)

        assertEquals(-6.34917143, density[0][0], 0.0000001)
        assertEquals(-6.34917143, density[0][1], 0.0000001)
        assertEquals(-3.84917143, density[1][0], 0.0000001)
        assertEquals(-3.84917143, density[1][1], 0.0000001)
        assertEquals(-3.34917143, density[2][0], 0.0000001)
        assertEquals(-3.34917143, density[2][1], 0.0000001)
        assertEquals(-4.84917143, density[3][0], 0.0000001)
        assertEquals(-4.84917143, density[3][1], 0.0000001)
        assertEquals(-8.34917143, density[4][0], 0.0000001)
        assertEquals(-8.34917143, density[4][1], 0.0000001)
    }

    @Test
    fun testLogMultivariateNormalDensityArrangedMeans() {
        val data = Utils.buildEmptyArray2D(5, 2, value = 10.0)
        val means = Utils.arrangeArray2D(2, 2)
        val covars =  Utils.buildEmptyArray2D(2, 2, value = 4.0)

        val diagonalCovariance =
            DiagonalCovariance()
        val density = diagonalCovariance.logMultivariateNormalDensity(data, means, covars)

        assertEquals(-25.84917143, density[0][0], 0.0000001)
        assertEquals(-17.34917143, density[0][1], 0.0000001)
        assertEquals(-25.84917143, density[1][0], 0.0000001)
        assertEquals(-17.34917143, density[1][1], 0.0000001)
        assertEquals(-25.84917143, density[2][0], 0.0000001)
        assertEquals(-17.34917143, density[2][1], 0.0000001)
        assertEquals(-25.84917143, density[3][0], 0.0000001)
        assertEquals(-17.34917143, density[3][1], 0.0000001)
        assertEquals(-25.84917143, density[4][0], 0.0000001)
        assertEquals(-17.34917143, density[4][1], 0.0000001)
    }

    @Test
    fun testLogMultivariateNormalDensityCovarianceValueZero() {
        val data = Utils.arrangeArray2D(5, 2)
        val means = Utils.buildEmptyArray2D(2, 2, value = 4.0)
        val covars =  Utils.arrangeArray2D(2, 2) // covars[0][0] is equal to zero

        val diagonalCovariance =
            DiagonalCovariance()
        val density = diagonalCovariance.logMultivariateNormalDensity(data, means, covars)

        assertEquals(Double.NEGATIVE_INFINITY, density[0][0], 0.0000001)
        assertEquals(-8.23375680, density[0][1], 0.0000001)
        assertEquals(Double.NEGATIVE_INFINITY, density[1][0], 0.0000001)
        assertEquals(-3.90042346, density[1][1], 0.0000001)
        // This value differs from the python implementation, because of how Double.MIN_VALUE is defined
        assertEquals(369.8821588, density[2][0], 0.0000001)
        assertEquals(-2.90042346, density[2][1], 0.0000001)
        assertEquals(Double.NEGATIVE_INFINITY, density[3][0], 0.0000001)
        assertEquals(-5.23375680, density[3][1], 0.0000001)
        assertEquals(Double.NEGATIVE_INFINITY, density[4][0], 0.0000001)
        assertEquals(-10.9004234, density[4][1], 0.0000001)
    }
}