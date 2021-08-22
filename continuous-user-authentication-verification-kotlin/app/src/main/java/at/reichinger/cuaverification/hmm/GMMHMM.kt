package at.reichinger.cuaverification.hmm

import at.reichinger.cuaverification.hmm.covariance.CovarianceType
import at.reichinger.cuaverification.hmm.covariance.SphericalCovariance
import at.reichinger.cuaverification.hmm.math.*
import com.google.gson.annotations.SerializedName

data class GMMHMM(
    @SerializedName("n_components") var nComponents: Int,
    @SerializedName("n_features") var nFeatures: Int,
    @SerializedName("n_mixtures") var nMixtures: Int,
    @SerializedName("covariance_type") var covarianceType: CovarianceType,
    @SerializedName("start_probabilities") var startProbabilities: Array<Double>,
    @SerializedName("transition_matrix") var transitionMatrix: Array2D<Double>,
    @SerializedName("weights") var weights: Array2D<Double>,
    @SerializedName("means") var means: Array3D<Double>,
    @SerializedName("covariance_matrix") var covarianceMatrix: Array3D<Double>

): HiddenMarkovModel() {

    /**
     * @param data Input data
     * @return Result (nSamples, nComponents)
     */
    override fun computeLogLikelihood(data: Array2D<Double>): Array2D<Double> {
        val rows = data.size
        val cols = nComponents

        // Build empty two dimensional array with rows * cols
        val result =
            Utils.buildEmptyArray2D(rows, cols)

        for (i in 0 until cols) {
            val logDensities = computeLogWeightedGaussianDensities(data, i)
            val logSumExp = logDensities.logSumExpHorizontal()

            for (j in 0 until rows) {
                result[j][i] = logSumExp[j]
            }
        }

        return result
    }


    private fun computeLogWeightedGaussianDensities(data: Array2D<Double>, ith_component: Int): Array2D<Double> {
        val currentMeans = means[ith_component]
        val currentCovars = covarianceMatrix[ith_component]
        val logCurrentWeights = weights[ith_component].ln()

        if (covarianceType is SphericalCovariance) {
            // Todo: currentCovars array dimension has to be adjusted (add one dimension for columns)
            // cur_covs = cur_covs[:, np.newaxis]
        }

        val logMultiNormalDensity = covarianceType.logMultivariateNormalDensity(data, currentMeans, currentCovars)
        return logMultiNormalDensity.plusArray(logCurrentWeights)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GMMHMM

        if (nComponents != other.nComponents) return false
        if (!startProbabilities.contentEquals(other.startProbabilities)) return false
        if (!transitionMatrix.contentDeepEquals(other.transitionMatrix)) return false
        if (!weights.contentDeepEquals(other.weights)) return false
        if (!means.contentDeepEquals(other.means)) return false
        if (!covarianceMatrix.contentDeepEquals(other.covarianceMatrix)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nComponents
        result = 31 * result + startProbabilities.contentHashCode()
        result = 31 * result + transitionMatrix.contentDeepHashCode()
        result = 31 * result + weights.contentDeepHashCode()
        result = 31 * result + means.contentDeepHashCode()
        result = 31 * result + covarianceMatrix.contentDeepHashCode()
        return result
    }
}