package at.reichinger.cuaverification.hmm

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.File

class ModelSerializerTest {

    @Test
    fun testGetGMMHMM() {
        val jsonTest = """{"n_components": 2, "n_features": 2, "n_mixtures": 4, "covariance_type": "diag", "start_probabilities": [0.1902990852421854, 0.8097009147578146], "transition_matrix": [[0.8367032585028124, 0.16329674149718765], [0.16712845872477605, 0.8328715412752239]], "weights": [[0.2344745570455695, 0.0829336532763014, 0.6800405502022553, 0.0025512394758750046], [0.0956554129558259, 0.5526441938391627, 0.348864260168463, 0.0028361330365481783]], "means": [[[0.7919529541295881, 0.7343103324733794], [0.8024067741294256, 0.7212958202711731], [0.7965243010778026, 0.7314626184705814], [0.7944981279484077, 0.7297473714566106]], [[0.7529184615755864, 0.780184581288779], [0.759427093178944, 0.776001251828049], [0.7580104145978047, 0.7756549790603438], [0.755458772340159, 0.7769504226795774]]], "covariance_matrix": [[[0.0015750041599045645, 0.002603047493310056], [0.0010953104825192604, 0.002110632014972188], [0.0012062354497082465, 0.0020449839828601897], [0.0017104791319194606, 0.0028811941216198627]], [[0.0014611652263041932, 0.001626985017678072], [0.001362585512001933, 0.0015416055667433156], [0.0015383533799394199, 0.0016381471411810059], [0.001674965682906525, 0.001957387042370726]]]}"""
        val file = File("test.json")
        file.writeText(jsonTest)

        val model = ModelSerializer.getGMMHMM(file.absolutePath)
        assertEquals(412.3738, ModelPredictor(model).score(sequence), 0.0001)
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