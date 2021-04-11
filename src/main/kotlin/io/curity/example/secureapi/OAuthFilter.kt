package io.curity.example.secureapi

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.jose4j.jwk.HttpsJwks
import org.jose4j.jwt.consumer.JwtConsumerBuilder
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver
import org.jose4j.jwa.AlgorithmConstraints
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jwt.consumer.InvalidJwtException

/*
 * Implement OAuth handling via the jose4j library
 */
class OAuthFilter(private val _configuration: Configuration) : Filter {

    // Get token signing public keys from the Authorization Server's JWKS endpoint
    private val _httpsJkws = HttpsJwks(_configuration.getJwksEndpoint())
    private val _httpsJwksKeyResolver = HttpsJwksVerificationKeyResolver(_httpsJkws)

    override fun init(config: FilterConfig?) {
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        try {

            // Read the authorization header
            val httpRequest = request as HttpServletRequest
            val jwt = this.getBearerToken(httpRequest)

            // Define rules for getting JWKS keys for the kid in the JWT header
            // Also define rules for verifying the JWT once keys have been retrieved
            val jwtConsumer = JwtConsumerBuilder()
                .setVerificationKeyResolver(_httpsJwksKeyResolver)
                .setJwsAlgorithmConstraints(
                    AlgorithmConstraints.ConstraintType.PERMIT,
                    AlgorithmIdentifiers.RSA_USING_SHA256)
                .setExpectedIssuer(_configuration.getIssuer())
                .setExpectedAudience(_configuration.getAudience())
                .build()

            // Do the validation and get claims
            val jwtClaims = jwtConsumer.processToClaims(jwt)

            // Set the principal object against the request
            request.setAttribute("principal", jwtClaims)

            // Continue processing and run the API logic
            chain?.doFilter(request, response)

        } catch (e: InvalidJwtException) {

            // e.errorDetails[0].errorCode
            // e.errorDetails[0].errorMessage

            // Provide a generic error message to the caller
            val httpResponse = response as HttpServletResponse
            httpResponse.setHeader("www-authenticate", "Bearer")
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED)
        }
    }

    override fun destroy() {
    }

    private fun getBearerToken(httpRequest: HttpServletRequest): String {
        return ""
    }
}