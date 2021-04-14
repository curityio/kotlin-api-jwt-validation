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

import spark.Spark.before
import spark.Spark.get
import spark.Spark.port

/*
 * A simple OAuth secured API
 */
fun main() {

    val configuration = Configuration()
    port(configuration.getPortNumber())

    val filter = OAuthFilter(configuration)
    before("*") { request, response ->
        filter.doFilter(request.raw(), response.raw(), null)
    }

    val routes = ApiRoutes()
    get("/api/data", routes::getData)
}
