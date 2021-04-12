package io.curity.example.secureapi

import java.util.Properties

/*
 * A simple configuration class that reads the api.properties file
 */
class Configuration {

    private val _properties = Properties()

    init {
        val inputStream = this.javaClass.getResourceAsStream("/api.properties")
        inputStream.use {
            _properties.load(inputStream)
        }
    }

    fun getPortNumber(): Int {
        return _properties.getProperty("port").toInt()
    }

    fun getJwksEndpoint(): String {
        return _properties.getProperty("jwks_endpoint")
    }

    fun getIssuerExpectedClaim(): String {
        return _properties.getProperty("issuer_claim")
    }

    fun getAudienceExpectedClaim(): String {
        return _properties.getProperty("audience_claim")
    }
}
