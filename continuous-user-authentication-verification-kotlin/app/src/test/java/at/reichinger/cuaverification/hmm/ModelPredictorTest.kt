package at.reichinger.cuaverification.hmm

import at.reichinger.cuaverification.hmm.covariance.DiagonalCovariance
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class ModelPredictorTest {

    @Test
    fun testScore() {
        val nComponents = 2
        val nFeatures = 2
        val nMixtures = 4
        val covarianceType =
            DiagonalCovariance()
        val startProbabilities = arrayOf(0.80955171, 0.19044829)
        val transitionMatrix = arrayOf(
            arrayOf(0.83314864, 0.16685136),
            arrayOf(0.16320584, 0.83679416)
        )
        val weights = arrayOf(
            arrayOf(0.55415349, 0.0958718, 0.00283712, 0.34713759),
            arrayOf(0.08296005, 0.23503473, 0.67943977, 0.00256544)
        )
        val means = arrayOf(
            arrayOf(
                arrayOf(0.75955234, 0.77588942),
                arrayOf(0.75299374, 0.78010704),
                arrayOf(0.75552408, 0.77688448),
                arrayOf(0.75810589, 0.77554861)
            ),
            arrayOf(
                arrayOf(0.80241694, 0.72126625),
                arrayOf(0.79199782, 0.73424346),
                arrayOf(0.79654626, 0.73140011),
                arrayOf(0.79452561, 0.7296996)
            )
        )
        val covars = arrayOf(
            arrayOf(
                arrayOf(0.00136156, 0.00154034),
                arrayOf(0.00146366, 0.00163006),
                arrayOf(0.00167788, 0.00196025),
                arrayOf(0.00153997, 0.00164185)
            ),
            arrayOf(
                arrayOf(0.00109649, 0.00211153),
                arrayOf(0.00157526, 0.00260401),
                arrayOf(0.00120878, 0.0020494),
                arrayOf(0.00170981, 0.00287916)
            )
        )

        val gmmHmm = GMMHMM(
            nComponents,
            nFeatures,
            nMixtures,
            covarianceType,
            startProbabilities,
            transitionMatrix,
            weights,
            means,
            covars
        )

        val predictor = ModelPredictor(gmmHmm)
        val score = predictor.score(sequence)
        val scoreNormalized = predictor.scoreNormalized(sequence)

        assertEquals(412.25530, score, 0.00001)
        assertEquals(3.5235495, scoreNormalized, 0.00001)
    }

    private val sequence = arrayOf(
        arrayOf(0.69891412, 0.83724978),
        arrayOf(0.69891412, 0.83637946),
        arrayOf(0.69990128, 0.83637946),
        arrayOf(0.69990128, 0.83550914),
        arrayOf(0.70088845, 0.83550914),
        arrayOf(0.70088845, 0.83420366),
        arrayOf(0.70187562, 0.83420366),
        arrayOf(0.70286278, 0.83246301),
        arrayOf(0.70286278, 0.83028721),
        arrayOf(0.70384995, 0.83028721),
        arrayOf(0.70384995, 0.82767624),
        arrayOf(0.70681145, 0.82767624),
        arrayOf(0.70681145, 0.82463011),
        arrayOf(0.70878578, 0.82463011),
        arrayOf(0.70878578, 0.82114883),
        arrayOf(0.71273445, 0.82114883),
        arrayOf(0.71273445, 0.81679721),
        arrayOf(0.71668312, 0.81679721),
        arrayOf(0.71668312, 0.81114012),
        arrayOf(0.71865745, 0.81114012),
        arrayOf(0.71865745, 0.80722367),
        arrayOf(0.72063179, 0.80722367),
        arrayOf(0.72063179, 0.80374238),
        arrayOf(0.72260612, 0.80374238),
        arrayOf(0.72260612, 0.80113142),
        arrayOf(0.72359329, 0.80113142),
        arrayOf(0.72359329, 0.79808529),
        arrayOf(0.72458045, 0.79808529),
        arrayOf(0.72458045, 0.79590949),
        arrayOf(0.72556762, 0.79590949),
        arrayOf(0.72556762, 0.79373368),
        arrayOf(0.72655479, 0.79373368),
        arrayOf(0.72655479, 0.79199304),
        arrayOf(0.72754195, 0.79199304),
        arrayOf(0.72754195, 0.79025239),
        arrayOf(0.72852912, 0.79025239),
        arrayOf(0.72852912, 0.78894691),
        arrayOf(0.72951629, 0.78764143),
        arrayOf(0.72951629, 0.78633594),
        arrayOf(0.73050346, 0.78633594),
        arrayOf(0.73050346, 0.7845953),
        arrayOf(0.73149062, 0.7845953),
        arrayOf(0.73149062, 0.78285466),
        arrayOf(0.73247779, 0.78285466),
        arrayOf(0.73247779, 0.78067885),
        arrayOf(0.73445212, 0.78067885),
        arrayOf(0.73543929, 0.77806789),
        arrayOf(0.73543929, 0.77502176),
        arrayOf(0.73741362, 0.77502176),
        arrayOf(0.73741362, 0.77241079),
        arrayOf(0.73938796, 0.76979983),
        arrayOf(0.74234946, 0.7667537),
        arrayOf(0.74432379, 0.76414273),
        arrayOf(0.74432379, 0.76196693),
        arrayOf(0.74629812, 0.76196693),
        arrayOf(0.74629812, 0.75979112),
        arrayOf(0.74925962, 0.75979112),
        arrayOf(0.74925962, 0.75761532),
        arrayOf(0.75222113, 0.75761532),
        arrayOf(0.75222113, 0.75543951),
        arrayOf(0.75518263, 0.75282855),
        arrayOf(0.75913129, 0.74978242),
        arrayOf(0.76307996, 0.74978242),
        arrayOf(0.76307996, 0.74673629),
        arrayOf(0.76801579, 0.74673629),
        arrayOf(0.76801579, 0.74281984),
        arrayOf(0.77295163, 0.74281984),
        arrayOf(0.77295163, 0.73890339),
        arrayOf(0.7769003, 0.73890339),
        arrayOf(0.7769003, 0.73542211),
        arrayOf(0.78084896, 0.73542211),
        arrayOf(0.78084896, 0.73194082),
        arrayOf(0.78479763, 0.72845953),
        arrayOf(0.7887463, 0.72454308),
        arrayOf(0.79269497, 0.72454308),
        arrayOf(0.79664363, 0.72062663),
        arrayOf(0.79664363, 0.71627502),
        arrayOf(0.79960513, 0.71627502),
        arrayOf(0.8035538, 0.71148825),
        arrayOf(0.8035538, 0.70713664),
        arrayOf(0.8065153, 0.70713664),
        arrayOf(0.8065153, 0.70322019),
        arrayOf(0.81046397, 0.70322019),
        arrayOf(0.81441264, 0.69930374),
        arrayOf(0.81441264, 0.69582245),
        arrayOf(0.8183613, 0.69582245),
        arrayOf(0.8183613, 0.69234117),
        arrayOf(0.82230997, 0.69234117),
        arrayOf(0.82625864, 0.68842472),
        arrayOf(0.82823297, 0.68450827),
        arrayOf(0.83119447, 0.68102698),
        arrayOf(0.83119447, 0.67754569),
        arrayOf(0.83316881, 0.67754569),
        arrayOf(0.83316881, 0.67449956),
        arrayOf(0.83514314, 0.67449956),
        arrayOf(0.83514314, 0.6718886),
        arrayOf(0.83613031, 0.6718886),
        arrayOf(0.83613031, 0.66971279),
        arrayOf(0.83810464, 0.66971279),
        arrayOf(0.83810464, 0.66753699),
        arrayOf(0.83909181, 0.66579634),
        arrayOf(0.84106614, 0.66449086),
        arrayOf(0.84205331, 0.66275022),
        arrayOf(0.84501481, 0.66013925),
        arrayOf(0.84501481, 0.65839861),
        arrayOf(0.84501481, 0.65752829),
        arrayOf(0.84402764, 0.65752829),
        arrayOf(0.84402764, 0.65665796),
        arrayOf(0.84402764, 0.6562228),
        arrayOf(0.84402764, 0.65578764),
        arrayOf(0.84402764, 0.65535248),
        arrayOf(0.84501481, 0.65535248),
        arrayOf(0.84501481, 0.65491732),
        arrayOf(0.84501481, 0.65448216),
        arrayOf(0.84501481, 0.654047),
        arrayOf(0.84600197, 0.654047),
        arrayOf(0.84698914, 0.654047)
    )
}
