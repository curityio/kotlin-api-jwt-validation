package io.curity.example.secureapi

import java.util.Properties

/*
 * A simple configuration class
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

    fun getIssuer(): String {
        return _properties.getProperty("issuer_claim")
    }

    fun getAudience(): String {
        return _properties.getProperty("audience_claim")
    }
}
