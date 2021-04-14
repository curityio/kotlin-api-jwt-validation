/*
 *  Copyright 2021 Curity AB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
 
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
