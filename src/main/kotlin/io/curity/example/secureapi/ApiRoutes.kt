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

import org.jose4j.jwt.JwtClaims
import com.google.gson.Gson
import spark.Request
import spark.Response

/*
 * API requests are received here once OAuth handling is complete
 */
class ApiRoutes {

    fun getData(request: Request, response: Response): String {

        val claims = request.attribute<JwtClaims>("principal")

        val role = claims.getClaimValueAsString("role")
        val scope = claims.getClaimValueAsString("scope")

        val data = DataResponse("API Request has role: $role and scope: $scope")
        return Gson().toJson(data)
    }
}
