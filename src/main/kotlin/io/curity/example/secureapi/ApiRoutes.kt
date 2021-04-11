package io.curity.example.secureapi;

import com.google.gson.Gson
import org.jose4j.jwt.JwtClaims
import spark.Request;
import spark.Response;

/*
 * API requests are received here once OAuth handling is complete
 */
class ApiRoutes {

    /*
     * A simple GET to show how to access claims and scopes from the validated JWT
     */
    fun getData(request: Request, response: Response): String {

        val claims = request.attribute<JwtClaims>("principal")
        val role = claims.getClaimValueAsString("role")
        val scope = claims.getClaimValueAsString("scope")

        val data = DataResponse("API Request has role '${role}' and scope '${scope}'")
        return Gson().toJson(data)
    }
}
