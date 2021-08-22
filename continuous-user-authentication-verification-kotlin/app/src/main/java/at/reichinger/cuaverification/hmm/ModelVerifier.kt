package at.reichinger.cuaverification.hmm

import at.reichinger.cuaverification.hmm.math.almostOne

open class ModelVerifier(private val model: GMMHMM) {

    open fun validateModelParameters() {
        if (!startProbabilitiesSumToOne()) {
            throw PredictionException("Start probabilities must sum to one")
        }

        if (!isTransitionMatrixShapeCorrect()) {
            throw PredictionException(
                String.format(
                    "Transition matrix has to be of shape: %d x %d",
                    model.nComponents,
                    model.nComponents
                )
            )
        }

        if (!transitionMatrixRowsSumToOne()) {
            throw PredictionException(
                String.format(
                    "Transition matrix rows have to sum to one! Current sums: %s",
                    getTransitionMatrixRowSums()
                )
            )
        }
    }

    private fun getTransitionMatrixRowSums(): String {
        val builder = StringBuilder("{ ")
        for (row in model.transitionMatrix) {
            builder.append(row.sum())
            builder.append(", ")
        }
        builder.append(" }")
        return builder.toString()
    }

    private fun transitionMatrixRowsSumToOne(): Boolean {
        for (row in model.transitionMatrix) {
            if (!row.sum().almostOne()) {
                return false
            }
        }

        return true

    }

    private fun isTransitionMatrixShapeCorrect(): Boolean {
        return model.transitionMatrix.size == model.nComponents && model.transitionMatrix[0].size == model.nComponents
    }

    private fun startProbabilitiesSumToOne(): Boolean {
        return model.startProbabilities.sum().almostOne()
    }
}