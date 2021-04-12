package io.curity.example.secureapi

import org.jose4j.jwa.AlgorithmConstraints
import org.jose4j.jwk.HttpsJwks
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jwt.consumer.InvalidJwtException
import org.jose4j.jwt.consumer.JwtConsumerBuilder
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver
import org.slf4j.LoggerFactory
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import spark.Spark.halt

/*
 * Implement OAuth handling via the jose4j library
 */
class OAuthFilter(private val _configuration: Configuration) : Filter {

    private val _logger = LoggerFactory.getLogger(OAuthFilter::class.java)

    // Configure to download token signing public keys from the Authorization Server's JWKS endpoint
    private val _httpsJkws = HttpsJwks(_configuration.getJwksEndpoint())
    private val _httpsJwksKeyResolver = HttpsJwksVerificationKeyResolver(_httpsJkws)

    override fun init(config: FilterConfig?) {
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        try {
            _logger.debug("Authorization filter invoked for HTTP request")

            // Try to read the access token from the authorization header and return a 401 if not supplied
            val jwt = this.getBearerToken(httpRequest)
            if (jwt.isEmpty()) {
                _logger.info("No access token was received in the bearer header")
                this.unauthorizedResponse(httpResponse)
                return
            }

            // configure the Jose4J library to download JWKS keys and verify the access token
            val jwtConsumer = JwtConsumerBuilder()
                .setVerificationKeyResolver(_httpsJwksKeyResolver)
                .setJwsAlgorithmConstraints(
                    AlgorithmConstraints.ConstraintType.PERMIT,
                    AlgorithmIdentifiers.RSA_USING_SHA256
                )
                .setExpectedIssuer(_configuration.getIssuerExpectedClaim())
                .setExpectedAudience(_configuration.getAudienceExpectedClaim())
                .build()

            // Do the validation, then store claims in the request object
            val jwtClaims = jwtConsumer.processToClaims(jwt)
            request.setAttribute("principal", jwtClaims)
            _logger.debug("JWT was successfully validated")

            // Continue processing and run the API logic
            chain?.doFilter(request, response)

        } catch (e: InvalidJwtException) {

            _logger.info("JWT validation failed")
            for (item in e.errorDetails) {
                _logger.info("${item.errorCode} : ${item.errorMessage}")
            }

            this.unauthorizedResponse(httpResponse)
        }
    }

    override fun destroy() {
    }

    private fun getBearerToken(httpRequest: HttpServletRequest): String {

        val header = httpRequest.getHeader("Authorization")
        if (header != null) {
            val parts = header.split(" ").toTypedArray()
            if (parts.size == 2) {
                if (parts[0].toLowerCase() == "bearer") {
                    return parts[1];
                }
            }
        }

        return ""
    }

    private fun unauthorizedResponse(httpResponse: HttpServletResponse) {

        httpResponse.setHeader("www-authenticate", "Bearer")
        halt(401)
    }
}