package at.reichinger.cuaverification.hmm

import at.reichinger.cuaverification.CovarianceTypeDeserializer
import at.reichinger.cuaverification.hmm.covariance.CovarianceType
import com.google.gson.GsonBuilder
import java.io.File

object ModelSerializer {
    fun getGMMHMM(path: String): GMMHMM {
        val json = File(path).readText()
        val converter = GsonBuilder()
            .registerTypeAdapter(CovarianceType::class.java, CovarianceTypeDeserializer())
            .create()
        return converter.fromJson(json, GMMHMM::class.java)
    }
}