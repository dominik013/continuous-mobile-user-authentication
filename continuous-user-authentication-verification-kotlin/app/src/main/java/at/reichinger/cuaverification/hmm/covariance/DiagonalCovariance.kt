package at.reichinger.cuaverification.hmm.covariance

import at.reichinger.cuaverification.hmm.math.*
import kotlin.math.ln

class DiagonalCovariance : CovarianceType() {

    /**
     * Compute Gaussian log-density at {data} for a diagonal model
     *
     * @param data (nSamples, nComponents)
     * @param means (nComponents, nFeatures)
     * @param covars (nComponents, nFeatures)
     * @return result (nSamples, nComponents)
     */
    override fun logMultivariateNormalDensity(
        data: Array2D<Double>,
        means: Array2D<Double>,
        covars: Array2D<Double>
    ): Array2D<Double> {
        if (data.isEmpty() || data[0].isEmpty()) {
            throw Exception("Input data is of invalid shape")
        }

        val nDimensions = data[0].size
        // Avoid log 0 = NaN
        val covarsFixed = covars.maximumValue(Double.MIN_VALUE)

        val factor = -0.5
        val first = nDimensions * ln(2 * Math.PI)
        val second = covarsFixed.ln().sumOverAxis(true)
        val third = data.toThirdDimension()
            .subtractValuesWithOther(means)
            .powValues(2.0)
            .divideValuesWithOther(covarsFixed)
            .sumOverAxis(2)

        return third.plusArray(second).plusValue(first).timesValue(factor)
    }
}