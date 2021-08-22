package at.reichinger.cuaverification.hmm

import android.util.Log
import at.reichinger.cuaverification.hmm.covariance.DiagonalCovariance
import at.reichinger.cuaverification.hmm.math.almostOne

class GMMHMMVerifier(private val model: GMMHMM) : ModelVerifier(model) {

    companion object {
        private const val TAG = "GMMHMMVerifier"
    }

    override fun validateModelParameters() {
        super.validateModelParameters()
        setNumberOfFeaturesIfNotPresent()
        // TODO: Init covar priors
        // TODO: Fix priors shape
        checkMixtureWeightsShape()
        checkMixtureWeightsSumToOne()

        checkMeansShape()

        checkCovarianceMatrixShape()
        checkCovarianceMatrixValues()
    }

    private fun checkCovarianceMatrixValues() {
        //TODO: different co-variance types to have a different matrix shape - only for diagonal
        if (model.covarianceType !is DiagonalCovariance) {
            throw Exception(
                String.format(
                    "Covariance Type {%s} not implemented",
                    model.covarianceType.javaClass.simpleName
                )
            )
        }

        for (i in model.covarianceMatrix) {
            for (j in i) {
                for (k in j) {
                    if (k < 0) {
                        throw PredictionException(
                            "Mixture covariance matrix values must be non-negative"
                        )
                    }

                    if (k == 0.0) {
                        Log.w(TAG, "Degenerate mixture covariance")
                    }
                }
            }
        }
    }

    /**
     * nc = nComponents | nm = nMixtures | nf = nFeatures
     * needed_shapes = {
     *   "spherical": (nc, nm),
     *   "tied": (nc, nf, nf),
     *   "diagonal": (nc, nm, nf),
     *   "full": (nc, nm, nf, nf),
     * }
     */
    private fun checkCovarianceMatrixShape() {
        //TODO: See description, different co-variance types to have a different matrix shape
        if (model.covarianceType !is DiagonalCovariance) {
            throw Exception(
                String.format(
                    "Covariance Type {%s} not implemented",
                    model.covarianceType.javaClass.simpleName
                )
            )
        }

        if (model.covarianceMatrix.size != model.nComponents ||
            model.covarianceMatrix[0].size != model.nMixtures || model.covarianceMatrix[0][0].size != model.nFeatures
        ) {
            throw PredictionException(
                String.format(
                    "Mixture means must be of shape (%d, %d, %d) but actual shape is (%d, %d, %d)",
                    model.nComponents,
                    model.nMixtures,
                    model.nFeatures,
                    model.covarianceMatrix.size,
                    model.covarianceMatrix[0].size,
                    model.covarianceMatrix[0][0].size
                )
            )
        }
    }

    private fun checkMeansShape() {
        if (model.means.size != model.nComponents ||
            model.means[0].size != model.nMixtures || model.means[0][0].size != model.nFeatures
        ) {
            throw PredictionException(
                String.format(
                    "Mixture means must be of shape (%d, %d, %d) but actual shape is (%d, %d, %d)",
                    model.nComponents,
                    model.nMixtures,
                    model.nFeatures,
                    model.means.size,
                    model.means[0].size,
                    model.means[0][0].size
                )
            )
        }
    }

    private fun checkMixtureWeightsSumToOne() {
        for (weight in model.weights) {
            if (!weight.sum().almostOne()) {
                throw PredictionException("Mixture weights must sum up to one")
            }
        }
    }

    private fun checkMixtureWeightsShape() {
        // nComponents is always > 0
        if (model.weights.size != model.nComponents || model.weights[0].size != model.nMixtures) {
            throw PredictionException(
                String.format(
                    "Mixture weights must be of shape (%d, %d) but actual shape is (%d, %d)",
                    model.nComponents,
                    model.nMixtures,
                    model.weights.size,
                    model.weights[0].size
                )
            )
        }
    }

    private fun setNumberOfFeaturesIfNotPresent() {
        if (model.nFeatures == 0) {
            if (model.means.isNotEmpty() && model.means[0].isNotEmpty() && model.means[0][0].isNotEmpty()) {
                model.nFeatures = model.means[0][0].size
            }
        }
    }
}