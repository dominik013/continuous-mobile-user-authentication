package at.reichinger.cuaverification

import at.reichinger.cuaverification.hmm.covariance.*
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.json.JSONException
import java.lang.reflect.Type

class CovarianceTypeDeserializer : JsonDeserializer<CovarianceType> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): CovarianceType {

        when (json?.asString) {
            "spherical" -> return SphericalCovariance()
            "diag" -> return DiagonalCovariance()
            "full" -> return FullCovariance()
            "tied" -> return TiedCovariance()
        }

        throw JSONException("Unable to deserialize covariance-type ${json?.asString}")
    }

}