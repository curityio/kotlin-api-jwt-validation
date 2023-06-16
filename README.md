# Kotlin API OAuth Integration

[![Quality](https://img.shields.io/badge/quality-experiment-red)](https://curity.io/resources/code-examples/status/)
[![Availability](https://img.shields.io/badge/availability-source-blue)](https://curity.io/resources/code-examples/status/)

A basic sample to show how to use the [jose4j security library](https://bitbucket.org/b_c/jose4j/wiki/Home) for managing validation of OAuth (JWT) access tokens. API routes can then use scopes and claims from the JWT to authorize requests.

## Configuration

The API is configured with these details in its `api.properties` file.\
Point these to the equivalent values for your own instance of the Curity Identity Server.

```text
jwks_endpoint=https://idsvr.example.com/oauth/v2/oauth-anonymous/jwks
issuer=https://idsvr.example.com/oauth/v2/oauth-anonymous
audience=api.example.com
```

## Run the API

Ensure that maven and a Java SDK of 17 or higher is installed.
Then build and run the API with these commands:

```bash
mvn package
java -jar target/secureapi-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Call the API

Call the running API with an HTTP request such as the following:

```bash
curl -i http://localhost:3000 -H "Authorization: Bearer eyJraWQiOiIyV01TWGcwekE..."
```

If the authorization server is configured with the appropriate scopes and claims, a 200 response will be returned:   

```json
{
  "message": "API Request has role: admin and scope read"
}
```

If there is a token validation problem, a 401 response will be returned, with a www-authenticate response header:

```text
WWW-Authenticate: Bearer, error=invalid_token, error_description=Access token is missing, invalid or expired
```

## Further Information

- See the [Kotlin API Tutorial](https://curity.io/resources/learn/kotlin-api/) for a technical walkthrough
- Please visit [curity.io](https://curity.io/) for more information about the Curity Identity Server
