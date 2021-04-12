package io.curity.example.secureapi

import spark.Spark.before
import spark.Spark.get
import spark.Spark.port

/*
 * A simple OAuth secured API
 */
fun main() {

    // Load configuration and configure the Spark port to listen on
    val configuration = Configuration()
    port(configuration.getPortNumber())

    // Run the OAuth filter before API routes
    val filter = OAuthFilter(configuration)
    before("*") { request, response ->
        filter.doFilter(request.raw(), response.raw(), null);
    }

    // API routes can then use scopes and claims from the OAuth filter
    val routes = ApiRoutes()
    get("/api/data", routes::getData)
}
