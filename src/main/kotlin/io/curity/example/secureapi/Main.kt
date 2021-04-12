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
