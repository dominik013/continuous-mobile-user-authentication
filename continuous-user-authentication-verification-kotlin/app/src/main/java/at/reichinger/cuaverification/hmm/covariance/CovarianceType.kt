package at.reichinger.cuaverification.hmm.covariance

import at.reichinger.cuaverification.hmm.math.Array2D

abstract class CovarianceType() {
    abstract fun logMultivariateNormalDensity(data: Array2D<Double>, means: Array2D<Double>, covars: Array2D<Double>): Array2D<Double>
}