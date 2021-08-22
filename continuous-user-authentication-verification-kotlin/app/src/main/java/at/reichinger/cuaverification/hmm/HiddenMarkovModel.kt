package at.reichinger.cuaverification.hmm

import at.reichinger.cuaverification.hmm.math.Array2D
import at.reichinger.cuaverification.hmm.math.ln
import at.reichinger.cuaverification.hmm.math.logSumExp

abstract class HiddenMarkovModel {

    abstract fun computeLogLikelihood(data: Array2D<Double>): Array2D<Double>

    fun forwardPass(
        logProbabilities: Array2D<Double>,
        startProbabilities: Array<Double>,
        transitionMatrix: Array2D<Double>
    ): Double {
        val nSamples = logProbabilities.size
        val nComponents = logProbabilities[0].size
        val forwardProbabilities =
            Utils.buildEmptyArray2D(
                nSamples,
                nComponents
            )

        val startProbabilitiesLog = startProbabilities.ln()
        val transitionMatrixLog = transitionMatrix.ln()

        val buffer = Array(nComponents) { 0.0 }
        // Initial step
        for (i in 0 until nComponents) {
            forwardProbabilities[0][i] = startProbabilitiesLog[i] + logProbabilities[0][i]
        }

        // Recursion
        for (t in 1 until nSamples) {
            for (j in 0 until nComponents) {
                for (i in 0 until nComponents) {
                    buffer[i] = forwardProbabilities[t - 1][i] + transitionMatrixLog[i][j]
                }

                forwardProbabilities[t][j] = buffer.logSumExp() + logProbabilities[t][j]
            }
        }


        return forwardProbabilities[forwardProbabilities.size - 1].logSumExp()
    }
}