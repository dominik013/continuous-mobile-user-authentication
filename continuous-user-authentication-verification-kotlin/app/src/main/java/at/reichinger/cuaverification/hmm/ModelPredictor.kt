package at.reichinger.cuaverification.hmm

import at.reichinger.cuaverification.hmm.math.Array2D

class ModelPredictor(private val model: GMMHMM) {

    fun score(sequence: Array2D<Double>): Double {
        if (sequence.isEmpty()) {
            throw PredictionException("Can't predict empty sequence.")
        }

        ModelVerifier(model).validateModelParameters()
        Utils.checkArrayValid(sequence)

        val frameLogProb = model.computeLogLikelihood(sequence)
        return model.forwardPass(frameLogProb, model.startProbabilities, model.transitionMatrix)
    }

    fun scoreNormalized(sequence: Array2D<Double>): Double {
        return score(sequence) / sequence.size
    }

}